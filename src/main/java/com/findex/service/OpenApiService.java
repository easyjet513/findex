package com.findex.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class OpenApiService {

    @Value("${openapi.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode fetchMarketIndexData(String idxNm, String beginBasDt) {
        try {
            String url = String.format(
                "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex?serviceKey=%s&resultType=json&numOfRows=100&pageNo=1",
                serviceKey
            );
            if (idxNm != null && !idxNm.isBlank()) {
                url += "&idxNm=" + idxNm;
            }
            if (beginBasDt != null && !beginBasDt.isBlank()) {
                url += "&beginBasDt=" + beginBasDt;
            }

            URI uri = URI.create(url);
            String response = restTemplate.getForObject(uri, String.class);

            JsonNode root = objectMapper.readTree(response);
            return root.path("response").path("body").path("items").path("item");
        } catch (Exception e) {
            throw new RuntimeException("공공데이터 API 호출 실패: " + e.getMessage(), e);
        }
    }
}
