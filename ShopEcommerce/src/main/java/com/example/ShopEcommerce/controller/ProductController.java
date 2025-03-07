package com.example.ShopEcommerce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ShopEcommerce.dto.resp.CategoryResp;
import com.example.ShopEcommerce.dto.resp.ProductResp;
import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.service.CategoryService;
import com.example.ShopEcommerce.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String getProducts(
        Model model,
        @RequestParam(required = false, defaultValue = "1") int categoryId,
        @RequestParam(required = false, defaultValue = "1") int page
    ) {
        // Gọi service để lấy danh sách sản phẩm
        Page<ProductResp> products = productService.getAllProductsByCategoryId(categoryId, page - 1, 5);
        List<CategoryResp> categories = categoryService.getAllCategories();
        int totalPages = products.getTotalPages();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("selectedCategoryId", categoryId);
        
        // Xử lý dữ liệu hoặc trả về view tương ứng
        return "shop/listItems"; // Trả về tên view để hiển thị danh sách sản phẩm
    }
    @GetMapping("/details")
    public String getProductDetail(@RequestParam int id, Model model) {
        ProductResp product = productService.getProductById(id);
        Map<String, Object> attributes = productService.getAttributesByProductId(id);
        model.addAttribute("product", product);
        model.addAttribute("attributes", attributes);
        return "shop/ItemDetails";
    }
    
}
