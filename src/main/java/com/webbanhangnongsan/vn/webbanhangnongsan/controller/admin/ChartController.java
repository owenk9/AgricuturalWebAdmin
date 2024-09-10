package com.webbanhangnongsan.vn.webbanhangnongsan.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin1")
public class ChartController {
    @GetMapping("/charts")
    public String chart(){
        return "admin/charts";
    }
}
