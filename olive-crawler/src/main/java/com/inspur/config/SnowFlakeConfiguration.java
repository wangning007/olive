package com.inspur.config;

import com.inspur.config.exception.SnowFlakeGenInitException;
import com.inspur.utils.SnowFlakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * @author ：zcbz-liangxing
 * @date ：Created in 2020/3/5 15:11
 * 文件说明： </p>
 */
@Configuration
public class SnowFlakeConfiguration {

    @Autowired
    TinyConfiguration tinyConfiguration;
    private final String LOCAL = "127.0.0.1";
    private final int IPV4_SEGMENT = 4;
    private final String SNOWFLAKE_GEN_EXCEPTION_MSG = "SnowFlakeIdGenerator初始化错误";

    public SnowFlakeConfiguration() {
    }

    @Bean(name = "snowFlakeIdGenerator")
    SnowFlakeIdGenerator getSnowFlakeIdGenerator() throws SnowFlakeGenInitException {
        String strategy = this.tinyConfiguration.getSnowFlake().getWorkerIdAutoGenerateStrategy();
        int dataCenterId;
        int machineId;
        if ("manual".equals(strategy)) {
            dataCenterId = this.tinyConfiguration.getSnowFlake().getDataCenterId();
            machineId = this.tinyConfiguration.getSnowFlake().getMachineId();
            System.out.println("雪花算法生成！生成策略为：{}，数据中心ID为：{}，机器ID为：{}"+new Object[]{strategy, dataCenterId, dataCenterId});
            return new SnowFlakeIdGenerator((long) dataCenterId, (long) machineId);
        } else {
//            int dataCenterId;
            if ("random".equals(strategy)) {
                Random random = new Random();
                dataCenterId = random.nextInt(4);
                machineId = random.nextInt(256);
                System.out.println("雪花算法生成！生成策略为：{}，数据中心ID为：{}，机器ID为：{}"+new Object[]{strategy, dataCenterId, dataCenterId});
                return new SnowFlakeIdGenerator((long) dataCenterId, (long) machineId);
            } else if ("ipv4".equals(strategy)) {
                String hostAddress = "127.0.0.1";

                try {
                    hostAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException var6) {
                    ;
                }

                String[] ipGroup = StringUtils.split(hostAddress, ",");
                if (ipGroup == null) {
                    System.out.println("SNOWFLAKE配置错误，请检查配置和系统环境。当前的策略为：{}"+strategy);
                    throw new SnowFlakeGenInitException("SnowFlakeIdGenerator初始化错误");
                } else if (ipGroup.length == 4) {
                    dataCenterId = Integer.valueOf(ipGroup[2]) % 4;
                    machineId = Integer.valueOf(ipGroup[3]) % 256;
                    System.out.println("雪花算法生成！生成策略为：{}，数据中心ID为：{}，机器ID为：{}"+ new Object[]{strategy, dataCenterId, machineId});
                    return new SnowFlakeIdGenerator((long) dataCenterId, (long) machineId);
                } else {
                    System.out.println("SNOWFLAKE配置错误，请检查配置和系统环境。当前的策略为：{}"+strategy);
                    throw new SnowFlakeGenInitException("SnowFlakeIdGenerator初始化错误");
                }
            } else {
                System.out.println("SNOWFLAKE配置错误，请检查配置和系统环境。当前的策略为：{}"+strategy);
                throw new SnowFlakeGenInitException("SnowFlakeIdGenerator初始化错误");
            }
        }
    }
}
