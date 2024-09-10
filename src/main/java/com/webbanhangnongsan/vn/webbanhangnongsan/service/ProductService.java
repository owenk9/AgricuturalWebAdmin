package com.webbanhangnongsan.vn.webbanhangnongsan.service;

import com.webbanhangnongsan.vn.webbanhangnongsan.dto.ProductDTO;
import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Product;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.CategoryRepository;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> listProductFiltered(long categoryId, int minAmount, int maxAmount, int page) {
        List<Product> productList = productRepository.listAllProduct();

        // Lọc theo categoryId nếu được cung cấp
        if (categoryId != 0) {
            productList = productList.stream()
                    .filter(product -> product.getCategory().getCategoryId() == categoryId)
                    .toList();
        }

        // Lọc theo minAmount nếu được cung cấp
        if (minAmount != 0) {
            productList = productList.stream()
                    .filter(product -> product.getPrice() >= minAmount)
                    .toList();
        }

        // Lọc theo maxAmount nếu được cung cấp
        if (maxAmount != 0) {
            productList = productList.stream()
                    .filter(product -> product.getPrice() <= maxAmount)
                    .toList();
        }

        // Xác định kích thước trang (số sản phẩm trên mỗi trang)
        int pageSize = 1; // Bạn có thể thay đổi giá trị này theo nhu cầu của mình
        int skip = (page - 1) * pageSize;

        // Phân trang danh sách sản phẩm
        List<Product> paginatedList = productList.stream()
                .skip(skip)
                .limit(pageSize)
                .toList();

        return paginatedList;
    }


    public int numPage(long categoryId,int minAmount,int maxAmount){
        List<Product> productList = productRepository.listAllProduct();
        if(categoryId != 0){
            productList = productList.stream()
                    .filter(product -> product.getCategory().getCategoryId() == categoryId)
                    .toList();
        }
        if(minAmount != 0){
            productList = productList.stream()
                    .filter(product -> product.getPrice() >= minAmount)
                    .toList();
        }
        // lọc sản phẩm theo giá lớn hơn hoặc bằng minAmount và nhỏ hơn hoặc bằng maxAmount
        // Lọc theo minAmount nếu được cung cấp
        if (minAmount != 0) {
            productList = productList.stream()
                    .filter(product -> product.getPrice() >= minAmount)
                    .toList();
        }

        // Lọc theo maxAmount nếu được cung cấp
        if (maxAmount != 0) {
            productList = productList.stream()
                    .filter(product -> product.getPrice() <= maxAmount)
                    .toList();
        }
        int pageSize = 1;
        int totalProducts = productList.size();
        return (totalProducts + pageSize - 1) / pageSize;  // Phép chia lấy trần
    }

    public int productFound(long categoryId,int minAmount,int maxAmount){
        List<Product> productList = productRepository.listAllProduct();
        if(categoryId != 0){
            productList = productList.stream()
                    .filter(product -> product.getCategory().getCategoryId() == categoryId)
                    .toList();
        }
        if(minAmount != 0){
            productList = productList.stream()
                    .filter(product -> product.getPrice() >= minAmount)
                    .toList();
        }
        // lọc sản phẩm theo giá lớn hơn hoặc bằng minAmount và nhỏ hơn hoặc bằng maxAmount
        // Lọc theo minAmount nếu được cung cấp
        if (minAmount != 0) {
            productList = productList.stream()
                    .filter(product -> product.getPrice() >= minAmount)
                    .toList();
        }

        // Lọc theo maxAmount nếu được cung cấp
        if (maxAmount != 0) {
            productList = productList.stream()
                    .filter(product -> product.getPrice() <= maxAmount)
                    .toList();
        }
        int pageSize = 1;
        int totalProducts = productList.size();
        return totalProducts;
    }
}
