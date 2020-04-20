package ab.tjl.controller;

import ab.tjl.pojo.Users;
import ab.tjl.pojo.vo.UsersVO;
import ab.tjl.service.UserService;
import ab.tjl.utils.ABJSONResult;
import ab.tjl.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
