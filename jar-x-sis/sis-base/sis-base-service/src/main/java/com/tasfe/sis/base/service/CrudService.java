package com.tasfe.sis.base.service;

import com.tasfe.sis.base.dao.GenericCrudRepository;
import com.tasfe.sis.base.dao.jpa.impls.Query;
import com.tasfe.sis.base.model.PageResults;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lait on 2017/7/28.
 */
public interface CrudService<T,ID  extends Serializable> {

    GenericCrudRepository getGenericCrudRepository();

    /************************* jpa 实现 方便快速开发  *************************/
    /**
     * 保存对象
     *
     * @param model 需要添加的对象
     */
    void save(final T model) throws Exception;

    /**
     * 批量保存对象
     *
     * @param modelList 需要增加的对象的集合
     *                  失败会抛异常
     */
    void save(List<T> modelList) throws Exception;

    /**
     * 删除对象
     *
     * @param model 需要删除的对象
     *              失败会抛异常
     */
    void delete(final T model) throws Exception;

    /**
     * 批量删除对象
     *
     * @param modelList 需要删除的对象的集合
     *                  失败会抛异常
     */
    void deleteAll(final List<T> modelList) throws Exception;

    /**
     * 按照id删除对象
     *
     * @param id 需要删除的对象的id
     *           失败抛出异常
     */
    void deleteById(final ID id) throws Exception;

    /**
     * 更新或保存对象
     *
     * @param model 需要更新的对象
     *              失败会抛出异常
     */
    void saveOrUpdate(final T model) throws Exception;

    /**
     * 批量更新或保存对象
     *
     * @param modelList 需要更新或保存的对象
     *                  失败会抛出异常
     */
    void saveOrUpdateAll(final List<T> modelList) throws Exception;

    /**
     * 通过主键, 查询对象
     *
     * @param id 主键(Serializable)
     * @return model
     */
    T get(final ID id) throws Exception;

    List<T> get(T t);

    /**
     * 获得全部
     *
     * @return List
     */
    List<T> get() throws Exception;


    /**
     * 分页查询
     *
     * @param currentPageNumber 页码
     * @param pageSize          每页数量
     * @return 查询结果
     */
    List<T> list(final Integer currentPageNumber,
                          final Integer pageSize)
            throws Exception;

    /**
     * 按条件分页
     *
     * @param currentPageNumber 页码
     * @param pageSize          每页数量
     * @param query             封装的查询条件
     * @return 查询结果
     */
    PageResults<T> list(Integer currentPageNumber,
                                         Integer pageSize,
                                         Query query)
            throws Exception;

    /**
     * 获得数量 利用Count(*)实现
     *
     * @return 数量
     */
    long count() throws Exception;

    /**
     * 获得符合对应条件的数量 利用Count(*)实现
     *
     * @param query 查询条件
     * @return 数量
     */
    int count(final Query query) throws Exception;

    /**
     * 执行Sql语句
     *
     * @param sql    sql
     * @param values 不定参数数组
     * @return 受影响的行数
     */
    int executeSql(final String sql, final Object... values)
            throws Exception;

    /**
     * 通过jpql查询
     *
     * @param jpql   jpql语句
     * @param values 参数列表
     * @return 受影响的行数
     */
    Object queryByJpql(final String jpql, final Object... values);

    /**
     * 获得符合对应条件的数量 利用Count(*)实现
     *
     * @param jpql jpql查询条件
     * @return 数量
     */
    int getCountByJpql(final String jpql, final Object... values);


    /**
     * 通过Jpql分页查询
     *
     * @param currentPageNumber 当前页
     * @param pageSize          每页数量
     * @param jpql              jpql语句
     * @param values            jpql参数
     * @return 查询结果
     */
    PageResults<Object> getListByPageAndJpql(Integer currentPageNumber,
                                             Integer pageSize,
                                             final String jpql,
                                             Object... values);

    /**
     * 执行jpql语句
     *
     * @param jpql   jpql语句
     * @param values 参数列表
     * @return 受影响的行数
     */
    int executeJpql(final String jpql, final Object... values);

    /**
     * refresh 刷新实体状态
     *
     * @param model 实体
     */
    void refresh(T model) throws Exception;
}
