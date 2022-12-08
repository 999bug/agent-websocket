package com.info2soft.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lisy
 * @Date: 2022/11/25/11:03
 * @Description: 检测条件集合
 */
@Getter
@Setter
public class AgentConditionListItem implements Serializable {

    private static final long serialVersionUID = -1488897752455699318L;

    /**
     * 每一个检测条件内层集合
     */
    @JSONField(name = "list")
    private List<AgentListItem> agentListItems;

    /**
     * 逻辑关系:
     * and - '&&'
     * or - '|| '
     */
    @JSONField(name = "operator")
    private String operator;

}