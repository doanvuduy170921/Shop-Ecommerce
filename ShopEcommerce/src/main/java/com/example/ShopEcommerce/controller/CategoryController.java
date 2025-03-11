package com.example.ShopEcommerce.controller;


import com.example.ShopEcommerce.entity.Category;
import com.example.ShopEcommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy danh sách tất cả danh mục và hiển thị lên giao diện
    @GetMapping("/all")
    public String getCategory(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/addProduct"; // Trang Thymeleaf để hiển thị danh sách
    }

    // Hiển thị form thêm danh mục
    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-form"; // Trang form để nhập thông tin danh mục mới
    }

    // Xử lý thêm danh mục mới
    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") Category category) {
        categoryRepository.save(category);
        return "redirect:/category/all"; // Quay lại danh sách sau khi thêm thành công
    }

    // Hiển thị form cập nhật danh mục
    @GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable int id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + id));
        model.addAttribute("category", category);
        return "category-form"; // Dùng lại trang form cho cập nhật
    }

    // Xử lý cập nhật danh mục
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable int id, @ModelAttribute("category") Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + id));

        category.setName(categoryDetails.getName());
        categoryRepository.save(category);
        return "redirect:/category/all"; // Quay lại danh sách sau khi cập nhật thành công
    }

    // Xóa danh mục
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + id));

        categoryRepository.delete(category);
        return "redirect:/category/all"; // Quay lại danh sách sau khi xóa
    }
}
