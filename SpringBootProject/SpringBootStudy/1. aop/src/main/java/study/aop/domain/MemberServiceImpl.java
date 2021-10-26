package study.aop.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void save(Member member){
        memberRepository.save(member);
    }

    @Override
    public Member findByIdAndPassword(String name, String password) {
        return memberRepository.findByNameAndPassword(name, password);
    }
}
