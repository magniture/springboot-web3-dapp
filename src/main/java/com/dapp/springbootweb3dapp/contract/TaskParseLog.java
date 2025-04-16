package com.dapp.springbootweb3dapp.contract;


import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.EventEncoder;
import org.web3j.protocol.core.methods.response.Log;
import sol.Main;

import java.math.BigInteger;

@Slf4j
public class TaskParseLog implements Runnable {

    private final String id;
    private final Log eth_log;

    public TaskParseLog(String id, Log eth_log) {
        this.id = id;
        this.eth_log = eth_log;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        ProProductApi proProductApi = SpringUtils.getBean(ProProductApi.class);
        ProProductCycleApi proProductCycleApi = SpringUtils.getBean(ProProductCycleApi.class);
        ProOrderApi proOrderApi = SpringUtils.getBean(ProOrderApi.class);

        if (id.equals(EventEncoder.encode(Main.CREATEPRODUCT_EVENT))) {
            log.info("获取到 创建产品事件： {}", id);
            Main.CreateProductEventResponse response = Main.getCreateProductEventFromLog(eth_log);
            ProProduct product = ProProduct.fillEventResponse(response, eth_log.getAddress(), eth_log.getBlockNumber());
            // 保存起来
            proProductApi.save(product);

        }


        if (id.equals(EventEncoder.encode(Main.CREATEPRODUCTCYCLE_EVENT))) {
            log.info("获取到 创建产品周期事件： {}", id);
            Main.CreateProductCycleEventResponse response = Main.getCreateProductCycleEventFromLog(eth_log);
            ProProductCycle cycle = ProProductCycle.fillEventResponse(response, eth_log.getAddress(), eth_log.getBlockNumber());
            // 保存起来
            proProductCycleApi.save(cycle);
        }


        if (id.equals(EventEncoder.encode(Main.USERCONFIRMJOIN_EVENT))) {
            log.info("获取到 用户参与事件： {}", id);
            Main.UserConfirmJoinEventResponse response = Main.getUserConfirmJoinEventFromLog(eth_log);
            ProOrder order = ProOrder.fillEventResponse(response, eth_log.getAddress(), eth_log.getBlockNumber());
            // 保存起来
            proOrderApi.save(order);

        }

        if (id.equals(EventEncoder.encode(Main.WAITDRAW_EVENT))) {
            log.info("获取到 等待开奖事件： {}", id);
            Main.WaitDrawEventResponse response = Main.getWaitDrawEventFromLog(eth_log);

            // 预开奖区块号
            BigInteger pre_open_draw_block_number = response.block_number.add(BigInteger.valueOf(20));


            // 得到产品周期
            ProProductCycle cycle = proProductCycleApi.getSingle(eth_log.getAddress(), response.token_address, response.product_value, response.current_index);
            if (cycle == null) {
                log.error("找不到产品周期 {} {} {} {}", eth_log.getAddress(), response.token_address, response.product_value, response.current_index);
                return;
            }

            if (cycle.getState().intValue() != StateProProductCycle.ing.getState()) {
                log.error("产品周期状态错误 {} {} {} {}", eth_log.getAddress(), response.token_address, response.product_value, response.current_index);
                return;
            }
            // 变更产品周期状态
            cycle.setState(BigInteger.valueOf(StateProProductCycle.wait.getState()));

            cycle.setCompleteBlockNumber(eth_log.getBlockNumber());
            cycle.setPreOpenDrawBlockNumber(pre_open_draw_block_number);

            proProductCycleApi.updateById(cycle);

        }

        if (id.equals(EventEncoder.encode(Main.OPENDRAW_EVENT))) {
            log.info("获取到 开奖事件： {}", id);
            Main.OpenDrawEventResponse response = Main.getOpenDrawEventFromLog(eth_log);

            // 得到产品周期
            ProProductCycle cycle = proProductCycleApi.getSingle(eth_log.getAddress(), response.token_address, response.product_value, response.current_index);
            if (cycle == null) {
                log.error("找不到产品周期 {} {} {} {}", eth_log.getAddress(), response.token_address, response.product_value, response.current_index);
                return;
            }

            if (cycle.getState().intValue() != StateProProductCycle.wait.getState()) {
                log.error("产品周期状态错误 {} {} {} {}", eth_log.getAddress(), response.token_address, response.product_value, response.current_index);
                return;
            }

            // 变更产品周期状态
            cycle.setState(response.state);
            cycle.setReqOpenDrawBlockNumber(eth_log.getBlockNumber());
            cycle.setWinCode(response.win_code);
            cycle.setWinAddress(response.win_address);
            cycle.setWinBlockNumber(eth_log.getBlockNumber());
            cycle.setClaimState(response.claim_state);
            proProductCycleApi.updateById(cycle);
        }

    }
}
