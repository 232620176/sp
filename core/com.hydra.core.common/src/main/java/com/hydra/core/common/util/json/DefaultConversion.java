package com.hydra.core.common.util.json;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.ClassUtils;

import com.hydra.core.common.util.CommonUtil;
import com.hydra.core.common.util.MapUtil;

public class DefaultConversion implements Conversion {
	static final class DateToDateConverterFactory implements ConverterFactory<java.util.Date, java.util.Date> {
		public <T extends java.util.Date> Converter<java.util.Date, T> getConverter(Class<T> targetType) {
			return new DateToDate<T>(targetType);
		}
		
		private final class DateToDate<T extends java.util.Date> implements Converter<java.util.Date, T> {
			private final Class<T> targetType;
			
			public DateToDate(Class<T> targetType) {
				this.targetType = targetType;
			}
			
			public T convert(java.util.Date source) {
				if(targetType == source.getClass())
					return CommonUtil.transform(source);
				if(targetType == java.sql.Date.class)
					return CommonUtil.transform(new java.sql.Date(source.getTime()));
				if(targetType == java.sql.Time.class)
					return CommonUtil.transform(new java.sql.Time(source.getTime()));
				if(targetType == java.sql.Timestamp.class)
					return CommonUtil.transform(new java.sql.Timestamp(source.getTime()));
				throw new ConversionFailedException(TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType), source, null);
			}
		}
	}
	
	final static class DateToLongConverter implements Converter<java.util.Date, Long> {
		public Long convert(java.util.Date source) {
			return source.getTime();
		}
	}
	
	static final class LongToDateConverterFactory implements ConverterFactory<Long, java.util.Date> {
		public <T extends java.util.Date> Converter<Long, T> getConverter(Class<T> targetType) {
			return new LongToDate<T>(targetType);
		}
		
		private final class LongToDate<T extends java.util.Date> implements Converter<Long, T> {
			private final Class<T> targetType;
			
			public LongToDate(Class<T> targetType) {
				this.targetType = targetType;
			}
			
			public T convert(Long source) {
				if(targetType == java.sql.Date.class)
					return CommonUtil.transform(new java.sql.Date(source));
				if(targetType == java.sql.Time.class)
					return CommonUtil.transform(new java.sql.Time(source));
				if(targetType == java.sql.Timestamp.class)
					return CommonUtil.transform(new java.sql.Timestamp(source));
				throw new ConversionFailedException(TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType), source, null);
			}
		}
	}
	
	static final  class StringToTimeZoneConverter implements Converter<String, TimeZone> {
		public TimeZone convert(String source) {
			return TimeZone.getTimeZone(source);
		}
	}
	
	static final  class TimeZoneToStringConverter implements Converter<TimeZone, String> {
		public String convert(TimeZone source) {
			return source.getID();
		}
	}
	
	private static DefaultConversion instances = new DefaultConversion();
	
	public static DefaultConversion getInstance() {
		return instances;
	}
	
	private Map<String, String> formats;
	
	private Set<?> converters;
	
	private ConversionService conversionService;
	
	private static ThreadLocal<Map<String, Format>> currentFormats = new ThreadLocal<Map<String, Format>>() {
		protected Map<String, Format> initialValue() {
			return MapUtil.getMap();
		}
	};
	
	DefaultConversion() {
		GenericConversionService svr = new GenericConversionService();
		DefaultConversionService.addDefaultConverters(svr);
		svr.addConverter(new StringToTimeZoneConverter());
		svr.addConverter(new TimeZoneToStringConverter());
		svr.addConverter(new DateToLongConverter());
		svr.addConverterFactory(new DateToDateConverterFactory());
		svr.addConverterFactory(new LongToDateConverterFactory());
		this.conversionService = svr;
	}
	
	public DefaultConversion(ConversionService conversion) {
		this.conversionService = conversion;
	}
	
	public void setFormats(Map<String, String> formats) {
		this.formats = formats;
	}
	
	public void setConverters(Set<?> converters) {
		this.converters = converters;
		if (converters != null && this.conversionService instanceof ConverterRegistry)
			ConversionServiceFactory.registerConverters(this.converters, (ConverterRegistry) this.conversionService);
	}
	
	public boolean canConvert(Class<?> from, Class<?> to) {
		return conversionService.canConvert(from, to) || getDefaultFormat(to == String.class ? from : to) != null;
	}
	
	public <To> To convert(Object in, Class<To> to) throws ConversionException {
		return convert(in, to, null);
	}
	
	public <To> To convert(Object in, Class<To> to, String format) throws ConversionException {
		if (in == null)
			return null;
		return convert(in, in.getClass(), to, format);
	}
	
	private String getDefaultFormat(Class<?> type) {
		if (java.util.Date.class.isAssignableFrom(type)){
			if(type == java.sql.Date.class)
				return "yyyy-MM-dd";
			if(type == java.sql.Time.class)
				return "HH:mm:ss";
			if(type == java.sql.Timestamp.class)
				return "yyyy-MM-dd HH:mm:ss";
			return "yyyy-MM-dd HH:mm:ss";
		} else if (Number.class.isAssignableFrom(type)) {
			if(type == Float.class || type == Double.class || type == java.math.BigDecimal.class)
				return "#,###.##";
			else
				return "#,###";
		} else {
			return null;
		}
	}
	
	public <From, To> To convert(Object in, Class<From> from, Class<To> to, String format) throws ConversionException {
		if (in == null)
			return null;
		if (format == null) {
			if(conversionService.canConvert(from, to)) {
				return conversionService.convert(in, to);
			} else {
				format = getDefaultFormat(to == String.class ? from : to);
			}
		}
		if(format != null) {
			if (to == String.class) {
				String tmp = doFormat(in, from, format);
				return CommonUtil.transform(tmp);
			}
			if (from == String.class) {
				Object tmp = doParse((String) in, to, format);
				if(tmp.getClass() == to)
					return CommonUtil.transform(tmp);
				return conversionService.convert(tmp, to);
			}
		}
		throw new ConversionException("unsupport conversion: " + from.getName() + " -> " + to.getName());
	}
	
	private void checkFormat(Format fmt, Class<?> type) {
		boolean valid = false;
		if (java.util.Date.class.isAssignableFrom(type)) {
			valid = fmt instanceof DateFormat;
		} else if (Number.class.isAssignableFrom(type)) {
			valid = fmt instanceof NumberFormat;
		} else if (type.isArray()) {
			valid = fmt instanceof MessageFormat;
		}
		if (!valid){
			throw new ConversionException("type not match format, type: " + type + ", format: "
					+ fmt.getClass().getName());
		}
	}
	
	private Format newFormat(String format, Class<?> type) {
		if (java.util.Date.class.isAssignableFrom(type)) {
			return new SimpleDateFormat(format);
		} else if (Number.class.isAssignableFrom(type)) {
			return new DecimalFormat(format);
		} else if (type.isArray()) {
			return new MessageFormat(format);
		}
		throw new ConversionException("type not match format, type: " + type + ", format: " + format);
	}
	
	private Format getFormat(Map<String, Format> formats, String format, Class<?> type) {
		Format fmt = formats.get(format);
		if (fmt != null) {
			checkFormat(fmt, type);
			return fmt;
		}
		return null;
	}
	
	private Format findFormat(String format, Class<?> type) {
		if(type.isPrimitive()) {
			type = ClassUtils.resolvePrimitiveIfNecessary(type);
		}
		Map<String, Format> fmts = currentFormats.get();
		Format fmt = getFormat(fmts, format, type);
		if (fmt == null) {
			String alias = formats == null ? null : formats.get(format);
			if (alias != null) {
				fmt = getFormat(fmts, alias, type);
				if (fmt == null) {
					fmt = newFormat(alias, type);
					fmts.put(format, fmt);
				}
			} else {
				fmt = newFormat(format, type);
				fmts.put(format, fmt);
			}
		}
		return fmt;
	}
	
	protected String doFormat(Object in, Class<?> type, String format) {
		Format fmt = findFormat(format, type);
		return fmt.format(in);
	}
	
	protected Object doParse(String in, Class<?> type, String format) {
		Format fmt = findFormat(format, type);
		try {
			return fmt.parseObject(in);
		} catch (ParseException e) {
			throw new ConversionException("cannot parse to " + type + ", format: " + format + ", value: " + in);
		}
	}
}
