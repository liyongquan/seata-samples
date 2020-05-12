package io.seata.samples.dubbo.service.impl;

import io.seata.samples.dubbo.service.AccountService;
import io.seata.samples.dubbo.service.BusinessLockService2;
import io.seata.samples.dubbo.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;

public class BusinessLockServiceImpl2 implements BusinessLockService2 {
    private StorageService storageService;
    private AccountService accountService;
    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
    public void purchase(String userId, String commodityCode, int orderCount) {
        // 计算订单金额
        int orderMoney = calculate(commodityCode, orderCount);
        accountService.debit(userId,orderMoney);
        storageService.deduct(commodityCode, orderCount);
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
