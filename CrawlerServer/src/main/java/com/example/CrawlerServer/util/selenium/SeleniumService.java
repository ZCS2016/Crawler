package com.example.CrawlerServer.util.selenium;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeleniumService {
    @Autowired
    private GenericObjectPool<WebDriver> pool;

    public WebDriver getDriver() {
        WebDriver driver = null;

        try {
            driver = pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return driver;
    }

    public void returnDriver(WebDriver driver){
        pool.returnObject(driver);
    }

}
