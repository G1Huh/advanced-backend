package com.lion.demo.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Map;

// 맛집정보.csv
@Document(indexName = "restaurant")   // ElasticSearch
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    private String id;
    // 상호명
    private String name;
    // 식당 설명
    private String intro;
    private String imgSrc;
    // 업소 정보 -> Map
    @Field(type = FieldType.Object)
    private Map<String, Object> info;
    // 리뷰 -> List
    @Field(type=FieldType.Nested)
    private List<Map<String, Object>> reviews;
}
