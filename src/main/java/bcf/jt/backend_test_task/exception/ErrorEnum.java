package bcf.jt.backend_test_task.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorEnum {

    COUNTRY_NOT_FOUND("001", "Country not found", HttpStatus.NOT_FOUND),
    ROAD_NOT_FOUND("002", "Road not found", HttpStatus.NOT_FOUND),
    FILE_NOT_FOUND("003", "File not found", HttpStatus.NOT_FOUND),
    COUNTRY_NAME_IS_INVALID("004", "Name of country can not be empty or null!", HttpStatus.BAD_REQUEST),
    COUNTRY_NAME_LENGTH_IS_INVALID("005", "Country name should have 3 letters", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorEnum(String errorCode, String message, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
