package bcf.jt.backend_test_task.service;

import bcf.jt.backend_test_task.model.Country;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Log4j2
@Primary
@Service
public class DataMapperService {

    public static List<Country> loadCountriesData(String fileURL) throws IOException {
        log.info("Loading data started.");
        final URL url = new URL(fileURL);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        List<Country> countries = mapper.readValue(url, new TypeReference<>() {});
        log.info("Loading data finished.");
        return countries;
    }
}
