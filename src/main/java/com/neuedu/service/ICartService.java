package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICartService {

    //添加商品
    public ServerResponse addProduct(Integer productId,Integer userId,Integer count);
    //查询购物车列表
    public ServerResponse cartList(Integer userId);
    //删除商品
    public ServerResponse deleteProduct(String productIds,Integer userId);
    //选中某个商品
    public ServerResponse checkedProduct(Integer productId,Integer usreId);
    //取消选中商品
    public ServerResponse uncheckedProduct(Integer productId,Integer userId);
    //查询购物车商品数量
    public ServerResponse getCartProductCount(Integer userId);
    //购物车全选
    public ServerResponse selectAll(Integer userId);
    //购物车取消全选
    public ServerResponse unSelectAll(Integer userId);

}
