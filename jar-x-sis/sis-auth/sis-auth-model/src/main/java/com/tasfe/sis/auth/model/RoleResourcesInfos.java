package com.tasfe.sis.auth.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Lait on 2017/7/31.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleResourcesInfos {

    private Long id;
    // 角色id
    private Long roleId;
    // 资源id
    private List<Long> resourcesIds;

}
