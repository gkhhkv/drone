package com.md.basePlatform.exception;

import com.md.basePlatform.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * <p>使用 {@code @RestControllerAdvice} 拦截所有 @Controller/@RestController 抛出的异常，
 * 将其转换为统一的 {@link ApiResponse} JSON 格式返回给前端，避免异常栈泄露。
 *
 * <p>处理的异常类型：
 * <ul>
 *   <li>{@link BusinessException} — 业务逻辑异常 → HTTP 400</li>
 *   <li>{@link MethodArgumentNotValidException} — 参数校验失败（@Valid 触发） → HTTP 400</li>
 * </ul>
 *
 * <p>如需添加新的异常处理，只需新增一个 @ExceptionHandler 方法即可。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * <p>返回 HTTP 400 Bad Request，body 中包含业务错误信息。
     *
     * @param ex 业务异常对象
     * @return 包含错误信息的 ApiResponse
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        // BAD_REQUEST = 400，表示客户端请求有误
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(ex.getMessage()));
    }

    /**
     * 处理参数校验异常
     * <p>当 @Valid 标注的请求体校验失败时，Spring 抛出 MethodArgumentNotValidException。
     * 本方法提取第一个校验失败的字段和错误消息返回给前端。
     *
     * @param ex 参数校验异常对象
     * @return 包含字段校验错误信息的 ApiResponse（如 "batteryPercent 不能大于100"）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        // 获取第一个校验失败的字段错误
        FieldError fieldError = ex.getBindingResult().getFieldError();
        // 拼接友好的错误消息：字段名 + 校验消息
        String message = fieldError == null ? "参数校验失败" : fieldError.getField() + " " + fieldError.getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(message));
    }
}
