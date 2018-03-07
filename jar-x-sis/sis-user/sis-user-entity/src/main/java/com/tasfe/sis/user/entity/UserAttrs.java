package com.tasfe.sis.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Lait on 2017/7/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAttrs {
    private Long id;

    private Long uid;

    private String key;

    private String value;

    private String description;

    private int status;
}
