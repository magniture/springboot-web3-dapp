package com.dapp.springbootweb3dapp.access.state;

import lombok.Getter;

@Getter
public enum StateProProductCycle {

    ing(10, "参与中"),

    wait(20, "等待开奖"),

    ok(30, "已开奖");

    private final int state;

    private final String name;


    StateProProductCycle(int state, String name) {
        this.state = state;
        this.name = name;
    }
}
