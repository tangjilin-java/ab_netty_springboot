package ab.tjl.service.impl;

import ab.tjl.mapper.UsersMapper;
import ab.tjl.pojo.Users;
import ab.tjl.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.*;


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
    public Users saveUser(Users user) {
        String userId = sid.nextShort();
        //TODO 为每一个用户生成一个唯一的二维码
        user.setQrcode("");

        user.setId(userId);
        userMapper.insert(user);
        return user;
    }
}
