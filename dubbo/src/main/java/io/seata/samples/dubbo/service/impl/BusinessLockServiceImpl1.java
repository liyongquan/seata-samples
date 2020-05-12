package io.seata.samples.dubbo.service.impl;

import io.seata.samples.dubbo.service.AccountService;
import io.seata.samples.dubbo.service.BusinessLockService1;
import io.seata.samples.dubbo.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;

public class BusinessLockServiceImpl1 implements BusinessLockService1 {
    private StorageService storageService;
    private AccountService accountService;
    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
    public void purchase(String userId, String commodityCode, int orderCount) {
        storageService.deduct(commodityCode, orderCount);
        // 计算订单金额
        int orderMoney = calculate(commodityCode, orderCount);
        accountService.debit(userId,orderMoney);
    }

    private int calculate(String commodityId, int orderCount) {
        return 200 * orderCount;
    }

    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
