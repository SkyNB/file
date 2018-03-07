package com.tasfe.sis.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by hefusang on 2017/8/8.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="bi_operation_case")
public class OrderOperationCase {

    //主键id
    @Id
    private Long id;

    //拒绝原因描述
    private String operationExpression;

}
