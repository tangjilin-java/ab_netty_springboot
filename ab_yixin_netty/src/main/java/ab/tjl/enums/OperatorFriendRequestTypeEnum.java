package ab.tjl.enums;

/**
 * @Author:TangJiLin
 * @Description: 忽略或者通过 好友请求的枚举
 * @Date: Created in 2020/4/21 20:44
 * @Modified By:
 */
public enum OperatorFriendRequestTypeEnum {
	
	IGNORE(0, "忽略"),
	PASS(1, "通过");
	
	public final Integer type;
	public final String msg;
	
	OperatorFriendRequestTypeEnum(Integer type, String msg){
		this.type = type;
		this.msg = msg;
	}
	
	public Integer getType() {
		return type;
	}  
	
	public static String getMsgByType(Integer type) {
		for (OperatorFriendRequestTypeEnum operType : OperatorFriendRequestTypeEnum.values()) {
			if (operType.getType() == type) {
				return operType.msg;
			}
		}
		return null;
	}
	
}
