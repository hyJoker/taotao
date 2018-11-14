package com.taotao.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Override
    public List<TbContent> getContentListByCid(long cid) {
        //首先查询缓存,如果缓存中存在,直接返回
        try {
            String json = jedisClient.hget("INDEX_CONTENT", cid+"");
            //从缓存中查到结果
            if (StringUtils.isNoneBlank(json)) {
                //将json转化为List<TbContent>
                List<TbContent> list = JSON.parseArray(json, TbContent.class);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        //添加缓存,不影响业务流程
        try {
            String json = JSON.toJSONString(list);
            jedisClient.hset("INDEX_CONTENT", cid+"", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return list;
    }
}
