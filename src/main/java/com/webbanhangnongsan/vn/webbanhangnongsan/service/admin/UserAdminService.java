package com.webbanhangnongsan.vn.webbanhangnongsan.service.admin;

import com.webbanhangnongsan.vn.webbanhangnongsan.entity.User;
import com.webbanhangnongsan.vn.webbanhangnongsan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdminService {
    @Autowired
    private UserRepository userRepository;
    private final static int pageSize = 1;
    public List<User> paginatedUsers(int currentPage) {
        int offSet = (currentPage - 1) * pageSize;
        return userRepository.findAll()
                .stream()
                .skip(offSet)
                .limit(pageSize)
                .toList();
    }
    public int totalPage() {
        long UserQuantity = userRepository.count();
        return (int) Math.ceil((double) UserQuantity / pageSize);
    }
}
