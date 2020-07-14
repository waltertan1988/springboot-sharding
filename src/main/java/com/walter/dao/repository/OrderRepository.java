package com.walter.dao.repository;

import com.walter.dao.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderIdIn(Set<Long> ids);
}
