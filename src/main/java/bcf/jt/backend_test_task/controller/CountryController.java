package bcf.jt.backend_test_task.controller;

import bcf.jt.backend_test_task.exception.ErrorEnum;
import bcf.jt.backend_test_task.exception.MainException;
import bcf.jt.backend_test_task.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
public class CountryController {
    private final CountryService countryService;

    @GetMapping(value = "/routing/{origin}/{destination}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, List<String>> getAnyPossibleRoad(@PathVariable final String origin, @PathVariable final
    String destination)  {
        try {
            return countryService.getAnyPossibleRoad(origin,destination);
        } catch (MainException exception) {
            throw new MainException(exception.getErrorEnum());
        } catch (Exception exception) {
            log.error("Unexpected error during parsing file");
            throw new MainException(ErrorEnum.FILE_NOT_FOUND);
        }
    }
}
