package io.seata.sample.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import io.seata.sample.feign.OrderFeignClient;
import io.seata.sample.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@Service
public class BusinessService {

    @Autowired
    private StorageFeignClient storageFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private OrderService orderService;
    @Autowired
    private StorageService storageService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 减库存，下订单
     *
     * @param userId
     * @param commodityCode
     * @param orderCount
     */
    @GlobalTransactional
    public void purchase(String userId, String commodityCode, int orderCount) {
    	String commodityCode1 = orderService.create(null,userId, commodityCode, orderCount);   	
    	storageService.deduct(null,commodityCode1, orderCount);  	
        if (!validData()) {
            throw new RuntimeException("账户或库存不足,执行回滚");
        }
    }

    @PostConstruct
    public void initData() {
        jdbcTemplate.update("delete from account_tbl");
        jdbcTemplate.update("delete from order_tbl");
        jdbcTemplate.update("delete from storage_tbl");
        jdbcTemplate.update("insert into account_tbl(user_id,money) values('U100000','10000') ");
        jdbcTemplate.update("insert into storage_tbl(commodity_code,count) values('C100000','200') ");
    }

    public boolean validData() {
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Map accountMap = jdbcTemplate.queryForMap("select * from account_tbl where user_id='U100000'");
        if (Integer.parseInt(accountMap.get("money").toString()) < 0) {
            return false;
        }
        Map storageMap = jdbcTemplate.queryForMap("select * from storage_tbl where commodity_code='C100000'");
        if (Integer.parseInt(storageMap.get("count").toString()) < 0) {
            return false;
        }
        return true;
    }
}
