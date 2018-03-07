package com.tasfe.sis.user.api;

import com.tasfe.sis.user.api.dto.UserResisteDTO;

/**
 * Created by Lait on 2017/7/27.
 */
public interface UserContract {

    void registe(UserResisteDTO userResisteDTO);

    /**
     * @param customerId
     * @return
     */
    //List<Users> getUsersByCustomerId(Integer customerId);

    /**
     * @param id
     * @return
     */
    //Users getUserById(Integer id);


    /**
     * @param example
     * @return
     */
    //List<Users> getUsersByExample(UsersExample example);


    /**
     * @param example
     * @return
     */
    //long usersCountByExample(UsersExample example);
}
