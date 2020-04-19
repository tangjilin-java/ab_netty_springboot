package ab.tjl.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author:TangJiLin
 * @Description: web通信服务
 * @Date: Created in 2020/4/19 15:42
 * @Modified By:
 */
public class WSServer {

    public static void main(String[] args) throws Exception{
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server =  new ServerBootstrap();

            server.group(mainGroup,subGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WSServerInitialzer());
            ChannelFuture future = server.bind(8088).sync();

            future.channel().closeFuture().sync();
        } finally  {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
