package beinet.cn.socketclientdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

@SpringBootApplication
public class SocketClientDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SocketClientDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String msg = "我是客户端，发个文件";
        // base64结果(长度40): 5oiR5piv5a6i5oi356uv77yM5Y+R5Liq5paH5Lu2
        String file = "d:\\kafka.png";
        try (Socket socket = new Socket("127.0.0.1", 11335)) {
            writeSocket(socket, msg);
            Thread.sleep(100);
            String response = readSocket(socket);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeSocket(Socket socket, String msg) throws IOException {
        // encodeBuffer的base64会多出\r\n这2个字符，要trim掉
        String base64Str = new BASE64Encoder().encode(msg.getBytes());
        System.out.println(base64Str.length() + ": " + base64Str);
        byte[] byteLen = getBytes(base64Str.length());
        OutputStream os = socket.getOutputStream();

        PrintWriter pw = new PrintWriter(os);
        // 写入4字节长度
        os.write(byteLen);
        // 写入信息
        pw.print(base64Str);
        pw.flush();
        //socket.shutdownOutput();
    }

    private String readSocket(Socket socket) {
        byte[] datalen = new byte[4];
        int len = 1024;
        byte[] bytesReceived = new byte[len];
        StringBuilder sbRet = new StringBuilder();

        try (InputStream is = socket.getInputStream()) {
            int recieved = is.read(datalen, 0, 4);
            if (recieved != datalen.length) {
                return ""; // 长度接收错误
            }
            int intDataLen = toInt32(datalen); // 待接收的数据长度

            int recievedLen = 0;
            int leftLen = intDataLen;
            while (recievedLen < intDataLen) {
                // 避免多接收数据
                int currentLen = leftLen < len ? leftLen : len;
                int tmp = is.read(bytesReceived, 0, currentLen);
                if (tmp <= 0) {
//                    log.Error("{0} 收到的base64信息长度{1} 实际字节长度{2}",
//                            begin.ToString("HH:mm:ss.fff"),
//                            intDataLen.ToString(),
//                            recievedLen.ToString());
                    return ""; //获取的数据与长度不一致
                }

                sbRet.append(new String(bytesReceived, 0, tmp));
                recievedLen += tmp;
                leftLen -= tmp;
            }
            if (sbRet.length() > 0) {
                return new String(new BASE64Decoder().decodeBuffer(sbRet.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 小端序返回，对应C#的 BitConverter.GetBytes(int)
     *
     * @param num 32位整数
     * @return 4字节byte数组
     */
    private static byte[] getBytes(int num) {
        byte[] result = new byte[4];
        result[3] = (byte) ((num >> 24) & 0xFF);
        result[2] = (byte) ((num >> 16) & 0xFF);
        result[1] = (byte) ((num >> 8) & 0xFF);
        result[0] = (byte) (num & 0xFF);
        return result;
    }

    /**
     * 对应C#的BitConverter.ToInt32
     *
     * @param bytes 4字节byte数组
     * @return 32位整数
     */
    public static int toInt32(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }
}
