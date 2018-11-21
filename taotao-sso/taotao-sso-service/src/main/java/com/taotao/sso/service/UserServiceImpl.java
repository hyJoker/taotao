package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public TaotaoResult checkUserData(String data, int type) {
        //设置查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //判断用户名是否可用
        if (type == 1) {
            criteria.andUsernameEqualTo(data);
        } else if (type == 2) {
            //判断电话是否可用
            criteria.andPhoneEqualTo(data);
        } else if (type == 3) {
            //判断邮箱是否可用
            criteria.andEmailEqualTo(data);
        } else {
            return TaotaoResult.build(400, "所传参数非法");
        }
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if (tbUsers != null || tbUsers.size() > 0) {
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }
}
