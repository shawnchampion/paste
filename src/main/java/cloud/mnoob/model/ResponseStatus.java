package cloud.mnoob.model;

public enum ResponseStatus {
    OK(200, "OK", "接口调用成功并且返回数据"),
    CREATED(201, "Created", "数据创建成功"),
    NO_CONTENT(204, "No Content", "数据删除成功，返回空数据"),
    BAD_REQUEST(400, "Bad Request", "普通格式或参数问题，不能正常解析请求"),
    UNAUTHORIZED(401, "Unauthorized", "用户认证失败，未授权访问"),
    FORBIDDEN(403, "Forbidden", "禁止访问，可能是由于访问过于频繁而被拒绝"),
    NOT_FOUND(404, "Not Found", "该资源不存在"),
    NOT_ACCEPTABLE(406, "Not Acceptable", "请求的格式不被服务器支持"),
    CONFLICT(409, "Conflict", "由于冲突，请求无法完成，通常由于更新资源所需的状态无效"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type", "响应的内容格式是不受支持的"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "服务器内部错误"),

    FILE_ERROR_SPACE_OUT_LIMIT(1001, "知识已经爆满啦~", "服务器可用空间不足"),
    FILE_ERROR_FILE_NOT_EXIST(1002, "知识不见了~", "下载文件不存在"),
    ;

    public final int code;
    public final String message;
    public final String description;

    ResponseStatus(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
