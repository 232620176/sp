/**<p>Title: HostException.java</p>
 * @Package com.lakala.core.common.exception
 * <p>Description: TODO</p>
 * @author Hydra wangshuang@lakala.com
 * @date 2018年3月23日 下午2:40:55
 * @version V1.0
 */
package com.hydra.core.common.exception;

/**TODO
 * <p>
 * Created on 2018年3月23日 下午2:40:55
 * </p>
 * @author Hydra wangshuang@lakala.com
 * @since 2018年3月23日
 */
public class HostException extends CoreException {
	public HostException(String code, String msg) {
		super(code, msg);
	}
	
	public HostException(String code, Object[] args, String msg) {
		super(code, args, msg);
	}
	
	public HostException(String code, Throwable error) {
		super(code, error);
	}
	
	public HostException(String code, Object[] args, Throwable error) {
		super(code, args, error);
	}
	
	public HostException(String code, String msg, Throwable error) {
		super(code, msg, error);
	}
	
	public HostException(String code, Object[] args, String msg, Throwable error) {
		super(code, args, msg, error);
	}
	
	/**@Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 7239904449777618761L;
}
