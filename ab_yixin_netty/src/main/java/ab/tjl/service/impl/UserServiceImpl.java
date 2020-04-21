package ab.tjl.service.impl;

import ab.tjl.enums.MsgActionEnum;
import ab.tjl.enums.SearchFriendsStatusEnum;
import ab.tjl.mapper.FriendsRequestMapper;
import ab.tjl.mapper.MyFriendsMapper;
import ab.tjl.mapper.UsersMapper;
import ab.tjl.mapper.UsersMapperCustom;
import ab.tjl.pojo.FriendsRequest;
import ab.tjl.pojo.MyFriends;
import ab.tjl.pojo.Users;
import ab.tjl.pojo.vo.FriendRequestVO;
import ab.tjl.pojo.vo.MyFriendsVO;
import ab.tjl.service.UserService;
import ab.tjl.utils.FastDFSClient;
import ab.tjl.utils.FileUtils;
import ab.tjl.utils.JsonUtils;
import ab.tjl.utils.QRCodeUtils;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * @Author:TangJiLin
 * @Description: 用户业务层接口实现类
 * @Date: Created in 2020/4/20 15:55
 * @Modified By:
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UsersMapper userMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private MyFriendsMapper myFriendsMapper;
    @Autowired
    private FriendsRequestMapper friendsRequestMapper;
    @Autowired
    private UsersMapperCustom userMapperCustom;

    /**
     * 判断用户名知否存在
     * @param username
     * @return
     */
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);

        Users result = userMapper.selectOne(user);

    return result != null ? true : false;
    }

    /**
     * 查询用户密码是否正确
     * @param username
     * @param password
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",password);

        Users result = userMapper.selectOneByExample(userExample);

        return result;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users saveUser(Users user) {
        String userId = sid.nextShort();
        //为每一个用户生成一个唯一的二维码
        String qrCodePath = "D://user" + userId + "qecode.png";
        //yixin_qrcode:[username]
        qrCodeUtils.createQRCode(qrCodePath,"yixin_qrcode:" + user.getUsername());
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = "";
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setQrcode(qrCodeUrl);

        user.setId(userId);
        userMapper.insert(user);
        return user;
    }

    /**
     * 修改用户数据
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(Users user) {
        userMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }

    /**
     * 搜索朋友的前置条件
     * @param myUserId
     * @param friendUsername
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
        //1. 搜索用户如果不存在 返回【无此用户】
        Users user = queryUserInfoByUsername(friendUsername);
        if (user == null){
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }
        // 2.搜索的用户是自己 返回【不能添加自己】
        if (user.getId().equals(myUserId)){
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }
        //3.搜索的用户已经是自己的好友 返回【该用户已经是你的好友了】
        Example mfe = new Example(MyFriends.class);
        Example.Criteria mfc = mfe.createCriteria();
        mfc.andEqualTo("myUserId",myUserId);
        mfc.andEqualTo("myFriendUserId",user.getId());
        MyFriends myFriendsRel = myFriendsMapper.selectOneByExample(mfe);
        if (myFriendsRel != null){
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }

        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfoByUsername(String username){
        Example ue = new Example(Users.class);
        Example.Criteria uc = ue.createCriteria();
        uc.andEqualTo("username",username);
        return userMapper.selectOneByExample(ue);
    }

    /**
     * 添加好友请求
     * @param myUserId
     * @param friendUsername
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendFriendRequest(String myUserId, String friendUsername) {
        // 根据用户名把朋友信息查询出来
        Users friend = queryUserInfoByUsername(friendUsername);

        // 1. 查询发送好友请求记录表
        Example fre = new Example(FriendsRequest.class);
        Example.Criteria frc = fre.createCriteria();
        frc.andEqualTo("sendUserId", myUserId);
        frc.andEqualTo("acceptUserId", friend.getId());
        FriendsRequest friendRequest = friendsRequestMapper.selectOneByExample(fre);
        if (friendRequest == null) {
            // 2. 如果不是你的好友，并且好友记录没有添加，则新增好友请求记录
            String requestId = sid.nextShort();

            FriendsRequest request = new FriendsRequest();
            request.setId(requestId);
            request.setSendUserId(myUserId);
            request.setAcceptUserId(friend.getId());
            request.setRequestDateTime(new Date());
            friendsRequestMapper.insert(request);
        }
    }

    /**
     * 查找添加好友请求列表
     * @param acceptUserId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return userMapperCustom.queryFriendRequestList(acceptUserId);
    }

    /**
     * 删除好友请求的数据库表记录
     * @param sendUserId
     * @param acceptUserId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteFriendRequest(String sendUserId, String acceptUserId) {
        Example fre = new Example(FriendsRequest.class);
        Example.Criteria frc = fre.createCriteria();
        frc.andEqualTo("sendUserId", sendUserId);
        frc.andEqualTo("acceptUserId", acceptUserId);
        friendsRequestMapper.deleteByExample(fre);
    }

    /**
     * 如果是通过好友请求，则互相增加好友记录到数据库对应的表 然后删除好友请求的数据库表记录
     * @param sendUserId
     * @param acceptUserId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void passFriendRequest(String sendUserId, String acceptUserId) {
        //互相保存好友
        saveFriends(sendUserId, acceptUserId);
        saveFriends(acceptUserId, sendUserId);
        deleteFriendRequest(sendUserId, acceptUserId);

        /*Channel sendChannel = UserChannelRel.get(sendUserId);
        if (sendChannel != null) {
            // 使用websocket主动推送消息到请求发起者，更新他的通讯录列表为最新
            DataContent dataContent = new DataContent();
            dataContent.setAction(MsgActionEnum.PULL_FRIEND.type);

            sendChannel.writeAndFlush(
                    new TextWebSocketFrame(
                            JsonUtils.objectToJson(dataContent)));
        }*/
    }

    /**
     * 查询好友列表
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<MyFriendsVO> queryMyFriends(String userId) {
        List<MyFriendsVO> myFirends = userMapperCustom.queryMyFriends(userId);
        return myFirends;
    }

    /**
     * 私有方法 保存好友
     * @param sendUserId
     * @param acceptUserId
     */
    private void saveFriends(String sendUserId,String  acceptUserId){
        MyFriends myFriends = new MyFriends();
        String recordId = sid.nextShort();
        myFriends.setId(recordId);
        myFriends.setMyFriendUserId(acceptUserId);
        myFriends.setMyUserId(sendUserId);
        myFriendsMapper.insert(myFriends);
    }

    /**
     * 内部方法  通过主键查找用户
     * @param userId
     * @return
     */
    private Users queryUserById(String userId){
       return userMapper.selectByPrimaryKey(userId);
    }
}
