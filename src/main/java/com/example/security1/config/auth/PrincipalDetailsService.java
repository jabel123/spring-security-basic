package com.example.security1.config.auth;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session = Authentication = UserDetails

    // form에서 전달오는 값이 username이 아닐경우 동작을 안한다고 하니 주의하도록 하자
    // 만약 바꾸고 싶으면 SecurityConfig에서 .usernameParameter라는 메소드를 넣어서 커스터마이징 해야한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null)
        {
            // 리턴되면 다음과 같이 감싸는 구조가 이뤄진다. session(Authentication(내부 UserDetails))
            return new PrincipalDetails(userEntity);

        }
        return null;
    }
}
