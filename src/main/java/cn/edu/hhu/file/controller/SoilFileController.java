package cn.edu.hhu.file.controller;

import cn.edu.hhu.file.dto.FileDTO;
import cn.edu.hhu.file.service.SoilFileService;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/soil")
public class SoilFileController {

    @Autowired
    private SoilFileService soilFileService;

    @Value("${hehai.file.soil.hanjiang}")
    private String soilHanjiangLocation;
    @Value("${hehai.file.soil.huaihe}")
    private String soilHuaiheLocation;
    @Value("${hehai.file.soil.huaihebengbu}")
    private String soilHuaihebengbuLocation;
    @Value("${hehai.file.soil.weihe}")
    private String soilHeiheLocation;
    @Value("${hehai.file.soil.ziyahe}")
    private String soilZiyaheLocation;
    @Value("${spring.servlet.multipart.location}")
    private String rootLocation;

    @GetMapping("/helloSoil")
    public String hello(){
        return "Hello dingy, this is SoilFileController";
    }

    @GetMapping({ "/list/{dir}" })
    public Object getFileList(@PathVariable final String dir) {
        return this.soilFileService.listFile(dir);
    }

    @PostMapping({ "/upload/hanjiang" })
    public Object uploadHjFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.soilFileService.uploadSoilFile(file, this.soilHanjiangLocation);
        return uploadFile.getName();
    }

    @PostMapping({ "/upload/huaihe" })
    public Object uploadHhFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.soilFileService.uploadSoilFile(file, this.soilHuaiheLocation);
        return uploadFile.getName();
    }

    @PostMapping({ "/upload/huaihebengbu" })
    public Object uploadHhbbFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.soilFileService.uploadSoilFile(file, this.soilHuaihebengbuLocation);
        return uploadFile.getName();
    }

    @PostMapping({ "/upload/weihe" })
    public Object uploadWhFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.soilFileService.uploadSoilFile(file, this.soilHeiheLocation);
        return uploadFile.getName();
    }

    @PostMapping({ "/upload/ziyahe" })
    public Object uploadZyhFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.soilFileService.uploadSoilFile(file, this.soilZiyaheLocation);
        return uploadFile.getName();
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam final String dir, @RequestParam final String names, final HttpServletResponse response) {
        try {
            final List<String> files = Arrays.asList(names.split(","));
            final InputStream inputStream = this.soilFileService.downloadFiles(dir, files);
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
