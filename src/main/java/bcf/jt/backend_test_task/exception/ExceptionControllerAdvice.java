package bcf.jt.backend_test_task.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MainException.class)
    public ResponseEntity<ExceptionResponse> handlerMainException(MainException exception) {

        return new ResponseEntity<>(ExceptionResponse.builder()
                .message(exception.getMessage())
                .status(exception.getErrorEnum().getHttpStatus().value())
                .code(exception.getErrorEnum().getErrorCode()).build(), exception.getErrorEnum().getHttpStatus());

    }

}
