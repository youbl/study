package beinet.cn.frontstudy;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/4/26 16:29
 */
@RestController
public class UploadController {
    @PostMapping("/upload")
    @SneakyThrows
    public String upload(@RequestParam("fieldNameHere") MultipartFile file,
                         @RequestParam(value = "fileName", required = false) String fileName,
                         @RequestParam(value = "file_type", required = false) String fileType,
                         @RequestParam(value = "otherVar", required = false) String otherVar) {
        System.out.println("收到的变量值: fileName=" + fileName + "; file_type=" + fileType + "; otherVar=" + otherVar);
        if (file == null || file.isEmpty()) {
            return "文件内容为空";
        }
        String savePath = "d:/" + fileName;
        file.transferTo(new File(savePath));
        return "OK:" + savePath;
    }

}
