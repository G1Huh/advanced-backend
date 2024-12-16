package com.lion.demo.service;

import com.lion.demo.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {
    public static final int PAGE_SIZE = 10;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

}
