package cn.edu.hhu.file.controller;

import cn.edu.hhu.file.service.RainfallService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/rainfull")
public class RainfallController {

    @Autowired
    private RainfallService rainfallService;

    @Value("${hehai.file.rainfall.hanjiang}")
    private String rainfullHanjiangLocation;
    @Value("${hehai.file.rainfall.huaihe}")
    private String rainfallHuaiheLocation;
    @Value("${hehai.file.rainfall.huaihebengbu}")
    private String rainfallHuaihebengbuLocation;
    @Value("${hehai.file.rainfall.weihe}")
    private String rainfallHeiheLocation;
    @Value("${hehai.file.rainfall.ziyahe}")
    private String rainfallZiyaheLocation;

    @GetMapping("/helloRainfall")
    public String hello() {
        return "Hello dingy, this is RainfallController";

    }

    @GetMapping({"/list/{dir}"})
    public Object getFileList(@PathVariable final String dir) {
        return this.rainfallService.listFile(dir);
    }

    @PostMapping({"/upload/hanjiang"})
    public Object uploadHjFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.rainfallService.uploadSoilFile(file, this.rainfullHanjiangLocation);
        return uploadFile.getName();
    }

    @PostMapping({"/upload/huaihe"})
    public Object uploadHhFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.rainfallService.uploadSoilFile(file, this.rainfallHuaiheLocation);
        return uploadFile.getName();
    }

    @PostMapping({"/upload/huaihebengbu"})
    public Object uploadHhbbFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.rainfallService.uploadSoilFile(file, this.rainfallHuaihebengbuLocation);
        return uploadFile.getName();
    }

    @PostMapping({"/upload/weihe"})
    public Object uploadWhFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.rainfallService.uploadSoilFile(file, this.rainfallHeiheLocation);
        return uploadFile.getName();
    }

    @PostMapping({"/upload/ziyahe"})
    public Object uploadZyhFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.rainfallService.uploadSoilFile(file, this.rainfallZiyaheLocation);
        return uploadFile.getName();
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam final String dir, @RequestParam final String names, final HttpServletResponse response) {
        try {
            final List<String> files = Arrays.asList(names.split(","));
            final InputStream inputStream = this.rainfallService.downloadFiles(dir, files);
            if (null != inputStream) {
                response.setContentType("application/force-download");
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + ((files.size() > 1) ? "\u5f52\u6863.zip" : files.get(0)));
                IOUtils.copy(inputStream, (OutputStream) response.getOutputStream());
            }
        } catch (IOException ex) {
        }
    }
}
