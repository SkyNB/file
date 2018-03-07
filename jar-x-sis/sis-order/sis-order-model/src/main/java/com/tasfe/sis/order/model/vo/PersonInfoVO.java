package com.tasfe.sis.order.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by hefusang on 2017/12/7.
 */
@Data
public class PersonInfoVO {

    private String name;

    private String phone;

    private String idCard;

    private String address;

    private List<ContractVO> contractVOList;

}
