package com.EnumDayTask.service;


import com.EnumDayTask.data.model.Talent;
import com.EnumDayTask.dto.Request.CreateAccountReq;
import com.EnumDayTask.dto.Request.LoginTalentReq;
import com.EnumDayTask.dto.Response.CreateAccountRes;
import com.EnumDayTask.dto.Response.LoginTalentRes;
import org.springframework.stereotype.Service;

@Service
public interface TalentAuthService {
    CreateAccountRes signup(CreateAccountReq createAccountReq);
    Talent verifyEmail(String token);
    LoginTalentRes login(LoginTalentReq request);
    void logout(String token);
    void deleteAll();
}
