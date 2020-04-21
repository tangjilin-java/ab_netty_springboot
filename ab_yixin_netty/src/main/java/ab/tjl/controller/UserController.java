package ab.tjl.controller;

import ab.tjl.enums.OperatorFriendRequestTypeEnum;
import ab.tjl.enums.SearchFriendsStatusEnum;
import ab.tjl.pojo.Users;
import ab.tjl.pojo.bo.UsersBO;
import ab.tjl.pojo.vo.MyFriendsVO;
import ab.tjl.pojo.vo.UsersVO;
import ab.tjl.service.UserService;
import ab.tjl.utils.ABJSONResult;
import ab.tjl.utils.FastDFSClient;
import ab.tjl.utils.FileUtils;
import ab.tjl.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:TangJiLin
 * @Description: 用户控制层
 * @Date: Created in 2020/4/20 15:49
 * @Modified By:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FastDFSClient fastDFSClient;

    /**
     * 用户登录或者注册功能集一体
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/registerOrLogin")
    public ABJSONResult registerOrLogin(@RequestBody Users user) throws Exception{

        //判断用户名密码不能为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return ABJSONResult.errorMsg("用户名或密码不能为空！");
        }

        //判断用户名是否存在 如果存在就登录 如果不存在就注册
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;

        if (usernameIsExist){
            //登录
            userResult = userService.queryUserForLogin(user.getUsername(), MD5Utils.getMD5Str(user.getPassword()));
            if (userResult == null){
                return ABJSONResult.errorMsg("用户名或密码不正确！");
            }
        }else {
            //注册
            user.setNickname(user.getUsername());
            user.setFaceImage("");
            user.setFaceImageBig("");
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));

            userResult = userService.saveUser(user);

        }

        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userResult,userVO);

        return ABJSONResult.ok(userVO);
    }

    /**
     * 上传图片转换文件对象
     * @param userBO
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFaceBase64")
    public ABJSONResult uploadFaceBase64(@RequestBody UsersBO userBO) throws Exception{
        //获取前端传过来的base64字符串 然后转换成文件对象再上传
        String base64Data = userBO.getFaceData();
        String userFacePath = "D:\\fdfsimg" + userBO.getUserId() + "userFace64.png";
        //将前端传过来的base64字符串转换成一个文件对象存在定义的磁盘位置
        FileUtils.base64ToFile(userFacePath,base64Data);
        //将文件转换成multipart格式进行上传
        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);

        String url = fastDFSClient.uploadBase64(faceFile);
        System.out.println(url);//tasdfhuhasfhhfkh.png   fyasdgfuiasgugu_80X80.png

        //获取缩略图的URL
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String thumpImgUrl = arr[0] + thump + arr[1];

        //更新用户头像
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(url);

        Users result = userService.updateUserInfo(user);

        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(result,userVO);
        return ABJSONResult.ok(userVO);
    }

    /**
     * 修改昵称
     * @param userBO
     * @return
     * @throws Exception
     */
    @PostMapping("/setNickname")
    public ABJSONResult setNickname(@RequestBody UsersBO userBO) throws Exception {

        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setNickname(userBO.getNickname());

        Users result = userService.updateUserInfo(user);

        return ABJSONResult.ok(result);
    }

    /**
     * 搜索好友 根据账号做匹配查询 而不是模糊查询
     * @param myUserId
     * @param friendUsername
     * @return
     * @throws Exception
     */
    @PostMapping("/searchUser")
    public ABJSONResult searchUser(String myUserId,String friendUsername){

        //判断myUserId friendUsername 不能为空
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername)){
            return ABJSONResult.errorMsg("名称不能为空!");
        }

        //前置条件 1.搜索的用户不存在 返回【无此用户】
        //前置条件 2.搜索的用户是自己 返回【不能添加自己】
        //前置条件 3.搜索的用户已经是自己的好友 返回【该用户已经是你的好友了】
        Integer status = userService.preconditionSearchFriends(myUserId,friendUsername);
        if (status == SearchFriendsStatusEnum.SUCCESS.status){
            Users user = userService.queryUserInfoByUsername(friendUsername);
            UsersVO userVO = new UsersVO();
            BeanUtils.copyProperties(user,userVO);
            return ABJSONResult.ok(userVO);
        }else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return ABJSONResult.errorMsg(errorMsg);
        }
    }

    /**
     * 发送添加好友的请求
     * @param myUserId
     * @param friendUsername
     * @return
     * @throws Exception
     */
    @PostMapping("/addFriendRequest")
    public ABJSONResult addFriendRequest(String myUserId, String friendUsername){

        // 0. 判断 myUserId friendUsername 不能为空
        if (StringUtils.isBlank(myUserId)
                || StringUtils.isBlank(friendUsername)) {
            return ABJSONResult.errorMsg("名称不能为空!");
        }

        // 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
        // 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
        // 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        Integer status = userService.preconditionSearchFriends(myUserId, friendUsername);
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
            userService.sendFriendRequest(myUserId, friendUsername);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return ABJSONResult.errorMsg(errorMsg);
        }

        return ABJSONResult.ok();
    }

    /**
     * 发送添加好友的请求
     * @param userId
     * @return
     */
    @PostMapping("/queryFriendRequests")
    public ABJSONResult queryFriendRequests(String userId) {

        // 0. 判断不能为空
        if (StringUtils.isBlank(userId)) {
            return ABJSONResult.errorMsg("");
        }

        // 1. 查询用户接受到的朋友申请
        return ABJSONResult.ok(userService.queryFriendRequestList(userId));
    }

    /**
     * 接受方 通过或者忽略朋友请求
     * @param acceptUserId
     * @param sendUserId
     * @param operType
     * @return
     */
    @PostMapping("/operFriendRequest")
    public ABJSONResult operFriendRequest(String acceptUserId, String sendUserId,
                                             Integer operType) {

        // 0. acceptUserId sendUserId operType 判断不能为空
        if (StringUtils.isBlank(acceptUserId)
                || StringUtils.isBlank(sendUserId)
                || operType == null) {
            return ABJSONResult.errorMsg("");
        }

        // 1. 如果operType 没有对应的枚举值，则直接抛出空错误信息
        if (StringUtils.isBlank(OperatorFriendRequestTypeEnum.getMsgByType(operType))) {
            return ABJSONResult.errorMsg("");
        }

        if (operType == OperatorFriendRequestTypeEnum.IGNORE.type) {
            // 2. 判断如果忽略好友请求，则直接删除好友请求的数据库表记录
            userService.deleteFriendRequest(sendUserId, acceptUserId);
        } else if (operType == OperatorFriendRequestTypeEnum.PASS.type) {
            // 3. 判断如果是通过好友请求，则互相增加好友记录到数据库对应的表
            //	   然后删除好友请求的数据库表记录
            userService.passFriendRequest(sendUserId, acceptUserId);
        }

        // 4. 数据库查询好友列表
        List<MyFriendsVO> myFirends = userService.queryMyFriends(acceptUserId);

        return ABJSONResult.ok(myFirends);
    }

}
