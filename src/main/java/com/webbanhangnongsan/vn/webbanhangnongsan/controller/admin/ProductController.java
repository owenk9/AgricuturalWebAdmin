package com.webbanhangnongsan.vn.webbanhangnongsan.controller.admin;

import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Product;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin1")
public class ProductController {
    @Value("${upload.path}")
    private String pathUploadImage;
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
        try {

            File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
            System.out.println("-----------------" + convFile);
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {

        }

        product.setProductImage(file.getOriginalFilename());
        Product p = productRepository.save(product);
        if(p != null){
            model.addAttribute("message", "Update success");
            model.addAttribute("product", product);
        } else {
            model.addAttribute("message", "Update failure");
            model.addAttribute("product", product);
        }
        return "redirect:/admin1/tables";

    }


}
