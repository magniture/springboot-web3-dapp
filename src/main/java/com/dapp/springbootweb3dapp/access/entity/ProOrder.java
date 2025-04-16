package com.dapp.springbootweb3dapp.access.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import sol.Main;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * <p>
 *
 * </p>
 *
 * @author sunyoki
 * @since 2025-02-08
 */
@TableName("pro_order")
@ApiModel(value = "ProOrder对象", description = "")
@Data
public class ProOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String contractAddress;

    private String tokenAddress;

    private BigInteger productValue;

    private BigInteger currentIndex;

    private String userAddress;

    private BigInteger count;

    private BigInteger blockNumber;

    private BigInteger lockCode;

    private BigInteger ticketValue;

    private BigInteger createBlockNumber;

    public static ProOrder fillEventResponse(Main.UserConfirmJoinEventResponse response, String address, BigInteger number) {

        ProOrder order = new ProOrder();

        order.setContractAddress(address);
        order.setTokenAddress(response.token_address);
        order.setProductValue(response.product_value);
        order.setCurrentIndex(response.current_index);
        order.setUserAddress(response.user_address);
        order.setCount(response.count);
        order.setBlockNumber(response.block_number);
        order.setLockCode(response.lock_code);
        order.setTicketValue(response.ticket_value);
        order.setCreateBlockNumber(number);
        return order;
    }


    @Override
    public String toString() {
        return "ProOrder{" +
                "id=" + id +
                ", contractAddress=" + contractAddress +
                ", tokenAddress=" + tokenAddress +
                ", productValue=" + productValue +
                ", currentIndex=" + currentIndex +
                ", userAddress=" + userAddress +
                ", count=" + count +
                ", blockNumber=" + blockNumber +
                ", lockCode=" + lockCode +
                ", ticketValue=" + ticketValue +
                ", createBlockNumber=" + createBlockNumber +
                "}";
    }
}
