package ru.practicum.ewm.server.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.server.stats.entity.EndpointHit;
import ru.practicum.ewm.server.stats.entity.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("""
        SELECT new ru.practicum.ewm.server.stats.entity.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip))
        FROM EndpointHit h
        WHERE h.timestamp BETWEEN :start AND :end
        AND (:uris IS NULL OR h.uri IN :uris)
        GROUP BY h.app, h.uri
        ORDER BY COUNT(DISTINCT h.ip) DESC
    """)
    List<ViewStats> findDistinctViews(LocalDateTime start,
                                      LocalDateTime end,
                                      List<String> uris);

    @Query("""
        SELECT new ru.practicum.ewm.server.stats.entity.ViewStats(h.app, h.uri, COUNT(h.ip))
        FROM EndpointHit h
        WHERE h.timestamp BETWEEN :start AND :end
        AND (:uris IS NULL OR h.uri IN :uris)
        GROUP BY h.app, h.uri
        ORDER BY COUNT(h.ip) DESC
    """)
    List<ViewStats> findViews(LocalDateTime start,
                              LocalDateTime end,
                              List<String> uris);
}