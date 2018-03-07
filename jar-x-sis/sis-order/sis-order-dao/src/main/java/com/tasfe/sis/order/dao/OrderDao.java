package com.tasfe.sis.order.dao;

import com.tasfe.sis.base.dao.GenericCrudRepository;
import com.tasfe.sis.base.dao.annotation.MybatisQuery;
import com.tasfe.sis.base.dao.mybatis.plugins.page.MybatisPage;
import com.tasfe.sis.order.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by liupeng on 2016-6-15.
 */
public interface OrderDao extends GenericCrudRepository<Order,Long> {

    @MybatisQuery
    Long countByExample(OrderExample example);


    @MybatisQuery
    List<Order> selectByExample(OrderExample example);

}
