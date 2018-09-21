package com.example.CrawlerServer.util.io.download;

import com.example.CrawlerServer.util.io.download.entity.PictureDownloadTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiThreadPictureDownloadService {
    public static final int DEFAULT_THREAD_POOL_SIZE = 4;
    public static final int DEFAULT_MIN_THREAD_POOL_SIZE = 1;
    public static final int DEFAULT_MAX_THREAD_POOL_SIZE = 8;
    private ExecutorService executorService;
    private LinkedBlockingQueue<PictureDownloadTask> pictureDownloadTaskQ = new LinkedBlockingQueue<>();

    public MultiThreadPictureDownloadService(){
        executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        init(DEFAULT_THREAD_POOL_SIZE);
    }

    public MultiThreadPictureDownloadService(int threadPoolSize){
        if(threadPoolSize >= DEFAULT_MIN_THREAD_POOL_SIZE && threadPoolSize <= DEFAULT_MAX_THREAD_POOL_SIZE) {
            executorService = Executors.newFixedThreadPool(threadPoolSize);
            init(threadPoolSize);
        }else {
            executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
            init(DEFAULT_THREAD_POOL_SIZE);
        }
    }

    private void init(int threadPoolSize){
        for(int i = 0; i < threadPoolSize; i++){
            PictureDownloadThread pictureDownloadThread = new PictureDownloadThread();
            pictureDownloadThread.setName(Thread.currentThread().getName()+" imgDownloadThread "+i);
            pictureDownloadThread.setPictureDownloadTaskQ(pictureDownloadTaskQ);
            executorService.submit(pictureDownloadThread);
        }
    }

    public void addPictureDownloadTask(PictureDownloadTask pictureDownloadTask){
        boolean success = pictureDownloadTaskQ.offer(pictureDownloadTask);
        if(!success){
            System.out.println("Error: Failed to addPictureDownloadTask "+pictureDownloadTask.getUrlStr());
        }
    }

}
