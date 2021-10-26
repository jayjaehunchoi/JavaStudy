package study.validation.domain;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<Member> findAllMembers();
    void join(Member member);
    Member checkLoginInfoCorrect(String name, String password);
}
