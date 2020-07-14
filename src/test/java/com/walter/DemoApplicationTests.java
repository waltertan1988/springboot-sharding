package com.walter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walter.dao.entity.Order;
import com.walter.dao.repository.OrderRepository;
import com.walter.util.SequenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired
	private OrderRepository orderRepository;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void insert() {
		Order order = new Order();
		order.setOrderId(SequenceGenerator.nextSequence());
		order.setUserId(12558);
		order.setStatus("NEW");
		orderRepository.save(order);
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
		orderRepository.findByOrderIdIn(Sets.newLinkedHashSet(4435827939624378368L, 4434030864976863232L)).forEach(x -> {
			try {
				log.info("result: {}", objectMapper.writeValueAsString(x));
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
