package com.walter;

import com.google.common.collect.Sets;
import com.walter.dao.entity.Order;
import com.walter.dao.entity.OrderItem;
import com.walter.dao.repository.OrderItemRepository;
import com.walter.dao.repository.OrderRepository;
import com.walter.util.JsonUtil;
import com.walter.util.SequenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.Set;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Test
	public void insertOrder() {
		Order order = new Order();
		order.setOrderId(SequenceGenerator.nextSequence());
		order.setUserId(9785);
		order.setStatus("NEW");
		orderRepository.save(order);
		log.info("result: {}", JsonUtil.toJson(order));
	}

	@Test
	public void insertOrderItem() {
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderItemId(SequenceGenerator.nextSequence());
		orderItem.setOrderId(4435827939624378367L);
		orderItem.setUserId(12558);
		orderItemRepository.save(orderItem);
		log.info("result: {}", JsonUtil.toJson(orderItem));
	}

	@Test
	public void selectOrder() {
		Optional<Order> optional = orderRepository.findById(4435827939624378368L);
		optional.ifPresent(order ->log.info("result: {}", JsonUtil.toJson(order)));
	}

	/**
	 * 测试绑定表效果
	 */
	@Test
	public void selectOrderJoinOrderItem(){
		Set<Long> orderIds = Sets.newHashSet(
				4435827939624378367L
				,4435827939624378368L
				,4434030864976863231L
				,4434030864976863232L
		);
		orderItemRepository.findAllByOrderIdIn(orderIds)
				.forEach(order -> log.info("result: {}", JsonUtil.toJson(order)));
	}

	@Test
	public void selectOrderByOrderIdIn() {
		Set<Long> orderIds = Sets.newHashSet(
				4435827939624378367L
				,4435827939624378368L
				,4434030864976863231L
				,4434030864976863232L
		);
		orderRepository.findByOrderIdIn(orderIds)
				.forEach(order -> log.info("result: {}", JsonUtil.toJson(order)));
	}

	@Test
	public void updateOrderWithNonShardingKey(){
		Order order = orderRepository.findById(4434030864976863232L).get();
		order.setStatus("PENDING");
		orderRepository.save(order);
	}

	@Test
	public void updateOrderWithShardingKey(){
		Order order = orderRepository.findById(4405126608379207680L).get();
		orderRepository.delete(order);
		orderRepository.flush();
		order.setUserId(12558);
		orderRepository.save(order);
	}
}
