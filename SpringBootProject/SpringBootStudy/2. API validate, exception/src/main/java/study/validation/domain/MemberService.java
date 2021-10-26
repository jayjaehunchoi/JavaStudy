package study.validation.domain;

import java.util.List;

public interface MemberService {
    List<Member> findAllMembers();
    void save(Member member);
    Member findByIdAndPassword(String name, String password);
}
