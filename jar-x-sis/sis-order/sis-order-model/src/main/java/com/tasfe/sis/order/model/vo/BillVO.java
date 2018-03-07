package com.tasfe.sis.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单对账单信息.<br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BillVO implements Serializable {

	private Long summaryOrderId;

	/**
	 * 账单编号
	 */
	private String billNo;

	/**
	 * 对账单标识.
	 */
	private String orderId;
	/**
	 * 对账单时间.<br>
	 */
	private String date;
	/**
	 * 对账单状态.<br>
	 */
	private String status;
	/**
	 * 对账单对应的订单数量.<br>
	 */
	private String quantity;
	/**
	 * 总金额.<br>
	 */
	private BigDecimal amount;

	/**
	 * 按钮权限控制
	 * @return
	 */
	private Integer permissionSet;

	/**
	 * 来源
	 */
	private String orderSource;

	/**
	 * 放款状态
	 */
	private String loanStatus;
}
