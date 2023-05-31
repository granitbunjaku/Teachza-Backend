package com.website.courses.services;

import com.website.courses.models.Category;
import com.website.courses.models.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public ResponseEntity<?> create(Category category)
    {
        Optional<Category> category1 = categoryRepository.findByName(category.getName());
        if(category1.isPresent())
        {
            return ResponseEntity.badRequest().body("Category already exists!");
        }

        return ResponseEntity.ok().body(categoryRepository.save(category));
    }


    public ResponseEntity<String> delete(int id)
    {
        Optional<Category> category = categoryRepository.findById(id);

        if(category.isPresent())
        {
            categoryRepository.delete(category.get());
            return ResponseEntity.ok().body("Category Deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category doesn't exist!");
        }
    }

    public List<Category> allCategories()
    {
        return categoryRepository.findAll();
    }

    public ResponseEntity<?> getCategory(@PathVariable("id") int id)
    {
        Optional<Category> category = categoryRepository.findById(id);

        if(category.isPresent())
        {
            return ResponseEntity.ok().body(category.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category doesn't exist!");
        }
    }

    public ResponseEntity<?> update(int id, @RequestBody Category category)
    {
        Optional<Category> oldCategory = categoryRepository.findById(id);

        if (oldCategory.isPresent()) {
            Category existingCategory = oldCategory.get();

            if (category.getName() != null && !category.getName().isEmpty()) {
                if (!category.getName().equals(existingCategory.getName())) {
                    existingCategory.setName(category.getName());
                    categoryRepository.save(existingCategory);
                } else {
                    return ResponseEntity.ok().body(existingCategory);
                }
            } else {
                return ResponseEntity.badRequest().body("Name field shouldn't be null");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category doesn't exist!");
    }

}
