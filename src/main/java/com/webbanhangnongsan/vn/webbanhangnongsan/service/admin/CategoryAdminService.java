package com.webbanhangnongsan.vn.webbanhangnongsan.service.admin;


import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Category;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryAdminService {
    @Autowired
    private CategoryRepository CategoryRepository;

    private final static int pageSize = 1;

    public List<Category> paginatedCategories(int currentPage) {
        int offSet = (currentPage - 1) * pageSize;
        return CategoryRepository.findAll()
                .stream()
                .skip(offSet)
                .limit(pageSize)
                .toList();
    }

    public int totalPage() {
        long CategoryQuantity = CategoryRepository.count();
        return (int) Math.ceil((double) CategoryQuantity / pageSize);
    }
}
