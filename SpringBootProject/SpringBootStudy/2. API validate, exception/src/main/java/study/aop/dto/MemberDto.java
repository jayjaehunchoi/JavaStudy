package study.aop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.aop.domain.Member;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDto {
    private String name;
    private String password;

    public MemberDto(final Member member) {
        name = member.getName();
        password = member.getPassword();
    }
}
