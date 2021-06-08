package com.sh.doctorapplication.network.request;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class VisitExamUpdateRequest {

    private String sickName;

    private String sickStatus;

    private String status;

}
