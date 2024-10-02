package com.webbanhangnongsan.vn.webbanhangnongsan.repository;

import com.webbanhangnongsan.vn.webbanhangnongsan.entity.Order;
import com.webbanhangnongsan.vn.webbanhangnongsan.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {
    @Query(value = "select * from order_details where order_id = ?;", nativeQuery = true)
    List<OrderDetail> findByOrderId(Long id);
}
