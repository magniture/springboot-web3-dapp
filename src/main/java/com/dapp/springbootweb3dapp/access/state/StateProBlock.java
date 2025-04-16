
package com.dapp.springbootweb3dapp.access.state;

import lombok.Getter;

@Getter
public enum StateProBlock {

    error(1, "未处理-报错了"),

    ok(2, "处理成功");

    private final int state;

    private final String name;


    StateProBlock(int state, String name) {
        this.state = state;
        this.name = name;
    }
}
