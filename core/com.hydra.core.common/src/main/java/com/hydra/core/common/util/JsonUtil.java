package com.hydra.core.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hydra.core.common.util.json.Conversion;

public class JsonUtil {
	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.getSerializationConfig().with(new SimpleDateFormat() {
			private static final long serialVersionUID = 727686277241170700L;
			
			public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
				String str = Conversion.DEFAULT.convert(date, String.class);
				return toAppendTo.append(str);
			}
			
			public Date parse(String source, ParsePosition pos) {
				return null;
			}
		});
		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
	}
	
	private static final Map<String, JsonEncoding> encodings = MapUtil.getMap();
	
	static {
		encodings.put("UTF-8", JsonEncoding.UTF8);
		encodings.put("UTF16-BE", JsonEncoding.UTF16_BE);
		encodings.put("UTF16-LE", JsonEncoding.UTF16_LE);
		encodings.put("UTF32-BE", JsonEncoding.UTF32_BE);
		encodings.put("UTF32-LE", JsonEncoding.UTF32_LE);
	}
	
	public static byte[] jsonFromObject(Object object, String encoding) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JsonGenerator json = null;
		try {
			JsonEncoding enc = encodings.get(encoding);
			if(enc == null)
				throw new IllegalStateException("unsupport encoding: " + encoding);
			json = mapper.getFactory().createGenerator(out, enc);
			json.writeObject(object);
			json.flush();
			//mapper.writeValue(writer, object);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			log.error("Unable to serialize to json: " + object, e);
			return null;
		} finally {
			if(json!=null)
				try {
					json.close();
				} catch (IOException e) {
					//
				}
		}
		return out.toByteArray();
	}
	
	public static <T> T objectFromJson(InputStream json, Class<T> klass) {
		T object;
		JsonParser parser = null;
		try {
			parser = mapper.getFactory().createParser(json);
			object = parser.readValueAs(klass);
			//object = mapper.readValue(json, klass);
		} catch (RuntimeException e) {
			log.error("Runtime exception during deserializing " + klass.getSimpleName());
			throw e;
		} catch (Exception e) {
			log.error("Exception during deserializing " + klass.getSimpleName());
			return null;
		} finally {
			if(parser != null) {
				try {
					parser.close();
				} catch (IOException e) {
					//
				}
			}
		}
		return object;
	}
	
	public static <T> T objectFromJson(String json, Class<T> klass) {
		T object;
		JsonParser parser = null;
		try {
			parser = mapper.getFactory().createParser(json);
			object = parser.readValueAs(klass);
			//object = mapper.readValue(json, klass);
		} catch (RuntimeException e) {
			log.error("Runtime exception during deserializing " + klass.getSimpleName() + " from " + abbreviate(json, 80));
			throw e;
		} catch (Exception e) {
			log.error("Exception during deserializing " + klass.getSimpleName() + " from " + abbreviate(json, 80));
			return null;
		} finally {
			if(parser != null) {
				try {
					parser.close();
				} catch (IOException e) {
					//
				}
			}
		}
		return object;
	}
	
	/*
	 * parse json allow unquoted control chars
	 */
	public static <T> T objectFromJsonAllowCC(InputStream json, Class<T> klass) {
		T object;
		JsonParser parser = null;
		try {
			parser = mapper.getFactory().createParser(json);
			parser.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			object = parser.readValueAs(klass);
			//object = mapper.readValue(json, klass);
		} catch (RuntimeException e) {
			log.error("Runtime exception during deserializing " + klass.getSimpleName());
			throw e;
		} catch (Exception e) {
			log.error("Exception during deserializing " + klass.getSimpleName());
			return null;
		} finally {
			if(parser != null) {
				try {
					parser.close();
				} catch (IOException e) {
					//
				}
			}
		}
		return object;
	}
	
	/*
	 * parse json allow unquoted control chars
	 */
	public static <T> T objectFromJsonAllowCC(String json, Class<T> klass) {
		T object;
		JsonParser parser = null;
		try {
			parser = mapper.getFactory().createParser(json);
			parser.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			object = parser.readValueAs(klass);
			//object = mapper.readValue(json, klass);
		} catch (RuntimeException e) {
			log.error("Runtime exception during deserializing " + klass.getSimpleName() + " from " + abbreviate(json, 80));
			throw e;
		} catch (Exception e) {
			log.error("Exception during deserializing " + klass.getSimpleName() + " from " + abbreviate(json, 80));
			return null;
		} finally {
			if(parser != null) {
				try {
					parser.close();
				} catch (IOException e) {
					//
				}
			}
		}
		return object;
	}
	
	public static String object2JsonString(Object object) {
		return JSONObject.valueToString(object);
	}
	
	static String abbreviate(String str, int len) {
		if (str == null || str.length() < len)
			return str;
		return str.substring(0, len) + " ...";
	}
}
