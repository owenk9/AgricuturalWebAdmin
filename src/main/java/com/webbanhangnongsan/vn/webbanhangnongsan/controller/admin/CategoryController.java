package com.webbanhangnongsan.vn.webbanhangnongsan.controller.admin;


import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Category;
import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Product;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin1")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/category")
    public String Category(Model model) {
        getData(model);
        return "admin/category";
    }
    public void getData(Model model){
        List<Category> showCategory = categoryRepository.findAll();
        model.addAttribute("showCategory", showCategory);
    }

    @GetMapping("/categories")
    public String category(Model model) {
        Category category = new Category();
        model.addAttribute("adminCategory", category);

        return "admin/forms/add_new_category";
    }
    @PostMapping("/addCategory")
    public String addNewCategory(@ModelAttribute("adminCategory") Category category, ModelMap model, @RequestParam("file") MultipartFile file){
        category.setCategoryImage(file.getOriginalFilename());
        Category c = categoryRepository.save(category);

        if(c != null){
            try {
                File saveFile = new ClassPathResource("src/main/resources/static/categoryImages").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e){
                e.printStackTrace();
            }
            model.addAttribute("message", "Update success");
            model.addAttribute("category", category);
        } else {
            model.addAttribute("message", "Update failure");
            model.addAttribute("category", category);
        }

        return "redirect:/admin1/category";

    }

    @GetMapping("/editCategory/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return "redirect:/admin1/category";
        }
        model.addAttribute("editCategory", category);
        return "admin/forms/edit_category";
    }


    @PostMapping("/editCategory")
    public String editProduct(@ModelAttribute("editCategory") Category category, @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            category.setCategoryImage(file.getOriginalFilename());
            try {
                File saveFile = new ClassPathResource("static/categoryImages").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                Path staticPath = Paths.get("src/main/resources/static/productImages" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), staticPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Nếu không upload file mới, giữ lại tên file cũ
            Category existingCategory = categoryRepository.findById(category.getCategoryId()).orElse(null);
            if (existingCategory != null) {
                category.setCategoryImage(existingCategory.getCategoryImage());
            }
        }

        categoryRepository.save(category);
        return "redirect:/admin1/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String delCategory(@PathVariable("id") Long id, Model model) {
        categoryRepository.deleteById(id);
        model.addAttribute("message", "Xóa loại sản phẩm thành công!");
        return "redirect:/admin1/category";
    }
}
