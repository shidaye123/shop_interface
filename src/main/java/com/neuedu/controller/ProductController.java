package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.constant.Constants;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.impl.CategoryServiceImpl;
import com.neuedu.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/product")
@CrossOrigin
public class ProductController {

    @Autowired
    ProductServiceImpl productService;
    @Autowired
    CategoryServiceImpl categoryService;

    //商品分页搜索
    @RequestMapping(value = "/search.do",method = RequestMethod.GET)
    public ServerResponse pageSearch(@RequestParam(name = "productName",required = false)String name,
                                     @RequestParam(name = "productId",required = false)Integer id,
                                     @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                     @RequestParam(name = "pageSize",required = false,defaultValue = "5")Integer pageSize,
                                     HttpSession session){

//        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
//        if (user==null){
//            return ServerResponse.createServerResponseFail(10,"用户未登录,请登录！");
//        }
//        if (user.getRole()!=0){
//            return ServerResponse.createServerResponseFail(1,"没有权限！");
//        }

        return productService.search(name, id, pageNum, pageSize);
    }

    //产品搜索及动态排序LIST
    @RequestMapping(value = "/list.do",method = RequestMethod.GET)
    public ServerResponse searchList(@RequestParam(name = "categoryId",required = false)Integer categoryId,
                                     @RequestParam(name = "keyword",required = false)String keyword,
                                     @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                     @RequestParam(name = "pageSize",required = false,defaultValue = "5")Integer pageSize,
                                     @RequestParam(name = "orderBy",required = false,defaultValue = "")String orderBy,
                                     HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(10,"用户未登录,请登录！");
        }
        if (user.getRole()!=0){
            return ServerResponse.createServerResponseFail(1,"没有权限！");
        }

        return productService.searchByOrder(keyword, categoryId, pageNum, pageSize, orderBy);
    }

    //产品detail
    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    public ServerResponse productDetail(@RequestParam("productId")int id,
                                        HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(10,"用户未登录,请登录！");
        }
        if (user.getRole()!=0){
            return ServerResponse.createServerResponseFail(2,"没有权限！");
        }
        if (productService.isGrounding(id)!=1){
            return ServerResponse.createServerResponseFail(4,"商品已下架或删除！");
        }
        List<Product> productList = productService.findProductDetail(id);
        if (productList!=null){
            return ServerResponse.createServerResponseSuccess(null,productList);
        }else {
            return ServerResponse.createServerResponseFail(1,"参数错误！");
        }

    }

    //获取产品主分类
    @RequestMapping(value = "/topcategory.do",method = RequestMethod.GET)
    public ServerResponse getTopcategory(@RequestParam("sid")int parentId,
                                         HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            return ServerResponse.createServerResponseFail(10,"用户未登录,请登录！");
        }
        if (user.getRole()!=0){
            return ServerResponse.createServerResponseFail(2,"没有权限！");
        }
        List<Category> categoryList = productService.findTopcategory(parentId);
        if (categoryList!=null){
            return ServerResponse.createServerResponseSuccess(null,categoryList);
        }else {
            return ServerResponse.createServerResponseFail(1,"没有类别！");
        }

    }

    //日志
    @RequestMapping(value = "/logempty.do",method = RequestMethod.GET)
    public ServerResponse logEmpty(){

        return ServerResponse.createServerResponseSuccess("调用成功！");

    }









    @RequestMapping("/info")
    public String showAll(HttpSession session, HttpServletRequest request,
                          @RequestParam("page") int page,
                          @RequestParam("size") int size){
        //当前页
        request.setAttribute("currentPage",page);

        page = (page-1)*size;

        //查询所有页数
        List<Integer> pageCounts = new ArrayList<>();
        int count = productService.findPagecounts();
        int totalPages = productService.findPagecounts()/size;

        if (count%size==0){
            for (int i=1;i<=count/size;i++){
                pageCounts.add(i);
            }
        }else {
            totalPages += 1;
            for (int i=1;i<=count/size+1;i++){
                pageCounts.add(i);
            }
        }
        //总页数
        request.setAttribute("totalPages",totalPages);
        request.setAttribute("pageCounts",pageCounts);
        //每页显示的条数
        request.setAttribute("size",size);
        List<Product> productList = productService.findByPage(page, size);

        //子图
        for (Product product:productList){
            if (product.getSubImages()!=null){
                String[] sub = product.getSubImages().split("#");
                for (String s:sub){
                    product.getSubimage().add(s);
                }
            }
        }

        session.setAttribute("productList",productList);
        List<Category> parentCategory = categoryService.findAllParents();
        request.setAttribute("parentCategory",parentCategory);
        return "/product/list";
    }


    @RequestMapping("/delete")
    public String deleteById(@RequestParam("id") int id,
                             @RequestParam("page") int page,
                             @RequestParam("size") int size,
                             HttpSession session,
                             HttpServletRequest request){

        request.setAttribute("currentPage",page);

        page = (page-1)*size;

        int counts = productService.deleteById(id);
        if (counts == 1){
            List<Integer> pageCounts = new ArrayList<>();
            int count = productService.findPagecounts();
            int totalPages = productService.findPagecounts()/size;

            if (count%size==0){
                for (int i=1;i<=count/size;i++){
                    pageCounts.add(i);
                }
            }else {
                totalPages += 1;
                for (int i=1;i<=count/size+1;i++){
                    pageCounts.add(i);
                }
            }
            //总页数
            request.setAttribute("totalPages",totalPages);
            request.setAttribute("pageCounts",pageCounts);
            //每页显示的条数
            request.setAttribute("size",size);
            List<Product> productList = productService.findByPage(page, size);

            //子图
            for (Product product:productList){
                if (product.getSubImages()!=null){
                    String[] sub = product.getSubImages().split("#");
                    for (String s:sub){
                        product.getSubimage().add(s);
                    }
                }
            }

            session.setAttribute("productList",productList);
            List<Category> parentCategory = categoryService.findAllParents();
            request.setAttribute("parentCategory",parentCategory);
            return "/product/list";
        }
        return "/product/list";
    }

    @RequestMapping("/insert")
    public String insertProduct(HttpServletRequest request){

        List<Category> categoryList = productService.findCategory();
        request.setAttribute("categoryList",categoryList);
        return "/product/index";

    }

    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public String insertProduct(@RequestParam("name") String name,
                                 @RequestParam("subtitle") String subtitle,
                                 @RequestParam("price") BigDecimal price,
                                 @RequestParam("stock") int stock,
                                 @RequestParam("detail") String detail,
                                 @RequestParam("categoryId") int categoryId,
                                 @RequestParam("pic") MultipartFile picfile,
                                 @RequestParam("subpic") MultipartFile[] subpicfile,
                                 HttpSession session,
                                 HttpServletRequest request){

        File file = null;
        String newFileName = null;
        if (picfile!=null){
            String oldFileName = picfile.getOriginalFilename();
            String extendName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString();
            newFileName = uuid + extendName;
            file = new File("D:\\picture");
            if (!file.exists()){
                file.mkdir();
            }
            File newfile = new File(file,newFileName);
            try {
                picfile.transferTo(newfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] newSubFileName = new String[subpicfile.length];
        if (subpicfile!=null){
            for(int i=0;i<subpicfile.length;i++){

                String oldFileName = subpicfile[i].getOriginalFilename();
                String extendName = oldFileName.substring(oldFileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString();
                newSubFileName[i] = uuid + extendName;
                if (!file.exists()){
                    file.mkdir();
                }
                File newFile = new File(file,newSubFileName[i]);
                try {
                    subpicfile[i].transferTo(newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String subImages = "";
        if (newSubFileName!=null&&newSubFileName.length>0){
            for (String i:newSubFileName){
                subImages += i + "#" ;
            }
        }

        int page = 0,size = 5;
        request.setAttribute("currentPage",page+1);
        int counts = productService.insertAll(name,subtitle,price,stock,detail,categoryId,newFileName,subImages);
        if (counts==1){
            List<Integer> pageCounts = new ArrayList<>();
            int count = productService.findPagecounts();
            int totalPages = productService.findPagecounts()/size;

            if (count%size==0){
                for (int i=1;i<=count/size;i++){
                    pageCounts.add(i);
                }
            }else {
                totalPages += 1;
                for (int i=1;i<=count/size+1;i++){
                    pageCounts.add(i);
                }
            }
            //总页数
            request.setAttribute("totalPages",totalPages);
            request.setAttribute("pageCounts",pageCounts);
            //每页显示的条数
            request.setAttribute("size",size);
            List<Product> productList = productService.findByPage(page, size);
            session.setAttribute("productList",productList);

            //子图
            for (Product product:productList){
                if (product.getSubImages()!=null){
                    String[] sub = product.getSubImages().split("#");
                    for (String s:sub){
                        product.getSubimage().add(s);
                    }
                }
            }

            List<Category> parentCategory = categoryService.findAllParents();
            request.setAttribute("parentCategory",parentCategory);
            return "/product/list";
        }

        return "/product/index";
    }


    @RequestMapping("/update")
    public String updateProduct(@RequestParam("id") int id,
                                @RequestParam("page") int page,
                                @RequestParam("size") int size,
                                HttpServletRequest request){
        request.setAttribute("currentPage",page);
        request.setAttribute("size",size);
        List<Category> categoryList = productService.findCategory();
        request.setAttribute("categoryList",categoryList);
        Product product = productService.findById(id);
        request.setAttribute("productInfo",product);
        return "/product/index";
    }
    //下拉框选中
//更新图片，要删除原有图片（主图，子图）
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String updateProduct(@RequestParam("id") int id,
                                @RequestParam("name") String name,
                                @RequestParam("subtitle") String subtitle,
                                @RequestParam("price") BigDecimal price,
                                @RequestParam("stock") int stock,
                                @RequestParam("detail") String detail,
                                @RequestParam("categoryId") int categoryId,
                                @RequestParam("pic") MultipartFile picfile,
                                @RequestParam("subpic") MultipartFile[] subpicfile,
                                @RequestParam("currentPage") int page,
                                @RequestParam("size") int size,
                                HttpSession session,
                                HttpServletRequest request){
        //删除原始图片
        String oldName = productService.findNameById(id);
        File oldFile = new File("D:/picture/"+oldName);
        if (oldFile.exists()&&oldFile.isFile()){
            oldFile.delete();
        }

        File file = null;
        String newFileName = null;
        if (picfile!=null){
            String oldFileName = picfile.getOriginalFilename();
            String extendName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString();
            newFileName = uuid + extendName;
            file = new File("D:\\picture");
            if (!file.exists()){
                file.mkdir();
            }
            File newfile = new File(file,newFileName);
            try {
                picfile.transferTo(newfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] newSubFileName = new String[subpicfile.length];
        if (subpicfile!=null){
            for(int i=0;i<subpicfile.length;i++){

                String oldFileName = subpicfile[i].getOriginalFilename();
                String extendName = oldFileName.substring(oldFileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString();
                newSubFileName[i] = uuid + extendName;
                if (!file.exists()){
                    file.mkdir();
                }
                File newFile = new File(file,newSubFileName[i]);
                try {
                    subpicfile[i].transferTo(newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String subImages = "";
        if (newSubFileName!=null&&newSubFileName.length>0){
            for (String i:newSubFileName){
                subImages += i + "#";
            }
        }

        //当前页
        request.setAttribute("currentPage",page);

        page = (page-1)*size;
        int counts = productService.update(id,name,subtitle,price,stock,detail,categoryId,newFileName,subImages);
        if (counts==1){
            //查询所有页数
            List<Integer> pageCounts = new ArrayList<>();
            int count = productService.findPagecounts();
            int totalPages = productService.findPagecounts()/size;

            if (count%size==0){
                for (int i=1;i<=count/size;i++){
                    pageCounts.add(i);
                }
            }else {
                totalPages += 1;
                for (int i=1;i<=count/size+1;i++){
                    pageCounts.add(i);
                }
            }
            //总页数
            request.setAttribute("totalPages",totalPages);
            request.setAttribute("pageCounts",pageCounts);
            //每页显示的条数
            request.setAttribute("size",size);
            List<Product> productList = productService.findByPage(page, size);
            session.setAttribute("productList",productList);

            //子图
            for (Product product:productList){
                if (product.getSubImages()!=null){
                    String[] sub = product.getSubImages().split("#");
                    for (String s:sub){
                        product.getSubimage().add(s);
                    }
                }
            }

            List<Category> parentCategory = categoryService.findAllParents();
            request.setAttribute("parentCategory",parentCategory);
            return "/product/list";
        }
        return "/product/index";
    }


    //上下架
    @RequestMapping("/grounding")
    public String grounding(@RequestParam("id") int id,
                            @RequestParam("page") int page,
                            @RequestParam("size") int size,
                            HttpSession session,
                            HttpServletRequest request){

        //当前页
        request.setAttribute("currentPage",page);

        page = (page-1)*size;
        int counts = productService.grounding(id);
        if (counts==1){
            //查询所有页数
            List<Integer> pageCounts = new ArrayList<>();
            int count = productService.findPagecounts();
            int totalPages = productService.findPagecounts()/size;

            if (count%size==0){
                for (int i=1;i<=count/size;i++){
                    pageCounts.add(i);
                }
            }else {
                totalPages += 1;
                for (int i=1;i<=count/size+1;i++){
                    pageCounts.add(i);
                }
            }
            //总页数
            request.setAttribute("totalPages",totalPages);
            request.setAttribute("pageCounts",pageCounts);
            //每页显示的条数
            request.setAttribute("size",size);
            List<Product> productList = productService.findByPage(page, size);
            session.setAttribute("productList",productList);
            //子图
            for (Product product:productList){
                if (product.getSubImages()!=null){
                    String[] sub = product.getSubImages().split("#");
                    for (String s:sub){
                        product.getSubimage().add(s);
                    }
                }
            }
            List<Category> parentCategory = categoryService.findAllParents();
            request.setAttribute("parentCategory",parentCategory);
            return "/product/list";
        }
        return "/product/list";
    }

    @RequestMapping("/undercarriage")
    public String undercarriage(@RequestParam("id") int id,
                                @RequestParam("page") int page,
                                @RequestParam("size") int size,
                                HttpSession session,
                                HttpServletRequest request){

        //当前页
        request.setAttribute("currentPage",page);

        page = (page-1)*size;
        int counts = productService.undercarriage(id);
        if (counts==1){
            //查询所有页数
            List<Integer> pageCounts = new ArrayList<>();
            int count = productService.findPagecounts();
            int totalPages = productService.findPagecounts()/size;

            if (count%size==0){
                for (int i=1;i<=count/size;i++){
                    pageCounts.add(i);
                }
            }else {
                totalPages += 1;
                for (int i=1;i<=count/size+1;i++){
                    pageCounts.add(i);
                }
            }
            //总页数
            request.setAttribute("totalPages",totalPages);
            request.setAttribute("pageCounts",pageCounts);
            //每页显示的条数
            request.setAttribute("size",size);
            List<Product> productList = productService.findByPage(page, size);
            session.setAttribute("productList",productList);

            //子图
            for (Product product:productList){
                if (product.getSubImages()!=null){
                    String[] sub = product.getSubImages().split("#");
                    for (String s:sub){
                        product.getSubimage().add(s);
                    }
                }
            }

            List<Category> parentCategory = categoryService.findAllParents();
            request.setAttribute("parentCategory",parentCategory);
            return "/product/list";
        }
        return "/product/list";

    }

}
