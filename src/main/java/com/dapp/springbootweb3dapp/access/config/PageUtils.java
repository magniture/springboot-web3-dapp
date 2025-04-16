package com.dapp.springbootweb3dapp.access.config;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PageUtils {


    // 状态过滤
    public Integer state;


    // 快捷状态
    private Integer quick_state;


    // 全链路数据, 或只看自己的数据
    private Boolean is_me_create = false;

    // 每页大小
    @JSONField(serialize = false)
    private int size = 10;
    // 当前页， 页码从1开始
    @JSONField(serialize = false)
    private int current = 1;
    // antd 需要的 这个优先级更高
    private Integer pageSize;
    // 开始时间，要小于结束时间
    private String start_time;
    // 结束时间， 要大于开始时间
    private String end_time;
    // 多用户，过滤
    private String uuid;
    // 关键字聚合搜索
    private String keyword;

    // 数据模式
    private String need_mode;



    private String ascend;

    private String descend;


    // 通用过滤部门ID
    private String dept_id;

    public int getSize() {
        if (pageSize != null) {
            return Math.min(pageSize, 500);
        }
        return Math.min(size, 500);
    }


}
