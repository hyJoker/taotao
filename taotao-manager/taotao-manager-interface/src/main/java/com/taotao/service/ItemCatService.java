package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    //根据父加点id查询属性结构,使用懒加载,先显示第一级目录,当点击下一级目录时查询第二级目录
    List<EasyUITreeNode> getItemCatList(long parentId);
}
