package com.tasfe.sis.order.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 授权用户
 * Created by Rich on 2017/7/27.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonalCreditVO {

    /**
     *身份证ID
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
