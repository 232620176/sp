/**<p>Title: EnviromentInfo.java</p>
 * @Package com.lakala.core.common.service.captcha
 * <p>Description: TODO</p>
 * @author Hydra wangshuang@lakala.com
 * @date 2018年4月4日 下午3:31:23
 * @version V1.0
 */
package com.hydra.core.common.service;

import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**TODO
 * <p>
 * Created on 2018年4月4日 下午3:31:23
 * </p>
 * @author Hydra wangshuang@lakala.com
 * @since 2018年4月4日
 */
@Slf4j
@Component
@ConditionalOnProperty(name="lakala.environment.show", havingValue="true")
@Order(0)
public class EnvironmentInfo implements EnvironmentAware {
	/* (non-Javadoc)
	 * @Title: setEnvironment
	 * @Description: TODO
	 * @param environment
	 * @see org.springframework.context.EnvironmentAware#setEnvironment(org.springframework.core.env.Environment)
	 */
	@Override
	public void setEnvironment(Environment environment) {
		final Properties properties = System.getProperties();
		try {
			Formatter formatter = new Formatter();
			formatter.format("%n%s%n", LINE_SEPARATOR);
			formatter.format("EnvironmentBanner %s%n", "begin");
			formatter.format("Version: %s%n", "1.0.0");
			
			formatter.format("Java Home: %s%n", properties.get("java.home"));
			formatter.format("Java Vendor: %s%n", properties.get("java.vendor"));
			formatter.format("Java Version: %s%n", properties.get("java.version"));
			final Runtime runtime = Runtime.getRuntime();
			formatter.format("JVM Free Memory: %s%n", FileUtils.byteCountToDisplaySize(runtime.freeMemory()));
			formatter.format("JVM Maximum Memory: %s%n", FileUtils.byteCountToDisplaySize(runtime.maxMemory()));
			formatter.format("JVM Total Memory: %s%n", FileUtils.byteCountToDisplaySize(runtime.totalMemory()));
			formatter.format("%s%n", LINE_SEPARATOR);
			
			formatter.format("OS Architecture: %s%n", properties.get("os.arch"));
			formatter.format("OS Name: %s%n", properties.get("os.name"));
			formatter.format("OS Version: %s%n", properties.get("os.version"));
			formatter.format("OS Date/Time: %s%n", LocalDateTime.now());
			formatter.format("OS Temp Directory: %s%n", FileUtils.getTempDirectoryPath());
			formatter.format("EnvironmentBanner %s%n", " end ");
			formatter.format("%n%s%n", LINE_SEPARATOR);
			
			log.warn(formatter.toString());
			formatter.close();
		}catch(Exception e){}
	}
	
	public static final String LINE_SEPARATOR = "------------------------------------------------------------";
}
