package com.ykdz.jdk8;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.Base64;

public class poiRead {
    private static final String fixedFolder = "D://mnt//data3//image//face_driver_id_recog//hoist//";
    
    public static void main(String[] args) {
        try {
            FileInputStream file = new FileInputStream(new File("D://mnt//data3//B14施工升降机人员信息.xlsx"));

            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0); // Assuming first sheet

            //行代表Row，单元格代表Cell

            for (Row row : sheet) {
                Cell cell = row.getCell(1);
                String lastPart = "";
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    //获取黑匣子号
                    String equipmentId = cell.getStringCellValue();
                    String[] parts = equipmentId.split("-");
                    if (parts.length > 1) {
                        lastPart = parts[parts.length - 1]; // 获取最后一个部分
                    }
                }

                Cell photoCell = row.getCell(5);
                if (photoCell != null && photoCell.getCellType() == CellType.STRING && cell.getStringCellValue().endsWith(".png")) {
                    XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                    for (XSSFShape shape : drawing.getShapes()) {
                        if (shape instanceof XSSFPicture) {
                            XSSFPicture picture = (XSSFPicture) shape;
                            ClientAnchor anchor = picture.getClientAnchor();
                            int pictureRowIndex = anchor.getRow1();
                            int pictureColumnIndex = anchor.getCol1();
                            if (pictureRowIndex == row.getRowNum() && pictureColumnIndex == cell.getColumnIndex()) {
                                byte[] pictureData = picture.getPictureData().getData();
                                String base64Encoded = Base64.getEncoder().encodeToString(pictureData);
                                // 创建新的.txt文件
                                File newFile = new File(fixedFolder+lastPart+"//"+lastPart + ".txt");
                                newFile.getParentFile().mkdirs();
                                newFile.createNewFile();


                                // 将Base64编码的数据写入文档中
                                try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                                    writer.write(base64Encoded);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

            workbook.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
