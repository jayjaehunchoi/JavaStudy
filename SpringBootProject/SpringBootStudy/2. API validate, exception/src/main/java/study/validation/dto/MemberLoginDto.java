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
    private String password;

    public MemberLoginDto(final Member member) {
        name = member.getName();
        password = member.getPassword();
    }
}
