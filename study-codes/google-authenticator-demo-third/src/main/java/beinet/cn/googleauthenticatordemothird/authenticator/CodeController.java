package beinet.cn.googleauthenticatordemothird.authenticator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Google Authenticator身份验证器接入步骤：
 * 1、手机上安装Google Authenticator
 * 苹果：https://apps.apple.com/cn/app/google-authenticator/id388497605
 * 安卓：https://apkpure.com/cn/
 * 2、调用接口： /generate/{username}
 * - 为该用户分配一个secretKey，进行绑定（写入数据库，如果用户的secretKey存在时，不能再绑定第2个）
 * - 生成Google Authenticator所需的二维码
 * 3、使用手机上的Google Authenticator，点+加号，扫描上面的二维码，会自动绑定
 * 4、给用户提供验证码输入框，用户输入后，根据用户名和验证码，调用接口进行验证：/validate/key
 * 接口返回true表示验证通过
 *
 * @author youbl
 * @since 2023/5/30 13:40
 */
@RestController
@RequiredArgsConstructor
public class CodeController {
    private final GoogleAuthenticator gAuth;

    /**
     * 初始化密钥，
     * 生成一个二维码
     *
     * @param username 用户名
     * @param response 响应上下文
     */
    @SneakyThrows
    @GetMapping("/generate/{username}")
    public void generate(@PathVariable String username, HttpServletResponse response) {
        // 为指定用户，生成secretKey，并存盘
        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);

        // 使用生成的数据，拼接成google身份验证器所需的url
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("beinet.cn", username, key);
        System.out.println(otpAuthURL);

        // 下面是绘制二维码，并输出到响应流
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //边框距
        hints.put(EncodeHintType.MARGIN, 1);

        //I've decided to generate QRCode on backend site
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 400, 400, hints);

        //Simple writing to outputstream
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        }
    }

    @GetMapping("/validate/key")
    public boolean validateKey2(@RequestParam String username, @RequestParam int code) {
        return gAuth.authorizeUser(username, code);
    }

}
