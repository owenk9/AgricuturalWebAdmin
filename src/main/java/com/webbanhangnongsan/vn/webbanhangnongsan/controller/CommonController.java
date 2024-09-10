package com.webbanhangnongsan.vn.webbanhangnongsan.controller;

import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Category;
import com.webbanhangnongsan.vn.webbanhangnongsan.entity.User;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.CategoryRepository;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.ProductRepository;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;

@Controller
public class CommonController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @ModelAttribute(value = "user")
    public User user(Model model, Principal principal, User user) {
        if (principal != null) {
            model.addAttribute("user", new User());
            user = userRepository.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }
        return user;
    }

    @ModelAttribute("categoryList")
    public List<Category> showCategory(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);
        return categoryList;
    }

    @ModelAttribute
    public void addAuthAttributes(Model model, User user) {
        if (user != null && user.getName() != null) {
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("username", user.getName());
        } else {
            model.addAttribute("isLoggedIn", false);
        }
    }
}
