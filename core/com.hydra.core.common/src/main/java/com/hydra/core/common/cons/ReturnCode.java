package com.hydra.core.common.cons;

public class ReturnCode {
	public static final String ERROR_CODE_F00001 = "F00001";// 文件上传大小超过限制
	
	public static final String ERROR_CODE_F00002 = "F00002";// 文件上传错误
	
	public static final String ERROR_CODE_RS0000 = "RS0000";// 默认异常返回码
	
	public static final String RET_CODE_0000 = "0000";// 四位成功返回码
	
	public static final String RET_CODE_000000 = "000000";// 六位成功返回码
	
	// 静态工具类，防误生成
	private ReturnCode(){throw new UnsupportedOperationException();}
}
