package ab.tjl.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author:TangJiLin
 * @Description: 实现客户端发送请求 服务器会返回hello netty
 * @Date: Created in 2020/4/19 14:50
 * @Modified By:
 */
public class HelloServer {

    public static void main(String[] args) throws Exception{
        //定义一对线程池
        //主线程组：用于接收客户端的连接 但是不做任何处理 跟老板一样不做事
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //从线程组：老板线程组会把任务丢给他 让手下去做任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //定义一个netty服务器的启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)//将主从线程组都加到里面
                    .channel(NioServerSocketChannel.class)//设置nio的双向通道
                    .childHandler(new HelloServerInitializer());//子处理器：用于处理workerGroup

            //启动server 并且设置8088为启动的端口号 启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
            //监听关闭的channel 设置同步方式
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
