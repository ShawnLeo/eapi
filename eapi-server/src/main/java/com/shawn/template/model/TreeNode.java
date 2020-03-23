package com.shawn.template.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName TreeNode
 * @Description TODO
 * @Author Shawn
 * @Date 2020/3/19 15:43
 * @Version 1.0
 **/
@Data
@Builder
public class TreeNode<T> {

    private T data;

    private String title;

    private Integer type;

    private Boolean expand = false;

    private Boolean disabled = false;

    List<TreeNode> children;

}
