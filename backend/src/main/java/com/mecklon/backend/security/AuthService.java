package com.mecklon.backend.security;

import com.mecklon.backend.DTO.LoginRequestDto;
import com.mecklon.backend.DTO.LoginResponseDto;
import com.mecklon.backend.DTO.SignupRequestDto;
import com.mecklon.backend.DTO.SignupResponseDto;
import com.mecklon.backend.model.User;
import com.mecklon.backend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(token, user.getUsername());

    }

    public SignupResponseDto signup(SignupRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user != null) {
            throw new IllegalArgumentException("This user already exists");
        }

        user = userRepository.save(User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .roles(Set.of())
                .build());

        String token = authUtil.generateAccessToken(user);



        return new SignupResponseDto(token, user.getUsername());
    }
}
