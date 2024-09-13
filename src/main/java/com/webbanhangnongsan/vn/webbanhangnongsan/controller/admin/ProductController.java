package com.webbanhangnongsan.vn.webbanhangnongsan.controller.admin;

import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Category;
import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Product;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.CategoryRepository;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin1")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @GetMapping("/tables")
    public String Product(Model model) {
        getData(model);
        return "admin/tables";
    }

    public String hello() {
        return "hello";
    }
    @GetMapping("/search")
    public String findByProductName(@RequestParam(value = "search", required = false) String search, Model model) {

        List<Product> showProducts;
        if (search != null && !search.isEmpty()) {
            showProducts = productRepository.findByProductNameContaining(search);
            System.out.println(showProducts.stream().toList());
        } else {
            showProducts = productRepository.findAll();
            System.out.println(showProducts.stream().toList());
        }
        model.addAttribute("showProducts", showProducts);
        return "admin/tables";
    }

    public void getData(Model model){
        List<Product> showProducts = productRepository.findAll();
        model.addAttribute("showProducts", showProducts);
    }

    @GetMapping("/products")
    public String products(Model model) {
        Product product = new Product();
        model.addAttribute("adminProduct", product);

        return "admin/forms/add_new_products";
    }

    @ModelAttribute("categoryList")
    public List<Category> showCategory(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);

        return categoryList;
    }

    @PostMapping("/addProducts")
    public String addNewProducts(@ModelAttribute("adminProduct") Product product, ModelMap model, @RequestParam("file") MultipartFile file){
        product.setProductImage(file.getOriginalFilename());
        Product p = productRepository.save(product);

        if(p != null){
            try {
                File saveFile = new ClassPathResource("static/productImages").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e){
                e.printStackTrace();
            }
            model.addAttribute("message", "Update success");
            model.addAttribute("product", product);
        } else {
            model.addAttribute("message", "Update failure");
            model.addAttribute("product", product);
        }

        return "redirect:/admin1/tables";

    }

//     Hiển thị form chỉnh sửa sản phẩm
    @GetMapping("/editProducts/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/admin1/tables";
        }
        model.addAttribute("editProduct", product);
        return "admin/forms/edit_products";
    }

//    @PostMapping("/editProducts")
//    public String editProduct(@ModelAttribute("editProduct") Product product, @RequestParam("file") MultipartFile file) {
//        if (!file.isEmpty()) {
//            product.setProductImage(file.getOriginalFilename());
//            try {
//                File saveFile = new ClassPathResource("static/productImages").getFile();
//                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
//                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        productRepository.save(product);
//        return "redirect:/admin1/tables";
//    }

    @PostMapping("/editProducts")
    public String editProduct(@ModelAttribute("editProduct") Product product, @RequestParam("file") MultipartFile file) {
        // Kiểm tra nếu người dùng có upload file mới không
        if (!file.isEmpty()) {
            // Nếu có, cập nhật file mới
            product.setProductImage(file.getOriginalFilename());
            try {
                File saveFile = new ClassPathResource("static/productImages").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Nếu không upload file mới, giữ lại tên file cũ
            Product existingProduct = productRepository.findById(product.getProductId()).orElse(null);
            if (existingProduct != null) {
                product.setProductImage(existingProduct.getProductImage());
                product.setEnteredDate(existingProduct.getEnteredDate());
            }
        }

        productRepository.save(product);
        return "redirect:/admin1/tables";
    }



    // delete category
    @GetMapping("/deleteProducts/{id}")
    public String delProduct(@PathVariable("id") Long id, Model model) {
        productRepository.deleteById(id);
        model.addAttribute("message", "Xóa sản phẩm thành công!");
        return "redirect:/admin1/tables";
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }



}
