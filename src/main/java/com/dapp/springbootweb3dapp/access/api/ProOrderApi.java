package com.dapp.springbootweb3dapp.access.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dapp.springbootweb3dapp.access.config.MeBaseServiceImpl;
import com.dapp.springbootweb3dapp.access.entity.ProOrder;
import com.dapp.springbootweb3dapp.access.mapper.ProOrderMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProOrderApi extends MeBaseServiceImpl<ProOrderMapper, ProOrder> {
    public List<ProOrder> listByProductId(String contractAddress, String tokenAddress, BigInteger productValue, BigInteger currentIndex) {
        LambdaQueryWrapper<ProOrder> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(ProOrder::getContractAddress, contractAddress);
        queryWrapper.eq(ProOrder::getTokenAddress, tokenAddress);
        queryWrapper.eq(ProOrder::getProductValue, productValue);
        queryWrapper.eq(ProOrder::getCurrentIndex, currentIndex);
        List<ProOrder> list = list(queryWrapper);
        if (list.isEmpty()) {
            list = new ArrayList<>();
        }
        return list;
    }
}
