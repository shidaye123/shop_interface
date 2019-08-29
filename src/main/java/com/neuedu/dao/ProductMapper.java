package com.neuedu.dao;


import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductMapper {

    //产品分页搜索
    public List<Product> findProductByNameAndId(@Param("name")String name,@Param("id")Integer id);
    //产品搜索及动态排序List
    public List<Product> findProductByKeywordAndCategoryId(@Param("keyword")String keyword,
                                                           @Param("categoryId")Integer categoryId,
                                                           @Param("orderBy")String orderBy);
    //商品详情
    public List<Product> findProductDetail(int id);
    //查看商品是否下架
    public int isGrounding(int id);

    //查找商品主类别
    public List<Category> findTopcategory(int parentId);







    //显示所有商品
    public List<Product> findByPage(@Param("page") int page, @Param("size") int size);
    public int findPagecounts();

    //删除商品
    public int deleteById(int id);

    //父类类别
    public List<Category> findCategory();

    //添加商品
    public int insertAll(@Param("name") String name,
                         @Param("subtitle") String subtitle,
                         @Param("price") BigDecimal price,
                         @Param("stock") int stock,
                         @Param("detail") String detail,
                         @Param("categoryId") int categoryId,
                         @Param("mainImage") String mainImage,
                         @Param("subImages") String subImages);

    //修改商品
    public Product findById(int id);

    public int update(@Param("id") int id,
                      @Param("name") String name,
                      @Param("subtitle") String subtitle,
                      @Param("price") BigDecimal price,
                      @Param("stock") int stock,
                      @Param("detail") String detail,
                      @Param("categoryId") int categoryId,
                      @Param("mainImage") String mainImage,
                      @Param("subImages") String subImages);


    public String findNameById(int id);

    //上下架
    public int grounding(int id);
    public int undercarriage(int id);




    //测试
    public List<Product> findA();

}