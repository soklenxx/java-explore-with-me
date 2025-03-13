package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.ewm.dto.create.CompilationCreateDto;
import ru.practicum.ewm.dto.response.CompilationResponseDto;
import ru.practicum.ewm.dto.update.CompilationUpdateDto;
import ru.practicum.ewm.entity.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    CompilationResponseDto toResponse(Compilation compilation);

    @Mapping(target = "events", ignore = true)
    Compilation toEntity(CompilationCreateDto compilationCreateDto);

    @Mapping(target = "events", ignore = true)
    Compilation toEntity(CompilationUpdateDto compilationUpdateDto, @MappingTarget Compilation compilation);
}
