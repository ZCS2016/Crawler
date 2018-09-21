package com.example.CrawlerServer.util.io.download;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PictureDownloadService {

    public static void downloadPicture(String urlStr, String fileName, String localDir){
        String filePathName = localDir + File.separator + fileName;
        File localFile = new File(filePathName);
        try {
            File dir = new File(localDir);
            if(!dir.exists()){
                dir.mkdirs();
            }

            if(!localFile.exists()){
                URL url=new URL(urlStr);
                FileUtils.copyURLToFile(url,localFile);
            }
        } catch (IOException e) {
            //System.out.println(urlStr+"\t"+"copyURLToFile failed!");
            PictureDownloadUtil.download(urlStr,filePathName);
        }
    }

}
