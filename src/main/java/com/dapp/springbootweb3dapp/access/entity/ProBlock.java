package com.dapp.springbootweb3dapp.access.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author sunyoki
 * @since 2025-02-08
 */
@TableName("pro_block")
@ApiModel(value = "ProBlock对象", description = "")
@Data
public class ProBlock implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private BigInteger number;

    private String hash;

    private BigInteger timestamp;

    private BigInteger transactionCount;

    @ApiModelProperty("1, 已处理， 2，报错了")
    private Integer state;

    private LocalDateTime readTime;

    private LocalDateTime okTime;


    @Override
    public String toString() {
        return "ProBlock{" +
        "id=" + id +
        ", number=" + number +
        ", hash=" + hash +
        ", timestamp=" + timestamp +
        ", transactionCount=" + transactionCount +
        ", state=" + state +
        ", readTime=" + readTime +
        ", okTime=" + okTime +
        "}";
    }
}
