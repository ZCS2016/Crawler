package com.example.CrawlerServer.api.web.controller;

import com.example.CrawlerServer.api.web.dao.CategoryRepository;
import com.example.CrawlerServer.api.web.dao.WebsiteRepository;
import com.example.CrawlerServer.api.web.entity.Category;
import com.example.CrawlerServer.api.web.entity.Website;
import com.example.CrawlerServer.crawler.CategoryCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/CrawlerServer/crawler")
public class CrawlerController {
    @Autowired
    private WebsiteRepository websiteRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    CategoryCrawler categoryCrawler;

    @RequestMapping(value = "/crawlerCategory/{websiteName}" ,method = RequestMethod.POST)
    public String crawlerCategory(@PathVariable String websiteName){
        Optional<Website> websiteOptional = websiteRepository.findById(websiteName);
        if(websiteOptional.isPresent()) {
            Website website = websiteOptional.get();
            categoryCrawler.getCategoryList(website);
            websiteRepository.save(website);
        }

        return "OK";
    }

}
