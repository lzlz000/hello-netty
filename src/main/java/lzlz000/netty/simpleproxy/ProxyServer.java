package lzlz000.netty.simpleproxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lzlz000.netty.BaseServer;
import lzlz000.netty.helloworld.EchoOutboundHandler;
import lzlz000.netty.helloworld.EchoServerHandler;

import java.net.InetSocketAddress;

import static lzlz000.Const.DEFAULT_PORT;

/**
 * 实现一个流量转发服务器，接收到消息转发给EchoServer
 */
public class ProxyServer extends BaseServer {

    private ProxyServer(int port) {
        super(port);
    }

    public static void main(String[] args) throws InterruptedException {
        ProxyServer server = new ProxyServer(DEFAULT_PORT);
        System.out.println("服务器启动");
        server.start();
        System.out.println("服务器关闭");
    }
}
