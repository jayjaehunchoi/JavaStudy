package study.validation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.validation.util.exception.DuplicateEmailException;
import study.validation.util.exception.DuplicateIdException;
import study.validation.util.exception.LoginFailedException;

import java.util.List;
import java.util.Optional;

import static ch.qos.logback.core.joran.spi.ConsoleTarget.findByName;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void join(Member member){
        saveValidator(member);
        member.setEncodedPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    @Override
    public Member checkLoginInfoCorrect(String name, String password) {
        Member findMember = memberRepository.findAll().stream().filter(member -> member.getName().equals(name)).findAny().orElse(null);
        loginValidator(password, findMember);
        return findMember;
    }

    private void loginValidator(String password, Member findMember) {
        if(findMember == null){
            throw new LoginFailedException("존재하지 않는 아이디 입니다.");
        }
        if(!passwordEncoder.matches(password, findMember.getPassword())){
            throw new LoginFailedException("비밀번호가 틀렸습니다.");
        }
    }

    private void saveValidator(Member member){
        if(memberRepository.findByName(member.getName()) != null){
            throw new DuplicateIdException("중복된 아이디 입니다.");
        }
        if(memberRepository.findByEmail(member.getEmail()) != null){
            throw new DuplicateEmailException("중복된 이메일 입니다");
        }
    }
}
