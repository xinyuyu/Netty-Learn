package cn.edu.hust.learn.netty.evn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class EvnServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 解决服务打印异常信息问题
        if (msg instanceof HttpRequest) {
            ByteBuf content = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            ctx.writeAndFlush(response);
        }
    }

    // 各种回调函数的测试
    /*
        调用顺序：
        add------chandlerAdded 被调用
        register------channelRegistered 被调用
        active------channelActive 被调用
        inactive------channelInactive 被调用了
        unregister------channelUnregistered 被调用
        remove------chandlerRemoved 被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 通道处于活动状态时候调用的方法
        System.out.println("active------channelActive 被调用");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 通道释放时候调用的方法
        System.out.println("inactive------channelInactive 被调用了");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // 通道被注册
        System.out.println("register------channelRegistered 被调用");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // 通道取消注册
        System.out.println("unregister------channelUnregistered 被调用");
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 被处理
        System.out.println("add------chandlerAdded 被调用");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("remove------chandlerRemoved 被调用");
    }
}
