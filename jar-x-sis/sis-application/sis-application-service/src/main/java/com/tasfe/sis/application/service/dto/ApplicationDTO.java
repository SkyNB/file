package com.tasfe.sis.application.service.dto;

import com.tasfe.sis.application.model.dto.OrderDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongruixi on 2017/12/12.
 */
@Data
public class ApplicationDTO extends ArrayList<OrderDTO> implements Serializable {

}
