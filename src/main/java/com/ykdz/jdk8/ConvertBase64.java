package com.ykdz.jdk8;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ConvertBase64 {
    public static void main(String[] args) throws IOException {
        File file = new File("D://mnt//data3//image//face_driver_id_recog//crane//10270012//10270012.jpg");
        String base64String = convertFileToBase64(file);

        // 获取照片名（不包括扩展名）
        String photoName = file.getName().substring(0, file.getName().lastIndexOf('.'));

        // 指定固定文件夹
        String fixedFolder = "D://mnt//data3//image//face_driver_id_recog//crane//10270012//";

        // 创建新的.txt文件
        File newFile = new File(fixedFolder+photoName + ".txt");
        newFile.getParentFile().mkdirs();
        newFile.createNewFile();

        // 将Base64编码的数据写入文档中
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
            writer.write(base64String);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String convertFileToBase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(file.getPath()));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
