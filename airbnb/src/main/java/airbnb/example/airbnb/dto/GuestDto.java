package airbnb.example.airbnb.dto;
import airbnb.example.airbnb.entity.User;
import airbnb.example.airbnb.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestDto {
    private Long id;
//    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
