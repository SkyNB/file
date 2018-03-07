package com.tasfe.sis.base.service;


import java.io.Serializable;

/**
 * Created by Lait on 2017/8/1.
 */
public interface FileService<T,ID extends Serializable> extends CrudService<T,ID> {

    /**
     * 下载文件
     *
     * @param path 存档路径
     * @param uri  文件的uri
     */
    void download(String path, String uri);

    /**
     *
     * @param md5
     * @return
     */
    boolean isMd5Exist(String md5) ;
}
