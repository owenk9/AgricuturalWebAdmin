package com.webbanhangnongsan.vn.webbanhangnongsan.controller.admin;

import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Product;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.ProductRepository;
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
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @GetMapping("/tables")
    public String Product(Model model) {
        getData(model);
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

    @PostMapping("/addProducts")
    public String addNewProducts(@ModelAttribute("adminProduct") Product product, ModelMap model, @RequestParam("file") MultipartFile file){
//        try {
//
//            File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
//            FileOutputStream fos = new FileOutputStream(convFile);
//            fos.write(file.getBytes());
//            fos.close();
//        } catch (IOException e) {
//
//        }
//
        System.out.println(file.getOriginalFilename());

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

    @PutMapping("/editProducts/{id}")
    public String editProducts(@PathVariable("id") Long id, Model model){
        Product p = productRepository.findById(id).orElse(null);

        return "";
    }


    // delete category
    @GetMapping("/deleteProducts/{id}")
    public String delProduct(@PathVariable("id") Long id, Model model) {
        productRepository.deleteById(id);
        model.addAttribute("message", "Xóa sản phẩm thành công!");
        return "redirect:/admin1/tables";
    }



}
