package com.example.springutilsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;

@SpringBootApplication
public class SpringUtilsDemoApplication {

    public static void main(String[] args) {
        showFonts();
		SpringApplication.run(SpringUtilsDemoApplication.class, args);
    }

    private static void showFonts() {
        // 获取本地的字体环境
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // 获取系统支持的字体名称数组
        String[] fontNames = ge.getAvailableFontFamilyNames();
        // 注：在阿里云的linux会抛null异常，需要先在系统里安装 yum install fontconfig 这样也可以支持fc-list命令

        // 输出系统支持的字体名称
        for (String fontName : fontNames) {
            System.out.println(fontName);
        }
        /*
        *
阿里云linux系统（Alibaba Cloud Linux release 3 (Soaring Falcon) ）输出
和
Red Hat Enterprise Linux release 8.3 (Ootpa) 输出相同
* 如下：
DejaVu Sans
DejaVu Sans Condensed
DejaVu Sans Light
Dialog
DialogInput
Monospaced
SansSerif
Serif


CentOS Linux release 7.9.2009 (Core)系统输出如下：
Bitstream Charter
Courier 10 Pitch
Cursor
DejaVu Sans
DejaVu Sans Condensed
DejaVu Sans Light
Dialog
DialogInput
Monospaced
SansSerif
Serif
Utopia
        * */
    }
}
