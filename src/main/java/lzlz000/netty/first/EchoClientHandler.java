package lzlz000.netty.first;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lzlz000.Const;

import java.util.Scanner;


public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Scanner scanner = new Scanner(System.in);
    /*客户端读取到数据后干什么*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
            throws Exception {
        String s = msg.toString(CharsetUtil.UTF_8);
        System.out.println(s);
        /* 服务器接收到该信息便断开连接 */
        ctx.writeAndFlush(Unpooled.copiedBuffer(Const.BYE, CharsetUtil.UTF_8));
    }

    /*客户端被通知channel活跃以后，做事*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //往服务器写数据
        System.out.print("请输入: ");
        String next = scanner.nextLine();
        System.out.println(next);
        ctx.writeAndFlush(Unpooled.copiedBuffer(next,
                CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
