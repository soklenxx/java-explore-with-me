package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.create.CompilationCreateDto;
import ru.practicum.ewm.dto.response.CompilationResponseDto;
import ru.practicum.ewm.dto.update.CompilationUpdateDto;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

import static ru.practicum.ewm.controller.URIConstants.ADMIN_URI;
import static ru.practicum.ewm.controller.URIConstants.COMPILATIONS_ID_PARAM;
import static ru.practicum.ewm.controller.URIConstants.COMPILATIONS_URI;

@RestController
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService service;
    private final CompilationMapper mapper;

    @GetMapping(COMPILATIONS_URI)
    public List<CompilationResponseDto> getEventsCompilations(@RequestParam(required = false) Boolean pinned,
                                                              @RequestParam(defaultValue = "0") int from,
                                                              @RequestParam(defaultValue = "10") int size) {
        return service.getEventsCompilations(pinned, from, size).stream().map(mapper::toResponse).toList();
    }

    @GetMapping(COMPILATIONS_URI + COMPILATIONS_ID_PARAM)
    public CompilationResponseDto getEventCompilationById(@PathVariable Long compId) {
        return mapper.toResponse(service.getEventCompilationById(compId));
    }

    @PostMapping(ADMIN_URI + COMPILATIONS_URI)
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto createCompilation(@RequestBody @Valid CompilationCreateDto compilationCreateDto) {
        return mapper.toResponse(service.createCompilation(compilationCreateDto));
    }

    @PatchMapping(ADMIN_URI + COMPILATIONS_URI + COMPILATIONS_ID_PARAM)
    public CompilationResponseDto patchCompilation(@RequestBody @Valid CompilationUpdateDto compilationUpdateDto,
                                                   @PathVariable Long compId) {
        return mapper.toResponse(service.patchCompilation(compilationUpdateDto, compId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(ADMIN_URI + COMPILATIONS_URI + COMPILATIONS_ID_PARAM)
    public void deleteCompilationById(@PathVariable long compId) {
        service.deleteCompilationById(compId);

    }
}
