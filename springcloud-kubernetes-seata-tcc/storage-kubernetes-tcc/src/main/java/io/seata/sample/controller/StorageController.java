package io.seata.sample.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.sofa.runtime.api.annotation.SofaReference;
import com.alipay.sofa.runtime.api.annotation.SofaReferenceBinding;

import io.seata.sample.service.StorageService;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@RestController
public class StorageController {

	@SofaReference(interfaceType = StorageService.class, binding = @SofaReferenceBinding(bindingType = "bolt"))
    private StorageService storageService;

    @RequestMapping(value = "/deduct", produces = "application/json")
    public Boolean deduct(String commodityCode, Integer count) {
        return storageService.deduct(null,commodityCode, count);
       
    }
}
