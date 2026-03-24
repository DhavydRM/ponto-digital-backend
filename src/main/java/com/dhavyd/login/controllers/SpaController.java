package com.dhavyd.login.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController implements ErrorController {

    @RequestMapping(value = "/error")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        // Só redireciona para o React se for 404 (rota não encontrada no backend)
        if (statusCode != null && statusCode == 404) {
            return "forward:/index.html";
        }

        // Outros erros deixa o Spring tratar normalmente
        return "forward:/index.html";
    }
}