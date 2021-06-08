package com.sh.doctorapplication.network.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientRequest {

    private String fullName;
    private String mobile;
    private String address;

}
