/**<p>Title: ControllerAspect.java</p>
 * @Package com.lakala.core.common.aspect
 * <p>Description: TODO</p>
 * @author Hydra wangshuang@lakala.com
 * @date 2018年3月23日 下午5:16:04
 * @version V1.0
 */
package com.hydra.core.common.aspect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hydra.core.common.cons.Dict;
import com.hydra.core.common.util.MapUtil;
import com.hydra.core.common.util.NetUtil;
import com.hydra.core.common.util.StringUtil;

/**TODO
 * <p>
 * Created on 2018年3月23日 下午5:16:04
 * </p>
 * @author Hydra wangshuang@lakala.com
 * @since 2018年3月23日
 */
@Aspect
@Component
@Order(0)
@Slf4j
public class ControllerAspect {
	@Pointcut("execution(* com.hydra..*.*Controller.*(..))")
	public void pointcut() {}
	
	@Around("pointcut()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		long start=System.currentTimeMillis();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String uri = request.getRequestURI();
		String methodName = joinPoint.getSignature().getName();
		String ip = NetUtil.getRealIp();
		log.info("@" + ip + ": process [" + uri + "] [" + methodName + "] start");
		
		//打印参数
		String method = request.getMethod();
		Map<String, Object> res = null;
		if("GET".equals(method)){
			res = getParamMap(request);
		}else if("POST".equals(method)){
			res = postPrarmMap(request);
		}
		if(null != res){
			Object guid = res.get(Dict._GUID);
			if(null == guid){
				res.put(Dict._GUID, StringUtil.getGuid());
			}
			request.setAttribute(Dict._PARAM_MAP, res);
			log.info("request: {}", res);
		}
		
		Object result = joinPoint.proceed();
		long end = System.currentTimeMillis();
		log.info("@" + ip + ": process [" + uri + "] [" + methodName + "] end, cost " + (end - start) + "ms...");
		return result;
	}
	

	private Map<String, Object> getParamMap(HttpServletRequest request) {
		Map<String, Object> res = MapUtil.getMap();
		Map<String, String[]> resMap = request.getParameterMap();
		for(String key : resMap.keySet()){
			String[] val = resMap.get(key);
			if(null != val){
				if(val.length == 1){
					res.put(key, val[0]);
				}else{
					res.put(key, new ArrayList<String>(Arrays.asList(val)));
				}
			}
		}
		return res;
	}
	
	private Map<String, Object> postPrarmMap(HttpServletRequest request){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String body = IOUtils.toString(reader);
			Map<String, Object> res = MapUtil.str2Map(body);
			return res;
		} catch (Exception e) {
			log.error("{}", e);
			return MapUtil.getMap();
		}
	}
}
