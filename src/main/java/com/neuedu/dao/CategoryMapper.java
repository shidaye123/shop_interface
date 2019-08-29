package com.neuedu.dao;

import com.neuedu.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {

    //获取平级子类类别
    public List<Category> findSameLevelSubcategory(int id);
    //增加类别
    public int addCategory(@Param("parentId") int parentId,
                           @Param("name") String name);
    //修改类别名字
    public int updateCategoryName(@Param("id") int id,
                                  @Param("name") String name);
    //获取当前分类ID和子类分类ID
    public int findCurrentCategoryId(int id);
    public List<Integer> findSubCategoryId(int id);

    //======
    public List<Category> findAll();
    public List<Category> findAllParents();
    //======

    public int deleteById(int id);

    public Category findAllById(int id);

    public int update(@Param("id") int id, @Param("parentId") int parentId, @Param("name") String name, @Param("status") int status);

    public int insert(Category category);

    public List<String> findParents();
}