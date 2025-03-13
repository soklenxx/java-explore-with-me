package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.dto.create.CategoryCreateDto;
import ru.practicum.ewm.dto.response.CategoryResponseDto;
import ru.practicum.ewm.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryCreateDto categoryCreateDto);

    CategoryResponseDto toResponse(Category category);
}
