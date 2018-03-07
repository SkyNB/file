package com.tasfe.sis.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItemVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * entity 主键
	 */
	private Long id;

	/**
	 * 对账日期（跑批日期）
	 */
	private Date batchDate;

	/**
	 * 借款用户id
	 */
	private String userId;

	/**
	 * 受理订单号
	 */
	private String borrowNid;

	/**
	 * 借款人姓名
	 */
	private String renter;

	/**
	 * 身份证号
	 */
	private String idcardno;

	/**
	 * 手机号
	 */
	private String cellphone;

	/**
	 * 家庭住址
	 */
	private String homeadr;

	/**
	 * 借款金额
	 */
	private BigDecimal iamount;

	/**
	 * 开始日期
	 */
	private Date startdate;

	/**
	 * 到期日期
	 */
	private Date expiredate;

	/**
	 * 扣款账号
	 */
	private String debitaccount;

	/**
	 * 发放账号
	 */
	private String loadaccount;

	/**
	 * 婚姻状况
	 */
	private String married;

	/**
	 * 借款期限（天）
	 */
	private Integer periodDays;

	/**
	 * 支付订单号
	 */
	private String billNo;

	private BigDecimal loanRate;

	private BigDecimal overdueRange;


	/**
	 * 处理状态（0正常，1退回）
	 */
	private Integer status;

	/**
	 * 拒绝原因
	 */
	private String resusalEx;

	/**
	 * 审核状态
	 */
	private String checkStatus;

}
