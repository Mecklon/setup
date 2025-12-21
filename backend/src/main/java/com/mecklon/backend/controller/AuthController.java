package com.mecklon.backend.controller;


import com.mecklon.backend.DTO.LoginRequestDto;
import com.mecklon.backend.DTO.LoginResponseDto;
import com.mecklon.backend.DTO.SignupRequestDto;
import com.mecklon.backend.DTO.SignupResponseDto;
import com.mecklon.backend.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @GetMapping("/autoLogin")
    ResponseEntity<Map<String,String>> autoLogin(Authentication auth){
        Map<String,String> map = new HashMap<>();
        map.put("username",auth.getName());
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto request){
        try{
            System.out.println(request);
            return ResponseEntity.status(HttpStatus.OK).body(authService.signup(request));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
