package com.neuedu.service.impl;

import com.neuedu.dao.CategoryMapper;
import com.neuedu.exception.MyException;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    //获取平级子类类别
    @Override
    public List<Category> findSameLevelSubcategory(int id) {

        List<Category> categoryList = categoryMapper.findSameLevelSubcategory(id);
        return categoryList;
    }

    //添加商品
    @Override
    public int addCategory(int parentId, String name) {

        int count = categoryMapper.addCategory(parentId,name);
        return count;
    }

    @Override
    public int updateCategoryName(int id, String name) {

        int count = categoryMapper.updateCategoryName(id, name);
        return count;
    }

    @Override
    public int findCurrentCategoryId(int id) {

        int currentId = categoryMapper.findCurrentCategoryId(id);
        return currentId;
    }

    List<Integer> allList = new ArrayList<>();
    @Override
    public List<Integer> findSubCategoryId(int id) {

        List<Integer> lists = categoryMapper.findSubCategoryId(id);
        if (lists!=null){
            for (Integer list:lists){
                allList.add(list);
                findSubCategoryId(list);
            }
        }
        return allList;
    }

    //============
    @Override
    public List<Category> findAll() {
        List<Category> categories = categoryMapper.findAll();
        if (categories==null){
            throw new MyException("商品信息为空！");
        }
        return categories;
    }

    @Override
    public List<Category> findAllParents() throws MyException {
        List<Category> categories = categoryMapper.findAllParents();
        if (categories!=null){
            return categories;
        }
        throw new MyException("没有父类！");
    }
    //==============

    @Override
    public int deleteById(int id) {
        int count = categoryMapper.deleteById(id);
        if (count>0){
            return 1;
        }
        throw new MyException("删除失败！");
    }

    @Override
    public Category findAllById(int id) throws MyException {

        Category category = categoryMapper.findAllById(id);
        return category;
    }

    @Override
    public int update(int id,int parentId, String name, int status) {
        int count = categoryMapper.update(id,parentId, name, status);
        if (count>0){
            return 1;
        }
        throw new MyException("修改失败！");
    }

    @Override
    public int insert(Category category) throws MyException {
        int count = categoryMapper.insert(category);
        if (count>0){
            return 1;
        }
        throw new MyException("插入失败！");
    }

    @Override
    public List<String> findParents() throws MyException {

        List<String> list = categoryMapper.findParents();
        if (list!=null){
            return list;
        }
        throw new MyException("没有类别！");
    }
}
