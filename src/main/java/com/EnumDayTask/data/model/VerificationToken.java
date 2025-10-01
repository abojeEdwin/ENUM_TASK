package com.EnumDayTask.data.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "Token cannot be blank")
    @Column(unique = true)
    private String token;
    @NotNull(message = "Expiry date cannot be blank")
    private Date expiryDate;
    @NotNull(message = "User email cannot be blank")
    private String userEmail;
    @NotNull(message = "Is used cannot be blank")
    private boolean isUsed;

}
