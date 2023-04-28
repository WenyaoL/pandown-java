package com.pan.pandown.web.config.exception;

import com.pan.pandown.util.baseResp.FailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author yalier(wenyao)
 * @since 2023/4/28
 * @Description
 */
@RestControllerAdvice
@Slf4j
public class ExceptionResponse {

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    public FailResponse exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);

        //针对@Valid注解抛出的数据校验异常做处理
        if (e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException error = (MethodArgumentNotValidException)e;
            AtomicReference<String> msg = new AtomicReference<>("");
            List<FieldError> fieldErrors = error.getBindingResult().getFieldErrors();
            fieldErrors.forEach(err ->{
                String field = err.getField();
                String defaultMessage = err.getDefaultMessage();
                msg.set(msg + defaultMessage + "\n");
            });
            return new FailResponse(msg.get());
        }

        return new FailResponse(e);

    }

}
