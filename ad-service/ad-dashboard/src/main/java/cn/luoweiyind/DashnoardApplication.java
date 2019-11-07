package cn.luoweiyind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
//监控服务，后面由spring-cloud-starter-netflix-hystrix-dashboard自动完成
@SpringBootApplication
@EnableHystrixDashboard
@EnableEurekaClient
public class DashnoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashnoardApplication.class, args);
    }
}
