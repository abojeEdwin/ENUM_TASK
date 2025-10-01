package com.EnumDayTask.controller;


import com.EnumDayTask.dto.Request.UpdateProfileRequest;
import com.EnumDayTask.dto.Response.UpdateProfileResponse;
import com.EnumDayTask.dto.Response.ViewProfileResponse;
import com.EnumDayTask.service.TalentProfileImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/profile")
@AllArgsConstructor
public class TalentProfileController {



    @Autowired
    private TalentProfileImpl talentProfileService;



    @PostMapping("/talent")
    public ResponseEntity<UpdateProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request){
        return ResponseEntity.ok(talentProfileService.updateProfile(request));
    }

    @GetMapping("/me")
    public ResponseEntity<ViewProfileResponse> viewProfile(@RequestParam long talentId){
        return ResponseEntity.ok(talentProfileService.viewProfile(talentId));
    }
}
