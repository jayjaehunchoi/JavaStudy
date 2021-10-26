package study.validation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponseDto {

    private String code;
    private String field;
    private String defaultMessage;

}
