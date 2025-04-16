package com.dapp.springbootweb3dapp.controller;


import com.alibaba.fastjson.JSONObject;
import com.sunyoki.seeker.access.api.ProBlockApi;
import com.sunyoki.seeker.access.api.ProOrderApi;
import com.sunyoki.seeker.access.api.ProProductApi;
import com.sunyoki.seeker.access.api.ProProductCycleApi;
import com.sunyoki.seeker.access.entity.ProBlock;
import com.sunyoki.seeker.access.entity.ProOrder;
import com.sunyoki.seeker.access.entity.ProProduct;
import com.sunyoki.seeker.access.entity.ProProductCycle;
import com.sunyoki.seeker.access.state.StateProProductCycle;
import com.sunyoki.seeker.contract.Web3Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Service
@Slf4j
public class ProductService {

    @Resource
    ProProductCycleApi proProductCycleApi;

    @Resource
    ProProductApi proProductApi;

    @Resource
    ProOrderApi proOrderApi;
    @Resource
    ProBlockApi proBlockApi;

    public void eth_call(String token_address, String data, CallBackEth callBackEth) {
        try {
            EthCall ethCall = Web3Client.getInstance().ethCall(Transaction
                    .createEthCallTransaction(
                            "0x17e0D173CA4Ef9A61EDC2042C91DC97d4fcA5DbA",
                            token_address,
                            data), DefaultBlockParameterName.LATEST).send();

            callBackEth.apply(ethCall.getValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse<List<ProProductCycle>> list(Integer state) {

        if (state == null) {
            state = StateProProductCycle.ing.getState();
        }

        StateProProductCycle ing;
        if (state == StateProProductCycle.ing.getState()) {
            ing = StateProProductCycle.ing;
        } else if (state == StateProProductCycle.wait.getState()) {
            ing = StateProProductCycle.wait;
        } else {
            ing = StateProProductCycle.ok;
        }


        List<ProProductCycle> cycles = proProductCycleApi.listByState(ing);


        for (ProProductCycle cycle : cycles) {
//             //  获取币种名称 0x06fdde03
//            //new String(Numeric.hexStringToByteArray(ethCall.getValue().replaceAll("0x",""))).trim();
//            //  获取币种符号 0x95d89b41
//            //new String(Numeric.hexStringToByteArray(ethCall.getValue().replaceAll("0x",""))).trim()
//            //   币种小数位 0x95d89b41 6. decimals (0x313ce567)
//            //new BigInteger(ethCall.getValue().replaceAll("0x",""), 16)
            fillData(cycle);

        }

        return ApiResponse.success(cycles);
    }


    public void fillData(ProProductCycle cycle) {


        if (cycle.getState().intValue() == StateProProductCycle.ok.getState()){
            // 执行开奖逻辑
            List<ProBlock> blocks = proBlockApi.listByNumbers(cycle.getCreateBlockNumber(), cycle.getPreOpenDrawBlockNumber().subtract(cycle.getCreateBlockNumber()));
            cycle.setBlocks(blocks);
        }



        JSONObject object = new JSONObject();
        eth_call(cycle.getTokenAddress(), "0x06fdde03", new CallBackEth() {
            @Override
            public void apply(String value) {
                object.put("name", new String(Numeric.hexStringToByteArray(value.replaceAll("0x", ""))).trim());
            }
        });
        eth_call(cycle.getTokenAddress(), "0x95d89b41", new CallBackEth() {
            @Override
            public void apply(String value) {
                object.put("symbol", new String(Numeric.hexStringToByteArray(value.replaceAll("0x", ""))).trim());
            }
        });

        eth_call(cycle.getTokenAddress(), "0x313ce567", new CallBackEth() {
            @Override
            public void apply(String value) {
                object.put("decimals", new BigInteger(value.replaceAll("0x", ""), 16));
            }
        });

        cycle.setSymbol(object);


        // 读取产品信息
        ProProduct product = proProductApi.getSingle(cycle.getContractAddress(), cycle.getTokenAddress(), cycle.getProductValue());
        if (product != null) {
            cycle.setProduct(product);
        }

        // 读取订单信息

        List<ProOrder> proOrders = proOrderApi.listByProductId(cycle.getContractAddress(), cycle.getTokenAddress(), cycle.getProductValue(), cycle.getCurrentIndex());
        cycle.setOrders(proOrders);
    }

    public ApiResponse<ProProductCycle> detail(String contractAddress, String productValue, String tokenAddress, String currentIndex) {

        ProProductCycle cycle = proProductCycleApi.getSingle(contractAddress, tokenAddress, new BigInteger(productValue), new BigInteger(currentIndex));
        if (cycle == null) {
            return ApiResponse.error(500, "产品不存在");
        }

        fillData(cycle);

        return ApiResponse.success(cycle);
    }
}
