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
import ru.practicum.ewm.dto.create.CategoryCreateDto;
import ru.practicum.ewm.dto.response.CategoryResponseDto;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;

import static ru.practicum.ewm.controller.URIConstants.CATEGORIES_ADMIN_URI;
import static ru.practicum.ewm.controller.URIConstants.CATEGORIES_URI;
import static ru.practicum.ewm.controller.URIConstants.ID_PARAM;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;
    private final CategoryMapper mapper;

    @PostMapping(CATEGORIES_ADMIN_URI)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryCreateDto categoryCreateDto) {
        return mapper.toResponse(service.addCategory(mapper.toEntity(categoryCreateDto)));
    }

    @GetMapping(CATEGORIES_URI)
    public List<CategoryResponseDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        return service.getCategories(from, size).stream().map(mapper::toResponse).toList();
    }

    @GetMapping(CATEGORIES_URI + ID_PARAM)
    public CategoryResponseDto getCategoryById(@PathVariable long id) {
        return mapper.toResponse(service.getCategoryById(id));
    }

    @PatchMapping(CATEGORIES_ADMIN_URI + ID_PARAM)
    public CategoryResponseDto updateCategoryById(@RequestBody @Valid CategoryCreateDto categoryDto,
                                                  @PathVariable long id) {
        return mapper.toResponse(service.patchCategoryById(mapper.toEntity(categoryDto), id));
    }

    @DeleteMapping(CATEGORIES_ADMIN_URI + ID_PARAM)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable long id) {
        service.deleteCategoryById(id);
    }
}
