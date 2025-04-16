package com.dapp.springbootweb3dapp.access.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dapp.springbootweb3dapp.access.config.MeBaseServiceImpl;
import com.dapp.springbootweb3dapp.access.entity.ProBlock;
import com.dapp.springbootweb3dapp.access.mapper.ProBlockMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProBlockApi extends MeBaseServiceImpl<ProBlockMapper, ProBlock> {
    public ProBlock last() {

        LambdaQueryWrapper<ProBlock> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(ProBlock::getId);
        queryWrapper.last("limit 1");
        return getOne(queryWrapper);
    }

    public ProBlock getByBlockNumber(Long number) {
        LambdaQueryWrapper<ProBlock> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProBlock::getNumber, number);
        return getOne(queryWrapper);
    }

    public List<ProBlock> listByNumbers(BigInteger startBlockNumber, BigInteger num) {

        List<Long> longs = new ArrayList<>();
        for (int i = 0; i < num.longValue(); i++) {
            longs.add(startBlockNumber.add(BigInteger.valueOf(i)).add(BigInteger.valueOf(1)).longValue());
        }
        LambdaQueryWrapper<ProBlock> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(ProBlock::getNumber,longs);
        return list(queryWrapper);
    }
}
