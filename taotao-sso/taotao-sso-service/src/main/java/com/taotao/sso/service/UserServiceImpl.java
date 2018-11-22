package com.taotao.sso.service;

import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${USER_SESSION}")
    private String USER_SESSION;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

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
        if (tbUsers != null && tbUsers.size() > 0) {
            return TaotaoResult.ok(false);
        }
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult register(TbUser tbUser) {
        //检查数据有效性
        if (StringUtils.isBlank(tbUser.getUsername())) {
            return TaotaoResult.build(400, "用户名不能为空");
        }
        TaotaoResult result = checkUserData(tbUser.getUsername(), 1);
        if (!(Boolean) result.getData()) {
            return TaotaoResult.build(400, "用户名不能重复");
        }
        if (StringUtils.isBlank(tbUser.getPassword())) {
            return TaotaoResult.build(400, "密码不能为空");
        }
        if (StringUtils.isNotBlank(tbUser.getPhone())) {
            //如果电话不为空,那么接着判断是否重复,电话是不能重复的
            result = checkUserData(tbUser.getPhone(), 2);
            if (!(Boolean) result.getData()) {
                return TaotaoResult.build(400, "电话不能重复");
            }
        }
        if (StringUtils.isNotBlank(tbUser.getEmail())) {
            //如果电话不为空,那么接着判断是否重复,电话是不能重复的
            result = checkUserData(tbUser.getEmail(), 3);
            if (!(Boolean) result.getData()) {
                return TaotaoResult.build(400, "邮箱不能重复");
            }
        }
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        //密码MD5加密
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(md5DigestAsHex);
        //添加
        tbUserMapper.insert(tbUser);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult login(String username, String password) {
        //判断用户名,密码是否正确
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);
        if (tbUsers == null || tbUsers.size() == 0) {
            //返回登录失败
            return TaotaoResult.build(400, "用户名或密码不正确");
        }
        TbUser tbUser = tbUsers.get(0);
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())) {
            //返回登录失败
            return TaotaoResult.build(400, "用户名或密码不正确");
        }
        //生成token,使用uuid
        String token = UUID.randomUUID().toString();
        //把用户信息保存在redis中,key为token,val就是用户信息,过滤密码
        tbUser.setPassword(null);
        jedisClient.set(USER_SESSION + ":" + token, JSON.toJSONString(tbUser));
        //设置过期时间
        jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);

        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        String json = jedisClient.get(USER_SESSION + ":" + token);
        if (StringUtils.isBlank(json)) {
            return TaotaoResult.build(400, "token已过期");
        }
        TbUser tbUser = JSON.parseObject(json, TbUser.class);
        //每访问一次token,如果token没有过期,便进行更新,将token恢复为最大值
        jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
        return TaotaoResult.ok(tbUser);
    }

    @Override
    public TaotaoResult logout(String token) {
        jedisClient.expire(USER_SESSION + ":" + token, 0);
        return TaotaoResult.ok();
    }


}
