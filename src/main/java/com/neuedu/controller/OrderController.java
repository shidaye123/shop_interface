package com.neuedu.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.constant.Constants;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    IOrderService orderService;


    //创建订单
    @RequestMapping(value = "/create.do",method = RequestMethod.GET)
    public ServerResponse createOrder(@RequestParam("shippingId")Integer shippingId,
                                      HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return orderService.createOrder(user.getId(),shippingId);
    }

    //获取订单商品信息
    @RequestMapping(value = "/get_order_cart_product.do",method = RequestMethod.GET)
    public ServerResponse getOrderProductInfo(HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return orderService.getOrderProductInfo(user.getId());
    }

    //订单列表
    @RequestMapping(value = "/list.do",method = RequestMethod.GET)
    public ServerResponse getOrderList(@RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                       @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                                       HttpSession session){

//        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
//        if (user==null){
//            return ServerResponse.createServerResponseFail(1,"用户未登录");
//        }user.getId()

        return orderService.getOrderList(pageNum,pageSize,21);

    }

    //订单详情
    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    public ServerResponse getOrderDetail(@RequestParam("orderNo") long orderNo,
                                         HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return orderService.getOrderDetail(orderNo,user.getId());
    }

    //取消订单
@RequestMapping(value = "/cancel.do",method = RequestMethod.GET)
    public ServerResponse cancelOrder(@RequestParam("orderNo")long orderNo,
                                      HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return orderService.cancelOrder(orderNo,user.getId());
    }

    //支付
    @RequestMapping(value = "/pay.do",method = RequestMethod.GET)
    public ServerResponse payOrder(@RequestParam("orderNo") long orderNo,
                                   HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return orderService.pay(user.getId(),orderNo);
    }

    //支付回调接口
    @RequestMapping(value = "/alipay_callback.do",method = RequestMethod.GET)
    public ServerResponse callback(HttpServletRequest request,
                                   HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        Map<String,String[]> params = request.getParameterMap();
        Map<String,String> requestParams = Maps.newHashMap();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            String[] strArr = params.get(key);
            String value = "";
            for (int i=0;i<strArr.length;i++){
                value = (i==strArr.length-1)?value + strArr[i]:value + strArr[i]+",";
            }
            requestParams.put(key,value);
        }
        //支付宝验签
        try {
            requestParams.remove("sign_type");
            boolean result = AlipaySignature.rsaCheckV2(requestParams, Configs.getPublicKey(),"utf-8",Configs.getSignType());
            if (!result){
                return ServerResponse.createServerResponseFail("验签不通过！");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        System.out.println("=============q=============");

        return orderService.alipay_callback(requestParams);
    }
}
