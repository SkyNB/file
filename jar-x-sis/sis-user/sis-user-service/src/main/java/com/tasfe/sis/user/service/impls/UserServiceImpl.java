package com.tasfe.sis.user.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.base.model.response.ResponseData;
import com.tasfe.sis.base.service.CacheService;
import com.tasfe.sis.user.api.UserContract;
import com.tasfe.sis.user.api.dto.UserResisteDTO;
import com.tasfe.sis.user.entity.Adress;
import com.tasfe.sis.user.entity.User;
import com.tasfe.sis.user.model.UserInfos;
import com.tasfe.sis.user.service.AdressService;
import com.tasfe.sis.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhou on 17/6/26.
 */

@Service
public class UserServiceImpl implements UserService, UserContract {


    @Autowired
    private CacheService cacheService;



    @Autowired
    private AdressService adressService;

    @Autowired
    private CrudTemplate crudTemplate;

    /************************* 入参为DTO的为提供给内部使用( 如controller 或其他 http 调用使用 ) *************************/

    /**
     * 注册
     * @param userInfos
     */
    @Override
    public void registe(UserInfos userInfos) throws Exception {

        User user = new User();
        BeanUtils.copyProperties(userInfos,user);
        crudTemplate.save(user);
        Adress adress = new Adress();
        BeanUtils.copyProperties(userInfos,adress);
        adressService.addAdress(adress);



    }

    /**
     * 编辑用户信息
     * @param user
     */
    @Override
    public void edit(UserInfos user) throws Exception {
        crudTemplate.update(user,Criteria.from(User.class));
    }

    /**
     * 删除用户 逻辑删除
     * @param id
     */
    @Override
    public void delete(Long id) throws Exception {
        crudTemplate.del(User.class,id);
    }

    @Override
    public List<User> getUsersByCustomerId(String id) {
        User user = new User();
        user.setCustomerId(new Long(id));
        List<User> users = null;
        try {
            users = crudTemplate.find(user, Criteria.from(User.class).where().and("customer_id", Operator.EQ, new Long(id)).endWhere());
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User getUsersByUserId(String userId) {
        try {
            User record = new User();
            record.setId(new Long(userId));
            List<User> users = crudTemplate.find(record, Criteria.from(User.class).where().and("id", Operator.EQ).endWhere());
            if(users.size()>0)
                return users.get(0);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /************************* 入参为DTO的为提供给外部使用( 如dubbo 或其他 rpc 框架使用 ) *************************/
    @Override
    public void registe(UserResisteDTO userResisteDTO) {


    }
}
