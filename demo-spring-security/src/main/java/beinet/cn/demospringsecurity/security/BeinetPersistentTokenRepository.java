package beinet.cn.demospringsecurity.security;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 记住我的仓储层
 * 默认使用 InMemoryTokenRepositoryImpl 实现
 */
public class BeinetPersistentTokenRepository implements PersistentTokenRepository {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 创建了新的Token之后，调用这个方法，
     * 我们可以用于存入Redis或数据库。
     * 注：生成的token，会以set-cookie的形式输出给前端
     *
     * @param token 生成的token
     */
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        // 这里写文件用于测试，也可以写入Redis或数据库，以便分布式访问用
        writeFile(token);
    }

    /**
     * 如果只有 remember-me 这个cookie时，并且getTokenForSeries成功返回数据，
     * 则会调用此方法，并下发一个新的 remember-me，重新更新有效期。
     * 但是如果一直持有 JSESSIONID，则不会调用此方法，
     * 即使反复刷新页面，到 remember-me过期，都不会更新remember-me
     *
     * @param series     token序号, 随机数的base64结果，在PersistentTokenBasedRememberMeServices.generateSeriesData生成
     * @param tokenValue token值, 随机数的base64结果，在PersistentTokenBasedRememberMeServices.generateTokenData生成
     * @param lastUsed   最后使用时间
     */
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentRememberMeToken token = readFile(series);
        if (token == null)
            return;
        // (String username, String series, String tokenValue, Date date)
        token = new PersistentRememberMeToken(token.getUsername(), series, tokenValue, lastUsed);
        writeFile(token);
    }

    /**
     * 根据序列号，返回token的方法。
     * 比如关闭浏览器后，新开
     *
     * @param seriesId token的序号
     * @return token
     */
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return readFile(seriesId);
    }

    /**
     * 移除token
     *
     * @param username 要移除的name
     */
    @Override
    public void removeUserTokens(String username) {
        // 根据username，去查找token并删除
        // 感觉不太合理，数据库还好，可以建索引，Redis或文件，就要遍历，或增加新的Redis key，否则效率太差。
        // 这里暂不处理
    }

    private static void writeFile(PersistentRememberMeToken token) {
        String username = token.getUsername();
        String tokenValue = token.getTokenValue();
        String series = token.getSeries();
        Date createTime = token.getDate();

        String str = String.format("%s,%s,%s,%s", username, series, tokenValue, format.format(createTime));
        String fileName = "d:\\" + series + ".txt";
        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str);
    }

    private static PersistentRememberMeToken readFile(String series) {
        String fileName = "d:\\" + series + ".txt";
        File file = new File(fileName);
        if (!file.exists())
            return null;

        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream reader = new FileInputStream(file)) {
            reader.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String[] content = new String(bytes).split(",");
        if (content.length != 4 || !content[1].equals(series))
            return null;

        Date date;
        try {
            date = format.parse(content[3]);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        // (String username, String series, String tokenValue, Date date)
        return new PersistentRememberMeToken(content[0], content[1], content[2], date);
    }
}
