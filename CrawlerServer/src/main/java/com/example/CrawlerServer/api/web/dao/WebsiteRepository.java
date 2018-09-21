package com.example.CrawlerServer.api.web.dao;

import com.example.CrawlerServer.api.web.entity.Website;
import org.springframework.data.repository.CrudRepository;

public interface WebsiteRepository extends CrudRepository<Website,String> {
}
