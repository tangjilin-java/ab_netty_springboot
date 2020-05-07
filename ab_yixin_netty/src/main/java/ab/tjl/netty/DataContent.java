package ab.tjl.netty;



import java.io.Serializable;

/**
 * @Author:TangJiLin
 * @Description: 数据整合
 * @Date: Created in 2020/5/7 20:18
 * @Modified By:
 */
public class DataContent implements Serializable {

    private static final long serialVersionUID = 8021381444738260454L;

    private Integer action;		// 动作类型
    private ChatMsg chatMsg;	// 用户的聊天内容entity
    private String extand;		// 扩展字段

    public Integer getAction() {
        return action;
    }
    public void setAction(Integer action) {
        this.action = action;
    }
    public ChatMsg getChatMsg() {
        return chatMsg;
    }
    public void setChatMsg(ChatMsg chatMsg) {
        this.chatMsg = chatMsg;
    }
    public String getExtand() {
        return extand;
    }
    public void setExtand(String extand) {
        this.extand = extand;
    }
}
