package com.sh.doctorapplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rate {

    private Long id;
    private Long star;
    private Patient patient;
    private Doctor doctor;

}
