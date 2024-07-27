package com.ykdz.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class read {
    public static void main(String[] args) throws IOException {
        getFileContent();
    }

    public static void getFileContent() throws IOException {
        FileReader fr = new FileReader("D:\\software\\excel\\11.txt");
        BufferedReader br = new BufferedReader(fr);
        while(br.ready()){
            String s = br.readLine();
            String[] ret = s.split(",");
            for (int i = 0; i < ret.length; i++) {
                System.out.print("'"+ret[i]+"'"+',');
            }
//            System.out.println(br.readLine());
        }
    }
}
