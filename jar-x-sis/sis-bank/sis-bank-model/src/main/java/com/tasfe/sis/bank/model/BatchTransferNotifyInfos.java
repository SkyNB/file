package com.tasfe.sis.bank.model;

import lombok.Data;

/**
 * Created by Lait on 2017/8/8.
 */
@Data
public class BatchTransferNotifyInfos {

    private String notify_id;
    private String notify_type;
    private String notify_time;
    private String _input_charset;
    private String sign;
    private String sign_type;
    private String version;
    private String outer_trade_no;
    private String inner_trade_no;
    private String withdrawal_amount;
    private String withdrawal_status;
    private String gmt_withdrawal;

}
