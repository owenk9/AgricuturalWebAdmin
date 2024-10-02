package com.webbanhangnongsan.vn.webbanhangnongsan.service.admin;


import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Order;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    OrderRepository orderRepository;
    public List<Order> listAll()
    {
        return orderRepository.findAll();
    }
}
