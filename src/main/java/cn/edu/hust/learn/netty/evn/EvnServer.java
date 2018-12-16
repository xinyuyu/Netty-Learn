package cn.edu.hust.learn.netty.evn;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EvnServer {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(boosGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new EvnServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind("localhost", 8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
