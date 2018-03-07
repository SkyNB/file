package com.tasfe.sis.application.service;

import com.tasfe.sis.application.model.dto.OrderDTO;

/**
 * Created by dongruixi on 2017/11/7.
 */
public interface ApplicationService {
    public String newOrder(OrderDTO order) throws Exception;
}
