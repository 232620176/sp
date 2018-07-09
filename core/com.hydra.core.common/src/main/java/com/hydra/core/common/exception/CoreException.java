/**<p>Title: CoreException.java</p>
 * @Package com.lakala.core.common.exception
 * <p>Description: TODO</p>
 * @author Hydra wangshuang@lakala.com
 * @date 2018年3月23日 下午2:27:31
 * @version V1.0
 */
package com.hydra.core.common.exception;

import java.util.Map;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.hydra.core.common.util.MapUtil;

/**核心异常
 * <p>
 * Created on 2018年3月23日 下午2:27:31
 * </p>
 * @author Hydra wangshuang@lakala.com
 * @since 2018年3月23日
 */
public class CoreException extends RuntimeException implements MessageSourceResolvable{
	/**
	 * <p>Title: getCode</p>
	 * <p>Description: 获取错误码</p>
	 * @return String
	 */
	public String getCode() {
		return messageSourceResolvable.getCode();
	}
	
	/* (non-Javadoc)
	 * @Title: getCodes
	 * @Description: TODO
	 * @return
	 * @see org.springframework.context.MessageSourceResolvable#getCodes()
	 */
	@Override
	public String[] getCodes() {
		return messageSourceResolvable.getCodes();
	}
	
	/* (non-Javadoc)
	 * @Title: getArguments
	 * @Description: TODO
	 * @return
	 * @see org.springframework.context.MessageSourceResolvable#getArguments()
	 */
	@Override
	public Object[] getArguments() {
		return messageSourceResolvable.getArguments();
	}
	
	/* (non-Javadoc)
	 * @Title: getDefaultMessage
	 * @Description: TODO
	 * @return
	 * @see org.springframework.context.MessageSourceResolvable#getDefaultMessage()
	 */
	@Override
	public String getDefaultMessage() {
		return messageSourceResolvable.getDefaultMessage();
	}
	
	public CoreException(String code) {
		super(code);
		messageSourceResolvable = new DefaultMessageSourceResolvable(code);
	}
	
	public CoreException(String code, Object[] args) {
		super(code);
		messageSourceResolvable = new DefaultMessageSourceResolvable(new String[]{code}, args);
	}
	
	CoreException(String code, String msg) {
		super(code);
		messageSourceResolvable = new DefaultMessageSourceResolvable(new String[]{code}, null, msg);
	}
	
	CoreException(String code, Object[] args, String msg) {
		super(code);
		messageSourceResolvable = new DefaultMessageSourceResolvable(new String[]{code}, args, msg);
	}
	
	CoreException(String code, Throwable error) {
		super(code, error);
		messageSourceResolvable = new DefaultMessageSourceResolvable(new String[]{code}, null, error.getMessage());
	}
	
	CoreException(String code, Object[] args, Throwable error) {
		super(code, error);
		messageSourceResolvable = new DefaultMessageSourceResolvable(new String[]{code}, args, error.getMessage());
	}
	
	CoreException(String code, String msg, Throwable error) {
		super(code, error);
		messageSourceResolvable = new DefaultMessageSourceResolvable(new String[]{code}, new Object[]{error.getClass().getName()}, msg);
	}
	
	CoreException(String code, Object[] args, String msg, Throwable error) {
		super(code, error);
		messageSourceResolvable = new DefaultMessageSourceResolvable(new String[]{code}, args, msg);
	}
	
	public final Map<String, Object> _ErrorData = MapUtil.getMap();
	private final DefaultMessageSourceResolvable messageSourceResolvable;
	/**@Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -2887735087769111818L;
}
