package com.neuedu.service;

import com.neuedu.exception.MyException;
import com.neuedu.pojo.Category;

import java.util.List;

public interface ICategoryService {

    //获取平级子类类别
    public List<Category> findSameLevelSubcategory(int id);
    //添加类别
    public int addCategory(int parentId,String name);
    //修改类别名字
    public int updateCategoryName(int id,String name);
    //获取当前分类ID和子类分类ID
    public int findCurrentCategoryId(int id);
    public List<Integer> findSubCategoryId(int id);

    //===========
    public List<Category> findAll() throws MyException;
    public List<Category> findAllParents() throws MyException;
    //===========

    public int deleteById(int id) throws MyException;

    public Category findAllById(int id) throws MyException;

    public int update(int id, int parentId, String name, int status) throws MyException;

    public int insert(Category category) throws MyException;

    public List<String> findParents() throws MyException;

}
