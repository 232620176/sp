/**<p>Title: Response.java</p>
 * @Package com.lakala.core.common.response
 * <p>Description: TODO</p>
 * @author Hydra wangshuang@lakala.com
 * @date 2018年3月23日 下午4:25:50
 * @version V1.0
 */
package com.hydra.core.common.response;

import com.hydra.core.common.cons.ReturnCode;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**TODO
 * <p>
 * Created on 2018年3月23日 下午4:25:50
 * </p>
 * @author Hydra wangshuang@lakala.com
 * @since 2018年3月23日
 */
@Data
@ToString
@NoArgsConstructor
public class Response<T> {
	private String _ReturnCode;
	private String _ReturnMSG;
	private T _ReturnData;
	
	public boolean isSuccessful(){
		return ReturnCode.RET_CODE_000000.equals(_ReturnCode);
	}
}
