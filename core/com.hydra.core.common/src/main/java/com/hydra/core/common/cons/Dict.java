package com.hydra.core.common.cons;

public class Dict {
	public static final char[] HEXADECIMAL_CHAR = { '0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	public static final char[] HEXADECIMAL_CHAR_UPPER = { '0', '1', '2', '3',
		'4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	
	public static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static final String _GUID = "_Guid";// 请求流水号
	
	public static final String _PARAM_MAP = "_paramMap";// 参数
	
	public static final String _RETURNCODE = "_ReturnCode";// 交易状态码
	
	public static final String _RETURNDATA = "_ReturnData";// 返回数据
	
	public static final String _RETURNMSG = "_ReturnMsg";// 交易信息
	
	public static final String BROKEN_BAR = "\\|";// 竖杠
	
	public static final String IS_END = "isEnd";// 是否末尾
	
	public static final String MASK_FILL = "*";// 脱敏填充
	
	public static final String RANDOM = "random";// 随机数
	
	public static final String RESULT = "result";// 结果
	
	public static final String USER_ID = "userId";// 用户ID
	
	public static final String USERNAME = "username";// 用户ID
	
	// 静态工具类，防误生成
	private Dict(){throw new UnsupportedOperationException();}
}
