package com.tasfe.sis.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by hefusang on 2017/8/8.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="hlj_users_session")
public class UserSession /*extends BaseEntity*/ {

    @Id
    private Long id;

    private Long userId;

    private String code;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
