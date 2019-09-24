package io.seata.sample.controller;

import io.seata.sample.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping(value = "/create", produces = "application/json")
	public Boolean create(String userId, String commodityCode, Integer count) {
		boolean result = false;
		try {
			result = orderService.create(userId, commodityCode, count);
		} catch (Exception ex) {

		}
		return result;
	}

	@GetMapping(value = "/delete", produces = "application/json")
	public Boolean delete(String userId, String commodityCode, Integer count) {
		boolean result = false;
		try {
			result = orderService.delete(userId, commodityCode, count);
		} catch (Exception ex) {

		}
		return result;
	}
}
