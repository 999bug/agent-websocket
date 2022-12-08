package com.info2soft.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Lisy
 * @Date: 2022/11/25/11:03
 * @Description: 接收状态条件JSON
 */
@Getter
@Setter
public class AgentCondition implements Serializable {

    private static final long serialVersionUID = 7446449986251055561L;

    /**
     * 外层检测条件集合
     */
    @JSONField(name = "lv1")
    private List<AgentConditionListItem> agentListItems;
    /**
     * 逻辑关系:
     * and - '&&'
     * or - '|| '
     */
    @JSONField(name = "operator")
    private String operator;

}