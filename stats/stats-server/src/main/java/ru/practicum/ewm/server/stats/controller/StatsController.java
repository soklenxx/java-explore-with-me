package ru.practicum.ewm.server.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.stats.EndpointHitCreateDto;
import ru.practicum.ewm.dto.stats.ViewStatsResponseDto;
import ru.practicum.ewm.server.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.dto.stats.ConstantDate.DATE;


@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody EndpointHitCreateDto endpointHitCreateDto) {
        statsService.saveHit(endpointHitCreateDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsResponseDto> getStats(@DateTimeFormat(pattern = DATE) @RequestParam LocalDateTime start,
                                               @DateTimeFormat(pattern = DATE) @RequestParam LocalDateTime end,
                                               @RequestParam(required = false) List<String> uris,
                                               @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}