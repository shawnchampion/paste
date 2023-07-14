package cloud.mnoob.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PasteException extends Exception {
    private int errorCode;
    private String errorMessage;

    public PasteException(ResponseStatus responseStatus) {
        this.errorCode = responseStatus.code;
        this.errorMessage = responseStatus.message;
    }

    public PasteException(ResponseStatus responseStatus, String errorMessage) {
        this.errorCode = responseStatus.code;
        this.errorMessage = errorMessage;
    }
}
