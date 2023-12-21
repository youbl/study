package benet.cn.springfeignstudy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class SpringFeignStudyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringFeignStudyApplication.class, args);
    }

    @Autowired
    FeignDemoAsync feignDemo;

    @Override
    public void run(String... args) throws Exception {
        log.info("程序启动: {}", Thread.currentThread().getName());
        log.info(feignDemo.xxx());
        log.info("程序结束: {}", Thread.currentThread().getName());

        /**
         * 运行结果参考：可以看到:
         * 1、feignDemo.xxx返回值为null，因为是异步，无法直接拿到返回值；
         * 2、“程序结束”这几个字先输出，feignDemo.xxx里的“异步启动”后输出，可见确实是异步执行；
         * 3、“异步启动”输出的线程名为“feignPool-1”，确实是AsyncPoolConfig.getTaskExecutor定义的线程池；
         * 4、feignDemo.xxx里面是同步的，能正确输出feign接口返回的值
         2023-12-21 21:18:50.864  INFO 8540 --- [           main] b.c.s.SpringFeignStudyApplication        : 程序启动: main
         2023-12-21 21:18:50.868  INFO 8540 --- [           main] b.c.s.SpringFeignStudyApplication        : null
         2023-12-21 21:18:50.868  INFO 8540 --- [           main] b.c.s.SpringFeignStudyApplication        : 程序结束: main
         2023-12-21 21:18:50.874  INFO 8540 --- [    feignPool-1] b.cn.springfeignstudy.FeignDemoAsync     : 异步启动: feignPool-1
         2023-12-21 21:18:51.246  INFO 8540 --- [    feignPool-1] b.cn.springfeignstudy.FeignDemoAsync     : 调用结束:feignPool-1 接口返回:<!DOCTYPE html>
         <!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compatible content=IE=Edge><meta content=always name=referrer><link rel=stylesheet type=text/css href=https://ss1.bdstatic.com/5eN1bjq8AAUYm2zgoY3K/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link=#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div class=s_form> <div class=s_form_wrapper> <div id=lg> <img hidefocus=true src=//www.baidu.com/img/bd_logo1.png width=270 height=129> </div> <form id=form name=f action=//www.baidu.com/s class=fm> <input type=hidden name=bdorz_come value=1> <input type=hidden name=ie value=utf-8> <input type=hidden name=f value=8> <input type=hidden name=rsv_bp value=1> <input type=hidden name=rsv_idx value=1> <input type=hidden name=tn value=baidu><span class="bg s_ipt_wr"><input id=kw name=wd class=s_ipt value maxlength=255 autocomplete=off autofocus=autofocus></span><span class="bg s_btn_wr"><input type=submit id=su value=百度一下 class="bg s_btn" autofocus></span> </form> </div> </div> <div id=u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=https://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');
         </script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id=ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
         */
    }
}
