package com.example.CrawlerServer.util.io.download;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureDownloadUtil {

    public static void download(String url, String filePathName) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet(url);

            //伪装成google的爬虫JAVA问题查询
            httpget.setHeader("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
            // Execute HTTP request
            //System.out.println("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);

            File storeFile = new File(filePathName);
            FileOutputStream output = new FileOutputStream(storeFile);

            // 得到网络资源的字节数组,并写入文件
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    byte b[] = new byte[1024];
                    int j = 0;
                    while ((j = instream.read(b)) != -1) {
                        output.write(b, 0, j);
                    }
                    output.flush();
                    output.close();
                } catch (IOException ex) {
                    // In case of an IOException the connection will be released
                    // back to the connection manager automatically
                    throw ex;
                } catch (RuntimeException ex) {
                    // In case of an unexpected exception you may want to abort
                    // the HTTP request in order to shut down the underlying
                    // connection immediately.
                    httpget.abort();
                    throw ex;
                } finally {
                    // Closing the input stream will trigger connection release
                    try {
                        instream.close();
                    } catch (Exception ignore) {
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    public static boolean isImgUrlAvailable(String imgUrl){
        boolean available = true;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(imgUrl);
            HttpResponse response = httpclient.execute(httpget);
            Header[] headers = response.getHeaders("Content-Type");
            if(headers != null && headers.length == 1 ){
                if(headers[0].getValue().contains("image")){
                    available = true;
                }else{
                    available = false;
                }
            }else{
                System.out.println("Error: Unknown HttpHeaders " + headers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return available;
    }
}
