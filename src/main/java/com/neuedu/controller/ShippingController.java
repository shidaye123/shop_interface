package com.neuedu.controller;

import ch.qos.logback.core.joran.event.SaxEventRecorder;
import com.neuedu.common.ServerResponse;
import com.neuedu.constant.Constants;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    IShippingService shippingService;

    //未登录状态添加收获地址
    @RequestMapping(value = "/add.do",method = RequestMethod.GET)
    public ServerResponse addAdress(@RequestParam("userId")Integer userId,
                                    @RequestParam("receiverName")String receiverName,
                                    @RequestParam("receiverPhone")String receiverPhone,
                                    @RequestParam("receiverMobile")String receiverMobile,
                                    @RequestParam("receiverProvince")String receiverProvince,
                                    @RequestParam("receiverCity")String receiverCity,
                                    @RequestParam("receiverAddress")String receiverAddress,
                                    @RequestParam("receiverZip")String receiverZip){

        return shippingService.addAdress(userId, receiverName,
                                         receiverPhone, receiverMobile,
                                         receiverProvince, receiverCity,
                                         receiverAddress, receiverZip);

    }

    //删除地址
    @RequestMapping(value = "/del.do",method = RequestMethod.GET)
    public ServerResponse deleteAddess(@RequestParam("shippingId")Integer id,
                                       HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }
        return shippingService.deleteAdress(id, user.getId());

    }

    //登录状态更新地址
    @RequestMapping(value = "/update.do",method = RequestMethod.GET)
    public ServerResponse updateAdress(@RequestParam("id")Integer id,
                                       @RequestParam("receiverName")String receiverName,
                                       @RequestParam("receiverPhone")String receiverPhone,
                                       @RequestParam("receiverMobile")String receiverMobile,
                                       @RequestParam("receiverProvince")String receiverProvince,
                                       @RequestParam("receiverCity")String receiverCity,
                                       @RequestParam("receiverAddress")String receiverAddress,
                                       @RequestParam("receiverZip")String receiverZip,
                                       HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }
        return shippingService.updateAdress(id,user.getId(),receiverName,
                                            receiverPhone,receiverMobile,receiverProvince,
                                            receiverCity,receiverAddress,receiverZip);
    }

    //选中查看具体地址
    @RequestMapping(value = "/select.do",method = RequestMethod.GET)
    public ServerResponse getDetailAdress(@RequestParam("shippingId")Integer id,
                                          HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return shippingService.getDetailAdress(id,user.getId());

    }

    //地址列表
    @RequestMapping(value = "/list.do",method = RequestMethod.GET)
    public ServerResponse shippingList(@RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                       @RequestParam(name = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                                       HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return shippingService.getShippingList(user.getId(),pageNum,pageSize);

    }
}
