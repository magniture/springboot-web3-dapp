package com.dapp.springbootweb3dapp.access.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sunyoki.seeker.access.config.MeBaseServiceImpl;
import com.sunyoki.seeker.access.entity.ProProductCycle;
import com.sunyoki.seeker.access.mapper.ProProductCycleMapper;
import com.sunyoki.seeker.access.state.StateProProductCycle;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class ProProductCycleApi extends MeBaseServiceImpl<ProProductCycleMapper, ProProductCycle> {
    public ProProductCycle getSingle(String address, String tokenAddress, BigInteger productValue, BigInteger currentIndex) {

        LambdaQueryWrapper<ProProductCycle> queryWrapper = Wrappers.lambdaQuery();


        queryWrapper.eq(ProProductCycle::getContractAddress, address);
        queryWrapper.eq(ProProductCycle::getTokenAddress, tokenAddress);
        queryWrapper.eq(ProProductCycle::getProductValue, productValue);
        queryWrapper.eq(ProProductCycle::getCurrentIndex, currentIndex);
        return getOne(queryWrapper);

    }

    public List<ProProductCycle> listByPreOpenDrawBlockNumber(Long number) {
        LambdaQueryWrapper<ProProductCycle> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProProductCycle::getPreOpenDrawBlockNumber, number);
        return list(queryWrapper);
    }

    public List<ProProductCycle> listByState(StateProProductCycle stateProProductCycle) {

        LambdaQueryWrapper<ProProductCycle> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProProductCycle::getState, stateProProductCycle.getState());
        return list(queryWrapper);
    }
}
