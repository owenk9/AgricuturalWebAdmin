package com.webbanhangnongsan.vn.webbanhangnongsan.controller.admin;

import com.webbanhangnongsan.vn.webbanhangnongsan.entity.*;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.OrderDetailRepository;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.OrderRepository;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.ProductRepository;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.UserRepository;
import com.webbanhangnongsan.vn.webbanhangnongsan.service.SendMailService;
import com.webbanhangnongsan.vn.webbanhangnongsan.service.admin.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin1")
public class OrderController {
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    SendMailService sendMailService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @ModelAttribute(value = "user")
    public User user(Model model, Principal principal, User user) {

        if (principal != null) {
            model.addAttribute("user", new User());
            user = userRepository.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }

        return user;
    }

    // list order
    @GetMapping("/orders")
    public String orders(Model model){
        getData(model);
        return "admin/orders";
    }
    public void getData(Model model){
        List<Order> showOrder = orderRepository.findAll();
        model.addAttribute("showOrder", showOrder);
    }


    @GetMapping("/order/detail/{order_id}")
    public ModelAndView detail(ModelMap model, @PathVariable("order_id") Long id){
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(id);
        model.addAttribute("amount", orderRepository.findById(id).get().getAmount());
        model.addAttribute("orderDetail", orderDetailList);
        model.addAttribute("orderId", id);

        model.addAttribute("menu0", "menu");
        return new ModelAndView("admin/forms/edit_orders", model);
    }

    @RequestMapping("/order/cancel/{order_id}")
    public ModelAndView cancel(ModelMap model, @PathVariable("order_id") Long id) {
        Optional<Order> o = orderRepository.findById(id);
        if (o.isEmpty()) {
            return new ModelAndView("forward:/admin1/orders", model);
        }
        Order oReal = o.get();
        oReal.setStatus((short) 3);
        orderRepository.save(oReal);

        return new ModelAndView("forward:/admin1/orders", model);
    }

    @RequestMapping("/order/confirm/{order_id}")
    public ModelAndView confirm(ModelMap model, @PathVariable("order_id") Long id) {
        Optional<Order> o = orderRepository.findById(id);
        if (o.isEmpty()) {
            return new ModelAndView("forward:/admin1/orders", model);
        }
        Order oReal = o.get();
        oReal.setStatus((short) 1);
        orderRepository.save(oReal);

        return new ModelAndView("forward:/admin1/orders", model);
    }

    @RequestMapping("/order/delivered/{order_id}")
    public ModelAndView delivered(ModelMap model, @PathVariable("order_id") Long id) {
        Optional<Order> o = orderRepository.findById(id);
        if (o.isEmpty()) {
            return new ModelAndView("forward:/admin1/orders", model);
        }
        Order oReal = o.get();
        oReal.setStatus((short) 2);
        orderRepository.save(oReal);

        Product p = null;
        List<OrderDetail> listDe = orderDetailRepository.findByOrderId(id);
        for (OrderDetail od : listDe) {
            p = od.getProduct();
            p.setQuantity(p.getQuantity() - od.getQuantity());
            productRepository.save(p);
        }

        return new ModelAndView("forward:/admin1/orders", model);
    }

}
