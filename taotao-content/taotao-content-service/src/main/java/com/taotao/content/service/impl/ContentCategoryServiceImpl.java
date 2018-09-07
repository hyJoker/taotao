package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //创建一个查询类
        TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
        //设置查询条件
        TbContentCategoryExample.Criteria criteria = contentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //查询
        List<TbContentCategory> categoryList = contentCategoryMapper.selectByExample(contentCategoryExample);
        //将categoryList转换为List<EasyUITreeNode>
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory contentCategory : categoryList
        ) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(contentCategory.getId());
            easyUITreeNode.setText(contentCategory.getName());
            easyUITreeNode.setStatus(contentCategory.getIsParent() ? "closed" : "open");
            resultList.add(easyUITreeNode);
        }
        return resultList;
    }
}
