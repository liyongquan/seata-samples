package io.seata.samples.dubbo.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/dubbo-business.xml"})

public class BusinessServiceTest {
    @Autowired
    private BusinessService businessService;
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
}
