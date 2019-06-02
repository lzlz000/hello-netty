package lzlz000.netty.first;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lzlz000.Const;

import java.net.InetSocketAddress;

public class EchoClient {
    private final int port;
    private final String host;

    public EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup(1);/*线程组*/
        try {
            Bootstrap b = new Bootstrap();/*客户端启动必备*/
            b.group(group)
                    .channel(NioSocketChannel.class)/*指明使用NIO进行网络通讯*/
                    .remoteAddress(new InetSocketAddress(host,port))/*配置远程服务器的地址*/
                    .handler(new EchoOutboundHandler());
            ChannelFuture f = b.connect().sync();/*连接到远程节点，阻塞等待直到连接完成*/
            /*阻塞，直到channel关闭*/
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient(Const.DEFAULT_PORT,Const.DEFAULT_SERVER_IP).start();
    }
}
