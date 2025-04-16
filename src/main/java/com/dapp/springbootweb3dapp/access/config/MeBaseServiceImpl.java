package com.dapp.springbootweb3dapp.access.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.base.MPJBaseService;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;


public class MeBaseServiceImpl<M extends MPJBaseMapper<T>, T> extends ServiceImpl<M, T> implements MPJBaseService<T> {


    @Override
    public Class<T> currentModelClass() {
        return super.currentModelClass();
    }

    /**
     * 过滤时间，默认为创建时间
     *
     * @param wrapper
     * @param request
     */
    public void setFilterDateRangeDefaultCreateTime(MPJLambdaWrapper<T> wrapper, PageUtils request) {
        setFilterDateRange(wrapper, request, "t.create_time");
    }


//    public static void main(String[] args) {
//        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        String start_time = "2021-07-21 07:17:34";
//        String s = LocalDate.parse(start_time, formatter).toString() + " 00:00:00";
//
//        System.out.println(s);
//
//    }


    private void setFilterDateRange(Wrapper<T> wrapper, String sql) {
        if (wrapper instanceof MPJLambdaWrapper) {
            MPJLambdaWrapper<T> lambdaWrapper = (MPJLambdaWrapper<T>) wrapper;
            lambdaWrapper.apply(sql);
        } else if (wrapper instanceof MPJQueryWrapper) {
            MPJQueryWrapper<T> mpjQueryWrapper = (MPJQueryWrapper<T>) wrapper;
            mpjQueryWrapper.apply(sql);
        } else if (wrapper instanceof QueryWrapper) {
            QueryWrapper<T> queryWrapper = (QueryWrapper<T>) wrapper;
            queryWrapper.apply(sql);
        } else if (wrapper instanceof LambdaQueryWrapper) {
            LambdaQueryWrapper<T> lambdaQueryWrapper = (LambdaQueryWrapper<T>) wrapper;
            lambdaQueryWrapper.apply(sql);
        }
    }




    /**
     * 设置排序
     * 可传 user_code or userCode
     * <p>
     * 注意 字段如果是my_开头，则表示非数据库字段， 则不加表的别名
     *
     * @param queryWrapper 查询文件
     * @param default_sort 默认排序字段
     * @param request      排序字段
     */

    public void setSort(MPJLambdaWrapper<T> queryWrapper, SFunction<T, ?> default_sort, PageUtils request) {


        Field[] declaredFields = this.entityClass.getDeclaredFields();
        HashSet<String> names = new HashSet<>();
        for (Field declaredField : declaredFields) {
            names.add(declaredField.getName());
        }

        if (StrUtil.isEmpty(request.getAscend()) && StrUtil.isEmpty(request.getDescend())) {
            defaultSort(queryWrapper, default_sort);
            return;
        }
        if (!StrUtil.isEmpty(request.getAscend())) {
            if (names.contains(StrUtil.toCamelCase(request.getAscend()))) {
                if (StrUtil.toUnderlineCase(request.getAscend()).contains("my_")) {
                    queryWrapper.orderByAsc(StrUtil.toUnderlineCase(request.getAscend()));
                } else {
                    queryWrapper.orderByAsc("t." + StrUtil.toUnderlineCase(request.getAscend()));
                }
            } else if (default_sort == null) {
                queryWrapper.orderByAsc(request.getAscend());
            } else {
                defaultSort(queryWrapper, default_sort);
            }
        } else if (!StrUtil.isEmpty(request.getDescend())) {
            if (names.contains(StrUtil.toCamelCase(request.getDescend()))) {

                if (StrUtil.toUnderlineCase(request.getDescend()).contains("my_")) {
                    queryWrapper.orderByDesc(StrUtil.toUnderlineCase(request.getDescend()));
                } else {
                    queryWrapper.orderByDesc("t." + StrUtil.toUnderlineCase(request.getDescend()));
                }
            } else if (default_sort == null) {
                queryWrapper.orderByDesc(request.getDescend());
            } else {
                defaultSort(queryWrapper, default_sort);
            }
        } else {
            defaultSort(queryWrapper, default_sort);
        }
    }

    private void defaultSort(MPJLambdaWrapper<T> queryWrapper, SFunction<T, ?> default_sort) {
        if (default_sort != null) {
            queryWrapper.orderByDesc(default_sort);
        }
    }

    /**
     * 设置时间过滤
     *
     * @param wrapper
     * @param request
     * @param col_name
     */
    public void setFilterDateRange(MPJLambdaWrapper<T> wrapper, PageUtils request, String col_name) {


        String start_time = request.getStart_time();
        String end_time = request.getEnd_time();
        try {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


            if (!StrUtil.isEmpty(request.getStart_time()) && !StrUtil.isEmpty(request.getEnd_time())) {

                String start_time_a;
                String end_time_a;
                try {
                    start_time_a = LocalDate.parse(start_time, pattern).toString() + " 00:00:00";
                } catch (Exception e) {
                    start_time_a = LocalDateTime.parse(start_time, formatter).toString();
                }

                try {
                    end_time_a = LocalDate.parse(end_time, pattern).toString() + " 23:59:59";
                } catch (Exception e) {
                    end_time_a = LocalDateTime.parse(end_time, formatter).toString();
                }

                wrapper.between(col_name, start_time_a, end_time_a);

            } else {
                if (!StrUtil.isEmpty(start_time)) {
                    try {
                        setFilterDateRange(wrapper, col_name + " >= " + "'" + LocalDate.parse(start_time, pattern).toString() + " 00:00:00'");
                    } catch (Exception e) {
                        setFilterDateRange(wrapper, col_name + " >= " + "'" + LocalDateTime.parse(start_time, formatter) + "'");
                    }
                }
                if (!StrUtil.isEmpty(end_time)) {
                    try {
                        setFilterDateRange(wrapper, col_name + " <= " + "'" + LocalDate.parse(end_time, pattern).toString() + " 23:59:59'");
                    } catch (Exception e) {
                        setFilterDateRange(wrapper, col_name + " <= " + "'" + LocalDateTime.parse(end_time, formatter) + "'");
                    }
                }
            }
        } catch (Exception ignored) {

        }
    }

}
