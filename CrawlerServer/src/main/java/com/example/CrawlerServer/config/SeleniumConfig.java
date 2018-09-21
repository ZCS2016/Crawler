package com.example.CrawlerServer.config;

import com.example.CrawlerServer.util.io.download.MultiThreadPictureDownloadService;
import com.example.CrawlerServer.util.selenium.SeleniumService;
import com.example.CrawlerServer.util.selenium.WebDriverFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {
    public static final int POOL_SIZE = 4;
    public static final int PICTURE_DOWNLOAD_THREAD_POOL_SIZE = 8;

    /////////////////////////////////////////////////////////////////////
    //Selenium WebDriver
    @Bean
    public WebDriverFactory getWebDriverFactory(){
        WebDriverFactory webDriverFactory = new WebDriverFactory();
        WebDriverFactory.createAndStartService();
        return webDriverFactory;
    }

    @Bean
    public GenericObjectPoolConfig getWebDriverPoolConfig(){
        GenericObjectPoolConfig webDriverPoolConfig = new GenericObjectPoolConfig();
        webDriverPoolConfig.setMaxTotal(POOL_SIZE);
        return webDriverPoolConfig;
    }

    @Bean
    public GenericObjectPool<WebDriver> getWebDriverPool(WebDriverFactory webDriverFactory, GenericObjectPoolConfig webDriverPoolConfig){
        GenericObjectPool<WebDriver> webDriverPool = new GenericObjectPool<WebDriver>(webDriverFactory,webDriverPoolConfig);
        return webDriverPool;
    }

    @Bean
    public SeleniumService getSeleniumService(){
        SeleniumService seleniumService = new SeleniumService();
        return seleniumService;
    }

    @Bean
    public MultiThreadPictureDownloadService getMultiThreadPictureDownloadService(){
        return new MultiThreadPictureDownloadService(PICTURE_DOWNLOAD_THREAD_POOL_SIZE);
    }

    /////////////////////////////////////////////////////////////////////
}
