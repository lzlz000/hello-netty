package lzlz000.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import static lzlz000.Const.DEFAULT_PORT;

public class BaseServer {
    private final int port;

    public BaseServer(int port) {
        this.port = port;
    }


    protected void start(ChannelHandler ... handlers) throws InterruptedException {
        /*线程组*/
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();/*服务端启动必备*/
            b.group(group)
                    /*指明使用NIO进行网络通讯*/
                    .channel(NioServerSocketChannel.class)
                    /*指明服务器监听端口*/
                    .localAddress(new InetSocketAddress(port))
                    /*接收到连接请求，新启一个socket通信，也就是channel，每个channel
                     * 有自己的事件的handler*/
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new EchoServerHandler());
//                            ch.pipeline().addLast("handler1",new EchoOutboundHandler());
                            ch.pipeline().addLast(handlers);

                        }
                    });
            ChannelFuture f = b.bind().sync();/*绑定到端口，阻塞等待直到连接完成*/
            /*阻塞，直到channel关闭*/
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
