package com.dapp.springbootweb3dapp.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("/product/")
public class ProductController {


    @Resource
    ProductService productService;

    @GetMapping("list")
    public ApiResponse<List<ProProductCycle>> list(Integer state) {
        return productService.list(state);
    }

    @GetMapping("detail")
    public ApiResponse<ProProductCycle> detail(String contract_address, String product_value, String token_address,String  current_index) {
        return productService.detail(contract_address, product_value, token_address, current_index);
    }

    public static void main(String[] args) {
        Web3j web3j = Web3Client.getInstance();

        try {
            EthCall ethCall = web3j.ethCall(Transaction
                    .createEthCallTransaction(
                            "0x17e0D173CA4Ef9A61EDC2042C91DC97d4fcA5DbA",
                            "0x8dc0e9662385e7c9f27b7bef61ed379c3fe08e87",
                            "0x313ce567"), DefaultBlockParameterName.LATEST).send();
            //  获取币种名称 0x06fdde03
            //new String(Numeric.hexStringToByteArray(ethCall.getValue().replaceAll("0x",""))).trim();
            //  获取币种符号 0x95d89b41
            //new String(Numeric.hexStringToByteArray(ethCall.getValue().replaceAll("0x",""))).trim()
            //   币种小数位 0x95d89b41
            //new BigInteger(ethCall.getValue().replaceAll("0x",""), 16)
            System.out.println(ethCall.getValue());;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
