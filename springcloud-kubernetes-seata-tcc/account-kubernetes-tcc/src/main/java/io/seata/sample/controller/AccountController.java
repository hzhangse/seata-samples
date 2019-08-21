package io.seata.sample.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.sofa.runtime.api.annotation.SofaReference;
import com.alipay.sofa.runtime.api.annotation.SofaReferenceBinding;

import io.seata.sample.service.AccountService;

@RestController
public class AccountController {

	@SofaReference(interfaceType = AccountService.class, binding = @SofaReferenceBinding(bindingType = "bolt"))
    private AccountService accountService;

    @RequestMapping(value = "/reduce", produces = "application/json")
    public Boolean debit(String userId, int money) {
    	return accountService.reduce(null,userId, money);
//        
    }
}
