package com.atguigu.springcloud.alibaba.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Hello world!
 */
@RestController
@SpringBootApplication
public class HelloServiceConsumer {


    @DubboReference()
    HelloService helloService;

    @GetMapping("/flowDemo")
    @SentinelResource(value = "flowDemo", blockHandler = "flowFunction", fallback = "fallBackFunction")
    public String flowDemo() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return helloService.flowDemo();
    }

    @GetMapping("/degradeDemo")
    @SentinelResource(value = "degradeDemo", blockHandler = "flowFunction", fallback = "fallBackFunction")
    public String degradeDemo() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return helloService.degradeDemo();
    }

    @GetMapping("/degradeDemo1")
    @SentinelResource(value = "degradeDemo1", blockHandler = "flowFunction", fallback = "fallBackFunction")
    public String degradeDemo1() {
        int a = 10 / 0;
        return helloService.degradeDemo();
    }

    public String flowFunction(BlockException exception) {
        System.out.println("当前请求已经被限流！" + "，时间:" + new Date().toString());
        return "当前请求已经被限流！" + "，时间:" + new Date().toString();
    }

    public String fallBackFunction(Throwable exception) {
        System.out.println("当前请求已经熔断降级！" + "，时间:" + new Date().toString());
        return "当前请求已经熔断降级！" + "，时间:" + new Date().toString();
    }


    @GetMapping("/paramFlowDemo")
    @SentinelResource(value = "paramFlowDemo", blockHandler = "flowFunction1", fallback = "fallBackFunction1")
    public String paramFlowDemo(String p1, String p2, String p3) {
        return helloService.paramFlowDemo(p1, p2, p3);
    }

    public String flowFunction1(String p1, String p2, String p3, BlockException exception) {
        System.out.println("当前请求已经被限流！" + "，时间:" + new Date().toString());
        return "当前请求已经被限流！" + "，时间:" + new Date().toString();
    }

    public String fallBackFunction1(String p1, String p2, String p3, Throwable exception) {
        System.out.println("当前请求已经熔断降级！" + "，时间:" + new Date().toString());
        return "当前请求已经熔断降级！" + "，时间:" + new Date().toString();
    }


    @GetMapping("/hello")
    @SentinelResource(value = "dubboHelloTest", blockHandler = "flowDemo", fallback = "fallBackDemo")
    public String hello() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        int i = 10 / 0;
        return helloService.sayHello();
    }

    @GetMapping("/hello1")
    @SentinelResource(value = "dubboHelloTest1", blockHandler = "flowDemo1", fallback = "fallBackDemo1")
    public String hello1(String id1, String id2, String id3) {
        System.out.println(id1);
        System.out.println(id2);
        System.out.println(id3);
        System.out.println(new Date().toLocaleString());
        return helloService.sayHello();
    }


    public String flowDemo(BlockException exception) {
        System.out.println("dubboHelloTest:" + "flowDemo");
        return "dubboHelloTest" + "flowDemo";
    }

    public String fallBackDemo(Throwable exception) {
        if (exception instanceof ParamFlowException) {
            System.out.println("热点参数超了");
        } else {
            System.out.println("dubboHelloTest:" + "fallBackDemo");
        }
        return "dubboHelloTest" + "fallBackDemo";
    }

    public String flowDemo1(String id1, String id2, String id3, BlockException exception) {
        System.out.println("dubboHelloTest:" + "flowDemo1");
        return "dubboHelloTest" + "flowDemo1";
    }

    public String fallBackDemo1(String id1, String id2, String id3, Throwable exception) {
        if (exception instanceof ParamFlowException) {
            System.out.println("热点参数超了");
        } else {
            System.out.println("dubboHelloTest:" + "fallBackDemo1");
        }
        return "dubboHelloTest" + "fallBackDemo1";
    }

    @GetMapping("/zengbangA")
    public String zengbangA() {
        Entry entry = null;
        try {
            entry = SphU.entry("zengbangA");
            // 被保护的业务逻辑
            // do something here...
            System.out.println("我是曾棒A");
            return "我是曾棒A";
        } catch (BlockException ex) {
            // 资源访问阻止，被限流或被降级
            // 在此处进行相应的处理操作
            System.out.println("我是曾棒A,我是一串代码资源，我被限流了");
            return "我是曾棒A,我是一串代码资源，我被限流了";
        } finally {
            // 务必保证 exit，务必保证每个 entry 与 exit 配对
            if (entry != null) {
                entry.exit();
            }
        }
    }

    @GetMapping("/zengbangB")
    @SentinelResource(value = "/zengbangB")
    public String zengbangB(String id1, String id2, String id3) {
        Entry entry = null;
        System.out.println(id1);
        System.out.println(id2);
        System.out.println(id3);
        System.out.println(new Date().toLocaleString());
        return id1 + id2 + id3 + new Date().toLocaleString();
/*        try {
            // 若需要配置例外项，则传入的参数只支持基本类型。
            // EntryType 代表流量类型，其中系统规则只对 IN 类型的埋点生效
            // count 大多数情况都填 1，代表统计为一次调用。
            entry = SphU.entry("zengbangB", EntryType.IN, 1, id1);
            // Your logic here.
        } catch (BlockException ex) {
            System.out.println("zengbangB被限流了");
            // Handle request rejection.
        } finally {
            // 注意：exit 的时候也一定要带上对应的参数，否则可能会有统计错误。
            if (entry != null) {
                entry.exit(1, id1);
            }
        }*/
    }

}
