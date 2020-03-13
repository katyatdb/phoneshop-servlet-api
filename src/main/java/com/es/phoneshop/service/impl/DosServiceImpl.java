package com.es.phoneshop.service.impl;

import com.es.phoneshop.service.DosService;

import java.util.HashMap;
import java.util.Map;

public class DosServiceImpl implements DosService {

    private static final int MAX_REQUEST_AMOUNT = 500;
    private static final int RESET_TIME = 1000 * 60;

    private Map<String, IpInfo> ipInfoMap;

    private static class IpInfo {
        private int requestAmount;
        private long lastRequestTime;

        public IpInfo(int requestAmount, long lastRequestTime) {
            this.requestAmount = requestAmount;
            this.lastRequestTime = lastRequestTime;
        }
    }

    private DosServiceImpl() {
        ipInfoMap = new HashMap<>();
    }

    public static DosServiceImpl getInstance() {
        return DosServiceHolder.instance;
    }

    private static class DosServiceHolder {
        private static final DosServiceImpl instance = new DosServiceImpl();
    }

    @Override
    public boolean isAllowed(String ip) {
        ipInfoMap.putIfAbsent(ip, new IpInfo(1, System.currentTimeMillis()));
        IpInfo ipInfo = ipInfoMap.get(ip);

        if (System.currentTimeMillis() - ipInfo.lastRequestTime > RESET_TIME) {
            ipInfo.requestAmount = 1;
            ipInfo.lastRequestTime = System.currentTimeMillis();
        }

        if (ipInfo.requestAmount <= MAX_REQUEST_AMOUNT) {
            ipInfo.requestAmount++;
            ipInfo.lastRequestTime = System.currentTimeMillis();
            return true;
        }

        return false;
    }
}
