package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.dto.resp.CategoryResp;
import com.example.ShopEcommerce.entity.Category;
import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.entity.ProductImage;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.repository.ProductImageRepository;
import com.example.ShopEcommerce.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ManagementController {
    private final UserService userService;
    private final ProductService productService;
    private final FileUploadService fileUploadService;
    private final CategoryService categoryService;
    private final ProductImageService productImageService;

    public ManagementController(UserService userService,
                                ProductService productService,
                                FileUploadService fileUploadService,
                                CategoryService categoryService, ProductImageService productImageService) {
        this.userService = userService;
        this.productService = productService;
        this.fileUploadService = fileUploadService;
        this.categoryService = categoryService;
        this.productImageService = productImageService;
    }

    @GetMapping("/productManagement")
    public String productManagement(@RequestParam(name = "keyword", required = false) String keyword,
                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                    @RequestParam(name = "size", defaultValue = "5") int size,
                                    Model model){

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productsPage = productService.searchProductPaginated(keyword, pageable);

        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "management/productManagement";

//        List<Product> products = productService.searchProducts(keyword);
//
//        model.addAttribute("products",products);
//        model.addAttribute("keyword", keyword);
//        return "management/productManagement";
    }
    @PostMapping("/productManagement/delete/{id}")
    @Transactional
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            Product product = productService.findById(id);

            if (product == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm với ID: " + id);
                return "redirect:/admin/productManagement";
            }

            productImageService.deleteByProductId(id);

            productService.deleteProduct(id);

            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }

        return "redirect:/admin/productManagement";
    }

    @GetMapping("/accountManagement")
    public String accountManagement(@RequestParam(name = "keyword", required = false) String keyword,
                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                    @RequestParam(name = "size", defaultValue = "5") int size,
                                    Model model) {
//        List<User> users = userService.searchUsers(keyword);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userService.searchUsersPaginated(keyword,keyword, pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "management/accountManagement";
//
//        model.addAttribute("users", users);
//        model.addAttribute("keyword", keyword);
//        return "management/accountManagement";
    }

    @GetMapping("/accountDetail/{id}")
    public String accountDetail(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/accountManagement";
        }
        model.addAttribute("user", user);
        return "management/accountDetail";
    }

    @GetMapping("/productDetail/{id}")
    public String productDetail(@PathVariable Long id, Model model){
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/productManagement";
        }
        model.addAttribute("product", product);
        return "management/productDetail";
    }

    @GetMapping("/addProduct")
    public String addProduct(Model model){
        List<CategoryResp> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "management/addProduct";
    }

    @PostMapping("/addProduct")
    public String saveProduct(@RequestParam("name") String name,
                              @RequestParam("category_id") int categoryId,
                              @RequestParam("description") String description,
                              @RequestParam("price") int price,
                              @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                              @RequestParam(value = "images", required = false) List<MultipartFile> images,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());

            Category category = categoryService.findCategoryById(categoryId);
            if (category == null) {
                throw new RuntimeException("Category không tồn tại");
            }
            product.setCategory(category);


            if (thumbnail != null && !thumbnail.isEmpty()) {
                String thumbnailFileName = fileUploadService.saveFile(thumbnail);
                product.setThumbnail(thumbnailFileName);
            }

            Product savedProduct = productService.saveProduct(product);

            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String imageFileName = fileUploadService.saveFile(image);

                        ProductImage productImage = new ProductImage();
                        productImage.setImageUrl(imageFileName);
                        productImage.setProduct(savedProduct);

                        productImageService.saveproductImage(productImage);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
            return "redirect:/admin/productManagement";

        } catch (Exception e) {
            List<CategoryResp> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
            return "management/addProduct";
        }
    }


    @GetMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable Long id, Model model){
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/updateProduct";
        }
        model.addAttribute("product", product);
        return "management/updateProduct";
    }

    @GetMapping("/logoutAdmin")
    public String logoutUser(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMsg", "Đăng xuất thành công!");
        return "redirect:/home";
    }
}

