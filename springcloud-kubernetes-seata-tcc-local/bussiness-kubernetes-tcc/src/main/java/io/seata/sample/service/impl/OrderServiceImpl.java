package io.seata.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iotbull.paascloud.idempotence.annotation.ApplyIdempotence;
import com.iotbull.paascloud.idempotence.annotation.GenIdempotenceKey;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.sample.feign.OrderFeignClient;
import io.seata.sample.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderFeignClient orderFeignClient;

	@Override
	@GenIdempotenceKey(uniqueKey = "#actionContext.xid+'_'+#actionContext.branchId")
	public String create(BusinessActionContext actionContext, String userId, String commodityCode, Integer count) {
		boolean result = false;
		try {

			orderFeignClient.create(userId, commodityCode, count);
			result = true;
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return commodityCode;
	}

	@Override
	@ApplyIdempotence(uniqueKey = "#actionContext.xid+'_'+#actionContext.branchId")
	public Boolean commit(BusinessActionContext actionContext) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	@ApplyIdempotence(uniqueKey = "#actionContext.xid+'_'+#actionContext.branchId")
	public Boolean rollback(BusinessActionContext actionContext) {
		boolean result = false;
		try {
			// 账户ID
			final String commodityCode = String.valueOf(actionContext.getActionContext("commodityCode"));
			final String userId = String.valueOf(actionContext.getActionContext("userId"));
			// 转出金额
			final int count = Integer.valueOf(String.valueOf(actionContext.getActionContext("count")));
			
			orderFeignClient.delete(userId, commodityCode, count);
			result = true;
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return result;
	}

}
