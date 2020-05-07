package ab.tjl.netty;

import java.util.HashMap;
import io.netty.channel.Channel;

/**
 * @Author:TangJiLin
 * @Description: 用户id和channel的关联关系处理
 * @Date: Created in 2020/5/7 20:17
 * @Modified By:
 */
public class UserChannelRel {
    private static HashMap<String, Channel> manager = new HashMap<>();

    public static void put(String senderId, Channel channel) {
        manager.put(senderId, channel);
    }

    public static Channel get(String senderId) {
        return manager.get(senderId);
    }

    public static void output() {
        for (HashMap.Entry<String, Channel> entry : manager.entrySet()) {
            System.out.println("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }
}
