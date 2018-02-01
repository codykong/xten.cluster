package com.xten.cluster.common.consul.listener.service;

import com.orbitz.consul.cache.ServiceHealthKey;
import com.orbitz.consul.model.health.ServiceHealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/17
 */
public abstract class  AbstractServiceCheckAction implements ServiceCheckAction<ServiceHealthKey,ServiceHealth> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractServiceCheckAction.class);
    private Map<String,CachedService> concernedServices = null;

    @Override
    public void notify(Map<ServiceHealthKey, ServiceHealth> newValues) {


        List<CachedService> changedServices = new ArrayList<>();
        if (concernedServices == null){
            initConcernedService(newValues);
            concernedServices.values().stream().forEach(p -> {
                p.setServiceStatus(CachedService.ServiceStatus.INIT);
                changedServices.add(p);
            });

        }else {
            Set<String> existedServiceIds = new HashSet<>();

            // 先找出启动的服务
            for (Map.Entry<ServiceHealthKey, ServiceHealth> entry : newValues.entrySet()){
                CachedService concernedService = concernedServices.get(entry.getValue().getService().getId());
                // 如果不存在，则是新的服务
                if (concernedService == null){
                    CachedService newService = new CachedService(entry.getValue().getService(), CachedService.CheckStatus.PASSING);
                    newService.setServiceStatus(CachedService.ServiceStatus.UP);
                    changedServices.add(newService);
                    concernedServices.put(newService.getId(),newService);
                    // 如果存在的服务是已停止的服务，则是已停止服务启动
                }else if (concernedService.getCheckStatus().equals(CachedService.CheckStatus.CRITICAL)){
                    concernedService.setServiceStatus(CachedService.ServiceStatus.UP);
                    concernedService.setCheckStatus(CachedService.CheckStatus.PASSING);

                    changedServices.add(concernedService);

                }

                existedServiceIds.add(entry.getValue().getService().getId());
                // 如果存在的服务是运行中的服务，则不做任何操作
            }

            // 找出宕机的服务
            concernedServices.values().stream()
                    .filter(p -> p.getCheckStatus().equals(CachedService.CheckStatus.PASSING))
                    .forEach(p -> {
                        if (!existedServiceIds.contains(p.getId())){

                            CachedService downService = concernedServices.get(p.getId());
                            downService.setServiceStatus(CachedService.ServiceStatus.DOWN);
                            downService.setCheckStatus(CachedService.CheckStatus.CRITICAL);
                            changedServices.add(downService);

                        }
                    });



        }

        changedServiceNotify(changedServices);
    }

    @Override
    public boolean isPassing() {
        return true;
    }


    public abstract void changedServiceNotify(List<CachedService> changedServices);


    public void initConcernedService(Map<ServiceHealthKey, ServiceHealth> map){
        concernedServices = new HashMap<>();
        for (Map.Entry<ServiceHealthKey, ServiceHealth> entry : map.entrySet()){
            concernedServices.put(entry.getValue().getService().getId(),
                    new CachedService(entry.getValue().getService(), CachedService.CheckStatus.PASSING));
        }
    }
}
