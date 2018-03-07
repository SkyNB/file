package com.tasfe.sis.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 请来模块开发人员来认领
 * Created by Rich on 2017/7/27.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BlackListVO {
    /**
     * 身份证号
     * Field:id_card_num
     */
    private String idCardNum;

    /**
     * 姓名
     * Field:name
     */
    private String name;

    /**
     * 配对签名
     * Field:match_sign
     */
    private String matchSign;
}
