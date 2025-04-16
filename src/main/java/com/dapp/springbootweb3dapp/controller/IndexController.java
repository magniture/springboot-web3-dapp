package com.dapp.springbootweb3dapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {


    @GetMapping("/")
    public ApiResponse<String> index() {
        return ApiResponse.success("恭喜你，项目搭建成功。。。");
    }
}
