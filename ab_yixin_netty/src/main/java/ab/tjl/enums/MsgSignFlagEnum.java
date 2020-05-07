package ab.tjl.enums;

/**
 * @Author:TangJiLin
 * @Description: 消息签收状态 枚举
 * @Date: Created in 2020/5/7 20:34
 * @Modified By:
 */
public enum MsgSignFlagEnum {
	
	unsign(0, "未签收"),
	signed(1, "已签收");	
	
	public final Integer type;
	public final String content;
	
	MsgSignFlagEnum(Integer type, String content){
		this.type = type;
		this.content = content;
	}
	
	public Integer getType() {
		return type;
	}  
}
