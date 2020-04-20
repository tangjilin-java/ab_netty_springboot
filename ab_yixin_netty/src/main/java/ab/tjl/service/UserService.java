package ab.tjl.service;

import ab.tjl.pojo.Users;

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
}
