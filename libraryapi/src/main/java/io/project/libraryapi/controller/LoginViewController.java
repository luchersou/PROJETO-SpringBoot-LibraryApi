package io.project.libraryapi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {

    @GetMapping("/")
    @ResponseBody
    public String homePage(Authentication authentication){ // The "authentication" is for us to know who is signed in.
        return "Hello, " + authentication;
    }
}
