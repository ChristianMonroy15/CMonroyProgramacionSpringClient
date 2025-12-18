package com.digi01.CMonroyProgramacionNCapasSpring.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @GetMapping("/token")
    @ResponseBody
    public String getToken(HttpSession session) {
        String token = (String) session.getAttribute("token");
        return token != null ? token : "";
    }

    @PostMapping("/guardarToken")
    @ResponseBody
    public ResponseEntity<String> guardarToken(@RequestParam("token") String token,
            HttpSession session) {

        System.out.println("🔥 TOKENSITO GUARDADO EN SESIÓN: " + token);
        session.setAttribute("token", token);

        session.setAttribute("accionesRestantes", 5);
        System.out.println("Acciones restantes inicializadas en 5");

        return ResponseEntity.ok("OK");
    }

    @GetMapping("/login")
    public String login() {
        return "Login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }

}
