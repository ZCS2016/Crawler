package com.example.CrawlerServer.util.io.download;

import com.example.CrawlerServer.util.io.download.entity.PictureDownloadTask;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.LinkedBlockingQueue;

public class PictureDownloadThread extends Thread {
    LinkedBlockingQueue<PictureDownloadTask> pictureDownloadTaskQ;

    @Override
    public void run() {
        while (true) {
            try {
                PictureDownloadTask pictureDownloadTask = pictureDownloadTaskQ.take();
                Instant start = Instant.now();
                PictureDownloadService.downloadPicture(pictureDownloadTask.getUrlStr(), pictureDownloadTask.getFileName(), pictureDownloadTask.getLocalDir());
                Instant end = Instant.now();
                Duration timeElapsed = Duration.between(start, end);
                long millis = timeElapsed.toMillis();
                System.out.println(Thread.currentThread().getName()+" Download Picture: "+pictureDownloadTask.getFileName()+" in "+millis+"ms");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+" Exit!");
                e.printStackTrace();
                break;
            }
        }
    }

    public LinkedBlockingQueue<PictureDownloadTask> getPictureDownloadTaskQ() {
        return pictureDownloadTaskQ;
    }

    public void setPictureDownloadTaskQ(LinkedBlockingQueue<PictureDownloadTask> pictureDownloadTaskQ) {
        this.pictureDownloadTaskQ = pictureDownloadTaskQ;
    }
}
