package com.walter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.walter.dao.entity.Order;
import com.walter.dao.repository.OrderRepository;
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

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void insert() throws JsonProcessingException {
		Order order = new Order();
		order.setOrderId(SequenceGenerator.nextSequence());
		order.setUserId(9785);
		order.setStatus("NEW");
		orderRepository.save(order);
		log.info("result: {}", objectMapper.writeValueAsString(order));
	}

	@Test
	public void select() throws JsonProcessingException {
		Optional<Order> optional = orderRepository.findById(4435827939624378368L);
		if(optional.isPresent()){
			log.info("result: {}", objectMapper.writeValueAsString(optional.get()));
		}
	}

	@Test
	public void selectByOrderIdIn() {
		Set<Long> orderIds = Sets.newHashSet(4435827939624378368L, 4434030864976863232L, 4436917796065009664L);
		orderRepository.findByOrderIdIn(orderIds).forEach(order -> {
			try {
				log.info("result: {}", objectMapper.writeValueAsString(order));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void updateNonShardingKey(){
		Order order = orderRepository.findById(4434030864976863232L).get();
		order.setStatus("PENDING");
		orderRepository.save(order);
	}

	@Test
	public void updateShardingKey(){
		Order order = orderRepository.findById(4405126608379207680L).get();
		orderRepository.delete(order);
		orderRepository.flush();
		order.setUserId(12558);
		orderRepository.save(order);
	}
}
