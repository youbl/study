package cn.beinet.core.utils;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件/目录工具类
 *
 * @author youbl
 * @since 2023/12/1 10:39
 */
public final class FileHelper {
    private final static String SLASH = "/";

    /**
     * 读取上传文件的字符串内容
     * @param file 上传文件
     * @return 字符串内容
     */
    @SneakyThrows
    public static String readUploadStr(MultipartFile file) {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
//        StringBuilder sb = new StringBuilder();
//        try (InputStream inputStream = file.getInputStream()) {
//            int ch;
//            while ((ch = inputStream.read()) != -1) {
//                sb.append((char) ch);  // 读取中文会有问题
//            }
//        }
//        return sb.toString();
    }

    /**
     * 从指定的路径读取文件的文本内容
     *
     * @param filePath 路径
     * @return 文本内容
     */
    @SneakyThrows
    public static String readFile(String filePath) {
        byte[] encoded = Files.readAllBytes(Paths.get(filePath));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    /**
     * 把内容写入文件
     *
     * @param filePath  文件路径
     * @param content   内容
     * @param overwrite 文件存在时，是否覆盖，true覆盖，false追加在文件后面
     */
    @SneakyThrows
    public static void saveFile(String filePath, String content, boolean overwrite) {
        // 创建文件并写入内容（直接覆盖）
        File file = createParentDir(filePath);

        boolean append = !overwrite;
        try (FileWriter fw = new FileWriter(file, append);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(content);
        }
    }


    /**
     * 把内容写入文件
     *
     * @param filePath  文件路径
     * @param content   内容
     * @param overwrite 文件存在时，是否覆盖，true覆盖，false抛异常
     */
    @SneakyThrows
    public static void saveFile(String filePath, MultipartFile content, boolean overwrite) {
        // 创建文件并写入内容（直接覆盖）
        File file = createParentDir(filePath);
        if (file.exists()) {
            if (overwrite) {
                file.delete();
            } else {
                throw new RuntimeException("文件已存在");
            }
        }
        content.transferTo(file);
    }

    /**
     * 对传递的filename进行清理和格式化
     * @param filename 目录
     * @return 清理后的目录
     */
    public static String clearFileName(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        // 统一使用斜杠，不用反斜杠
        filename = filename.replace("\\", SLASH);

        // 路径里的上级目录字样替换掉，防止安全问题; 不考虑文件名以..结尾的场景
        String replace = ".." + SLASH;
        while (filename.contains(replace)) {
            // 使用while，避免这种输入 ....//
            filename = filename.replace(replace, "");
        }
        if (filename.endsWith("..")) {
            filename = filename.substring(0, filename.length() - 2);
        }
        if (filename.endsWith(SLASH)) {
            filename = filename.substring(0, filename.length() - SLASH.length());
        }

        return filename.trim();
    }

    /**
     * 对传递的dir进行清理和格式化
     * @param dir 目录
     * @return 清理后的目录
     */
    public static String clearDirName(String dir) {
        if (dir == null || dir.isEmpty()) {
            return "";
        }
        return clearFileName(dir) + SLASH;
    }

    /**
     * 判断并创建指定路径的父目录
     * @param filePath 路径
     * @return 文本内容
     */
    public static File createParentDir(String filePath) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }
}
