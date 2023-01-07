package com.example.date_scheduling.util;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class FileUploadUtil {

    public static String makeUploadDirectory(String uploadRootPath){
        // 오늘 년,월,일 정보 가져오기
        LocalDateTime now = LocalDateTime.now();
        int y = now.getYear();
        int m = now.getMonthValue();
        int d = now.getDayOfMonth();

        //폴더 생성
        String[] dateInfo = {
                String.valueOf(y)
                ,len2(m)
                ,len2(d)
        };

        String newUploadPath = uploadRootPath;

        // File.separator : 운영체제에 맞는 디렉토리 경로구분문자를 생성
        // 리눅스 : / ,  윈도우 : \
        for (String date : dateInfo) {
            newUploadPath += File.separator + date;

            // 해당 경로대로 폴더를 생성
            File dirName = new File(newUploadPath);
            if (!dirName.exists()) dirName.mkdir();
        }

        return newUploadPath;
    }

    private static String len2(int n) { //숫자 하나를 받아 두 자릿수로 만들어 준다. ex: 8이 들어오면 08이된다
        return new DecimalFormat("00").format(n);
    }
}
