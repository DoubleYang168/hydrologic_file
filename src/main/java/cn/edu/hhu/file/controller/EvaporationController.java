package cn.edu.hhu.file.controller;

import cn.edu.hhu.file.service.EvaporationService;
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
@RequestMapping("/evaporation")
public class EvaporationController {

    @Autowired
    private EvaporationService evaporationService;

    @Value("${hehai.file.evaporation.rootdir}")
    private String evaporationLocation;

    @GetMapping("/helloSoil")
    public String hello(){
        return "Hello dingy, this is SoilFileController";
    }

    @GetMapping({ "/list/{dir}" })
    public Object getFileList(@PathVariable final String dir) {
        return this.evaporationService.listFile(dir);
    }

    @PostMapping({ "/upload/evaporation" })
    public Object uploadHjFile(@RequestParam final MultipartFile file) {
        final File uploadFile = this.evaporationService.uploadSoilFile(file, this.evaporationLocation);
        return uploadFile.getName();
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam final String dir, @RequestParam final String names, final HttpServletResponse response) {
        try {
            final List<String> files = Arrays.asList(names.split(","));
            final InputStream inputStream = this.evaporationService.downloadFiles(dir, files);
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
