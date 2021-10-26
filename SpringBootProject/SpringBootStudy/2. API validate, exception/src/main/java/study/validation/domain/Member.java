package study.validation.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.validation.dto.MemberDto;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String password;
    private String email;
    private Integer age;

    @Builder
    public Member(String name, String password, String email ,Integer age) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.age = age;
    }

    public void setEncodedPassword(String password){
        this.password = password;
    }

    public Member(final MemberDto dto){
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.age = dto.getAge();
    }
}
