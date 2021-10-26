package study.validation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.validation.util.annotation.Login;
import study.validation.util.annotation.TimeChecker;
import study.validation.domain.Member;
import study.validation.dto.MemberDto;
import study.validation.domain.MemberService;
import study.validation.dto.MemberLoginDto;
import study.validation.dto.ResponseDto;
import study.validation.util.exception.DuplicateEmailException;
import study.validation.util.exception.DuplicateIdException;
import study.validation.util.exception.LoginFailedException;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static study.validation.util.constant.SessionConst.SESSION_ID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @TimeChecker
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Validated MemberDto dto){
        Member member = new Member(dto);
        memberService.join(member);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody @Validated MemberLoginDto dto, HttpServletRequest request){
        Member findMember = memberService.checkLoginInfoCorrect(dto.getName(),dto.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_ID,findMember);
        MemberDto memberDto = new MemberDto(findMember);
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findAll(){
        List<Member> members = memberService.findAllMembers();
        ResponseDto dto = createAllMembersDto(members);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){

        HttpSession session = request.getSession(false);
        session.invalidate();
        return "로그아웃 성공";
    }

    @GetMapping("/error")
    public ResponseEntity<?> error(){
        ResponseDto<Object> responseDto = ResponseDto.builder().error("로그인 하세요").build();
        return ResponseEntity.badRequest().body(responseDto);
    }

    @GetMapping("/my")
    public ResponseEntity<?> myPage(@Login Member member){
        if (member == null){
            ResponseDto<Object> responseDto = ResponseDto.builder().error("로그인 하세요").build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        MemberDto memberDto = new MemberDto(member);
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }
    private ResponseDto createAllMembersDto(List<Member> members) {
        List<MemberDto> responseDto = members.stream().map(MemberDto::new).collect(Collectors.toList());
        return ResponseDto.<MemberDto>builder().data(responseDto).build();
    }

}
