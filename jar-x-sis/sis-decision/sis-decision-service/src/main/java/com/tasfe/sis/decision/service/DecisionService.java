package com.tasfe.sis.decision.service;

import com.tasfe.sis.application.model.dto.OrderDTO;
import com.tasfe.sis.decision.model.DecisionVO;

import java.util.List;
import java.util.Map;

/**
 * Created by dongruixi on 2017/11/9.
 */
public interface DecisionService {
    Map<String,String> evaluate(OrderDTO orderDTO) throws Exception;

    /**
     * 更新决策脚本
     * @param name
     * @param version
     * @param script
     * @return
     * @throws Exception
     */
    String updateDecision(String name,String version, String script) throws Exception;

    /**
     * 指定当前版本
     * @param name
     * @param version
     * @return
     * @throws Exception
     */
    String currentDecision(String name,String version) throws Exception;

    /**
     * 获取当前决策版本
     * @param name
     * @return
     * 返回当前版本
     * @throws Exception
     */
    String getCurrentDecision(String name) throws Exception;

    /**
     * 获取指定决策某个版本的脚本
     * @param name
     * @param version
     * @return
     * @throws Exception
     */
    String getDecision(String name,String version) throws Exception;

    /**
     * 获取某个决策的版本及当前有效版本
     * @param name
     * @return
     * @throws Exception
     */
    List<DecisionVO> listDecision(String name) throws Exception;

}
