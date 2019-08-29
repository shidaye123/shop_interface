package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.constant.Constants;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    @Autowired
    ICartService cartService;

    //添加商品
    @RequestMapping(value = "/add.do",method = RequestMethod.GET)
    public ServerResponse addProduct(@RequestParam(name = "productId")Integer productId,
                                     @RequestParam(name = "count")Integer count,
                                     HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }
        return cartService.addProduct(productId,user.getId(),count);
    }

    //查询购物车列表
    @RequestMapping(value = "/list.do",method = RequestMethod.GET)
    public ServerResponse cartList(HttpSession session,
                                   @RequestParam(name = "userId")Integer userId){

//        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
//        if (user==null){
//            return ServerResponse.createServerResponseFail(1,"用户未登录");
//        }

        return cartService.cartList(userId);
    }

    //更新购物车某个商品的数量
    @RequestMapping(value = "/update.do",method = RequestMethod.GET)
    public ServerResponse updateCount(Integer productId,Integer count,
                                      HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return cartService.addProduct(productId,user.getId(),count);
    }

    //删除某个商品
    @RequestMapping(value = "/delete_product.do",method = RequestMethod.GET)
    public ServerResponse deleteProduct(String productIds,HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return cartService.deleteProduct(productIds,user.getId());
    }

    //选中商品
    @RequestMapping(value = "/select.do",method = RequestMethod.GET)
    public ServerResponse checkedProduct(Integer productId,HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return cartService.checkedProduct(productId,user.getId());
    }

    //取消选中
    @RequestMapping(value = "/un_select.do",method = RequestMethod.GET)
    public ServerResponse uncheckedProduct(Integer productId,HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }
        return cartService.uncheckedProduct(productId,user.getId());
    }

    //查询购物车里产品数量
    @RequestMapping(value = "/get_cart_product_count.do",method = RequestMethod.GET)
    public ServerResponse getCartProductCount(HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }
        return cartService.getCartProductCount(user.getId());

    }

    //购物车全选
    @RequestMapping(value = "/select_all.do",method = RequestMethod.GET)
    public ServerResponse selectAll(HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return cartService.selectAll(user.getId());

    }

    //购物车取消全选
    @RequestMapping(value = "/un_select_all.do",method = RequestMethod.GET)
    public ServerResponse unselectAll(HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录");
        }

        return cartService.unSelectAll(user.getId());

    }

}
