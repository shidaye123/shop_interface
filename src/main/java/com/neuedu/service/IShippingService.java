package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

public interface IShippingService {

    //未登录状态添加收获地址
    ServerResponse addAdress(Integer userId, String receiverName, String receiverPhone,
                             String receiverMobile, String receiverProvince, String receiverCity,
                             String receiverAddress, String receiverZip);
    //删除地址
    ServerResponse deleteAdress(Integer id,Integer userId);
    //登录状态更新地址
    ServerResponse updateAdress(Integer id, Integer userId, String receiverName,
                                String receiverPhone, String receiverMobile, String receiverProvince,
                                String receiverCity, String receiverAddress, String receiverZip);
    //查看选中地址详情
    ServerResponse getDetailAdress(Integer id,Integer userId);
    //获取地址列表
    ServerResponse getShippingList(Integer userId,Integer pageNum,Integer pageSize);

}
