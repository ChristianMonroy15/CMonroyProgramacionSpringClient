
package com.digi01.CMonroyProgramacionNCapasSpring.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("demo")
public class DemoController {
    
    @GetMapping("saludo/{nombre}")
    public String Saludo(@PathVariable("nombre") String nombre, Model model) {
        model.addAttribute("nombre", nombre);
        return "HolaMundo";
    }
    
    @GetMapping("saludo2")
    public String Saludo2(@RequestParam("nombre") String nombre,Model model){
        model.addAttribute("nombre",nombre);
    return "HolaMundo";
    }
    
    @GetMapping("multi")
    public String Multi(@RequestParam("n1") int n1,@RequestParam("n2") int n2, Model model){
        
        int resultado=n1*n2; 
        model.addAttribute("resultado",resultado);
        
        return "multi";
    }

}
