package com.lion.demo.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {
    @Autowired
    private RestTemplate restTemplate;

    public String fetchData(){
        String url = "https://jsonplaceholder.typicode.com/posts/1"; // json 데이터 요청
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // 콜백으로 응답 메시지를 처리
        return handleResponse(response);
    }

    private String handleResponse(ResponseEntity<String> response){
        if(response.getStatusCode().is2xxSuccessful()){ // 응답 성공 -> response 받기
            return "Data fetched successfully : " + response.getBody();
        }else{
            return "failed to fetch data";  // 응답 실패
        }

    }
}
