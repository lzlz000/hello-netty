package lzlz000.netty.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lzlz000.netty.BaseServer;

import java.net.InetSocketAddress;

import static lzlz000.Const.DEFAULT_PORT;

/**
 * netty的helloworld程序
 * 实现这样一个功能
 * 客户端从控制台输入数据，发送给服务器，服务器保存当前收到的消息，并返回从这个客户端收到的所有数据
 * 当客户端输入BYE 服务器断开连接
 */
public class EchoServer extends BaseServer {

    private EchoServer(int port) {
        super(port);
    }

    public static void main(String[] args) throws InterruptedException {
        EchoServer echoServer = new EchoServer(DEFAULT_PORT);
        System.out.println("服务器启动");
        echoServer.start(new EchoServerHandler());
        System.out.println("服务器关闭");
    }

}
