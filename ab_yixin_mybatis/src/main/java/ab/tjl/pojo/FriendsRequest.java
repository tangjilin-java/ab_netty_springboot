package ab.tjl.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "friends_request")
public class FriendsRequest {
    @Id
    private String id;

    /**
     * 发送者id
     */
    @Column(name = "send_user_id")
    private String sendUserId;

    /**
     * 接收者id
     */
    @Column(name = "accept_user_id")
    private String acceptUserId;

    /**
     * 发送请求的事件
     */
    @Column(name = "request_date_time")
    private Date requestDateTime;

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
     * 获取接收者id
     *
     * @return accept_user_id - 接收者id
     */
    public String getAcceptUserId() {
        return acceptUserId;
    }

    /**
     * 设置接收者id
     *
     * @param acceptUserId 接收者id
     */
    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    /**
     * 获取发送请求的事件
     *
     * @return request_date_time - 发送请求的事件
     */
    public Date getRequestDateTime() {
        return requestDateTime;
    }

    /**
     * 设置发送请求的事件
     *
     * @param requestDateTime 发送请求的事件
     */
    public void setRequestDateTime(Date requestDateTime) {
        this.requestDateTime = requestDateTime;
    }
}