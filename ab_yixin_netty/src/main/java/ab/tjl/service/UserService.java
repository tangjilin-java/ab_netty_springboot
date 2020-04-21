package ab.tjl.service;

import ab.tjl.pojo.Users;
import ab.tjl.pojo.vo.FriendRequestVO;
import ab.tjl.pojo.vo.MyFriendsVO;

import java.util.List;

/**
 * @Author:TangJiLin
 * @Description: 用户业务层接口
 * @Date: Created in 2020/4/20 15:54
 * @Modified By:
 */
public interface UserService {
    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 查询用户密码是否正确
     * @param username
     * @param password
     * @return
     */
    Users queryUserForLogin(String username , String  password);


    /**
     * 用户注册
     * @param user
     * @return
     */
    Users saveUser(Users user);

    /**
     * 修改用户记录
     * @param user
     */
    Users updateUserInfo(Users user);

    /**
     * 搜索朋友的前置条件
     * @param myUserId
     * @param friendUsername
     * @return
     */
    Integer preconditionSearchFriends(String myUserId,String friendUsername);

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    Users queryUserInfoByUsername(String username);

    /**
     * 发送添加朋友请求
     * @param myUserId
     * @param friendUsername
     */
    void sendFriendRequest(String myUserId, String friendUsername);

    /**
     * 查找添加好友请求列表
     * @param acceptUserId
     * @return
     */
    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    /**
     * 删除好友请求的数据库表记录
     * @param sendUserId
     * @param acceptUserId
     */
    void deleteFriendRequest(String sendUserId, String acceptUserId);

    /**
     * 如果是通过好友请求，则互相增加好友记录到数据库对应的表
     * @param sendUserId
     * @param acceptUserId
     */
    void passFriendRequest(String sendUserId, String acceptUserId);

    /**
     * 查询好友列表
     * @param acceptUserId
     * @return
     */
    List<MyFriendsVO> queryMyFriends(String acceptUserId);
}
