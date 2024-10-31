package nhuquynh.demothymeleaf.controller;

import jakarta.validation.Valid;
import nhuquynh.demothymeleaf.entity.Category;
import nhuquynh.demothymeleaf.service.CategoryModel;
import nhuquynh.demothymeleaf.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("admin/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

//    @RequestMapping("")
//    public String all(Model model) {
//        List<Category> list = categoryService.findAll();
//        model.addAttribute("listcate", list);
//        return "list";
//    }

    @GetMapping("")
    public String all(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("categoryName")); // Thay đổi nếu cần
        Page<Category> categoryPage = categoryService.findAll(pageable); // Lấy đối tượng Page<Category>

        model.addAttribute("categoryPage", categoryPage); // Truyền categoryPage vào model

        int totalPages = categoryPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "list";
    }


    @GetMapping("/insert")
    public String add(Model model) {
        CategoryModel cate = new CategoryModel();
        model.addAttribute("cate", cate);
        return "AddOrEdit";
    }

    @PostMapping("/save")
    public ModelAndView handleModifyForm(ModelMap model,
                                         @Valid @ModelAttribute("cate") CategoryModel categoryModel,
                                         BindingResult bindingResult,
                                         @RequestParam("image") MultipartFile file) {
        Category entity = new Category();
        if (bindingResult.hasErrors()) {
            return new ModelAndView("AddOrEdit");
        }

        String image = this.handleSaveUploadFile(file, "D:\\upload");
        categoryModel.setImages(image);

        BeanUtils.copyProperties(categoryModel, entity);

        categoryService.save(entity);

        String message = "";

        if (categoryModel.getIsEdit()) {
            message="Category is edited successfully";
        } else {
            message=" Category is saved successfully";
        }

        model.addAttribute("message", message);
        return new ModelAndView("redirect:/admin/category", model);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditCategoryPage(ModelMap model, @PathVariable("id") Long categoryID) {
        Optional<Category> optionalCategory = categoryService.findById(categoryID);
        CategoryModel category = new CategoryModel();
        if (optionalCategory.isPresent()) {
            Category categoryEntity = optionalCategory.get();
            BeanUtils.copyProperties(categoryEntity, category);
            category.setIsEdit(true);
            model.addAttribute("cate", category);
            return new ModelAndView("AddOrEdit",model);
        }
        model.addAttribute("message", "Category is not existed");
        return new ModelAndView("forward:/admin/category", model);
    }

    @GetMapping("/delete/{id}")
    public ModelAndView handleDeleteForm(ModelMap model, @PathVariable("id") Long categoryID) {
        categoryService.deleteById(categoryID);
        model.addAttribute("message", "Category deleted successfully");
        return new ModelAndView("redirect:/admin/category", model);
    }
    public String handleSaveUploadFile(MultipartFile file, String uploadDir) {
        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
                Path path = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping("/searchpaginated")
    public String search(ModelMap model,
                         @RequestParam(name="name", required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("categoryName")); // Sửa Sort theo tên thực tế

        Page<Category> resultPage;
        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByCategoryNameContaining(name, pageable);
            model.addAttribute("name", name);
        } else {
            resultPage = categoryService.findAll(pageable);
        }

        model.addAttribute("categoryPage", resultPage); // Truyền categoryPage vào model

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "list";
    }



//    @PostMapping("/save")
//    public ModelAndView saveOrUpdate(ModelMap model,
//                                     @Valid @ModelAttribute("cate") CategoryModel categoryModel,
//                                     BindingResult result) {
//        if (result.hasErrors()) {
//            // Nếu có lỗi validation nghĩa là khng binding được, quay lại trang thêm hoặc cập nhật
//            System.out.println("Errors: " + result.getAllErrors());
//            return new ModelAndView("AddOrEdit");
//        }
//        Category entity = new Category();
//        //copy từ model sang entity
//        BeanUtils.copyProperties(categoryModel, entity);
//        //gọi hàm save từ service lên để thực hiện
//        System.out.println(categoryModel);
//        categoryService.save(entity);
//        String mess=" ";
//        if (categoryModel.getIsEdit()==true){
//            mess=" Edit Category";
//        }else{
//            mess=" Add Category";
//        }
//        // Sau khi lưu hoặc cập nhật thành công, chuyển hướng đến danh sách categories
//        model.addAttribute("message", mess);
//        return new ModelAndView("forward:/admin/category", model);
//    }
//
//    @GetMapping("/edit/{id}")
//    public ModelAndView edit(ModelMap model, @PathVariable ("id") Long categoryID) {
//        Optional<Category> optionalCategory = categoryService.findById(categoryID);
//        CategoryModel cate = new CategoryModel();
//        //kiểm tra sự tồn tại của category
//        if (optionalCategory.isPresent()) {
//            Category entity = optionalCategory.get();
//            //copy từ entity sang model
//            BeanUtils.copyProperties(entity, cate);
//            cate.setIsEdit(true);
//            //đẩy dữ liệu lên view
//            model.addAttribute("cate", cate);
//            //dùng chung với save
//            return new ModelAndView("AddOrEdit", model);
//        }
//        model.addAttribute("mess", "Category not found");
//        return new ModelAndView("forward:list", model);
//    }
//
//    @GetMapping("/delete/{id}")
//    public ModelAndView delete(ModelMap model, @PathVariable ("id") Long categoryID) {
//        categoryService.deleteById(categoryID);
//        model.addAttribute("message", "Delete Category");
//        return new ModelAndView("redirect:/admin/category", model);
//    }

//    @GetMapping("/search")
//    public String search(ModelMap model, @PathVariable("id") Long categoryID) {
//        List<Category> entity=null;
//        if(StringUtils.hasText(name)){
//            list=categoryService.findByNameContaining(name);
//        }else {
//            list=categoryService.findAll();
//        }
//        model.addAttribute("listcate", list);
//        return "admin/category/searchpaging";
//    }

}
