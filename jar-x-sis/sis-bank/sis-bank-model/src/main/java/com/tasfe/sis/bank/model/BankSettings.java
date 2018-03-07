package com.tasfe.sis.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hefusang on 2017/8/9.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="hlj_customer_setting")
public class BankSettings {

   @Id
   private Long id;

   
    private Long customerId;

   
    private String debitAccount;

   
    private String loadAccount;

   
    private BigDecimal loanRate;

   
    private BigDecimal overdueRange;

   
    private String borrowCustomerType;

   
    private String agriculturerelated;

   
    private String isCoorperate;

   
    private String coorperateCode;

   
    private String productName;

   
    private String subProductCode;

    private String customerCode;

   
    private Date ctime;

   
    private Date utime;

}
