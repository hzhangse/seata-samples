package io.seata.sample.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */

public interface AccountService {

   
	@TwoPhaseBusinessAction(name = "AccountService", commitMethod = "commit", rollbackMethod = "rollback")
    public boolean reduce(BusinessActionContext actionContext,
                           @BusinessActionContextParameter(paramName = "userId") String userId,
                           @BusinessActionContextParameter(paramName = "money") int money);

    /**
     * Commit boolean.
     *
     * @param actionContext the action context
     * @return the boolean
     */
    public boolean commit(BusinessActionContext actionContext);

    /**
     * Rollback boolean.
     *
     * @param actionContext the action context
     * @return the boolean
     */
    public boolean rollback(BusinessActionContext actionContext);
}
