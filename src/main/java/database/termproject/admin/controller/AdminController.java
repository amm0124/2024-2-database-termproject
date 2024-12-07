package database.termproject.admin.controller;


import database.termproject.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping ("/admin")
public class AdminController {


    @GetMapping("/login")
    public String login() {
        return "loginPage";
    }


    //secured
    @GetMapping("/console")
    public String adminConsole() {
        return "admin-console";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signUpPage";
    }

}
