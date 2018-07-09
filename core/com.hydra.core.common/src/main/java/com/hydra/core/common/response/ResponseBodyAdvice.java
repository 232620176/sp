/**<p>Title: ResponseBodyAdvice.java</p>
 * @Package com.lakala.core.common.response
 * <p>Description: TODO</p>
 * @author Hydra wangshuang@lakala.com
 * @date 2018年3月23日 下午4:36:06
 * @version V1.0
 */
package com.hydra.core.common.response;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.hydra.core.common.cons.CONSMSG;
import com.hydra.core.common.cons.Dict;
import com.hydra.core.common.cons.ReturnCode;
import com.hydra.core.common.util.CommonUtil;
import com.hydra.core.common.util.MapUtil;

/**TODO
 * <p>
 * Created on 2018年3月23日 下午4:36:06
 * </p>
 * @author Hydra wangshuang@lakala.com
 * @since 2018年3月23日
 */
@ControllerAdvice
public class ResponseBodyAdvice
	implements org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice<Object>{

	/* (non-Javadoc)
	 * @Title: supports
	 * @Description: TODO
	 * @param returnType
	 * @param converterType
	 * @return
	 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice#supports(org.springframework.core.MethodParameter, java.lang.Class)
	 */
	@Override
	public boolean supports(MethodParameter returnType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	/* (non-Javadoc)
	 * @Title: beforeBodyWrite
	 * @Description: TODO
	 * @param body
	 * @param returnType
	 * @param selectedContentType
	 * @param selectedConverterType
	 * @param request
	 * @param response
	 * @return
	 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice#beforeBodyWrite(java.lang.Object, org.springframework.core.MethodParameter, org.springframework.http.MediaType, java.lang.Class, org.springframework.http.server.ServerHttpRequest, org.springframework.http.server.ServerHttpResponse)
	 */
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType, 
			Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		Map<String, Object> resMap = MapUtil.getMap();
		if (body != null) {
			if(body instanceof byte[]) {//针对文件流处理，如图形验证码、下载文件等
				return body;
			} else if(body instanceof Response) {
				Response<?> res = CommonUtil.transform(body);
				resMap.put(Dict._RETURNCODE, res.get_ReturnCode());
				resMap.put(Dict._RETURNMSG, res.get_ReturnMSG());
				resMap.put(Dict._RETURNDATA, res.get_ReturnData());
				return resMap;
			}
			resMap.put(Dict._RETURNDATA, body);
		}
		if(!resMap.containsKey(Dict._RETURNCODE)){
			resMap.put(Dict._RETURNCODE, ReturnCode.RET_CODE_000000);
			resMap.put(Dict._RETURNMSG, CONSMSG.RET_MSG_SUCCESS);
		}
		return resMap;
	}

}
