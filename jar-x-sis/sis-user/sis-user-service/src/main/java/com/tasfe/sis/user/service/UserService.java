package com.tasfe.sis.user.service;

import com.tasfe.sis.user.entity.User;
import com.tasfe.sis.user.model.UserInfos;

import java.util.List;

/**
 * Created by Lait on 2017/7/27.
 */
public interface UserService {

    /**
     * 用户注册
     * @param user
     * @return
     */
    void registe(UserInfos user) throws Exception;


    /**
     * 编辑用户
     * @param user
     */
    void edit(UserInfos user) throws Exception;


    /**
     * 逻辑删除
     * @param id
     */
    void delete(Long id) throws Exception;


    /**
     *
     * @param id
     * @return
     */
    List<User> getUsersByCustomerId(String id);

    /**
     *
     * @param userId
     * @return
     */
    User getUsersByUserId(String userId);

}
