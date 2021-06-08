package com.sh.doctorapplication.network.request;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest implements Serializable {

    private String username;

    private String password;

    private String fullName;

    private String mobile;

    private String birthDay;

    private String address;

    private Boolean isDoctor = false;

}
