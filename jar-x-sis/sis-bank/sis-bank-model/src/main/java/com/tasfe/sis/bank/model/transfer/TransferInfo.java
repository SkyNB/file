package com.tasfe.sis.bank.model.transfer;

import lombok.Data;

/**
 * Created by lixiaofeng on 2017-8-15.
 */
@Data
public class TransferInfo {
    // 格式：value="<%=batchNo%>1~张三~123456~ICBC~工商银行~支行名称~~上海~上海市~C~0.01~~">(不可空)<br/>

    /**
     *  批次号
     */
    private String batch_no;

    /**
     * 姓名
     */
    private String name;

    /**
     * 银行卡号
     */
    private String card_no;

    /**
     *  银行表示 如工商银行：ICBC
     */
    private String bank;

    /**
     * 银行名称
     */
    private String bank_name;

    /**
     * 支行名称
     */
    private String branch_name;

    /**
     * 省
     */
    private String bank_prov;

    /**
     * 市
     */
    private String bank_city;

    /**
     * 对公对私：对公/B, 对私/C
     */
    private String company_or_personal;

    /**
     * 待定：
     */
    private String found_no;


    @Override
    public String toString() {
        return "TransferInfo{" +
                ", name='" + name + '\'' +
                ", card_no='" + card_no + '\'' +
                ", bank='" + bank + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", branch_name='" + branch_name + '\'' +
                ", bank_prov='" + bank_prov + '\'' +
                ", bank_city='" + bank_city + '\'' +
                ", company_or_personal='" + company_or_personal + '\'' +
                ", found_no='" + found_no + '\'' +
                '}';
    }

}

