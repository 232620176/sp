package com.hydra.core.common.util.json;


public interface Conversion{
	Conversion DEFAULT = DefaultConversion.getInstance();
	boolean canConvert(Class<?> from, Class<?> to);
	<To> To convert(Object in, Class<To> to) throws ConversionException;
	<To> To convert(Object in, Class<To> to, String format) throws ConversionException;
	<From,To> To convert(Object in, Class<From> from, Class<To> to, String format) throws ConversionException;
}
