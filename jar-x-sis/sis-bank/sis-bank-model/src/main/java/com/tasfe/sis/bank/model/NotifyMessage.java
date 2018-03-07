package com.tasfe.sis.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Lait on 2017/8/8.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyMessage {
    private String code;

    private String descr;
}
