package cn.edu.hust.learn.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 注意这个地方是需要是静态的，没个客户端启动会重写实例还一个MyChatServerHandler
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel self = ctx.channel();
        channels.forEach(channel -> {
            if (channel != self) {
                channel.writeAndFlush(channel.remoteAddress() + " 发送的消息: " + msg + "\n");
            } else {
                channel.writeAndFlush("自己发送的消息: " + msg + "\n");
            }
        });
    }

    // 客户端与服务器端已经建立好连接
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush("【服务器】- " + channel.remoteAddress() + " 加入\n");
        channels.add(channel);
    }

    // 与服务器断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channels.writeAndFlush("【服务器】-" + ctx.channel().remoteAddress() + " 离开\n");
    }

    // 表示连接激活状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "下线了");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
