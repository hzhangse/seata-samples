package io.seata.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alipay.sofa.runtime.api.annotation.SofaService;
import com.alipay.sofa.runtime.api.annotation.SofaServiceBinding;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.sample.service.AccountService;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */

@Component
@SofaService(interfaceType = AccountService.class, bindings = { @SofaServiceBinding(bindingType = "bolt") })
public class AccountServiceImpl implements AccountService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean reduce(BusinessActionContext actionContext, String userId, int money) {
		boolean result = false;
		try {
			int changed = jdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?",
					new Object[] { money, userId });
			result = changed > 0;
		} catch (Throwable ex) {
			ex.printStackTrace();
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
		String xid = actionContext.getXid();
		// 账户ID
		final String userId = String.valueOf(actionContext.getActionContext("userId"));
		// 转出金额
		final int money = Integer.valueOf(String.valueOf(actionContext.getActionContext("money")));
		try {
			int changed = jdbcTemplate.update("update account_tbl set money = money + ? where user_id = ?",
					new Object[] { money, userId });
			result = changed > 0;
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return result;
	}

}
