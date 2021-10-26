package study.validation.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
