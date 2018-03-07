package com.tasfe.sis.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="request_log")
public class RequestLog {

    @Id
    private Long id;

    private String type;

    private Date utime;

}
