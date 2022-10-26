package bcf.jt.backend_test_task.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    @JsonProperty("cca3")
    private String shortCountryName;

    @JsonProperty("borders")
    private List<String> borders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country country = (Country) o;
        return getShortCountryName().equals(country.getShortCountryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getShortCountryName());
    }
}
