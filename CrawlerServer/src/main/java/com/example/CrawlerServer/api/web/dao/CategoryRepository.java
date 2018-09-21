package com.example.CrawlerServer.api.web.dao;

import com.example.CrawlerServer.api.web.entity.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category,String> {
}
