    package com.webbanhangnongsan.vn.webbanhangnongsan.controller.admin;
    
    import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Category;
    import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Product;
    import com.webbanhangnongsan.vn.webbanhangnongsan.repository.CategoryRepository;
    import com.webbanhangnongsan.vn.webbanhangnongsan.repository.ProductRepository;
    import com.webbanhangnongsan.vn.webbanhangnongsan.service.ProductService;
    import com.webbanhangnongsan.vn.webbanhangnongsan.service.admin.ProductAdminService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.beans.propertyeditors.CustomDateEditor;
    import org.springframework.core.io.ClassPathResource;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.ui.ModelMap;
    import org.springframework.web.bind.WebDataBinder;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;
    
    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
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
        @Value("${upload.path}")
        private String pathUploadImage;
        @Autowired
        ProductRepository productRepository;
        @Autowired
        CategoryRepository categoryRepository;
        @Autowired
        ProductAdminService productAdminService;
        @Autowired
        private ProductService productService;
        @GetMapping("/tables")
        public String Product(Model model) {
            getData(model);
            paginatedProducts(model, 1);
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

            try {

                File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
                FileOutputStream fos = new FileOutputStream(convFile);
                fos.write(file.getBytes());
                fos.close();
            } catch (IOException e) {
                model.addAttribute("message", "File upload failed: " + e.getMessage());
                return "redirect:/admin1/tables";
            }

            product.setProductImage(file.getOriginalFilename());
            Product p = productRepository.save(product);
            if (null != p) {
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

        @PostMapping("/editProducts")
        public String editProduct(@ModelAttribute("editProduct") Product product, @RequestParam("file") MultipartFile file, ModelMap model) {
            try {
                if (!file.isEmpty()) {
                    // Save the new image file
                    File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
                    FileOutputStream fos = new FileOutputStream(convFile);
                    fos.write(file.getBytes());
                    fos.close();

                    // Set the new file name to the product
                    product.setProductImage(file.getOriginalFilename());
                } else {
                    // If no new file is uploaded, retain the old image
                    Product existingProduct = productRepository.findById(product.getProductId()).orElse(null);
                    if (existingProduct != null) {
                        product.setProductImage(existingProduct.getProductImage());
                        product.setEnteredDate(existingProduct.getEnteredDate());
                    }
                }

                // Save the updated product information
                Product updatedProduct = productRepository.save(product);
                if (updatedProduct != null) {
                    model.addAttribute("message", "Update success");
                    model.addAttribute("product", product);
                } else {
                    model.addAttribute("message", "Update failure");
                    model.addAttribute("product", product);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return "redirect:/admin1/tables";
        }
        // delete product
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
        // search products
        @GetMapping("/searchProducts")
        public String searchProducts(@RequestParam("search") String search,
                                     @RequestParam("page") int page,
                                     Model model){
            List<Product> productList = productService.listProductSearch(search, page);
            int numPages = productService.numPageSearch(search);
            System.out.println("numpage: " + numPages);
            model.addAttribute("productList", productList);
            model.addAttribute("page", page);
            model.addAttribute("search", search);
            model.addAttribute("numPages", numPages);
            return "admin/tables";
        }
        @PostMapping("/searchProducts")
        public String handleSearch(@RequestParam("search") String search, RedirectAttributes redirectAttributes) {
            redirectAttributes.addAttribute("search", search);
            redirectAttributes.addAttribute("page", 1); // Đặt trang mặc định là 1
            return "redirect:/searchProducts"; // Chuyển hướng đến phương thức GET searchProducts
        }
        @GetMapping("/paginationProducts")
        public String paginatedProducts(Model model, @RequestParam("currentPage") int currentPage) {
            List<Product> productList = productAdminService.paginatedProducts(currentPage);
            int totalPage = productAdminService.totalPage();
            model.addAttribute("paginatedProducts", productList);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", totalPage);
            return "admin/tables";
        }
    }
