package com.xten.cluster.common.transport.netty;

import com.google.common.net.HostAndPort;
import com.google.inject.Inject;
import com.xten.cluster.common.configuration.ClusterConfigValue;
import com.xten.cluster.common.configuration.ClusterOptions;
import com.xten.cluster.common.configuration.Configuration;
import com.xten.cluster.common.transport.TcpTransport;
import com.xten.cluster.common.transport.TransportStartException;
import com.xten.cluster.common.util.Executors;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/9
 */
public class NettyTransport extends TcpTransport {

    private static final Logger LOG = LoggerFactory.getLogger(NettyTransport.class);
    private final Configuration configuration;
    private ServerBootstrap serverBootstrap;

    @Inject
    public NettyTransport(Configuration configuration){
        this.configuration = configuration;
    }

    public void doStart() {

        serverBootstrap = createServerBootstrap();

        String ip = configuration.getString(ClusterOptions.NODE_RPC_ADDRESS_OPTION);
        int port = ClusterConfigValue.getNodeRpcPort(configuration);

        HostAndPort hostAndPort = HostAndPort.fromParts(ip,port);
        bindServer(hostAndPort);
    }

    private Bootstrap createBootstrap(){
        final Bootstrap bootstrap = new Bootstrap();
        int workerCount = Executors.boundedNumberOfProcessors();

        bootstrap.group(new NioEventLoopGroup(workerCount));
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.handler(getClientChannelInitializer());

        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        bootstrap.option(ChannelOption.TCP_NODELAY, false);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

        return bootstrap;

    }

    private ServerBootstrap createServerBootstrap(){

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        int workerCount = Executors.boundedNumberOfProcessors();
        serverBootstrap.group(new NioEventLoopGroup(workerCount))
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler( getServerChannelInitializer());


        return serverBootstrap;

    }

    private ChannelHandler getServerChannelInitializer() {
        return new ServerChannelInitializer();
    }

    private void bindServer(HostAndPort hostAndPort){

        if (LOG.isDebugEnabled()){
            LOG.debug("bind server bootstrap to:{}",hostAndPort);
        }

        try {
            ChannelFuture f = serverBootstrap.bind(hostAndPort.getPort()).sync();
        } catch (InterruptedException e) {
            throw new TransportStartException("start netty exception,hostAndPort:"+hostAndPort,e);
        }


    }

    private ChannelHandler getClientChannelInitializer() {
        return new ClientChannelInitializer();
    }



    private class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline p = socketChannel.pipeline();

            p.addLast(new EchoClientHandler());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//            Netty4Utils.maybeDie(cause);
            super.exceptionCaught(ctx, cause);
        }

    }

    private class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();

            //p.addLast(new LoggingHandler(LogLevel.INFO));
            p.addLast(new EchoServerHandler());
        }
    }

    private class EchoServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ctx.write(msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            // Close the connection when an exception is raised.
            cause.printStackTrace();
            ctx.close();
        }

    }

    private class EchoClientHandler extends ChannelInboundHandlerAdapter {

        private final ByteBuf firstMessage;

        /**
         * Creates a client-side handler.
         */
        public EchoClientHandler() {
            firstMessage = Unpooled.buffer(256);
            for (int i = 0; i < firstMessage.capacity(); i ++) {
                firstMessage.writeByte((byte) i);
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            ctx.writeAndFlush(firstMessage);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ctx.write(msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            // Close the connection when an exception is raised.
            cause.printStackTrace();
            ctx.close();
        }

    }

}
