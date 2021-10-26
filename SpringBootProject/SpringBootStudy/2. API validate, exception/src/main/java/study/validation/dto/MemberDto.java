package study.validation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.validation.domain.Member;

import javax.validation.constraints.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDto {

    @NotBlank(message = "아이디를 입력하세요")
    private String name;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "숫자, 문자 , 특수문자를 포함한 8 ~ 16자 비밀번호를 입력하세요")
    private String password;

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "올바른 형식이 아닙니다.")
    private String email;

    @Max(100)
    @NotNull(message = "나이를 입력하세요")
    private Integer age;

    public MemberDto(final Member member) {
        name = member.getName();
        password = member.getPassword();
        email = member.getEmail();
        age = member.getAge();
    }
}
