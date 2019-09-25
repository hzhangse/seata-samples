package io.seata.sample.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface OrderService {
    /**
     * return value can be any type
     * @param actionContext
     * @param userId
     * @param commodityCode
     * @param count
     * @return 
     */
	@TwoPhaseBusinessAction(name = "OrderService", commitMethod = "commit", rollbackMethod = "rollback")
	public String create(BusinessActionContext actionContext,
			@BusinessActionContextParameter(paramName = "userId") String userId,
			@BusinessActionContextParameter(paramName = "commodityCode") String commodityCode,
			@BusinessActionContextParameter(paramName = "count") Integer count);

	/**
	 * Commit boolean.
	 *
	 * @param actionContext
	 *            the action context
	 * @return the boolean
	 */
	public Boolean commit(BusinessActionContext actionContext);

	/**
	 * Rollback boolean.
	 *
	 * @param actionContext
	 *            the action context
	 * @return the boolean
	 */
	public Boolean rollback(BusinessActionContext actionContext);

}
