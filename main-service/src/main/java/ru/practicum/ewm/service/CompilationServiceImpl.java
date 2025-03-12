package ru.practicum.ewm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.create.CompilationCreateDto;
import ru.practicum.ewm.dto.update.CompilationUpdateDto;
import ru.practicum.ewm.entity.Compilation;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;

import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public List<Compilation> getEventsCompilations(Boolean pinned, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new RuntimeException("Параметр from не может быть меньше 1");
        }
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return repository.findAll(page).toList();
    }

    @Override
    @Transactional
    public Compilation getEventCompilationById(Long compId) {
        return  repository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id " + compId + " not found."));
    }

    @Override
    @Transactional
    public Compilation createCompilation(CompilationCreateDto compilationCreateDto) {

        Compilation compilation = mapper.toEntity(compilationCreateDto);
        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }
        if (compilationCreateDto.events() != null) {
            Set<Event> eventSet = eventRepository.findByIdIn(compilationCreateDto.events());
            compilation.setEvents(eventSet);
        }
        return repository.save(compilation);
    }


    @Override
    @Transactional
    public Compilation patchCompilation(CompilationUpdateDto compilationDto, long compId) {
        Compilation compilationOld = getEventCompilationById(compId);
        Compilation compilation = mapper.toEntity(compilationDto, compilationOld);

        if (compilationDto.events() == null) {
            compilation.setEvents(compilationOld.getEvents());
        } else {
            Set<Event> eventSet = eventRepository.findByIdIn(compilationDto.events());
            compilation.setEvents(eventSet);
        }

        return repository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilationById(long compId) {
        if (!repository.existsById(compId)) {
            throw new NotFoundException("Compilation with id " + compId + " not found.");
        }
        repository.deleteById(compId);
    }
}
