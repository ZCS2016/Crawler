package com.example.CrawlerServer.util.selenium;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory extends BasePooledObjectFactory<WebDriver> {
    private static ChromeDriverService service;

    public static void createAndStartService() {
        if(service == null) {
            service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File("D:/Java/Crawler/Selenium/chromedriver.exe"))
                    .usingAnyFreePort()
                    .build();
            try {
                service.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public WebDriver create() throws Exception {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> preferences = new HashMap<String, Object>();
        preferences.put("profile.default_content_setting_values.images",2);
        preferences.put("profile.default_content_setting_values.javascript",2);
        options.setExperimentalOption("prefs",preferences);
        WebDriver driver = new RemoteWebDriver(service.getUrl(), options);

//        HtmlUnitDriver driver = new HtmlUnitDriver();
//        driver.setDownloadImages(false);
//        driver.setJavascriptEnabled(false);
//        driver.setProxy("127.0.0.1",1080);
        return driver;
    }

    @Override
    public PooledObject<WebDriver> wrap(WebDriver webDriver) {
        return new DefaultPooledObject<WebDriver>(webDriver);
    }

}
