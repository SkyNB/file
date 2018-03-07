package com.tasfe.sis.application.web.controller.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by dongruixi on 2017/12/23.
 */
@Data
public class ContractInfo implements Serializable {
    private String signCode;
    private String signUrl;
    private String description;
}
