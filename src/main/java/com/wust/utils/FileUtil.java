package com.wust.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public String uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            logger.info("Upload Failed, Because this file is empty.");
            return "Upload Failed";
        }

        try {

            // 获取文件名
            String fileName = file.getOriginalFilename();
            logger.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            logger.info("上传的后缀名为：" + suffixName);
            // 文件上传后的路径
            String filePath = "E://image//Filedemo//";
            // 解决中文问题,liunx下中文路径,图片显示问题
            // fileName = UUID.randomUUID() + suffixName;

            File dest = new File(filePath + fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                //getParentFile()的作用是获得父目录
                logger.info("创建新的文件目录");
                dest.getParentFile().mkdirs();
                //mkdir()只能在已经存在的目录中创建创建文件夹。
                //mkdirs()可以在不存在的目录中创建文件夹。
            } else if (dest.exists() && !dest.isDirectory()) {

                logger.info("Upload Failed, Because the file exists");
                return "Upload Failed";

            }

            file.transferTo(dest);

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            logger.info("Upload Failed," + e.getMessage());
            return "Upload Failed";

        } catch (IOException e) {

            e.printStackTrace();
            logger.info("Upload Failed," + e.getMessage());
            return "Upload Failed";

        }
        return "Upload Success";

    }

    //多文件上传
    public String batchFiles(HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;

        int num = 0;
        for (int i = 0; i < files.size(); ++i) {

            file = files.get(i);
            num = i + 1;//用于记录上传第几个文件
            logger.info("----------Start To Upload the NO." + num + " File");
            // 判断当前文件上传结果
            String currState = uploadFile(file);
            if ("Upload Failed".equals(currState)) {

                // 某一文件上传失败则该多文件上传函数失败
                logger.info("----------You failed to upload the NO." + num + " File");
                return "Upload Failed";
            }
        }
        return "Upload Success";
    }


    //文件下载相关代码
    public String downloadFile(org.apache.catalina.servlet4preview.http.HttpServletRequest request, HttpServletResponse response) {
        String fileName = "FileUploadTests.java";
        if (fileName != null) {
            //当前是从该工程的WEB-INF//File//下获取文件(该目录可以在下面一行代码配置)然后下载到C:\\users\\downloads即本机的默认下载的目录
            String realPath = request.getServletContext().getRealPath(
                    "//WEB-INF//");
            File file = new File(realPath, fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

}
