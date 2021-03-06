package cn.edu.hhu.file.service;

import cn.edu.hhu.file.dto.FileDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class RainfallService {

    @Value("${hehai.file.rainfall.rootdir}")
    private String rainfullRootDirectory;

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
    @Value("${spring.servlet.multipart.location}")
    private String rootLocation;

    public List<FileDTO> listFile(final String dir) {
        final List<FileDTO> fileDTOList = new ArrayList<FileDTO>();
        if (this.rainfullHanjiangLocation.equals(dir) || this.rainfallHuaiheLocation.equals(dir) || this.rainfallHuaihebengbuLocation.equals(dir) || this.rainfallHeiheLocation.equals(dir) || this.rainfallZiyaheLocation.equals(dir)) {
            final String dst = Paths.get(this.rootLocation, this.rainfullRootDirectory, dir).toString();
            final File dstDir = Paths.get(dst, new String[0]).toFile();
            final File[] listFiles;
            final File[] files = listFiles = dstDir.listFiles();
            for (final File file : listFiles) {
                final FileDTO fileDTO = new FileDTO();
                fileDTO.setName(file.getName());
                fileDTO.setLastModified(Long.valueOf(file.lastModified()));
                fileDTO.setSize(Long.valueOf(file.length()));
                fileDTOList.add(fileDTO);
            }
        }
//        fileDTOList.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)FileDTO::getLastModified).reversed());
        return fileDTOList;
    }

    public File uploadSoilFile(final MultipartFile file, final String dir) {
        return this.uplaodHeihaiFile(file, dir);
    }

    public InputStream downloadFiles(final String dir, final List<String> names) throws IOException {
        if (names.size() <= 1) {
            final String fileDst = Paths.get(this.rootLocation, this.rainfullRootDirectory, dir, names.get(0)).toString();
            final File file = new File(fileDst);
            return new FileInputStream(file);
        }
        final List<File> fileList = new ArrayList<File>();
        for (final String name : names) {
            final String fileDst2 = Paths.get(this.rootLocation, this.rainfullRootDirectory, dir, name).toString();
            final File file2 = new File(fileDst2);
            fileList.add(file2);
        }
        final String zipFilename = "\u5f52\u6863.zip";
        final String zipFilePath = this.fileToZip(fileList, zipFilename);
        return new FileInputStream(zipFilePath);
    }

    private String fileToZip(final List<File> list, final String fileName) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        final String path = fileName;
        try {
            final File zipFile = new File(path);
            System.out.println(zipFile.getAbsolutePath());
            zipFile.deleteOnExit();
            zipFile.createNewFile();
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            final byte[] bufs = new byte[10240];
            for (final File file : list) {
                final File subFile = new File(file.getPath());
                if (!subFile.exists()) {
                    continue;
                }
                final ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                fis = new FileInputStream(subFile);
                bis = new BufferedInputStream(fis, 10240);
                int read = 0;
                while ((read = bis.read(bufs, 0, 10240)) != -1) {
                    zos.write(bufs, 0, read);
                }
            }
            System.out.println("\u538b\u7f29\u6210\u529f");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != zos) {
                    zos.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return path;
    }

    private File uplaodHeihaiFile(final MultipartFile file, final String dir) {
        File thumbnailFile = null;
        if (null != file) {
            final String dst = Paths.get(this.rootLocation, this.rainfullRootDirectory, dir).toString();
            try {
                thumbnailFile = this.uploadFile(file, dst);
            } catch (Exception ex) {
            }
        }
        return thumbnailFile;
    }

    private File uploadFile(final MultipartFile multipartFile, final String dst) throws IOException {
        final String originalFilename = multipartFile.getOriginalFilename();
        final Path saveFullPath = Paths.get(dst, originalFilename);
        final File dstDir = Paths.get(dst, new String[0]).toFile();
        if (!dstDir.exists()) {
            final boolean flag = dstDir.mkdirs();
            if (!flag) {
                throw new IOException();
            }
        }
        final File file = saveFullPath.toFile();
        multipartFile.transferTo(file);
        return file;
    }

    private String getSaveFileName(final String originalFilename) {
        final int lastIndex = originalFilename.lastIndexOf(".");
        return UUID.randomUUID().toString() + ((lastIndex > -1) ? originalFilename.substring(lastIndex) : "");
    }
}
