package dev.commerce.services.impl;

import dev.commerce.dtos.request.CategoryRequest;
import dev.commerce.dtos.response.CategoryResponse;
import dev.commerce.entitys.Category;
import dev.commerce.exception.ResourceNotFoundException;
import dev.commerce.mappers.CategoryMapper;
import dev.commerce.repositories.jpa.CategoryRepository;
import dev.commerce.services.CategoryService;
import dev.commerce.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuthenticationUtils utils;

    @Override
    public void deleteCategoryById(UUID categoryId) {
        Category cate = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        cate.setActive(false);
        categoryRepository.save(cate);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(String name, int page, int size, String sortBy, String sortDir) {
        Specification<Category> spec = (root,query,cr) -> cr.isTrue(root.get("isActive"));
        if(name != null && !name.isEmpty()){
        spec = spec.and((root,query,cr) -> cr.like(cr.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return categoryRepository.findAll(spec,pageable)
                .map(categoryMapper::entityToDto);
    }

    @Override
    public CategoryResponse getCategoryById(UUID categoryId) {
        Category cate = categoryRepository.findById(categoryId)
                .filter(Category::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return categoryMapper.entityToDto(cate);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category cate = categoryMapper.dtoToEntity(request);
        cate.setCreatedBy(utils.getCurrentUserId());
        cate.setActive(true);
        Category savedCate = categoryRepository.save(cate);
        return categoryMapper.entityToDto(savedCate);
    }

    @Override
    public CategoryResponse updateCategory(UUID categoryId, CategoryRequest request) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .filter(Category::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        existingCategory.setName(request.getName());
        existingCategory.setSlug(request.getSlug() != null ? request.getSlug() : slugify(request.getName()));
        existingCategory.setUpdatedBy(utils.getCurrentUserId());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.entityToDto(updatedCategory);
    }

    private String slugify(String input) {
        return input.toLowerCase().trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

}
