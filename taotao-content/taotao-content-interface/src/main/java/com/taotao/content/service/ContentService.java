package com.taotao.content.service;

import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {
    //根据内容分类id来获取内容列表
    List<TbContent> getContentListByCid(long cid);
}
