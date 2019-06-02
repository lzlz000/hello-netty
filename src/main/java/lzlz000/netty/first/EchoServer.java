package lzlz000.netty.first;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import static lzlz000.Const.DEFAULT_PORT;

/**
 * netty的helloworld程序
 * 实现这样一个功能
 * 客户端从控制台输入数据，发送给服务器，服务器保存当前收到的消息，并返回从这个客户端收到的所有数据
 * 当客户端输入BYE 服务器断开连接
 */
public class EchoServer {
    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        EchoServer echoServer = new EchoServer(DEFAULT_PORT);
        System.out.println("服务器启动");
        echoServer.start();
        System.out.println("服务器关闭");
    }

    private void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
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
                            ch.pipeline().addLast("handler1",new EchoOutboundHandler());
                            ch.pipeline().addLast(serverHandler);

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
