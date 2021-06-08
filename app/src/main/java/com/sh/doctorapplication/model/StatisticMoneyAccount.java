package com.sh.doctorapplication.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticMoneyAccount implements Serializable {

    private Account account;
    private Integer quantityExam;
    private Long totalMoneySpend;
    private List<Transaction> transactions;

}
