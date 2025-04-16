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
@TableName("pro_product")
@ApiModel(value = "ProProduct对象", description = "")
@Data
public class ProProduct implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String contractAddress;

    private String tokenAddress;

    private BigInteger productValue;

    private BigInteger startIndex;

    private BigInteger faceValue;

    private BigInteger feeValue;

    private String feeAddress;

    private BigInteger ticketValue;

    private BigInteger maxCount;

    private BigInteger startLockCode;

    private BigInteger createBlockNumber;


    public static ProProduct fillEventResponse(Main.CreateProductEventResponse response, String contractAddress, BigInteger createBlockNumber) {
        ProProduct product = new ProProduct();

        product.setTokenAddress(response.token_address);
        product.setProductValue(response.product_value);
        product.setContractAddress(contractAddress);
        product.setStartIndex(response.start_index);
        product.setFaceValue(response.face_value);
        product.setFeeValue(response.fee_value);
        product.setFeeAddress(response.fee_address);
        product.setTicketValue(response.ticket_value);
        product.setMaxCount(response.max_count);
        product.setStartLockCode(response.start_lock_code);
        product.setCreateBlockNumber(createBlockNumber);
        return product;
    }
}
