package com.fta.stock.tracking.helper;


import com.fta.stock.tracking.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ServiceExceptionHandlerAspect {

    private final EmailService emailService;



    @AfterThrowing(pointcut = "execution(* com.fta.stock.tracking.helper..*(..))", throwing = "ex")
    public void logAndReportException(Exception ex) throws Throwable {
        System.out.println("Hata yakalandı: " + ex.getMessage());
       emailService.sendEmail(ex.getMessage(),"Hata Yakalandi");
    }


    @AfterThrowing(pointcut = "execution(* com.fta.stock.tracking.controller..*(..))", throwing = "ex")
    public void logAndReportException2(Exception ex) throws Throwable {
        System.out.println("Hata yakalandı: " + ex.getMessage());
        emailService.sendEmail(ex.getMessage(),"Hata Yakalandi");
    }

    @AfterThrowing(pointcut = "execution(* com.fta.stock.tracking.auth..*(..))", throwing = "ex")
    public void logAndReportException3(Exception ex) throws Throwable {
        System.out.println("Hata yakalandı: " + ex.getMessage());
        emailService.sendEmail(ex.getMessage(),"Hata Yakalandi");
    }

}