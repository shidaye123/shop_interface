package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.exception.MyException;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.IProductService;
import com.neuedu.vo.product.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;

    //商品分页搜索
    @Override
    public ServerResponse search(String name, Integer id, Integer pageNum, Integer pageSize) {

        if (name!=null){
            name = "%"+name+"%";
        }
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.findProductByNameAndId(name, id);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(page);
        pageInfo.setList(productListVOList);
        return ServerResponse.createServerResponseSuccess(null,pageInfo);
    }

    //产品搜索及动态排序List
    @Override
    public ServerResponse searchByOrder(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy) {

        if (categoryId==null&&(keyword==null||keyword.equals(""))){
            return ServerResponse.createServerResponseFail(2,"不能为空！");
        }
        if (keyword!=null){
            keyword = "%"+keyword+"%";
        }
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.findProductByKeywordAndCategoryId(keyword, categoryId, orderBy);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList!=null||productList.size()>0){
            for (Product product:productList){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(page);
        pageInfo.setList(productListVOList);

        return ServerResponse.createServerResponseSuccess(null,pageInfo);
    }

    //商品详情
    @Override
    public List<Product> findProductDetail(int id) {

        List<Product> productList = productMapper.findProductDetail(id);
        return productList;
    }

    //查看商品是否下架
    @Override
    public int isGrounding(int id) {

        int status = productMapper.isGrounding(id);
        return status;
    }

    //查找商品主类别
    @Override
    public List<Category> findTopcategory(int parentId) {
        List<Category> categoryList = productMapper.findTopcategory(parentId);
        return categoryList;
    }

    //VO转换方法
    private ProductListVO assembleProductListVO(Product product){

        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setName(product.getName());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setPrice(product.getPrice());

        return productListVO;

    }








    @Override
    public List<Product> findByPage(int page,int size) {
        List<Product> productList = productMapper.findByPage(page,size);
        if (productList!=null){
            return productList;
        }
        throw new MyException("没有商品！");
    }

    @Override
    public int findPagecounts() throws MyException {
        int count = productMapper.findPagecounts();
        if (count>0){
            return count;
        }
        throw new MyException("不存在商品！");
    }

    @Override
    public int deleteById(int id) throws MyException {

        int count = productMapper.deleteById(id);
        if (count>0){
            return 1;
        }
        throw new MyException("删除商品失败！");
    }

    @Override
    public List<Category> findCategory() throws MyException {

        List<Category> list = productMapper.findCategory();
        if (list!=null){
            return list;
        }
        throw new MyException("没有种类！");
    }

    @Override
    public int insertAll(String name, String subtitle, BigDecimal price, int stock, String detail, int categoryId, String mainImage,String subImages) {

        int count = productMapper.insertAll(name,subtitle,price,stock,detail,categoryId,mainImage,subImages);
        if (count>0){
            return 1;
        }
        throw new MyException("添加商品失败！");
    }

    @Override
    public Product findById(int id) throws MyException {
        Product product = productMapper.findById(id);
        if (product!=null){
            return product;
        }
        throw new MyException("商品不存在！");
    }

    @Override
    public int update(int id, String name, String subtitle, BigDecimal price, int stock,
                      String detail, int categoryId, String mainImage, String subImages) throws MyException {

        int count = productMapper.update(id, name, subtitle, price, stock, detail, categoryId, mainImage,subImages);
        if (count>0){
            return 1;
        }
        throw new MyException("商品信息修改失败！");
    }

    @Override
    public String findNameById(int id) throws MyException {


        String name = productMapper.findNameById(id);
        if (name!=null){
            return name;
        }
        throw new MyException("名字不存在！");
    }

    //上下架
    @Override
    public int grounding(int id) throws MyException {
        int count = productMapper.grounding(id);
        if (count>0){
            return 1;
        }
        throw new MyException("无法下架商品！");
    }

    @Override
    public int undercarriage(int id) throws MyException {
        int count = productMapper.undercarriage(id);
        if (count>0){
            return 1;
        }
        throw new MyException("无法上架商品！");
    }

    @Override
    public List<Product> findA() throws MyException {


        List<Product> products = productMapper.findA();
        if (products!=null){
            return products;
        }
        throw new MyException("没有！");
    }


}
