package com.EnumDayTask.controller;


import com.EnumDayTask.data.model.Talent;
import com.EnumDayTask.dto.Request.CreateAccountReq;
import com.EnumDayTask.dto.Request.LoginTalentReq;
import com.EnumDayTask.dto.Response.CreateAccountRes;
import com.EnumDayTask.dto.Response.LoginTalentRes;
import com.EnumDayTask.service.TalentAuthImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class TalentAuthController {


    @Autowired
    private TalentAuthImpl talentAuthService;


    @PostMapping("/signup")
    public ResponseEntity<CreateAccountRes> signup(@Valid @RequestBody CreateAccountReq request){
        return ResponseEntity.ok(talentAuthService.signup(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Talent> verifyEmail(@RequestParam String token){
        return ResponseEntity.ok(talentAuthService.verifyEmail(token));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginTalentRes>login(@Valid @RequestBody LoginTalentReq request){
        return ResponseEntity.ok(talentAuthService.login(request));
    }

    @PostMapping("/logout")
    public void logout(@RequestParam String token){
        talentAuthService.logout(token);
    }
}
