package com.zxrh.ehomework.common.pojo;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.zxrh.ehomework.common.constant.Default;
import com.zxrh.ehomework.common.constant.MapKey;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler{

	@ExceptionHandler(UnauthenticatedException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Object> handleUnauthenticatedException(UnauthenticatedException e){
		return new Result<>().code(MapKey.INVALID_TOKEN).message(e.getMessage());
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
		return new Result<>().code(MapKey.INVALID_METHOD).message("不支持的请求类型");
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> handle(MethodArgumentTypeMismatchException e){
        return new Result<>().code(MapKey.INVALID_PARAMETER).message("参数类型不匹配");
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> handleConstraintViolationException(ConstraintViolationException cve){
        StringBuilder message = new StringBuilder("参数千万条，规范第一条，入参不规范，返回两行泪：");
        Set<ConstraintViolation<?>> violations = cve.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = path.toString().split(Default.PERIOD);
            message.append(pathArr[1]).append(violation.getMessage()).append(Default.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new Result<>().code(MapKey.INVALID_PARAMETER).message(message.toString());
    }
	
	@ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> validExceptionHandler(BindException e) {
		StringBuilder message = new StringBuilder("参数千万条，规范第一条，入参不规范，返回两行泪：");
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(Default.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new Result<>().code(MapKey.INVALID_PARAMETER).message(message.toString());
    }

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Object> handleUnauthorizedException(UnauthorizedException e){
		return new Result<>().code(MapKey.NOT_PERMITTED).message(e.getMessage());
	}
	
	@ExceptionHandler(FailException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result<Object> handleFailException(FailException e){
		return new Result<>().code(MapKey.FAIL).message(e.getMessage());
	}
	
}