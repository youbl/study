package beinet.cn.frontstudy;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/4/26 16:29
 */
@RestController
public class UploadController {
    @PostMapping("/upload")
    public String upload(@RequestParam("fieldNameHere") MultipartFile file, @RequestParam(value = "fileName", required = false) String fileName) throws IOException {
        if (file == null || file.isEmpty()) {
            return "文件内容为空";
        }
        String savePath = "d:/" + fileName;
        file.transferTo(new File(savePath));
        return "OK:" + savePath;
    }

}
