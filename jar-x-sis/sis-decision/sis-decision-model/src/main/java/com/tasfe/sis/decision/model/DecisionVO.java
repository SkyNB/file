package com.tasfe.sis.decision.model;

import lombok.Data;

/**
 * Created by dongruixi on 2017/12/6.
 */
@Data
public class DecisionVO {
    private Long id;
    private String name;
    private String version;
    private Integer isCurrent;
}
