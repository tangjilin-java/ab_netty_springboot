package ab.tjl.pojo.bo;

/**
 * @Author:TangJiLin
 * @Description: 用户请求数据封装
 * @Date: Created in 2020/4/21 10:52
 * @Modified By:
 */
public class UsersBO {
    private String userId;

    /**
     * 用户名，账号，易信号
     */
    private String faceData;

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFaceData() {
        return faceData;
    }

    public void setFaceData(String faceData) {
        this.faceData = faceData;
    }


}
