package com.dapp.springbootweb3dapp.access.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dapp.springbootweb3dapp.access.config.MeBaseServiceImpl;
import com.dapp.springbootweb3dapp.access.entity.ProProduct;
import com.dapp.springbootweb3dapp.access.mapper.ProProductMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class ProProductApi extends MeBaseServiceImpl<ProProductMapper, ProProduct> {
    public ProProduct getSingle(String contractAddress, String tokenAddress, BigInteger productValue) {
        LambdaQueryWrapper<ProProduct> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(ProProduct::getContractAddress, contractAddress);
        queryWrapper.eq(ProProduct::getTokenAddress, tokenAddress);
        queryWrapper.eq(ProProduct::getProductValue, productValue);
        return getOne(queryWrapper);
    }
}
