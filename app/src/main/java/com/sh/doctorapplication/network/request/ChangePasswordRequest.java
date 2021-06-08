package com.sh.doctorapplication.network.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordRequest {

    private Long userId;
    private String newPassword;

}
