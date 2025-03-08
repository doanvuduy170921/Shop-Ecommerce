package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.entity.ProductImage;
import com.example.ShopEcommerce.repository.ProductImageRepository;
import com.example.ShopEcommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import com.example.ShopEcommerce.dto.req.AddToCardReq;
import com.example.ShopEcommerce.dto.resp.CategoryResp;
import com.example.ShopEcommerce.dto.resp.ProductResp;
import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.service.CartService;
import com.example.ShopEcommerce.service.CategoryService;
import com.example.ShopEcommerce.service.ProductService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {


    private final CategoryService categoryService;
    private final CartService cartService;

    private final ProductRepository productRepository;


    private final ProductImageRepository productImageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    // 📌 Hiển thị trang upload ảnh
    @GetMapping("/{productId}/upload")
    public String showUploadPage(@PathVariable Long productId, Model model) {
        // Kiểm tra xem sản phẩm có tồn tại không
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            return "redirect:/product"; // Chuyển hướng nếu không tìm thấy
        }

        model.addAttribute("productId", productId);
        model.addAttribute("productName", productOpt.get().getName()); // Thêm tên sản phẩm để hiển thị
        return "upload";
    }

    // 📌 Upload ảnh
    @PostMapping("/{productId}/upload")
    public String uploadImage(@PathVariable Long productId,
                              @RequestParam("files") List<MultipartFile> files,
                              RedirectAttributes redirectAttributes) {
        try {
            // Thêm log để xác nhận ID từ đường dẫn
            System.out.println("Received productId from URL: " + productId);

            Optional<Product> productOpt = productRepository.findById(productId);
            if (!productOpt.isPresent()) {
                // Nếu không tìm thấy sản phẩm với ID này, thêm thông báo lỗi
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm với ID: " + productId);
                return "redirect:/product";
            }

            Product product = productOpt.get();
            System.out.println("Found product with ID: " + product.getId() + ", Name: " + product.getName());

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            ProductImage latestImage = null;

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                // Kiểm tra định dạng ảnh
                String contentType = file.getContentType();
                if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
                    redirectAttributes.addFlashAttribute("error", "Only JPG and PNG images are allowed!");
                    return "redirect:/product/" + productId + "/upload";
                }

                // Lưu file
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadPath, fileName);
                Files.write(filePath, file.getBytes());

                // Lưu vào database
                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setImageUrl(fileName);
                latestImage = productImageRepository.save(productImage);

                System.out.println("Saving ProductImage with product ID: " + productImage.getProduct().getId());
            }

            // ✅ Cập nhật thumbnail trong bảng `products` nếu có ảnh mới
            if (latestImage != null) {
                product.setThumbnail(latestImage.getImageUrl());
                productRepository.save(product);
                System.out.println("Updated thumbnail for product ID: " + productId);
            }

            System.out.println("Product ID from path: " + productId);
            System.out.println("Product ID from DB: " + product.getId());

            redirectAttributes.addFlashAttribute("success", "Images uploaded successfully!");
        } catch (Exception e) {
            System.err.println("Error during upload: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }

        return "redirect:/product/" + productId + "/upload";
    }

    @GetMapping("/{productId}/images")
    public String listImages(@PathVariable Long productId, Model model) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            return "redirect:/product";
        }

        List<String> images = productImageRepository.findAllByProductId(productId);
        System.out.println("Found " + images.size() + " images for product ID: " + productId);

        // Đường dẫn cơ sở để hiển thị ảnh
        model.addAttribute("baseImageUrl", "/product/images/");
        model.addAttribute("images", images);
        model.addAttribute("productId", productId);
        model.addAttribute("productName", productOpt.get().getName());
        return "list-images";
    }

    // 📌 Hiển thị ảnh
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get(uploadPath).resolve(filename);
            if (!Files.exists(imagePath)) return ResponseEntity.notFound().build();

            Resource resource = new UrlResource(imagePath.toUri());
            String contentType = Files.probeContentType(imagePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/details/add-to-cart")
    public String addToCart(@ModelAttribute AddToCardReq entity, RedirectAttributes redirectAttributes, HttpSession session) {
        //TODO: process POST request
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        entity.setUserId(user.getId());
        cartService.addToCart(entity);
        redirectAttributes.addFlashAttribute("successMessage", "Add to cart successfully");
        return "redirect:/products/details?id=" + entity.getProductId();
    }


}
