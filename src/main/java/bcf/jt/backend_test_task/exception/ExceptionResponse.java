package bcf.jt.backend_test_task.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ExceptionResponse {

    private String message;

    private String code;

    private Integer status;

}
