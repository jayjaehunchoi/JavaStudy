package study.validation.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByNameAndPassword(String name, String password);
    Member findByName(String name);
    Member findByEmail(String email);
}
