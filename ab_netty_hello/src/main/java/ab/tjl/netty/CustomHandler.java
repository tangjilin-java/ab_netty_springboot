package ab.tjl.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @Author:TangJiLin
 * @Description: 创建自定义助手类 SimpleChannelInboundHandler:对于请求来讲 其实相当于[入栈，入境]
 * @Date: Created in 2020/4/19 15:17
 * @Modified By:
 */
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {


    /**
     *
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject msg) throws Exception {
        //获取channel
        Channel channel = channelHandlerContext.channel();

        if (msg instanceof HttpRequest){
            //显示客户端的远程地址
            System.out.println(channel.remoteAddress());

            //定义发送数据的消息
            ByteBuf content = Unpooled.copiedBuffer("Hello netty!", CharsetUtil.UTF_8);

            //构建一个响应
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1
                            , HttpResponseStatus.OK
                            ,content);
            //为响应添加一个数据类型和长度
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //吧响应刷到客户端
            channelHandlerContext.writeAndFlush(response);
        }

    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。注册");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。移除");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。活跃");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。不活跃");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channeld读取完毕。。。");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("用户事件触发。。。");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel可写更改");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("补货到异常");
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("助手类添加");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("助手类移除");
        super.handlerRemoved(ctx);
    }
}
