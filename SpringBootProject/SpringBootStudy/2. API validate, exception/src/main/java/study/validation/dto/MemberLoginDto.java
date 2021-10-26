package study.validation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.validation.domain.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberLoginDto {

    @NotBlank(message = "아이디를 입력하세요")
    private String name;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "숫자, 문자 , 특수문자를 포함한 8 ~ 16자 비밀번호를 입력하세요")
    private String password;

    public MemberLoginDto(final Member member) {
        name = member.getName();
        password = member.getPassword();
    }
}
