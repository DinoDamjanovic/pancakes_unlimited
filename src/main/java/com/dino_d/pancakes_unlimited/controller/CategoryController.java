package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.CategoryDto;
import com.dino_d.pancakes_unlimited.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }


    //  TODO Import validation for String and int as Path parameters to get this method working?
//    @GetMapping("/{name}")
//    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable(name = "name") String name) {
//        return new ResponseEntity<>(categoryService.getCategoryByName(name), HttpStatus.OK);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategoryById(@RequestBody CategoryDto categoryDto,
                                                          @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(categoryService.updateCategoryById(id, categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable(name = "id") long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>("Category with id " + id + " deleted successfully.", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }
}
