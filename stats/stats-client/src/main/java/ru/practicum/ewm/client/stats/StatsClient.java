package ru.practicum.ewm.client.stats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.dto.stats.EndpointHitCreateDto;
import ru.practicum.ewm.dto.stats.ViewStatsResponseDto;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${stats-server.url}")
    private String serverUrl;

    public void addStats(EndpointHitCreateDto endpointHitCreateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EndpointHitCreateDto> requestEntity = new HttpEntity<>(endpointHitCreateDto, headers);
        restTemplate.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, Void.class);
    }

    public List<ViewStatsResponseDto> getStats(String start, String end, String uris, boolean unique) {
        String url = String.format("%s/stats?start=%s&end=%s&uris=%s&unique=%s",
                serverUrl, start, end, uris != null ? uris : "", unique);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return parseResponse(response);
    }

    private List<ViewStatsResponseDto> parseResponse(ResponseEntity<String> response) {
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return Collections.emptyList();
        }

        try {
            return List.of(objectMapper.readValue(response.getBody(), ViewStatsResponseDto[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON response: " + e.getMessage(), e);
        }
    }
}
