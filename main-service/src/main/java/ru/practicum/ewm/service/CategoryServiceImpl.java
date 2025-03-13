package ru.practicum.ewm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Category addCategory(Category category) {
        checkCategoryAlreadyExists(category);
        return repository.save(category);
    }

    @Override
    @Transactional
    public List<Category> getCategories(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new RuntimeException("Параметр from не может быть меньше 1");
        }
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return repository.findAllCategories(page);
    }

    @Override
    @Transactional
    public Category getCategoryById(long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Категория с id %s не найдена".formatted(id)));
    }

    @Override
    @Transactional
    public void deleteCategoryById(long id) {
        Category category = getCategoryById(id);
        checkCategoryThisEvents(category);
        repository.deleteById(category.getId());
    }

    @Override
    @Transactional
    public Category patchCategoryById(Category category, long id) {
        Category oldCategory = getCategoryById(id);
        category.setId(oldCategory.getId());
        checkCategoryAlreadyExists(category);
        oldCategory.setName(category.getName());
        return repository.save(oldCategory);
    }

    private void checkCategoryAlreadyExists(Category category) {
        Optional<Category> optionalCategory = repository.findByName(category.getName());
        if (optionalCategory.isPresent() && optionalCategory.get().getId() != category.getId()) {
            throw new ConflictException("Category this name already exists");
        }
    }

    private void checkCategoryThisEvents(Category category) {
        List<Event> events = eventRepository.findByCategory(category);
        if (!events.isEmpty()) {
            throw new ConflictException("This category have events");
        }
    }
}
