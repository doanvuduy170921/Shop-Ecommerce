package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.dto.resp.CategoryResp;
import com.example.ShopEcommerce.entity.Category;
import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.entity.ProductImage;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/productManagement")
    public String productManagement(@RequestParam(name = "keyword", required = false) String keyword,
                                    @RequestParam(name = "priceRange", required = false) String priceRange,
                                    @RequestParam(name = "categoryId", required = false) Integer categoryId,
                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                    @RequestParam(name = "size", defaultValue = "8") int size,
                                    Model model) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productsPage = productService.filterProducts(keyword, priceRange, categoryId, pageable);

        // Lấy danh sách tất cả các danh mục để hiển thị trong dropdown filter
        List<CategoryResp> categories = categoryService.getAllCategories();

        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categories);

        return "management/productManagement";
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



    @GetMapping("/accountManagement")
    public String accountManagement(@RequestParam(name = "keyword", required = false) String keyword,
                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                    @RequestParam(name = "size", defaultValue = "8") int size,
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

    @PostMapping("/accountManagement/deactivate/{id}")
    public String deactivateUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.activateUser(id);
            redirectAttributes.addFlashAttribute("success", "Tài khoản đã khóa thành công");
            return "redirect:/admin/accountManagement?page=";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi khóa tài khoản: " + e.getMessage());
            return "redirect:/admin/accountManagement";
        }
    }
    @PostMapping("/accountManagement/activate/{id}")
    public String activateUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.activateUser(id);
            redirectAttributes.addFlashAttribute("success", "Tài khoản đã mở khóa thành công");
            return "redirect:/admin/accountManagement";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi mở khóa tài khoản: " + e.getMessage());
            return "redirect:/admin/accountManagement";
        }
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

    @PostMapping("/accountDetail/deactivate/{id}")
    public String deactivateDetailUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.activateUser(id);
            redirectAttributes.addFlashAttribute("success", "Tài khoản đã khóa thành công");
            return "redirect:/admin/accountDetail/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi khóa tài khoản: " + e.getMessage());
            return "redirect:/admin/accountDetail/" + id;
        }
    }

    @PostMapping("/accountDetail/activate/{id}")
    public String activateDetailUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.activateUser(id);
            redirectAttributes.addFlashAttribute("success", "Tài khoản đã mở khóa thành công");
            return "redirect:/admin/accountDetail/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi mở khóa tài khoản: " + e.getMessage());
            return "redirect:/admin/accountDetail/" + id;
        }
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

//    @GetMapping("/ProfileAdmin/{id}")
//    public String ProfileAdmin(@PathVariable("id") Long id, Model model){
//        User user = userService.findById(id);
//        if (user == null) {
//            return "redirect:/login";
//        }
//        model.addAttribute("user", user);
//        return "management/ProfileAdmin";
//    }
    @GetMapping("/ProfileAdmin")
    public String ProfileAdmin(HttpSession session, Model model){
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User user = userService.findById(loggedInUser.getId());
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "management/ProfileAdmin";
    }

    @GetMapping("/updateProfileAdmin")
    public String updateAccount(HttpSession session, Model model){
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User user = userService.findById(loggedInUser.getId());
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "management/updateProfileAdmin";
    }

    @PostMapping("/updateProfileAdmin")
    public String updateProfileAdmin(
            @ModelAttribute("user") User updatedUser,
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User existingUser = userService.findById(loggedInUser.getId());
        if (existingUser == null) {
            redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại!");
            return "redirect:/login";
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setEmail(updatedUser.getEmail());

        if (newPassword != null && !newPassword.isEmpty()) {
            if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
                return "redirect:/admin/updateProfileAdmin";
            }
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Xác nhận mật khẩu không khớp!");
                return "redirect:/admin/updateProfileAdmin";
            }
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        userService.updateUser(existingUser);

        redirectAttributes.addFlashAttribute("success", "Cập nhật tài khoản thành công!");
        return "redirect:/admin/ProfileAdmin";
    }

//    @GetMapping("/updateProfileAdmin/{id}")
//    public String updateAccount(@PathVariable Long id, Model model){
//        User user = userService.findById(id);
//        if (user == null) {
//            return "redirect:/admin/updateProfileAdmin";
//        }
//        model.addAttribute("user", user);
//        return "management/updateProfileAdmin";
//    }
//
//    @PostMapping("/updateProfileAdmin/{id}")
//    public String updateProfileAdmin(
//            @PathVariable Long id,
//            @ModelAttribute("user") User updatedUser,
//            @RequestParam(value = "oldPassword", required = false) String oldPassword,
//            @RequestParam(value = "newPassword", required = false) String newPassword,
//            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
//            RedirectAttributes redirectAttributes) {
//
//        User existingUser = userService.findById(id);
//        if (existingUser == null) {
//            redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại!");
//            return "redirect:/admin/updateProfileAdmin/" + id;
//        }
//
//        existingUser.setName(updatedUser.getName());
//        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
//        existingUser.setAddress(updatedUser.getAddress());
//        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
//        existingUser.setEmail(updatedUser.getEmail());
//
//        if (newPassword != null && !newPassword.isEmpty()) {
//            if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
//                redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
//                return "redirect:/admin/updateProfileAdmin/" + id;
//            }
//            if (!newPassword.equals(confirmPassword)) {
//                redirectAttributes.addFlashAttribute("error", "Xác nhận mật khẩu không khớp!");
//                return "redirect:/admin/updateProfileAdmin/" + id;
//            }
//            existingUser.setPassword(passwordEncoder.encode(newPassword));
//        }
//
//        userService.updateUser(existingUser);
//
//        redirectAttributes.addFlashAttribute("success", "Cập nhật tài khoản thành công!");
//        return "redirect:/admin/ProfileAdmin/" + id;
//    }

    @GetMapping("/logoutAdmin")
    public String logoutUser(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMsg", "Đăng xuất thành công!");
        return "redirect:/home";
    }

}

