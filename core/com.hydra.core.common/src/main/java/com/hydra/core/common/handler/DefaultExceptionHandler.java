package com.hydra.core.common.handler;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import com.hydra.core.common.cons.ReturnCode;
import com.hydra.core.common.exception.CoreException;
import com.hydra.core.common.exception.HostException;
import com.hydra.core.common.response.Response;

@ControllerAdvice
@Slf4j
public class DefaultExceptionHandler {
	private MessageSource messageSource;
	private String defaultMessage = ReturnCode.ERROR_CODE_RS0000;
	
	@Autowired
	public DefaultExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Response<?> defaultErrorHandler(HttpServletRequest req, Exception exception) {
		log.error("Exception:{}", exception);
		Response<Object> error = new Response<>();
		String code = null;
		String message = null;
		Object[] arguments = null;
		List<FieldError> errorList = null;
		log.debug("{}", exception);
		if (exception instanceof MultipartException) { //file upload error
			MultipartException ex = (MultipartException) exception;
			Throwable t = ex.getCause();
			if(t instanceof IllegalStateException) {
				Throwable tx = ex.getCause();	
				if(tx.getCause() instanceof FileSizeLimitExceededException) {
					FileSizeLimitExceededException fb = (FileSizeLimitExceededException)t.getCause();
					log.debug("文件名称: " + fb.getFileName() + ", 上传文件实际大小: " + fb.getActualSize()
						+ ", 允许上传文件大小: " + fb.getPermittedSize() + ", 错误信息: " + fb.getMessage());
					code = ReturnCode.ERROR_CODE_F00001; //文件上传大小超过限制
				}
			} else {
				code = ReturnCode.ERROR_CODE_F00002; //文件上传错误
			}
		} else if (exception instanceof BindException) { // on form submit
			BindException bindException = (BindException) exception;
			errorList = bindException.getFieldErrors();
			FieldError f_error = errorList.get(0);
			code = f_error.getDefaultMessage();
			if (code == null) {
				code = defaultMessage;
			}
			message = f_error.getDefaultMessage();
		} else if (exception instanceof MethodArgumentNotValidException) { // on json submit
			MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
			BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
			FieldError f_error = bindingResult.getFieldError();
			code = defaultMessage; // f_error.getField();
			message = f_error.getDefaultMessage();
		} else if (exception instanceof MessageSourceResolvable) {
			MessageSourceResolvable exm = (MessageSourceResolvable)exception;
			if (exm.getCodes() != null) {
				code = exm.getCodes()[0];
			}
			if(exm.getArguments() != null) {
				arguments = exm.getArguments();
			}
			if(exception instanceof CoreException){
				error.set_ReturnData(((CoreException)exception)._ErrorData);
			}
			if(exception instanceof HostException) {//远程调用出错，把远程错误代码和错误信息返回前端
				message = exm.getDefaultMessage();
			}
		} else if (exception instanceof UndeclaredThrowableException) { // on aop error
			UndeclaredThrowableException e = (UndeclaredThrowableException) exception;
			Throwable t = e.getUndeclaredThrowable();
			if (t instanceof CoreException) {
				CoreException c = (CoreException) t;
				code = c.getMessage();
				message = c.getMessage();
			}
		} else {
			code = defaultMessage;
		}
		if (message == null) {
			Locale currentLocale = LocaleContextHolder.getLocale();
			message = messageSource.getMessage(code, arguments, defaultMessage, currentLocale);
		}
		log.error(">>>ReturnCode: " + code + ", retMsg: " + message);
		error.set_ReturnCode(code);
		error.set_ReturnMSG(message);
		return error;
	}
}
