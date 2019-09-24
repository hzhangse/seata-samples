package io.seata.sample.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@FeignClient(name = "storage-kubernetes", url = "storage-kubernetes-tcc:8081")
public interface StorageFeignClient {

    @GetMapping("/deduct")
    void deduct(@RequestParam("commodityCode") String commodityCode,
                @RequestParam("count") Integer count);
    
    @GetMapping("/deductRollback")
    void deductRollback(@RequestParam("commodityCode") String commodityCode,
                @RequestParam("count") Integer count);

}
