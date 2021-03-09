package my.myungjin.academyDemo.domain.member;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class RatingSetConverter implements AttributeConverter<Set<Rating>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Rating> ratings) {
        StringBuilder sb = new StringBuilder();
        for(Rating r : ratings) {
            sb.append(r.getCode())
               .append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public Set<Rating> convertToEntityAttribute(String codes) {
        return Arrays.stream(codes.split(","))
                .map(Rating::of)
                .collect(Collectors.toSet());
    }
}
