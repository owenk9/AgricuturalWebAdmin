package com.webbanhangnongsan.vn.webbanhangnongsan.controller.admin;

import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin1")
public class IndexController {
    @GetMapping("/index")
    public String index(){
        return "admin/index";
    }
}
