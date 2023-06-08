package beinet.cn.frontstudy.imgs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public final class ImgHelper {
    private static final int weight = 20;

    private static final int height = 40;
    private static final Random rnd = new Random();    //获取随机数对象
    //private String[] fontNames = {"宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};   //字体数组
    //字体数组
    private static final String[] fontNames = {"Georgia"};

    /**
     * 把字符串转成图片，并返回图片对象
     *
     * @param text 字符串
     * @return 图片
     */
    public static BufferedImage getImage(String text) {
        BufferedImage image = createImage(text.length());
        Graphics2D g = (Graphics2D) image.getGraphics(); //获取画笔
        for (int i = 0; i < text.length(); i++)             //画四个字符即可
        {
            String chr = text.substring(i, i + 1);
            float x = i * 1.0F * weight;   //定义字符的x坐标
            g.setFont(randomFont());           //设置字体，随机
            g.setColor(randomColor());         //设置颜色，随机
            g.drawString(chr, x, height - 5);
        }
        drawLine(image, text.length());
        return image;
    }

    /**
     * 把字符串转成图片，并返回图片的base64字符串形式
     *
     * @param text 字符串
     * @return 图片的base64流
     */
    public static String getImageBase64(String text) throws IOException {
        BufferedImage img = getImage(text);
        byte[] bytes;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ImageIO.write(img, "JPEG", stream);
            bytes = stream.toByteArray();
        }
        String base64 = Base64.getEncoder().encodeToString(bytes).trim();//转换成base64串
        return base64.replaceAll("[\\r\\n]", "");
    }


    private static Color randomColor() {
        int r = rnd.nextInt(225);  //这里为什么是225，因为当r，g，b都为255时，即为白色，为了好辨认，需要颜色深一点。
        int g = rnd.nextInt(225);
        int b = rnd.nextInt(225);
        return new Color(r, g, b);            //返回一个随机颜色
    }

    /**
     * 获取随机字体
     *
     * @return 字体
     */
    private static Font randomFont() {
        int index = rnd.nextInt(fontNames.length);  //获取随机的字体
        String fontName = fontNames[index];
        int style = rnd.nextInt(4);         //随机获取字体的样式，0是无样式，1是加粗，2是斜体，3是加粗加斜体
        int size = rnd.nextInt(10) + 24;    //随机获取字体的大小
        return new Font(fontName, style, size);   //返回一个随机的字体
    }

    /**
     * 画干扰线，用于验证码干扰线，防止计算机解析图片
     *
     * @param image 图片
     */
    private static void drawLine(BufferedImage image, int strLen) {
        int realWeight = strLen * weight;
        int num = rnd.nextInt(10); //定义干扰线的数量
        Graphics2D g = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            int x1 = rnd.nextInt(realWeight);
            int y1 = rnd.nextInt(height);
            int x2 = rnd.nextInt(realWeight);
            int y2 = rnd.nextInt(height);
            g.setColor(randomColor());
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 创建图片的方法
     *
     * @return 图片
     */
    private static BufferedImage createImage(int strLen) {
        int realWeight = weight * strLen;
        //创建图片缓冲区
        BufferedImage image = new BufferedImage(realWeight, height, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics();
        //设置背景色随机
        g.setColor(new Color(255, 255, rnd.nextInt(245) + 10));
        g.fillRect(0, 0, realWeight, height);
        //返回一个图片
        return image;
    }

}
