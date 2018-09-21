package com.example.CrawlerServer.crawler;

import com.example.CrawlerServer.api.web.entity.Category;
import com.example.CrawlerServer.api.web.entity.Website;
import com.example.CrawlerServer.util.codec.SHAUtil;
import com.example.CrawlerServer.util.selenium.SeleniumService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class CategoryCrawler {
    @Autowired
    private SeleniumService seleniumService;

    public void getCategoryList(Website website){
        //level 1
        getCategoryListLevel1(website);

        //level 2
        getCategoryListLevel2(website);
    }

    //level 1
    public void getCategoryListLevel1(Website website){
        List<Category> categoryList = new ArrayList<>();
        final String baseUrl = website.getCategory().getSrc();
        WebDriver driver = seleniumService.getDriver();
        driver.get(baseUrl);

        List<WebElement> categoriesElementList = driver.findElements(By.xpath("//ul[@class='side-panel categories']/li"));

        for(WebElement categoriesElement:categoriesElementList){
            WebElement aElement = categoriesElement.findElement(By.tagName("a"));
            WebElement smallElement = categoriesElement.findElement(By.tagName("small"));

            String title = aElement.getText();
            String src = aElement.getAttribute("href");
            String countStr = smallElement.getText().substring(1,smallElement.getText().length()-1);
            Integer count = Integer.parseInt(countStr);
            Integer level = 1;
            String hash = SHAUtil.getSHA256Str(src);

            Category categories = new Category();
            categories.setTitle(title);
            categories.setSrc(src);
            categories.setCount(count);
            categories.setLevel(level);
            categories.setHash(hash);

            categoryList.add(categories);
        }

        website.getCategory().setChildrenCategories(categoryList);

        seleniumService.returnDriver(driver);
    }

    //level 2
    public void getCategoryListLevel2(Website website){
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for(Category category:website.getCategory().getChildrenCategories()){
            executorService.submit(new getPageCategoryListLevel2Thread(category));
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(1,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class getPageCategoryListLevel2Thread implements Runnable{
        private Category category;

        public getPageCategoryListLevel2Thread(Category category) {
            this.category = category;
        }

        @Override
        public void run() {
            getPageCategoryListLevel2();
        }

        public void getPageCategoryListLevel2(){
            List<Category> categoriesList = new ArrayList<>();

            WebDriver driver = seleniumService.getDriver();
            driver.get(category.getSrc());

            //Update parent category.img
            List<WebElement> imgElementList = driver.findElements(By.className("thumb_img"));
            if(imgElementList.size()>0){
                WebElement imgElement = imgElementList.get(0);
                String imgSrc = imgElement.getAttribute("src");
                category.setImg(imgSrc);
            }

            List<WebElement> categoriesElementList = driver.findElements(By.xpath("//ul[@class='side-panel categories']/li[@style='padding-left:5px;']"));
            for(WebElement categoriesElement:categoriesElementList){
                WebElement aElement = categoriesElement.findElement(By.tagName("a"));
                WebElement smallElement = categoriesElement.findElement(By.tagName("small"));

                String title = aElement.getText();
                String src = aElement.getAttribute("href");
                String countStr = smallElement.getText().substring(1,smallElement.getText().length()-1);
                Integer count = Integer.parseInt(countStr);
                Integer level = 2;
                String hash = SHAUtil.getSHA256Str(src);

                Category categories = new Category();
                categories.setTitle(title);
                categories.setSrc(src);
                categories.setCount(count);
                categories.setLevel(level);
                categories.setHash(hash);
                categoriesList.add(categories);
            }
            category.setChildrenCategories(categoriesList);

            //Update total
            if(categoriesElementList.isEmpty()){
                //Update parent category.total
                List<WebElement> paginationList = driver.findElements(By.className("pagination"));

                if(paginationList.size()>1) {
                    WebElement paginationElement = paginationList.get(1);
                    List<WebElement> paginationElementList = paginationElement.findElements(By.tagName("a"));
                    if (paginationElementList.size() > 1 && paginationElementList.get(paginationElementList.size() - 1).getText().equals("Next »")) {
                        WebElement totalElement = paginationElementList.get(paginationElementList.size() - 2);
                        String totalStr = totalElement.getText();
                        Integer total = Integer.parseInt(totalStr);
                        category.setCount(total);
                    }
                }else{
                    WebElement paginationElement = paginationList.get(0);
                    List<WebElement> paginationElementList = paginationElement.findElements(By.tagName("a"));
                    if (paginationElementList.size() > 1 && paginationElementList.get(paginationElementList.size() - 1).getText().equals("Next »")) {
                        WebElement totalElement = paginationElementList.get(paginationElementList.size() - 2);
                        String totalStr = totalElement.getText();
                        Integer total = Integer.parseInt(totalStr);
                        category.setCount(total);
                    } else {
                        paginationElementList = paginationElement.findElements(By.tagName("span"));
                        if (paginationElementList.size() > 1 && paginationElementList.get(paginationElementList.size() - 1).getText().equals("Next »")) {
                            WebElement totalElement = paginationElementList.get(paginationElementList.size() - 2);
                            String totalStr = totalElement.getText();
                            Integer total = Integer.parseInt(totalStr);
                            category.setCount(total);
                        }
                    }
                }
            }

            seleniumService.returnDriver(driver);
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }
    }
}
