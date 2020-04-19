package ab.tjl.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "chat_msg")
public class ChatMsg {
    @Id
    private String id;

    /**
     * 发送者id
     */
    @Column(name = "send_user_id")
    private String sendUserId;

    /**
     * 接受者id
     */
    @Column(name = "accept_user_id")
    private String acceptUserId;

    /**
     * 消息
     */
    private String msg;

    /**
     * 消息是否签收状态
1：签收
0：未签收

     */
    @Column(name = "sign_flag")
    private Integer signFlag;

    /**
     * 发送请求的事件
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取发送者id
     *
     * @return send_user_id - 发送者id
     */
    public String getSendUserId() {
        return sendUserId;
    }

    /**
     * 设置发送者id
     *
     * @param sendUserId 发送者id
     */
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     * 获取接受者id
     *
     * @return accept_user_id - 接受者id
     */
    public String getAcceptUserId() {
        return acceptUserId;
    }

    /**
     * 设置接受者id
     *
     * @param acceptUserId 接受者id
     */
    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    /**
     * 获取消息
     *
     * @return msg - 消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置消息
     *
     * @param msg 消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取消息是否签收状态
1：签收
0：未签收

     *
     * @return sign_flag - 消息是否签收状态
1：签收
0：未签收

     */
    public Integer getSignFlag() {
        return signFlag;
    }

    /**
     * 设置消息是否签收状态
1：签收
0：未签收

     *
     * @param signFlag 消息是否签收状态
1：签收
0：未签收

     */
    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    /**
     * 获取发送请求的事件
     *
     * @return create_time - 发送请求的事件
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置发送请求的事件
     *
     * @param createTime 发送请求的事件
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}