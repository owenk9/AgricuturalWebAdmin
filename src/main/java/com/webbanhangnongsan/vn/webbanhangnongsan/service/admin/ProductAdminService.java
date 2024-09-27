package com.webbanhangnongsan.vn.webbanhangnongsan.service.admin;


import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Product;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PrinterURI;
import java.util.List;

@Service
public class ProductAdminService {
    @Autowired
    private ProductRepository productRepository;

    private final static int pageSize = 1;

    public List<Product> paginatedProducts(int currentPage) {
        int offSet = (currentPage - 1) * pageSize;
        return productRepository.findAll()
                .stream()
                .skip(offSet)
                .limit(pageSize)
                .toList();
    }

    public int totalPage() {
        long productQuantity = productRepository.count();
        return (int) Math.ceil((double) productQuantity / pageSize);
    }
}
