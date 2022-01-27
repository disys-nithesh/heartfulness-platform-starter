package org.heartfulness.starter.domain.exception;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class InvalidRequestException extends RuntimeException {
    private static final Metadata.Key<String> ERROR_CODE = Metadata.Key.of("error-code",
            Metadata.ASCII_STRING_MARSHALLER);

    private Status status;
    private ErrorCode errorCode;

    public InvalidRequestException(String message) {
        this(ErrorCode.UNSPECIFIED, message, Status.INVALID_ARGUMENT);
    }

    public InvalidRequestException(ErrorCode errorCode, String message) {
        this(errorCode, message, Status.INVALID_ARGUMENT);
    }

    public InvalidRequestException(ErrorCode errorCode, String message, Status status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public StatusRuntimeException toGRPCException() {
        Metadata metadata = new Metadata();
        metadata.put(ERROR_CODE, errorCode.name());
        return status.withDescription(getMessage()).asRuntimeException(metadata);
    }
}
