package io.seata.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import io.seata.sample.feign.UserFeignClient;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class OrderService {

	@Autowired
	private UserFeignClient userFeignClient;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean create(String userId, String commodityCode, Integer count) {

		int orderMoney = count * 100;
		int result = jdbcTemplate.update("insert order_tbl(user_id,commodity_code,count,money) values(?,?,?,?)",
				new Object[] { userId, commodityCode, count, orderMoney });
		return result == 1;
		// userFeignClient.reduce(userId, orderMoney);

	}

	public boolean delete(String userId, String commodityCode, Integer count) {

		int orderMoney = count * 100;
		int result = jdbcTemplate.update("delete from order_tbl where user_id=? and commodity_code=? and count=? and money=? ",
				new Object[] { userId, commodityCode, count, orderMoney });
		return result == 1;
		// userFeignClient.reduce(userId, orderMoney);

	}
}
