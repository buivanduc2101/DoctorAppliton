package com.sh.doctorapplication.network.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorUpdateRequest {

    private Long price;
    private String degree;
    private String specialize;
    private Long experience;
    private String learningProcess;
    private String workingProcess;
    private String sickCanDo;
    private String nghienCuuKhoaHoc;
    private String giangDayHuongDan;

}
