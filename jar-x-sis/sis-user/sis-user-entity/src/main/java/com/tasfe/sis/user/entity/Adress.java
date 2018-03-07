package com.tasfe.sis.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Lait on 2017/7/11.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="adress")
public class Adress {

    @Id
    private Long id;

    // 关联实体id
    private Long rid;

    // 省
    private String province;

    // 市
    private String city;

    // 区
    private String area;

    // 地址
    private String adress;

    // 区域代码
    private String code;

    // 排序
    private int sort;

    public Adress(String province, String city, String area, String address) {
        this.province = province;
        this.city = city;
        this.area = area;
        this.adress = address;
    }

    public Adress(Long rid, String province, String city, String area, String address) {
        this.rid = rid;
        this.province = province;
        this.city = city;
        this.area = area;
        this.adress = address;
    }

    public Adress(Long rid, String province, String city, String area, String address, String code, int sort) {
        this.rid = rid;
        this.province = province;
        this.city = city;
        this.area = area;
        this.adress = address;
        this.code = code;
        this.sort = sort;
    }

    /**
     * 获取详细地址
     * @return
     */
    public String toAdress(){
        String da = province + "," + city+"," + "," + area + "," + adress;
        return da;
    }


}
