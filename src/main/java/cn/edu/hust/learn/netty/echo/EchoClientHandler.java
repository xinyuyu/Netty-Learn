package cn.edu.hust.learn.netty.echo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;


public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
    }

    // 客户端与服务器端建立连接后该方法被激活
    // 激活后会想服务器端发送一条消息，接收该
    // 消息的服务器端的方法是channelRead()
    // 服务器接收到该消息后，在控制台打印消息
    // 并将消息原封不动的返回给客户端但是没有
    // flush，在完成读后，服务器端会调用
    // channelReadComplete()在该方法中flush
    // 这个时候客户端会接收到消息，并注册了一个
    // future的listener，该future完成服务器
    // 读完毕后关闭channel的操作。
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("netty rocks", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
