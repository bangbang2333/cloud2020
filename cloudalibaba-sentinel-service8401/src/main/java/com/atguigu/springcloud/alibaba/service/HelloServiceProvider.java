package com.atguigu.springcloud.alibaba.service;

import org.apache.dubbo.config.annotation.DubboService;

import java.util.Date;

/**
 * HelloServiceImpl:
 *
 * @author: wuhuhu
 * @date: 2020/12/15 18:13
 */
@DubboService()
public class HelloServiceProvider implements HelloService {
    @Override
    public String sayHello() {
        return "hello world: spring cloud";
    }

    @Override
    public String flowDemo() {
        return "这是流量控制的demo" + "，时间:" + new Date().toString();
    }

    @Override
    public String degradeDemo() {
        return "这是熔断降级的demo" + "，时间:" + new Date().toString();
    }

    @Override
    public String paramFlowDemo(String p1, String p2, String p3) {
        return "参数是：" + p1 + p2 + p3 + "这是热点key的demo" + "，时间:" + new Date().toString();
    }
}
