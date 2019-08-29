package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingService;
import com.neuedu.vo.shipping.ShippingIdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    ShippingMapper shippingMapper;

    //添加收获地址
    @Override
    public ServerResponse addAdress(Integer userId, String receiverName, String receiverPhone,
                                    String receiverMobile, String receiverProvince, String receiverCity,
                                    String receiverAddress, String receiverZip) {

        if (userId==null||userId.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverName==null||receiverName.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverPhone==null||receiverPhone.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverMobile==null||receiverMobile.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverProvince==null||receiverProvince.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverCity==null||receiverCity.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverAddress==null||receiverAddress.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverZip==null||receiverZip.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        Shipping shipping = new Shipping();
        shipping.setUserId(userId);
        shipping.setReceiverName(receiverName);
        shipping.setReceiverPhone(receiverPhone);
        shipping.setReceiverMobile(receiverMobile);
        shipping.setReceiverProvince(receiverProvince);
        shipping.setReceiverCity(receiverCity);
        shipping.setReceiverAddress(receiverAddress);
        shipping.setReceiverZip(receiverZip);

        int count = shippingMapper.insert(shipping);
        if (count<=0){
            return ServerResponse.createServerResponseFail(1,"新建地址失败！");
        }

        List<Shipping> shippingList = shippingMapper.getAllShipping(userId);
        List<ShippingIdVO> shippingIdVOList = Lists.newArrayList();
        if (shippingList!=null&&shippingList.size()>0){
            for (Shipping shipping1:shippingList){
                ShippingIdVO shippingIdVO = shippingIdVO(shipping1);
                shippingIdVOList.add(shippingIdVO);
            }
        }

        return ServerResponse.createServerResponseSuccess(shippingIdVOList);
    }

    //删除地址
    @Override
    public ServerResponse deleteAdress(Integer id, Integer userId) {

        if (id==null||id.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        int count = shippingMapper.deleteAdress(id,userId);
        if (count<=0){
            return ServerResponse.createServerResponseFail(1,"删除地址失败！");
        }
        return ServerResponse.createServerResponseSuccess("删除地址成功！");
    }

    //登录状态下更新地址信息
    @Override
    public ServerResponse updateAdress(Integer id, Integer userId, String receiverName, String receiverPhone, String receiverMobile, String receiverProvince, String receiverCity, String receiverAddress, String receiverZip) {
        if (id==null||id.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (userId==null||userId.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverName==null||receiverName.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverPhone==null||receiverPhone.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverMobile==null||receiverMobile.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverProvince==null||receiverProvince.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverCity==null||receiverCity.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverAddress==null||receiverAddress.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        if (receiverZip==null||receiverZip.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        Shipping shipping = new Shipping();
        shipping.setId(id);
        shipping.setUserId(userId);
        shipping.setReceiverName(receiverName);
        shipping.setReceiverPhone(receiverPhone);
        shipping.setReceiverMobile(receiverMobile);
        shipping.setReceiverProvince(receiverProvince);
        shipping.setReceiverCity(receiverCity);
        shipping.setReceiverAddress(receiverAddress);
        shipping.setReceiverZip(receiverZip);

        int count = shippingMapper.updateByPrimaryKey(shipping);
        if (count<=0){
            return ServerResponse.createServerResponseFail(1,"更新地址失败！");
        }

        return ServerResponse.createServerResponseSuccess("更新地址成功！");
    }

    //查看选中地址详情
    @Override
    public ServerResponse getDetailAdress(Integer id, Integer userId) {
        if (id==null||id.equals("")){
            return ServerResponse.createServerResponseFail(2,"参数不能为空！");
        }
        Shipping shipping = shippingMapper.getDetailAdress(id, userId);
        if (shipping==null){
            return ServerResponse.createServerResponseFail(3,"地址不存在！");
        }

        return ServerResponse.createServerResponseSuccess(shipping);
    }

    //获取地址列表
    @Override
    public ServerResponse getShippingList(Integer userId,Integer pageNum,Integer pageSize) {

        Page page = PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.getShippingList(userId);
        if (shippingList==null||shippingList.size()<=0){
            return ServerResponse.createServerResponseFail("地址列表为空！");
        }
        PageInfo pageInfo = new PageInfo(page);
        return ServerResponse.createServerResponseSuccess(pageInfo);
    }

    //转换
    private ShippingIdVO shippingIdVO(Shipping shipping){

        ShippingIdVO shippingIdVO = new ShippingIdVO();
        shippingIdVO.setShippingId(shipping.getId());

        return shippingIdVO;
    }
}
