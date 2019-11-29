package lzlz000.netty.simpleproxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

public class ProxyConnectHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
                        SocketAddress localAddress, ChannelPromise promise) throws Exception {
        super.connect(ctx, remoteAddress, localAddress, promise);
    }
}
