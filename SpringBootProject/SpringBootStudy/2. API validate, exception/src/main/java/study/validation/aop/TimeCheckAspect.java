package study.validation.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TimeCheckAspect {

    @Around("@annotation(study.validation.annotation.TimeChecker)")
    public Object timeChecker(ProceedingJoinPoint proceedingJoinPoint){
        Object result = null;

        try{
            long start = System.currentTimeMillis();
            result = proceedingJoinPoint.proceed();
            long end = System.currentTimeMillis();
            log.info("[타임 체커 AOP] 소요 시간 {}ms",(end-start));
        }catch (Throwable throwable){
            log.error("타임 체커 오류");
        }
        return result;

    }
//    @Pointcut("execution(* study.aop.controller.MemberController.*(..))")
//    public void timeCheckPointCut(){}
//    @Around("execution(* study.aop.controller.MemberController.*(..))")
//    @Around("timeCheckPointCut()")
//    public Object timeChecker2(ProceedingJoinPoint proceedingJoinPoint){
//        log.info("[AOP] timeChecker");
//        Object result = null;
//
//        try{
//            long start = System.currentTimeMillis();
//            result = proceedingJoinPoint.proceed();
//            long end = System.currentTimeMillis();
//            log.info("[타임 체커 AOP] 소요 시간 {}ms",(end-start));
//        }catch (Throwable throwable){
//            log.error("타임 체커 오류");
//        }
//        return result;
//    }
}
