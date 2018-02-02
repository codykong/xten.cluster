package com.xten.cluster.common.consul;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.net.HostAndPort;
import com.google.inject.Inject;
import com.orbitz.consul.Consul;
import com.orbitz.consul.ConsulException;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.State;
import com.orbitz.consul.model.agent.*;
import com.orbitz.consul.model.health.HealthCheck;
import com.orbitz.consul.model.kv.Value;
import com.orbitz.consul.model.session.ImmutableSession;
import com.orbitz.consul.model.session.Session;
import com.orbitz.consul.option.QueryOptions;
import com.xten.cluster.common.configuration.ConfigConstants;
import com.xten.cluster.common.configuration.Configuration;
import com.xten.cluster.common.configuration.IllegalConfigurationException;
import com.xten.cluster.common.consul.meta.JsonMeta;
import com.xten.cluster.common.consul.meta.LeaderMeta;
import com.xten.cluster.metadata.MetaKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/2
 */
public class ConsulService {

    private static final Logger LOG = LoggerFactory.getLogger(ConsulService.class);
    /**
     * 默认的服务检测时间间隔
     */
    public static final long DEFAULT_TTL = 3;
    public static final String DEFAULT_CHECK_INTERVAL = "300ms";
    public static final String DEFAULT_LOCK_DELAY = "1s";
    private static final int CREATE_SESSION_TRY_TIMES = 10;
    private static final int CREATE_SESSION_TRY_INTERVAL_MILLIS = 500;
    private static final int ELECT_LEADER_TRY_TIMES = 30;
    private static final int ELECT_LEADER_TRY_INTERVAL_MILLS = 500;



    private final Consul consul;

    public ConsulService(String host,int port){
        try{

            consul = Consul.builder().withHostAndPort(HostAndPort.fromParts(host,port)).build();
            LOG.info("create Consul client successfully! host is {},port is {}",host,port);
        }catch (Exception e){
            LOG.error("create consul client fail,{}",e);
            throw new ConsulException(e);
        }
    }

    @Inject
    public ConsulService(Configuration configuration) {
        Preconditions.checkArgument(configuration.containsKey(ConfigConstants.CONSUL_RPC_HOST_KEY),
                new IllegalConfigurationException(ConfigConstants.CONSUL_RPC_HOST_KEY + "must be set")
        );
        String consulHost = configuration.getString(ConfigConstants.CONSUL_RPC_HOST_KEY);

        Preconditions.checkArgument(configuration.containsKey(ConfigConstants.CONSUL_RPC_PORT_KEY),
                new IllegalConfigurationException(ConfigConstants.CONSUL_RPC_PORT_KEY + "must be set")
        );
        int consulPort = configuration.getInteger(ConfigConstants.CONSUL_RPC_PORT_KEY,0);

        consul = Consul.builder().withUrl("http://"+consulHost+":"+consulPort).build();
    }

    public ConsulService(HostAndPort hostAndPort){
        try{

            consul = Consul.builder().withHostAndPort(hostAndPort).build();
            LOG.info("create Consul client successfully! hostAndPort is {}",hostAndPort);
        }catch (Exception e){
            LOG.error("create consul client fail,{}",e);
            throw new ConsulException(e);
        }
    }

    /**
     * 返回Consul客户端
     * @return
     */
    public Consul getConsul(){
        return consul;
    }

    /**
     * 释放某一个key钟的锁
     * @param key
     * @param sessionId
     */
    public void releaseLock(final String key, final String sessionId){
        consul.keyValueClient().releaseLock(key,sessionId);
    }

    /**
     * 注册服务
     * @param address
     * @param port
     * @param id
     * @param name
     * @param tags
     */
    public void registerService(String address,int port,String id,String name,String... tags){
        HostAndPort hostAndPort = HostAndPort.fromParts(address,port);
        consul.agentClient().register(port,hostAndPort,DEFAULT_TTL,name,id,tags);
    }

    /**
     * 注销服务
     * @param serviceId
     */
    public void deregisterService(String serviceId){
        consul.agentClient().deregister(serviceId);
    }

    /**
     * 选举新的Leader
     * @param serviceName
     * @param info
     * @param sessionId
     * @return
     */
    public Optional<String> electLeaderForService(final String serviceName, final String info,String sessionId) {
        final String key = MetaKey.getServiceLeaderKey(serviceName);
        Boolean isLeader = false;
        String leaderMetaInfo = new LeaderMeta(info,sessionId).toString();

        for (int i =1;i < ELECT_LEADER_TRY_TIMES;i++){
            isLeader =consul.keyValueClient().acquireLock(key, leaderMetaInfo, sessionId);

            Optional<Value> value = consul.keyValueClient().getValue(MetaKey.getServiceLeaderKey(serviceName));
            if (isLeader || (value.isPresent() && value.get().getSession().isPresent())){
                break;
            }else {
                LOG.info("try to lock,try-num:{},key is:{},info is:{}",i,key,info);
                try {
                    Thread.sleep(ELECT_LEADER_TRY_INTERVAL_MILLS);
                } catch (InterruptedException e) {
                    LOG.error("try to lock sleep error,try-num:{},key is:{},info is:{},exception:{}",i,key,info, e.getMessage(),e);
                }
            }

        }

        LOG.info("host is :"+info+";isLeader: "+isLeader+";time is:"+ new Date());
        if(isLeader){
            return Optional.of(info);
        }else{
            Optional<LeaderMeta> metaOptional = getLeaderInfoForService(serviceName);
            if (metaOptional.isPresent()){
                return Optional.of(metaOptional.get().getInfo());
            }else {
                return Optional.absent();
            }
        }
    }

    /**
     * 获取Leader的信息
     * @param serviceName
     * @return
     */
    public Optional<LeaderMeta> getLeaderInfoForService(final String serviceName) {
        String key = MetaKey.getServiceLeaderKey(serviceName);
        Optional<Value> value = consul.keyValueClient().getValue(key);
        if(value.isPresent()){
            return Optional.of(LeaderMeta.fromJsonContent(value.get().getValueAsString().get())) ;
        }
        return Optional.absent();
    }


    /**
     * 注册检查服务，用于生成对应Session
     * @param address
     * @param port
     * @param id
     * @param name
     */
    public ServiceCheck registerSession(String address, int port, String id, String name){

        HostAndPort hostAndPort = HostAndPort.fromParts(address,port);

        Check check = ImmutableCheck.builder().tcp(hostAndPort.toString())
                .name(name).id(id)
                .interval(ConsulService.DEFAULT_CHECK_INTERVAL).build();

        consul.agentClient().registerCheck(check);

        String sessionId = buildSession(check.getId(),id);
        return new ServiceCheck(sessionId,check.getId(),id);
    }


    /**
     * 构造sessionId,因为注册session 会有一定的时长，故需要多次注册,如果注册仍不成功，则抛出异常
     * @param check
     * @param name
     * @return
     */
    private String buildSession(String check,String name){
        Session session = ImmutableSession.builder().addChecks(check).lockDelay(DEFAULT_LOCK_DELAY)
                .name(name).build();
        String sessionId = null;

        for (int i =1;i < CREATE_SESSION_TRY_TIMES;i++){
            try {
                sessionId = consul.sessionClient().createSession(session).getId();
                break;
            } catch (Exception e) {
                LOG.info("try to create session error,try-num:"+i+",check is:"+check+"name is:"+name);
                try {
                    Thread.sleep(CREATE_SESSION_TRY_INTERVAL_MILLIS);
                } catch (InterruptedException e1) {
                    LOG.error("try to create session sleep error,try-num:"+i+"check is:"+check+"name is:"+name);
                }
            }

        }

        if (sessionId == null){
            throw new ConsulException("try to create session error,try-num:3"+"check is:"+check+"name is:"+name);
        }
        return sessionId;

    }

    /**
     * Destroys a session.
     *
     * @param sessionId The session ID to destroy.
     */
    public void destroySession(String sessionId){
        consul.sessionClient().destroySession(sessionId);
    }

    /**
     * De-registers a Health Check with the Agent
     *
     * @param checkId the id of the Check to deregister
     */
    public void deregisterCheck(String checkId) {
        consul.agentClient().deregister(checkId);
    }

    /**
     * 增加K/V 到kv store
     * @param key
     * @param value
     * @return
     */
    public Boolean putValue(String key, String value){

        return consul.keyValueClient().putValue(key, value);
    }

    /**
     * 增加K/V 到kv store
     * @param metadata
     * @return
     */
    public Boolean putValue(JsonMeta metadata){

        return consul.keyValueClient().putValue(metadata.key(), metadata.toContent());
    }

    /**
     * 通过key获取value
     * @param key
     * @return
     */
    public Optional<String> getOptionalValue(String key){

        Optional<String> value = consul.keyValueClient().getValueAsString(key);
        return value;
    }

    /**
     * 通过key获取value
     * @param key
     * @return
     */
    public String getValue(String key){

        Optional<String> value = consul.keyValueClient().getValueAsString(key);

        if (value.isPresent()){
            return value.get();
        }else {
            return null;
        }
    }


    /**
     * 获取string类型的值集合
     * @param key
     * @return
     */
    public List<String> getValuesAsString(String key){

        List<String> result = new ArrayList<>();

        List<Value> values = consul.keyValueClient().getValues(key);
        if (values!=null){
            result = values.stream().filter(v -> v.getValue().isPresent())
                    .map(v -> v.getValueAsString().get()).collect(Collectors.toList());
        }

        return result;
    }

    /**
     * 删除某个key
     * @param key
     */
    public void deleteKey(String key){
        consul.keyValueClient().deleteKey(key);
    }

    public void deleteKeys(String key){
        consul.keyValueClient().deleteKeys(key);
    }

    public List<String> getKeys(String key){
        List<String> keys = consul.keyValueClient().getKeys(key);

        return keys;
    }

    /**
     * 清空失败的Check
     */
    public void clearFailedCheck(){
        ConsulResponse<List<HealthCheck>> response = consul.healthClient().getChecksByState(State.FAIL);

        response.getResponse().stream().forEach(p -> {

            Optional<String> service = p.getServiceId();

            if (service.isPresent() && !service.get().isEmpty()){
                consul.agentClient().deregister(service.get());
            }

            if (p.getCheckId() != null && !p.getCheckId().isEmpty()){

                consul.agentClient().deregisterCheck(p.getCheckId());
            }

        });
    }


}
