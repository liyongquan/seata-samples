package io.seata.samples.dubbo.service;

import org.apache.dubbo.common.threadpool.support.fixed.FixedThreadPool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/dubbo-business.xml"})

public class BusinessServiceTest {
    @Autowired
    private BusinessService businessService;
    @Autowired
    private BusinessLockService1 businessLockService1;
    @Autowired
    private BusinessLockService2 businessLockService2;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final String commodityCode = "C00321";
    public static final String userId = "U100001";

    @Before
    public void init() {
        int storage = 201;
        int money = 400;
        jdbcTemplate.update("update storage_tbl set count =  ? where commodity_code = ? ",
                new Object[]{storage, commodityCode});
        jdbcTemplate.update("update account_tbl set money = ? where user_id = ? ",
                new Object[]{money, userId});
    }

    @Test
    public void purchase_normal() {
        businessService.purchase(userId, commodityCode, 2);
        Assert.assertTrue(true);
    }

    @Test(expected = RuntimeException.class)
    public void purchase_money_not_enough() {
        businessService.purchase(userId, commodityCode, 3);
    }

    @Test
    public void lockTest(){
        businessLockService1.purchase(userId,commodityCode,1);
        businessLockService2.purchase(userId,commodityCode,1);
    }

    @Test
    public void lockTest2() throws InterruptedException {
        ExecutorService executor= Executors.newFixedThreadPool(10);
        executor.submit(() -> businessLockService1.purchase(userId, commodityCode, 1));
        executor.submit(() -> businessLockService2.purchase(userId, commodityCode, 1));
        Thread.sleep(30000);
    }
}
