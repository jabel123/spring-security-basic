package com.example.security1.controller;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View 를 리턴하겠다!
public class IndexController
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication
            , @AuthenticationPrincipal PrincipalDetails userDetails)
    {
        System.out.println("/test/login =========================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());
        System.out.println("userDetails : " + userDetails.getUser());


        return "세션정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String loginOauthTest(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oauth)
    {
        System.out.println("/test/oauth/login =========================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oauth2 user : " + oauth.getAttributes());

        return "세션정보 확인하기";
    }


    // localhost:8080
    @GetMapping({"", "/"})
    public String index()
    {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰 리졸버 설정 : templates (prefix), .mustache (suffix)
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user()
    {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin()
    {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager()
    {
        return "manager";
    }

    // SecurityConfig 이후 작동을 안하게 된다!(안낚아 채지게 되는거임)
    @GetMapping("/loginForm")
    public String login()
    {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String join()
    {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user)
    {
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 회원가입 잘됨. 비밀번호 : 1234 => 시큐리티로 로그인을 할 수 없음. 이유는 패스워드가 암호화가 안되었기 떄문이다.

        return "redirect:/login";
    }

    @GetMapping("/joinProc")
    public @ResponseBody String joinProc()
    {
        return "회원가입 완료됨!";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info()
    {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/data")
    public @ResponseBody String data()
    {
        return "데이터";
    }
}
