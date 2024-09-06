package garry.train.common.exception;

import garry.train.common.enums.ResponseEnum;

/**
 * @author Garry
 * 2024-09-06 17:53
 */
public class BusinessException extends RuntimeException {
    private final ResponseEnum responseEnum;

    public BusinessException(ResponseEnum responseEnum) {
        this.responseEnum = responseEnum;
    }

    public ResponseEnum getResponseEnum() {
        return responseEnum;
    }
}
