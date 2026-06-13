package com.md.basePlatform.exception;

/**
 * 业务异常类
 *
 * <p>用于表示业务逻辑层面的异常（如"无人机不存在"、"删除失败"等），
 * 区别于系统异常（NullPointerException、SQLException 等）。
 *
 * <p>由 {@link GlobalExceptionHandler#handleBusinessException} 统一捕获，返回 HTTP 400 + 友好错误消息。
 * 继承自 RuntimeException（非受检异常），无需在方法签名中声明 throws。
 *
 * <p>使用示例：
 * <pre>{@code
 * throw new BusinessException("无人机不存在，id=" + id);
 * }</pre>
 */
public class BusinessException extends RuntimeException {

    /**
     * 创建业务异常
     *
     * @param message 错误描述信息，会直接返回给前端
     */
    public BusinessException(String message) {
        super(message);
    }
}
