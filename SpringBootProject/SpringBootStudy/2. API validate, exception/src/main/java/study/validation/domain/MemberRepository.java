package study.validation.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByNameAndPassword(String name, String password);
}
