package com.example.CrawlerServer.api.web.entity;

import org.springframework.data.annotation.Id;

public class Website {
    @Id
    private String name;
    private String url;
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
