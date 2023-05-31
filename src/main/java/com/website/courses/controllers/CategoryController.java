package com.website.courses.controllers;

import com.website.courses.models.Category;
import com.website.courses.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category)
    {
        if (category.getName() == null)
        {
            return ResponseEntity.badRequest().body("Name field shouldn't be null");
        }

        return categoryService.create(category);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Category category)
    {
        return categoryService.update(id, category);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id)
    {
        return categoryService.delete(id);
    }

    @GetMapping
    public ResponseEntity<List<Category>> allCategories()
    {
        return ResponseEntity.ok().body(categoryService.allCategories());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") int id)
    {
        return categoryService.getCategory(id);
    }

}