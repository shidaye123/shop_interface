package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Order;

import java.util.List;
import java.util.Map;

public interface IOrderService {

    //创建订单
    ServerResponse createOrder(Integer userId,Integer shippingId);
    //获取订单商品信息
    ServerResponse getOrderProductInfo(Integer userId);
    //订单列表
    ServerResponse getOrderList(Integer pageNum,Integer pageSize,Integer userId);
    //订单详情
    ServerResponse getOrderDetail(long orderNo,Integer userId);
    //取消订单
    ServerResponse cancelOrder(long orderNo,Integer userId);
    //支付
    ServerResponse pay(Integer userId,long orderNo);
    //支付宝回调
    ServerResponse alipay_callback(Map<String,String> map);
    //定时关单
    List<Order> selectOrderByCreateTime(String closeOrderTime);

}
