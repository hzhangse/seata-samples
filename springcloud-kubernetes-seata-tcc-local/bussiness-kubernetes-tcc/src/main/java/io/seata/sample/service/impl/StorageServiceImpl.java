package io.seata.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iotbull.paascloud.idempotence.annotation.ApplyIdempotence;
import com.iotbull.paascloud.idempotence.annotation.GenIdempotenceKey;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.sample.feign.StorageFeignClient;
import io.seata.sample.service.StorageService;
@Service
public class StorageServiceImpl implements StorageService {

	@Autowired
	private StorageFeignClient storageFeignClient;
	@Override
	@GenIdempotenceKey(uniqueKey = "#actionContext.xid+'_'+#actionContext.branchId")
	public Boolean deduct(BusinessActionContext actionContext, String commodityCode, Integer count) {
		boolean result = false;
		try {
			storageFeignClient.deduct(commodityCode, count);
			result = true;
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	@ApplyIdempotence(uniqueKey = "#actionContext.xid+'_'+#actionContext.branchId")
	public Boolean commit(BusinessActionContext actionContext) {
		
		return true;
	}

	@Override
	@ApplyIdempotence(uniqueKey = "#actionContext.xid+'_'+#actionContext.branchId")
	public Boolean rollback(BusinessActionContext actionContext) {
		boolean result = false;
		try {
			String xid = actionContext.getXid();
			// 账户ID
			final String commodityCode = String.valueOf(actionContext.getActionContext("commodityCode"));			
			// 转出金额
			final int count = Integer.valueOf(String.valueOf(actionContext.getActionContext("count")));
			storageFeignClient.deductRollback(commodityCode, count);
			result = true;
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return result;
	}

}
