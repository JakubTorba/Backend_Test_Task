package bcf.jt.backend_test_task.exception;

import lombok.Getter;

@Getter
public class MainException extends RuntimeException {

    private final ErrorEnum errorEnum;

    public MainException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.errorEnum = errorEnum;
    }
}