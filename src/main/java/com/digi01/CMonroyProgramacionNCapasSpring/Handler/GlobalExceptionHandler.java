package com.digi01.CMonroyProgramacionNCapasSpring.Handler;

import com.digi01.CMonroyProgramacionNCapasSpring.Util.TokenExpiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public void handleTokenExpiredException(TokenExpiredException ex,
                                            HttpServletResponse response) throws IOException {

        System.out.println("🔥 Redirigiendo por token expirado (5 acciones)");

        response.sendRedirect("/login?expired=true");
    }
}
