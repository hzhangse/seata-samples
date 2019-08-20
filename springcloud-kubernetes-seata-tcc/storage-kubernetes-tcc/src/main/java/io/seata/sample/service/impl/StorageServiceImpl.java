package io.seata.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.sample.service.StorageService;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class StorageServiceImpl implements StorageService{

    @Autowired
    private JdbcTemplate jdbcTemplate;

  
	@Override
	public boolean deduct(BusinessActionContext actionContext, String commodityCode, Integer count) {
		boolean result = false;
		try {
			jdbcTemplate.update("update storage_tbl set count = count - ? where commodity_code = ?",
		            new Object[] {count, commodityCode});
			result = true;
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean commit(BusinessActionContext actionContext) {
		
		return true;
	}

	@Override
	public boolean rollback(BusinessActionContext actionContext) {
		boolean result = false;
		try {
			String xid = actionContext.getXid();
			// 账户ID
			final String commodityCode = String.valueOf(actionContext.getActionContext("commodityCode"));
			
			// 转出金额
			final int count = Integer.valueOf(String.valueOf(actionContext.getActionContext("count")));
			int changenum = jdbcTemplate.update("update storage_tbl set count = count + ? where commodity_code = ?",
		            new Object[] {count, commodityCode});
			result = (changenum==1);
		} catch (Throwable ex) {

		}
		return result;
	}
}
