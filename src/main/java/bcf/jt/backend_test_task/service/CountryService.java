package bcf.jt.backend_test_task.service;

import bcf.jt.backend_test_task.exception.ErrorEnum;
import bcf.jt.backend_test_task.exception.MainException;
import bcf.jt.backend_test_task.model.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;


@Log4j2
@RequiredArgsConstructor
@Primary
@Service
public class CountryService {

    private final DataMapperService dataMapperService;

    @Value("${file.url}")
    private static String fileURL;

    private List<Country> countries;

    @PostConstruct
    public void loadData() throws IOException {
        countries = dataMapperService.loadCountriesData(fileURL);
    }

    public HashMap<String, List<String>> getAnyPossibleRoad(final String origin, final String destination) throws FileNotFoundException {
        checkCountryNameForCountOfLetters(origin, destination);
        final var originCountry = getCountryByName(origin, countries);
        final var destinationCountry = getCountryByName(destination, countries);
        final List<String> possibleRoad = new LinkedList<>();
        if(originCountry != null && destinationCountry != null){
            if (originCountry.equals(destinationCountry)) {
                possibleRoad.add(originCountry.getShortCountryName());
                return convertListOfCountriesToMap(possibleRoad);
            }
        } else {
            throw new MainException(ErrorEnum.COUNTRY_CODE_NOT_FOUND);
        }
        final Map<Country, Integer> distanceToCountry = new HashMap<>();
        distanceToCountry.put(originCountry, 0);
        final Queue<Country> countryQueue = new ArrayDeque<>();
        countryQueue.add(originCountry);
        final List<Country> visitedCountries = new ArrayList<>();
        visitedCountries.add(originCountry);
        final Map<Country, Country> lastVisitedCountry = new HashMap<>();
        lastVisitedCountry.put(originCountry, null);
        while (!countryQueue.isEmpty()) {
            var currentCountry = countryQueue.remove();
            for (String borderCountryName : currentCountry.getBorders()) {
                var borderCountry = countries.stream().
                        filter(country -> country.getShortCountryName().equals(borderCountryName)).findFirst().get();
                if (!visitedCountries.contains(borderCountry)) {
                    visitedCountries.add(borderCountry);
                    countryQueue.add(borderCountry);
                    lastVisitedCountry.put(borderCountry, currentCountry);
                    distanceToCountry.put(borderCountry, distanceToCountry.get(currentCountry) + 1);
                }
                if (borderCountry.equals(destinationCountry)) {
                    possibleRoad.add(borderCountry.getShortCountryName());
                    var mapValue = lastVisitedCountry.get(borderCountry);
                    while (mapValue != null) {
                        possibleRoad.add(mapValue.getShortCountryName());
                        mapValue = lastVisitedCountry.get(mapValue);
                    }
                    Collections.reverse(possibleRoad);
                    return convertListOfCountriesToMap(possibleRoad);
                }
            }
        }
        throw new FileNotFoundException();
    }

    public HashMap<String, List<String>> convertListOfCountriesToMap(List<String> possibleRoad) {
        final var route = new HashMap<String, List<String>>();
        route.put("route", possibleRoad);
        if (route.get("route") == null || route.get("route").isEmpty()) {
            throw new MainException(ErrorEnum.ROAD_NOT_FOUND);
        }
        return route;
    }

    public final void checkCountryNameForNullOrEmptyString(String origin, String destination) {
        if ((origin == null || destination == null) || (origin.isEmpty() || destination.isEmpty())) {
            throw new MainException(ErrorEnum.COUNTRY_NAME_IS_INVALID);
        }
    }

    public final void checkCountryNameForCountOfLetters(String origin, String destination) {
        checkCountryNameForNullOrEmptyString(origin, destination);
        if (origin.codePoints().count() != 3 || destination.codePoints().count() != 3) {
            throw new MainException(ErrorEnum.COUNTRY_NAME_LENGTH_IS_INVALID);
        }
    }

    public final Country getCountryByName(String countryName, List<Country> countries) {
        if (countryName == null) {
            throw new MainException(ErrorEnum.COUNTRY_NAME_IS_INVALID);
        }
        return countries.stream().filter(country -> country.getShortCountryName().equals(countryName.toUpperCase())).findFirst().orElseThrow(()-> new MainException(ErrorEnum.COUNTRY_NOT_FOUND));
    }
}
