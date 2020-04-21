package ab.tjl.mapper;

import ab.tjl.pojo.Users;
import ab.tjl.pojo.vo.FriendRequestVO;
import ab.tjl.pojo.vo.MyFriendsVO;
import ab.tjl.utils.MyMapper;

import java.util.List;

/**
 * @Author:TangJiLin
 * @Description: 多表关联接口
 * @Date: Created in 2020/4/21 19:44
 * @Modified By:
 */
public interface UsersMapperCustom extends MyMapper<Users> {

    /**
     * 查找添加好友请求列表
     * @param acceptUserId
     * @return
     */
    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    List<MyFriendsVO> queryMyFriends(String userId);

    void batchUpdateMsgSigned(List<String> msgIdList);
}
