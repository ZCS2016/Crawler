package com.example.CrawlerServer.api.web.controller;

import com.example.CrawlerServer.api.web.dao.WebsiteRepository;
import com.example.CrawlerServer.api.web.entity.Category;
import com.example.CrawlerServer.api.web.entity.Website;
import com.example.CrawlerServer.util.codec.SHAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/CrawlerServer/website")
public class WebsiteController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WebsiteRepository websiteRepository;

    @RequestMapping(value = "/{websiteName}",method = RequestMethod.POST)
    public Website add(@PathVariable String websiteName, String url){
        Website website = new Website();
        website.setName(websiteName);
        website.setUrl(url);
        Category category = new Category();
        category.setTitle(websiteName);
        category.setSrc(url);
        category.setHash(SHAUtil.getSHA256Str(websiteName));
        category.setLevel(0);
        category.setCount(0);
        website.setCategory(category);

        mongoTemplate.save(website);

        return website;
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<Website> all() {
        List<Website> websiteList = mongoTemplate.findAll(Website.class);

        return websiteList;
    }

}
