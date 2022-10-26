package bcf.jt.backend_test_task;

import bcf.jt.backend_test_task.exception.MainException;
import bcf.jt.backend_test_task.model.Country;
import bcf.jt.backend_test_task.service.CountryService;
import bcf.jt.backend_test_task.service.DataMapperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CountryServiceTest.Configuration.class})
class CountryServiceTest {

    @TestConfiguration
    static class Configuration {
        @Bean
        public DataMapperService dataMapperService() throws IOException {
            DataMapperService dataMapperService = mock(DataMapperService.class);
            Mockito.when(dataMapperService.loadCountriesData(any())).thenReturn(countryList);
            return dataMapperService;
        }

        @Bean
        public CountryService countryService(DataMapperService dataMapperService) {
            return new CountryService(dataMapperService);
        }
    }

    @Autowired
    CountryService countryService;

    private static List<Country> countryList;

    @BeforeAll
    static void beforeAll() {
        var testCountryUkr = new Country("UKR");
        var testCountryPol = new Country("POL");
        var testCountryGer = new Country("GER");
        var testCountryFra = new Country("FRA");
        var testCountryUsa = new Country("USA");
        var testCountryRus = new Country("RUS");
        testCountryUkr.setBorders(List.of(testCountryRus.getShortCountryName(),
                testCountryPol.getShortCountryName()));
        testCountryPol.setBorders(List.of(testCountryUkr.getShortCountryName(),
                testCountryGer.getShortCountryName()));
        testCountryGer.setBorders(List.of(testCountryFra.getShortCountryName(),
                testCountryPol.getShortCountryName()));
        testCountryRus.setBorders(List.of(testCountryUkr.getShortCountryName()));
        testCountryFra.setBorders(List.of(testCountryGer.getShortCountryName()));
        countryList = List.of(testCountryFra, testCountryGer, testCountryPol, testCountryRus,
                testCountryUsa, testCountryUkr);
    }

    static Stream<Arguments> getAnyPossibleRoadData() {
        return Stream.of(
                Arguments.of("UKR", "POL", Map.of("route", List.of("UKR", "POL"))),
                Arguments.of("UKR", "GER", Map.of("route", List.of("UKR", "POL", "GER"))),
                Arguments.of("UKR", "UKR", Map.of("route", List.of("UKR"))),
                Arguments.of("RUS", "fra", Map.of("route", List.of("RUS","UKR","POL","GER","FRA"))),
                Arguments.of("ruS", "GeR", Map.of("route", List.of("RUS", "UKR", "POL", "GER")))
        );
    }

    static Stream<Arguments> getAnyPossibleRoadDataWithExceptionScenario() {
        return Stream.of(
                Arguments.of("UKR", "USA"),
                Arguments.of("GER", "USA")
        );
    }

    @ParameterizedTest
    @MethodSource("getAnyPossibleRoadData")
    void getAnyPossibleRoadShouldReturnValidMap(String origin, String destination,
                                                Map<String, List<String>> expectedCountryMap) throws IOException {
        Assertions.assertEquals(expectedCountryMap, countryService
                .getAnyPossibleRoad(origin, destination));
    }

    @ParameterizedTest
    @MethodSource("getAnyPossibleRoadDataWithExceptionScenario")
    void getAnyPossibleRoadShouldThrowException(String origin, String destination) {
        var exception = Assertions.assertThrows(MainException.class, () -> countryService
                .getAnyPossibleRoad(origin, destination));
        var expectedMessage = "Road not found";
        var expectedCode = "002";
        var expectedStatus = HttpStatus.NOT_FOUND;
        Assertions.assertEquals(expectedMessage, exception.getMessage());
        Assertions.assertEquals(expectedCode, exception.getErrorEnum().getErrorCode());
        Assertions.assertEquals(expectedStatus, exception.getErrorEnum().getHttpStatus());
    }


}
