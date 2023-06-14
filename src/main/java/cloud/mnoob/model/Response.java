package cloud.mnoob.model;

import lombok.Data;

@Data
public class Response<T> {
    private Integer code;
    private String message;
    private T data;

    public Response(T data) {
        this.code = ResponseStatus.OK.code;
        this.message = ResponseStatus.OK.message;
        this.data = data;
    }

    public Response(ResponseStatus status) {
        this.code = status.code;
        this.message = status.message;
    }

    public Response(ResponseStatus status, String message) {
        this.code = status.code;
        this.message = message;
    }
}
