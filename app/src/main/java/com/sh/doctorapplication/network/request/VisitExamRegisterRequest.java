package com.sh.doctorapplication.network.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VisitExamRegisterRequest {

    private Long patientId;

    private Long doctorId;

    private String sickName;

    private String sickStatus;

    private String examinationTime;

    private String status;

    private String description;

}
