package study.validation.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static study.validation.SessionConst.*;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[PreHandle] 로그인 체커 시작");
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(SESSION_ID) == null){
            response.sendRedirect("/member/error");
            return false;
        }
        return true;
    }
}
