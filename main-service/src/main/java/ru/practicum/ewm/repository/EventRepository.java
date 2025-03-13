package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.EventState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findEventsByInitiatorId(long userId, Pageable page);

    @Query("SELECT e FROM Event e " +
            "WHERE (:states IS NULL OR e.state IN :states) " +
            "AND (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:categories IS NULL OR e.category.id IN :categories)"
    )
    List<Event> findAllByStatesAndUsersAndCategories(
           List<EventState> states,
           List<Long> users,
           List<Long> categories,
           Pageable pageable);

    List<Event> findByCategory(Category category);

    Set<Event> findByIdIn(Set<Long> ids);

    Optional<Event> findByIdAndInitiatorId(long eventId, long userId);
}
