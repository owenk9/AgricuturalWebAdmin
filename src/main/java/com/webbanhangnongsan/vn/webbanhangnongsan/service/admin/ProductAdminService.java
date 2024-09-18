package com.webbanhangnongsan.vn.webbanhangnongsan.service.admin;


import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Product;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PrinterURI;

@Service
public class ProductAdminService {
    @Autowired
    private ProductRepository productRepository;

    public Page<Product> findPaginated(Pageable pageable){
        return productRepository.findAll(pageable);
    }
}
