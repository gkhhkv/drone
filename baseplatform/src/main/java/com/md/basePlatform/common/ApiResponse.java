package com.md.basePlatform.common;

/**
 * 统一接口响应模型 — 所有后端接口的返回格式
 *
 * <p>前后端约定统一的 JSON 响应结构：
 * <pre>{@code
 * {
 *   "success": true/false,   // 业务是否成功
 *   "message": "提示信息",     // 成功时为 "OK"，失败时为具体错误描述
 *   "data": {}               // 业务数据（泛型 T，根据接口不同而变化）
 * }
 * }</pre>
 *
 * <p>前端 axios 响应拦截器（见 {@code frontend/src/api/index.js}）会：
 * <ul>
 *   <li>success=true 时自动提取 data 字段</li>
 *   <li>success=false 时自动抛出 message 中的错误信息</li>
 * </ul>
 *
 * @param <T> 业务数据类型（如 List&lt;Drone&gt;、Drone、Void 等）
 */
public class ApiResponse<T> {

    /** 业务处理是否成功 */
    private boolean success;

    /** 提示信息，成功时默认 "OK"，失败时为错误原因 */
    private String message;

    /** 业务数据，失败时可能为 null */
    private T data;

    /**
     * 构建成功响应（静态工厂方法）
     *
     * @param data 业务数据
     * @param <T>  数据类型
     * @return success=true, message="OK" 的响应对象
     */
    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = "OK";
        response.data = data;
        return response;
    }

    /**
     * 构建失败响应（静态工厂方法）
     *
     * @param message 错误描述信息
     * @param <T>     数据类型（通常为 Void）
     * @return success=false 的响应对象
     */
    public static <T> ApiResponse<T> fail(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        return response;
    }

    // ---- Getters & Setters ----

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
