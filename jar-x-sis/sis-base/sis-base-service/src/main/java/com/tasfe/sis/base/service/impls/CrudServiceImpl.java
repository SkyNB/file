package com.tasfe.sis.base.service.impls;

import com.tasfe.sis.base.dao.GenericCrudRepository;
import com.tasfe.sis.base.dao.jpa.impls.Query;
import com.tasfe.sis.base.model.PageResults;
import com.tasfe.sis.base.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lait on 2017/7/26.
 */
@Service
public class CrudServiceImpl<T,ID extends Serializable> implements CrudService<T,ID> {

    //@Autowired(required=false)
    //private CrudTemplate crudTemplate;

    //@Autowired
    //private GenericDao<T,ID> baseDao;
    //private BaseDao<T> baseDao;


    /*private Class<T> modelClass;

    @Autowired
    protected EntityManager entityManager;*/


    /*public CrudServiceImpl() {
        modelClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }*/

    //@Autowired(required = true)
    private GenericCrudRepository<T,ID> genericCrudRepository;

    @Override
    public GenericCrudRepository getGenericCrudRepository() {
        return genericCrudRepository;
    }

    /**
     * 保存对象
     *
     * @param model 需要添加的对象
     */
    @Override
    public void save(final T model) throws Exception {
        genericCrudRepository.save(model);
    }

    /**
     * 批量保存对象
     *
     * @param modelList 需要增加的对象的集合
     *                  失败会抛异常
     */
    @Override
    public void save(final List<T> modelList) throws Exception {
        genericCrudRepository.save(modelList);
        //baseDao.save(modelList);
    }

    /**
     * 删除对象
     *
     * @param model 需要删除的对象
     *              失败会抛异常
     */
    @Override
    public void delete(T model) throws Exception {
        //baseDao.delete(model);
        genericCrudRepository.delete(model);
    }

    /**
     * 批量删除对象
     *
     * @param modelList 需要删除的对象的集合
     *                  失败会抛异常
     */
    @Override
    public void deleteAll(final List<T> modelList) throws Exception {
        genericCrudRepository.delete(modelList);
        //baseDao.deleteAll(modelList);
        //baseDao.delete(modelList);
    }

    /**
     * 按照id删除对象
     *
     * @param id 需要删除的对象的id
     *           失败抛出异常
     */
    @Override
    public void deleteById(ID id) throws Exception {
        genericCrudRepository.delete(id);
        //baseDao.deleteById(modelClass, id);
        //baseDao.delete(id);
    }

    /**
     * 更新或保存对象
     *
     * @param model 需要更新的对象
     *              失败会抛出异常
     */
    @Override
    public void saveOrUpdate(final T model) throws Exception {
        genericCrudRepository.save(model);
        //baseDao.saveOrUpdate(model);
        //baseDao.save(model);
    }

    /**
     * 批量更新或保存对象
     *
     * @param modelList 需要更新或保存的对象
     *                  失败会抛出异常
     */
    @Override
    public void saveOrUpdateAll(final List<T> modelList) throws Exception {
        genericCrudRepository.save(modelList);
        //baseDao.saveOrUpdateAll(modelList);
        //baseDao.save(modelList);
    }

    /**
     * 通过主键, 查询对象
     *
     * @param id 主键(Serializable)
     * @return model
     */
    @Override
    public T get(final ID id) throws Exception {
        return genericCrudRepository.getOne(id);
        //return baseDao.getById(modelClass, id);
        //return baseDao.getOne(id);
    }
    
    @Override
    public List<T> get(T t){
        return genericCrudRepository.findAll( Example.of(t,ExampleMatcher.matching()));
    }

    /**
     * 获得全部
     *
     * @return List
     */
    @Override
    public List<T> get() throws Exception {
        return genericCrudRepository.findAll();
        //return baseDao.getAll(modelClass);
        //return baseDao.findAll();
    }




    /**
     * 分页查询
     *
     * @param currentPageNumber 页码
     * @param pageSize          每页数量
     * @return 查询结果
     */
    @Override
    public List<T> list(final Integer currentPageNumber, final Integer pageSize) throws Exception {
        return null;
        //genericCrudRepository.
        //return baseDao.getListByPage(modelClass, currentPageNumber, pageSize);
        //return baseDao.fin
    }

    /**
     * 按条件分页
     *
     * @param currentPageNumber 页码
     * @param pageSize          每页数量
     * @param query             封装的查询条件
     * @return 查询结果
     */
    @Override
    public PageResults<T> list(Integer currentPageNumber, Integer pageSize, Query query) throws Exception {
        return null;
        //return baseDao.getListByPageAndQuery(currentPageNumber, pageSize, query);
    }

    /**
     * 获得数量 利用Count(*)实现
     *
     * @return 数量
     */
    @Override
    public long count() throws Exception {
        return genericCrudRepository.count();
        //return baseDao.getCount(modelClass);
    }

    /**
     * 获得符合对应条件的数量 利用Count(*)实现
     *
     * @param query 查询条件
     * @return 数量
     */
    @Override
    public int count(final Query query) throws Exception {
        //return genericCrudRepository.coun
        //return baseDao.getCountByQuery(query);
        return 0;
    }

    /**
     * 执行Sql语句
     *
     * @param sql    sql
     * @param values 不定参数数组
     * @return 受影响的行数
     */
    @Override
    public int executeSql(final String sql, final Object... values) throws Exception {
        return 0;
        //return baseDao.executeSql(sql, values);
    }

    /**
     * 通过jpql查询
     *
     * @param jpql   jpql语句
     * @param values 参数列表
     * @return 受影响的行数
     */
    @Override
    public Object queryByJpql(final String jpql, final Object... values) {
        return null;
        //return baseDao.queryByJpql(jpql, values);
    }

    /**
     * 获得符合对应条件的数量 利用Count(*)实现
     *
     * @param jpql jpql查询条件
     * @return 数量
     */
    @Override
    public int getCountByJpql(final String jpql, final Object... values) {
        return 0;
        //return baseDao.getCountByJpql(jpql, values);
    }


    /**
     * 通过Jpql分页查询
     *
     * @param currentPageNumber 当前页
     * @param pageSize          每页数量
     * @param jpql              jpql语句
     * @param values            jpql参数
     * @return 查询结果
     */
    @Override
    public PageResults<Object> getListByPageAndJpql(Integer currentPageNumber, Integer pageSize, final String jpql, Object... values) {
        return null;
        //return baseDao.getListByPageAndJpql(currentPageNumber, pageSize, jpql, values);
    }

    /**
     * 执行jpql语句
     *
     * @param jpql   jpql语句
     * @param values 参数列表
     * @return 受影响的行数
     */
    @Override
    public int executeJpql(final String jpql, final Object... values) {
        return 0;
        //return baseDao.executeJpql(jpql, values);
    }

    /**
     * refresh 刷新实体状态
     *
     * @param model 实体
     */
    @Override
    public void refresh(T model) throws Exception {
        //baseDao.refresh(model);
    }




}
