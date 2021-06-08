package com.sh.doctorapplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {

    private Long id;
    private String name;
    private String content;
    private String linkImage;
    private Hospital hospital;

}
