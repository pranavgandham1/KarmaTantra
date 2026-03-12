package com.interviewprep;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // CREATE → ADMIN only
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Category addCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    // GET ALL → USER + ADMIN
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // GET BY ID → USER + ADMIN
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // UPDATE → ADMIN only
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Category updateCategory(@PathVariable Long id,
                                   @RequestBody Category updatedCategory) {

        Category category = categoryRepository.findById(id).orElse(null);

        if (category != null) {
            category.setName(updatedCategory.getName());
            return categoryRepository.save(category);
        }

        return null;
    }

    // DELETE → ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "Category Deleted Successfully";
    }
}