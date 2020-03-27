package com.inspur.config;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

/**
 * @author ：zcbz-liangxing
 * @date ：Created in 2020/3/5 15:58
 * 文件说明： </p>
 */

@Component
public class TinyConfiguration {
    private final Security security = new Security();
    private final Async async = new Async();
    private final ApiDoc apidoc = new ApiDoc();
    private final Cache cache = new Cache();
    private final SnowFlake snowFlake = new SnowFlake();
    private final ControllerException controllerException = new ControllerException();

    public TinyConfiguration() {
    }

    public Security getSecurity() {
        return this.security;
    }

    public Async getAsync() {
        return this.async;
    }

    public ApiDoc getApidoc() {
        return this.apidoc;
    }

    public Cache getCache() {
        return this.cache;
    }

    public SnowFlake getSnowFlake() {
        return this.snowFlake;
    }

    public static class Security {
        private final Jwt jwt = new Jwt();
        private String exclude;
        private boolean authContextAutoInject = false;

        public Security() {
        }

        public String getExclude() {
            return this.exclude;
        }

        public void setExclude(String exclude) {
            this.exclude = exclude;
        }

        public boolean isAuthContextAutoInject() {
            return this.authContextAutoInject;
        }

        public void setAuthContextAutoInject(boolean authContextAutoInject) {
            this.authContextAutoInject = authContextAutoInject;
        }

        public Jwt getJwt() {
            return this.jwt;
        }

        public static class Jwt {
            @Length(
                    min = 64
            )
            private String secret = "3XKfzFReDSSipqnmYYbXNt6X9GBq83zzuW8N77sOtlGr8aLp0IxbYABRgU7HSNSr";

            public Jwt() {
            }

            public String getSecret() {
                return this.secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }
        }
    }

    public static class Async {
        private int corePoolSize = 5;
        private int maxPoolSize = 50;
        private int queueCapacity = 10000;

        public Async() {
        }

        public int getCorePoolSize() {
            return this.corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return this.maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return this.queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    public static class ApiDoc {
        private String apiBasePackage = "com.inspur";

        public ApiDoc() {
        }

        public String getApiBasePackage() {
            return this.apiBasePackage;
        }

        public void setApiBasePackage(String apiBasePackage) {
            this.apiBasePackage = apiBasePackage;
        }
    }

    public static class Cache {
        private int redisDefaultTTL = 1800;

        public Cache() {
        }

        public int getRedisDefaultTTL() {
            return this.redisDefaultTTL;
        }

        public void setRedisDefaultTTL(int redisDefaultTTL) {
            this.redisDefaultTTL = redisDefaultTTL;
        }
    }

    public static class SnowFlake {
        public static final String STRATEGY_MANUAL = "manual";
        public static final String STRATEGY_RANDOM = "random";
        public static final String STRATEGY_IPV4 = "ipv4";
        private int dataCenterId;
        private int machineId;
        private String workerIdAutoGenerateStrategy = "random";

        public SnowFlake() {
        }

        public int getDataCenterId() {
            return this.dataCenterId;
        }

        public void setDataCenterId(int dataCenterId) {
            this.dataCenterId = dataCenterId;
        }

        public int getMachineId() {
            return this.machineId;
        }

        public void setMachineId(int machineId) {
            this.machineId = machineId;
        }

        public String getWorkerIdAutoGenerateStrategy() {
            return this.workerIdAutoGenerateStrategy;
        }

        public void setWorkerIdAutoGenerateStrategy(String workerIdAutoGenerateStrategy) {
            this.workerIdAutoGenerateStrategy = workerIdAutoGenerateStrategy;
        }
    }

    public static class ControllerException {
        private boolean enabled = false;

        public ControllerException() {
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
