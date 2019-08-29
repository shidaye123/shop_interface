package com.neuedu.service.impl;


import com.neuedu.common.ServerResponse;
import com.neuedu.constant.Constants;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.vo.cart.CartProductVO;
import com.neuedu.vo.cart.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;

    //添加商品
    @Override
    public ServerResponse addProduct(Integer productId,Integer userId, Integer count) {

        if (productId==null||count==null){
            return ServerResponse.createServerResponseFail(9,"参数不能为空！");
        }

        Cart cart = cartMapper.findByProductIdAndUserId(productId, userId);
        if (cart==null){
            //添加
            Cart cart1 = new Cart();
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(count);
            cart1.setChecked(Constants.productCheckedEnum.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);
        }else {
            //更新
            Cart cart1 = new Cart();
            cart1.setId(cart.getId());
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(cart.getQuantity()+count);
            cart1.setChecked(Constants.productCheckedEnum.PRODUCT_CHECKED.getCode());
            cartMapper.updateByPrimaryKey(cart1);
        }

        CartVO cartVO = cartVO(userId);

        if (cartVO==null){
            return ServerResponse.createServerResponseFail(2,"更新数据失败");
        }

        return ServerResponse.createServerResponseSuccess(cartVO);
    }

    //购物车列表
    @Override
    public ServerResponse cartList(Integer userId) {

        CartVO cartVO = cartVO(userId);
        if (cartVO==null){
            return ServerResponse.createServerResponseFail(1,"没有商品！");
        }
        return ServerResponse.createServerResponseSuccess(cartVO);
    }

    //删除某些商品
    @Override
    public ServerResponse deleteProduct(String productIds, Integer userId) {

        if (productIds==null||productIds.equals("")){
            return ServerResponse.createServerResponseFail(9,"参数不能为空！");
        }

        String[] productIdArray = productIds.split(",");
        List<Integer> productIdList = new ArrayList<>();
        if (productIdArray!=null&&productIdArray.length>0){
            for (String productIdstr:productIdArray){
                Integer productId = Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }
        int count = cartMapper.deleteProduct(productIdList,userId);
        if (count<=0){
            return ServerResponse.createServerResponseFail(3,"商品不存在！");
        }
        CartVO cartVO = cartVO(userId);
        if (cartVO==null){
            return ServerResponse.createServerResponseFail("购物车为空！");
        }
        return ServerResponse.createServerResponseSuccess(cartVO);
    }

    //选中商品
    @Override
    public ServerResponse checkedProduct(Integer productId, Integer usreId) {

        if (productId!=null&&productId.equals("")){
            return ServerResponse.createServerResponseFail(9,"参数不能为空！");
        }
        int count = cartMapper.checkedProduct(productId, usreId);
        if (count<=0){
            return ServerResponse.createServerResponseFail(3,"商品不存在！");
        }
        CartVO cartVO = cartVO(usreId);
        if (cartVO==null){
            return ServerResponse.createServerResponseFail("购物车为空！");
        }
        return ServerResponse.createServerResponseSuccess(cartVO);
    }

    //取消选中商品
    @Override
    public ServerResponse uncheckedProduct(Integer productId, Integer userId) {

        if (productId==null||productId.equals("")){
            return ServerResponse.createServerResponseFail(9,"参数不能为空！");
        }
        int count = cartMapper.uncheckedProduct(productId, userId);
        if (count<=0){
            return ServerResponse.createServerResponseFail(3,"商品不存在！");
        }
        CartVO cartVO = cartVO(userId);
        if (cartVO==null){
            return ServerResponse.createServerResponseFail("购物车为空！");
        }

        return ServerResponse.createServerResponseSuccess(cartVO);
    }

    //查看购物车商品数量
    @Override
    public ServerResponse getCartProductCount(Integer userId) {

        int totalProductCount = 0;
        List<Cart> cartList = cartMapper.getCartProductCount(userId);
        if (cartList!=null&&cartList.size()>0){
            for (Cart cart:cartList){
                totalProductCount += cart.getQuantity();
            }
        }
        return ServerResponse.createServerResponseSuccess(totalProductCount);
    }

    //购物车全选
    @Override
    public ServerResponse selectAll(Integer userId) {

        int count = cartMapper.selectAllProduct(userId);
        if (count<=0){
            return ServerResponse.createServerResponseFail(3,"商品不存在！");
        }
        CartVO cartVO = cartVO(userId);
        if (cartVO==null){
            return ServerResponse.createServerResponseFail("购物车为空！");
        }
        return ServerResponse.createServerResponseSuccess(cartVO);
    }

    //取消全选
    @Override
    public ServerResponse unSelectAll(Integer userId) {

        int count = cartMapper.unselectAll(userId);
        if (count<=0){
            return ServerResponse.createServerResponseFail(3,"商品不存在！");
        }
        CartVO cartVO = cartVO(userId);
        if (cartVO==null){
            return ServerResponse.createServerResponseFail("购物车为空！");
        }
        return ServerResponse.createServerResponseSuccess(cartVO);
    }

    //转化为前台显示的VO
    private CartVO cartVO(Integer userId){

        CartVO cartVO = new CartVO();
        List<CartProductVO> productVO = cartVO.getCartProductVOList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        //根据用户id查找list<cart>,将其转化为list<cartProductVO>
        List<Cart> cartList = cartMapper.findByUserId(userId);
        if (cartList!=null&&cartList.size()>0){
            cartVO.setAllChecked(true);
            for (Cart cart:cartList){
                CartProductVO cartProductVO = cartProductVO(cart);
                productVO.add(cartProductVO);
                if (cartProductVO.getProductChecked()==Constants.productCheckedEnum.PRODUCT_UNCHECKED.getCode()){
                    cartVO.setAllChecked(false);
                }else {
                    cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                }
            }
        }else {
            return null;
        }
        //计算总价格
        cartVO.setCartTotalPrice(cartTotalPrice);

        return cartVO;

    }

    private CartProductVO cartProductVO(Cart cart){

        CartProductVO cartProductVO = new CartProductVO();

        cartProductVO.setId(cart.getId());
        cartProductVO.setUserId(cart.getUserId());
        cartProductVO.setProductId(cart.getProductId());
        cartProductVO.setQuantity(cart.getQuantity());
        //根据productId查找商品
        Product product = productMapper.findById(cart.getProductId());
        if (product!=null){
            cartProductVO.setProductName(product.getName());
            cartProductVO.setProductSubtitle(product.getSubtitle());
            cartProductVO.setProductMainImage(product.getMainImage());
            cartProductVO.setProductPrice(product.getPrice());
            cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(cart.getQuantity(),product.getPrice().doubleValue()));
            cartProductVO.setProductStock(product.getStock());
            cartProductVO.setProductStatus(product.getStatus());
        }
        cartProductVO.setProductChecked(cart.getChecked());
        if (cart.getQuantity()<=product.getStock()){
            cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
        }else {
            cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
        }

        return cartProductVO;
    }

}
