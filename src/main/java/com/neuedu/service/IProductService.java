package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.exception.MyException;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {

    //商品分页搜索
    public ServerResponse search(String name,Integer id,Integer pageNum,Integer pageSize);
    //产品搜索及动态排序List
    public ServerResponse searchByOrder(String keyword,Integer categoryId,Integer pageNum,Integer pageSize,String orderBy);
    //商品详情
    public List<Product> findProductDetail(int id);
    //查看商品是否下架
    public int isGrounding(int id);
    //查找商品主类别
    public List<Category> findTopcategory(int parentId);


    public List<Product> findByPage(int page, int size) throws MyException;
    public int findPagecounts() throws MyException;

    public int deleteById(int id) throws MyException;

    public List<Category> findCategory() throws MyException;

    public int insertAll(String name, String subtitle, BigDecimal price, int stock,
                         String detail, int categoryId, String mainImage, String subImages);

    public Product findById(int id) throws MyException;

    public int update(int id, String name, String subtitle, BigDecimal price, int stock,
                      String detail, int categoryId, String mainImage, String subImages) throws MyException;


    public String findNameById(int id) throws MyException;

    //上下架
    public int grounding(int id) throws MyException;
    public int undercarriage(int id) throws MyException;



    //测试
    public List<Product> findA() throws MyException;
}
