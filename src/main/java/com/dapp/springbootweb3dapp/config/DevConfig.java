package com.dapp.springbootweb3dapp.config;

import java.math.BigInteger;

public class DevConfig {
    public static final int AVAILABLE_PROCESSOR = Runtime.getRuntime().availableProcessors();
    public static boolean is_dev = false;


    public static final String network = "test";



    // 确认区块次数
    public static final BigInteger confirmed_blocks_Number = BigInteger.valueOf(20);

}
