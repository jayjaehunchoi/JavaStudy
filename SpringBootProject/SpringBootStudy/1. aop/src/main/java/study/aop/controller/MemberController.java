package study.aop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.aop.annotation.Login;
import study.aop.annotation.TimeChecker;
import study.aop.domain.Member;
import study.aop.dto.MemberDto;
import study.aop.domain.MemberService;
import study.aop.dto.ResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static study.aop.SessionConst.SESSION_ID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @TimeChecker
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberDto dto){
        Member member = Member.builder()
                        .name(dto.getName())
                        .password(dto.getPassword())
                        .build();
        memberService.save(member);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody MemberDto dto, HttpServletRequest request){
        Member findMember = memberService.findByIdAndPassword(dto.getName(), dto.getPassword());
        if(findMember == null){
            ResponseDto<Object> responseDto = ResponseDto.builder().error("Sign in failed").build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_ID,findMember);
        MemberDto memberDto = MemberDto.builder().name(findMember.getName()).password(findMember.getPassword()).build();
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findAll(){
        List<Member> members = memberService.findAllMembers();
        ResponseDto dto = makeDto(members);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){

        HttpSession session = request.getSession(false);
        session.invalidate();
        return "로그아웃 성공";
    }

    @GetMapping("/error")
    public String error(){
        return "로그인 하세요";
    }

    @GetMapping("/my")
    public ResponseEntity<?> myPage(@Login Member member){
        if (member == null){
            ResponseDto<Object> responseDto = ResponseDto.builder().error("로그인 하세요").build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        MemberDto memberDto = MemberDto.builder().name(member.getName()).password(member.getPassword()).build();
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }
    private ResponseDto makeDto(List<Member> members) {
        List<MemberDto> responseDto = members.stream().map(MemberDto::new).collect(Collectors.toList());
        return ResponseDto.<MemberDto>builder().data(responseDto).build();
    }

}
