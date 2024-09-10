package com.webbanhangnongsan.vn.webbanhangnongsan.controller;

import com.webbanhangnongsan.vn.webbanhangnongsan.common.CommonDataService;
import com.webbanhangnongsan.vn.webbanhangnongsan.entity.CartItem;
import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Product;
import com.webbanhangnongsan.vn.webbanhangnongsan.entity.User;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.ProductRepository;
import com.webbanhangnongsan.vn.webbanhangnongsan.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
public class CartController {
    @Autowired
    HttpSession session;

    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CommonDataService commomDataService;

    @PostMapping("/addToCartFromDetail")
    public String addToCart(@ModelAttribute("cartItem") CartItem cartItem, HttpServletRequest request, Model model, User user) {
        // Xử lý thêm vào giỏ hàng
        System.out.println("Cart Item Details:");
        System.out.println("ID: " + cartItem.getId());
        System.out.println("Name: " + cartItem.getName());
        System.out.println("Unit Price: " + cartItem.getUnitPrice());
        System.out.println("Quantity: " + cartItem.getQuantity());
        System.out.println("Total Price: " + cartItem.getTotalPrice());

        session = request.getSession();
        Collection<CartItem> cartItems = shoppingCartService.getCartItems();
        if (cartItem != null) {
            shoppingCartService.add(cartItem);
        }
        session.setAttribute("cartItems", cartItems);

        // Lấy lại sản phẩm hiện tại
        Product product = productRepository.findById(cartItem.getId()).orElse(null);
        model.addAttribute("product", product);

        // Cập nhật dữ liệu chung
        commomDataService.commonData(model, user);

        // Trả về cùng một trang
        return "redirect:/productDetail?id=" + cartItem.getId();
    }

    // print cart list to the console
    @GetMapping("/checkCartList")
    public String printCartList(HttpServletRequest request, Model model, User user) {
        // Lấy session hiện tại
        session = request.getSession();

        // Lấy danh sách các mục trong giỏ hàng từ session
        //Collection<CartItem> cartItems = (Collection<CartItem>) session.getAttribute("cartItems");
        Collection<CartItem> cartItems = shoppingCartService.getCartItems();
        if (cartItems != null && !cartItems.isEmpty()) {
            // In ra thông tin giỏ hàng
            System.out.println("Cart Items:");
            for (CartItem item : cartItems) {
                System.out.println("ID: " + item.getId());
                System.out.println("Name: " + item.getName());
                System.out.println("Unit Price: " + item.getUnitPrice());
                System.out.println("Quantity: " + item.getQuantity());
                System.out.println("Total Price: " + item.getTotalPrice());
                System.out.println("------------");
            }
        } else {
            System.out.println("Cart is empty.");
        }

        // Thêm danh sách này vào model để hiển thị trên giao diện
        model.addAttribute("cartItems", cartItems);

        // Cập nhật dữ liệu chung
        commomDataService.commonData(model, user);

        // Trả về trang hiển thị giỏ hàng
        return "redirect:/products";
    }

    @GetMapping(value = "/addToCart")
    public String add(@RequestParam("productId") Long productId, HttpServletRequest request, Model model) {

        Product product = productRepository.findById(productId).orElse(null);

        session = request.getSession();
        Collection<CartItem> cartItems = shoppingCartService.getCartItems();
        if (product != null) {
            CartItem cartItem = CartItem.builder()
                    .id(product.getProductId())  // Đảm bảo rằng `product.getProductId()` không phải là null
                    .name(product.getProductName())
                    .unitPrice(product.getPrice())
                    .product(product)
                    .quantity(1)
                    .totalPrice(product.getPrice())
                    .build();
            shoppingCartService.add(cartItem);
        }
        session.setAttribute("cartItems", cartItems);
        model.addAttribute("totalCartItems", shoppingCartService.getCount());

        return "redirect:/products";
    }
}
