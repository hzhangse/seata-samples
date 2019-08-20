package io.seata.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.sample.feign.UserFeignClient;
import io.seata.sample.service.OrderService;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private UserFeignClient userFeignClient;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean create(BusinessActionContext actionContext, String userId, String commodityCode, Integer count) {
		boolean result = false;
		try {
			int orderMoney = count * 100;
			jdbcTemplate.update("insert order_tbl(user_id,commodity_code,count,money) values(?,?,?,?)",
					new Object[] { userId, commodityCode, count, orderMoney });

			userFeignClient.reduce(userId, orderMoney);
			result = true;
		} catch (Throwable ex) {

		}
		return result;
	}

	@Override
	public boolean commit(BusinessActionContext actionContext) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean rollback(BusinessActionContext actionContext) {
		boolean result = false;
		try {
			String xid = actionContext.getXid();
			// 账户ID
			final String commodityCode = String.valueOf(actionContext.getActionContext("commodityCode"));
			final String userId = String.valueOf(actionContext.getActionContext("userId"));
			// 转出金额
			final int count = Integer.valueOf(String.valueOf(actionContext.getActionContext("count")));
			int orderMoney = count * 100;
			jdbcTemplate.update("delete from order_tbl where user_id=? and commodity_code=? and count=? and money=? ",
					new Object[] { userId, commodityCode, count, orderMoney });

			//userFeignClient.reduce(userId, orderMoney);
			result = true;
		} catch (Throwable ex) {

		}
		return result;
	}
}
