/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.digi01.CMonroyProgramacionNCapasSpring.Util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.function.Supplier;

@Component
public class RestClientUtil {

    public <T> T safeCall(Supplier<T> action) {
        try {
            return action.get(); // Ejecuta el RestTemplate normalmente

        } catch (HttpClientErrorException.Forbidden ex) {

            String body = ex.getResponseBodyAsString();
            if (body != null && body.contains("Límite de acciones alcanzado")) {
                System.out.println("⚠️ Token expirado por límite de acciones (capturado en safeCall)");
                throw new TokenExpiredException("Token expirado por límite de acciones");
            }

            throw ex; // otras excepciones siguen normal
        }
    }
}
