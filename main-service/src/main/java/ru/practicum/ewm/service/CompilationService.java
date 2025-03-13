package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.create.CompilationCreateDto;
import ru.practicum.ewm.dto.update.CompilationUpdateDto;
import ru.practicum.ewm.entity.Compilation;

import java.util.List;

public interface CompilationService {
    List<Compilation> getEventsCompilations(Boolean pinned, int from, int size);

    Compilation getEventCompilationById(Long compId);

    Compilation createCompilation(CompilationCreateDto compilation);

    Compilation patchCompilation(CompilationUpdateDto compilationDto, long compId);

    void deleteCompilationById(long compId);


}
