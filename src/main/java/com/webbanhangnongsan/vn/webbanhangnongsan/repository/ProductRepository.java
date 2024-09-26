package com.webbanhangnongsan.vn.webbanhangnongsan.repository;

import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query(value = "SELECT c.category_id, c.category_name, \r\n"
            + "COUNT(*) AS SoLuong \r\n"
            + "FROM products p \r\n"
            + "JOIN categories c ON p.category_id = c.category_id \r\n"
            + "GROUP BY c.category_id, c.category_name;", nativeQuery = true)
    public List<Object[]> listCategoryByProductName();

    @Query(value = "SELECT * FROM products LIMIT 8", nativeQuery = true)
    public List<Product> top8Products();

    @Query(value = "SELECT * FROM products", nativeQuery = true)
    public List<Product> listAllProduct();
}
