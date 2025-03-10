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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/toggleUserStatus/{id}")
    public String toggleUserStatus(@PathVariable Long id) {
        return userService.toggleUserStatus(id) ? "success" : "error";
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

            if (product.getThumbnail() != null && !product.getThumbnail().isEmpty()) {
                fileUploadService.deleteFile(product.getThumbnail());
            }

            // Lấy danh sách ảnh của sản phẩm trước khi xóa
            List<ProductImage> productImages = productImageService.getImagesByProductId(id);

            // Xóa file ảnh từ ổ D:/upload
            for (ProductImage image : productImages) {
                fileUploadService.deleteFile(image.getImageUrl());
            }

            productImageService.deleteByProductId(id);

            productService.deleteProduct(id);

            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }

        return "redirect:/admin/productManagement";
    }

    @PostMapping("/deleteProductImage/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteProductImage(@PathVariable Long id) {
        try {
            // Lấy thông tin ảnh
            ProductImage image = productImageService.findById(id);
            if (image == null) {
                return ResponseEntity.status(404).body("Không tìm thấy ảnh");
            }

            // Xóa file ảnh từ thư mục storage
            fileUploadService.deleteFile(image.getImageUrl());

            // Xóa record trong database
            productImageService.deleteByProductId(id);

            return ResponseEntity.ok("Xóa ảnh thành công");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi xóa ảnh: " + e.getMessage());
        }
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
                              @RequestParam(value = "images", required = false) MultipartFile[] images,
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

            if (images != null && images.length != 0) {
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
    public String updateProduct(@PathVariable("id") Long id, Model model){
        Product product = productService.findById(id);
        List<CategoryResp> categories = categoryService.getAllCategories();

        if (product == null) {
            return "redirect:/admin/updateProduct";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "management/updateProduct";
    }

    @PostMapping("/updateProduct/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @RequestParam("category_id") int categoryId,
            @ModelAttribute("product") Product updatedProduct,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "thumbnails", required = false) MultipartFile thumbnail,
            @RequestParam(value = "removedImageIds", required = false) String removedImageIds,
            @RequestParam(value = "removeThumbnail", required = false) String removeThumbnail,
            RedirectAttributes redirectAttributes,
            Model model) {

        Product existingProduct = productService.findById(id);
        if (existingProduct == null) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
            return "redirect:/admin/productManagement";
        }

        // Cập nhật thông tin cơ bản
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setUpdatedAt(LocalDateTime.now());

        // Cập nhật danh mục
        Category category = categoryService.findCategoryById(categoryId);
        if (category == null) {
            throw new RuntimeException("Category không tồn tại");
        }
        existingProduct.setCategory(category);

        // Xóa ảnh cũ nếu có yêu cầu
        if (removedImageIds != null && !removedImageIds.isEmpty()) {
            String[] imageIds = removedImageIds.split(",");
            for (String imageIdStr : imageIds) {
                try {
                    Long imageId = Long.parseLong(imageIdStr);
                    ProductImage productImage = productImageService.findById(imageId);
                    if (productImage != null) {
                        fileUploadService.deleteFile(productImage.getImageUrl());
                        // Xóa bản ghi trong database
                        productImageService.deleteById(imageId);
                    }
                } catch (NumberFormatException e) {
                }
            }
        }

        // Xóa thumbnail nếu được yêu cầu
        if ("true".equals(removeThumbnail)) {
            if (existingProduct.getThumbnail() != null && !existingProduct.getThumbnail().isEmpty()) {
                fileUploadService.deleteFile(existingProduct.getThumbnail());
                existingProduct.setThumbnail(null);
            }
        }

        // Xử lý ảnh thumbnail mới
        if (thumbnail != null && !thumbnail.isEmpty()) {
            try {
                // Xóa thumbnail cũ nếu có
                if (existingProduct.getThumbnail() != null && !existingProduct.getThumbnail().isEmpty()) {
                    fileUploadService.deleteFile(existingProduct.getThumbnail());
                }

                String thumbnailFileName = fileUploadService.saveFile(thumbnail);
                existingProduct.setThumbnail(thumbnailFileName);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu ảnh thumbnail.");
                return "redirect:/admin/updateProduct/" + id;
            }
        }

        // Lưu sản phẩm với thông tin đã cập nhật
        Product savedProduct = productService.saveProduct(existingProduct);

        // Xử lý các ảnh mới
        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        String imageFileName = fileUploadService.saveFile(image);
                        ProductImage productImage = new ProductImage();
                        productImage.setImageUrl(imageFileName);
                        productImage.setProduct(savedProduct);
                        productImageService.saveproductImage(productImage);
                    } catch (IOException e) {
                        redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu ảnh sản phẩm.");
                        return "redirect:/admin/updateProduct/" + id;
                    }
                }
            }
        }

        List<CategoryResp> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
        return "redirect:/admin/productManagement";
    }

    @GetMapping("/logoutAdmin")
    public String logoutUser(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMsg", "Đăng xuất thành công!");
        return "redirect:/home";
    }
}

