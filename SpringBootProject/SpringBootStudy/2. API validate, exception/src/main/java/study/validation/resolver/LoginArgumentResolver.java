package study.validation.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import study.validation.SessionConst;
import study.validation.annotation.Login;
import study.validation.domain.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean isParameterTypeMember = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasParameterAnnotation && isParameterTypeMember;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("[Argument Resolver]resolve Argumnet");
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if(session == null) {
            return null;
        }
        return session.getAttribute(SessionConst.SESSION_ID);
    }
}
