package lzlz000.netty.first;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

import java.net.SocketAddress;

import static lzlz000.Const.BYE;

/*指明我这个handler可以在多个channel之间共享，意味这个实现必须线程安全的。*/
@ChannelHandler.Sharable
public class EchoOutboundHandler extends ChannelOutboundHandlerAdapter {

    EchoOutboundHandler(){
        System.out.println("创建 EchoOutboundHandler");
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        System.out.println("flush");
        super.flush(ctx);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println(ctx.name()+" connect:"+remoteAddress);
        super.connect(ctx, remoteAddress, localAddress, promise);
        ctx.pipeline().remove(this).addLast(new EchoClientHandler());
    }

    /*** 发生异常后的处理*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
