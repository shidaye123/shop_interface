package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;

    //获取平级子类类别
    @RequestMapping(value = "/get_category.do",method = RequestMethod.GET)
    public ServerResponse getCategory(@RequestParam("categoryId") int id,
                                      HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user==null){
            return ServerResponse.createServerResponseFail(10,"用户未登录，请登录！");
        }
        List<Category> categoryList = categoryService.findSameLevelSubcategory(id);
        if (categoryList==null||categoryList.size()<=0){
            return ServerResponse.createServerResponseFail(1,"未找到该商品！");
        }else {
            return ServerResponse.createServerResponseSuccess(null,categoryList);
        }

    }
    //增加类别
    @RequestMapping(value = "/add_category.do",method = RequestMethod.GET)
    public ServerResponse addCategory(@RequestParam(value = "parentId",required = false,defaultValue = "0") int parentId,
                                      @RequestParam("categoryName") String name){

        if (name==null||name.equals("")){
            return ServerResponse.createServerResponseFail(2,"商品名字不能为空！");
        }
        int count = categoryService.addCategory(parentId, name);
        if (count>0){
            return ServerResponse.createServerResponseSuccess("添加品类成功！");
        }else {
            return ServerResponse.createServerResponseFail(1,"添加品类失败！");
        }

    }

    //修改品类名字
    @RequestMapping(value = "/set_category_name.do",method = RequestMethod.GET)
    public ServerResponse uodateCategoryName(@RequestParam("categoryId") int id,
                                             @RequestParam("categoryName") String name){

        if (name==null||name.equals("")){
            return ServerResponse.createServerResponseFail(2,"类别名字不能为空！");
        }
        int count = categoryService.updateCategoryName(id, name);
        if (count>0){
            return ServerResponse.createServerResponseSuccess("更改品类名字成功！");
        }else {
            return ServerResponse.createServerResponseFail(1,"更改品类名字失败！");
        }

    }

    //获取当前分类ID和子类分类ID
    @RequestMapping(value = "/get_deep_category.do",method = RequestMethod.GET)
    public ServerResponse findAllCategoryId(@RequestParam("categoryId") int id,
                                            HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user.getRole()!=0){
            return ServerResponse.createServerResponseFail(1,"无权限！");
        }
        int currentId = categoryService.findCurrentCategoryId(id);
        List<Integer> list = categoryService.findSubCategoryId(id);
        list.add(currentId);
        if (list!=null){
            return ServerResponse.createServerResponseSuccess(null,list);
        }else {
            return ServerResponse.createServerResponseFail(2,"不存在！");
        }

    }


    //所有类别显示
    @RequestMapping("/info")
    public String category(HttpSession session){
        List<Category> categoryList = categoryService.findAll();
        session.setAttribute("categoryList",categoryList);
        List<Category> parentCategory = categoryService.findAllParents();
        session.setAttribute("parentCategory",parentCategory);
        return "/category/list";
    }


    @RequestMapping("/delete")
    public String delete(@RequestParam("id") int id, HttpSession session){

        int count = categoryService.deleteById(id);
        if (count==1){
            List<Category> categoryList = categoryService.findAll();
            session.setAttribute("categoryList",categoryList);
            return "/category/list";
        }
        return "/category/list";
    }

    @RequestMapping("/update")
    public String update(@RequestParam("id") int id, HttpServletRequest request){

        Category category = categoryService.findAllById(id);
        request.setAttribute("category",category);
        List<Category> parentCategory = categoryService.findAllParents();
        request.setAttribute("parentCategory",parentCategory);
        return "/category/index";

    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String update1(@RequestParam(value = "id") int id,
                          @RequestParam(value = "parentId") int parentId,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "status") int status,
                          HttpSession session){

        int count = categoryService.update(id,parentId, name, status);
        if (count==1){
            List<Category> categoryList = categoryService.findAll();
            session.setAttribute("categoryList",categoryList);
            return "/category/list";
        }
        return "/category/list";

    }

    @RequestMapping("/insert")
    public String insert(HttpServletRequest request){
        List<Category> parentCategory = categoryService.findAllParents();
        request.setAttribute("parentCategory",parentCategory);
        request.setAttribute("new",1);
        return "/category/index";
    }

    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public String insert1(Category category, HttpSession session){

        int count = categoryService.insert(category);
        if (count==1){
            List<Category> categoryList = categoryService.findAll();
            session.setAttribute("categoryList",categoryList);
            return "/category/list";
        }
        return "/category/index";
    }


    @RequestMapping("/parents")
    public String showParents(HttpSession session){

        List<String> list = categoryService.findParents();
        session.setAttribute("list",list);
        return "selectmenu";

    }

}
