package com.fta.stock.tracking.controller.api;

import com.fta.stock.tracking.model.Product;
import com.fta.stock.tracking.model.User;
import com.fta.stock.tracking.service.ProductService;
import com.fta.stock.tracking.service.TrendyolApiService;
import com.fta.stock.tracking.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ApiProductController {

    private  final UserService userService;
    private  final ProductService productService;
    private  final TrendyolApiService trendyolApiService;
    @GetMapping("")
    public User getProducts(@AuthenticationPrincipal User user)
    {
      Optional<User> user1=  userService.getUserByEmail(user.getEmail());
      return userService.getUserByEmail(user.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @PostMapping("/update")
    public Product updateProduct(@AuthenticationPrincipal User user, @RequestBody Product product, HttpServletRequest request) {
        product.setUser(user);
        product=productService.updateProduct(product);
        trendyolApiService.updateToTrendyolProductQuantity(user);
        return product;
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<String> deleteProduct(@AuthenticationPrincipal User user , @PathVariable Integer productId) {
        Optional<Product> productOptional = productService.findById(productId);

        // Ürünün bulunamaması durumunda bir hata döndürelim
        Product product = productOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + productId));

        // Ürünü silme işlemini gerçekleştirin
        productService.delete(product);

        // Başarılı bir silme işlemi gerçekleştirildiğinde başarılı bir yanıt döndürelim
        return ResponseEntity.ok("Product successfully deleted with id: " + productId);
    }


}

