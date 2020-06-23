package com.walter.dao.repository;

import com.walter.dao.entity.Order;
import com.walter.dao.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
