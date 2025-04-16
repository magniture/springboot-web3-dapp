package com.dapp.springbootweb3dapp.access.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import sol.Main;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author sunyoki
 * @since 2025-02-08
 */
@TableName("pro_product_cycle")
@ApiModel(value = "ProProductCycle对象", description = "")
@Data
public class ProProductCycle implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String contractAddress;

    private String tokenAddress;

    private BigInteger productValue;

    private BigInteger currentIndex;

    private BigInteger state;

    private BigInteger totalValue;

    private BigInteger winCode;

    private String winAddress;

    private BigInteger winBlockNumber;

    private BigInteger claimState;


    private BigInteger completeBlockNumber;
    private BigInteger preOpenDrawBlockNumber;
    private BigInteger reqOpenDrawBlockNumber;

    private BigInteger createBlockNumber;


    @TableField(exist = false)
    private JSONObject symbol;

    @TableField(exist = false)
    private ProProduct product;

    @TableField(exist = false)
    private List<ProOrder> orders;

    @TableField(exist = false)
    private List<ProBlock> blocks;


    public static ProProductCycle fillEventResponse(Main.CreateProductCycleEventResponse response, String address, BigInteger number) {
        ProProductCycle cycle = new ProProductCycle();

        cycle.setContractAddress(address);
        cycle.setTokenAddress(response.token_address);
        cycle.setProductValue(response.product_value);
        cycle.setCurrentIndex(response.current_index);
        cycle.setState(response.state);
        cycle.setTotalValue(response.total_value);
        cycle.setWinCode(response.win_code);
        cycle.setWinAddress(response.win_address);
        cycle.setClaimState(response.claim_state);
        cycle.setCreateBlockNumber(number);
        return cycle;

    }


    @Override
    public String toString() {
        return "ProProductCycle{" +
                "id=" + id +
                ", contractAddress=" + contractAddress +
                ", tokenAddress=" + tokenAddress +
                ", productValue=" + productValue +
                ", currentIndex=" + currentIndex +
                ", state=" + state +
                ", totalValue=" + totalValue +
                ", winCode=" + winCode +
                ", winAddress=" + winAddress +
                ", winBlockNumber=" + winBlockNumber +
                ", claimState=" + claimState +
                ", createBlockNumber=" + createBlockNumber +
                "}";
    }
}
