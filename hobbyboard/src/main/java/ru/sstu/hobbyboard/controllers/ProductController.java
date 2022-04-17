package ru.sstu.hobbyboard.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sstu.hobbyboard.entities.PhotoProduct;
import ru.sstu.hobbyboard.entities.Product;
import ru.sstu.hobbyboard.entities.ProductInfo;
import ru.sstu.hobbyboard.entities.User;
import ru.sstu.hobbyboard.entities.dto.ImageDTO;
import ru.sstu.hobbyboard.entities.dto.ProductDTO;
import ru.sstu.hobbyboard.exceptionsOLD.ResourceNotFoundException;
import ru.sstu.hobbyboard.servicies.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final MakerService makerService;
    private final CategoryService categoryService;
    private final PhotoProductService photoProductService;
    private final ImageService imageService;
    private final BasketService basketService;
    private final UserService userService;

    public ProductController(ProductService productService, MakerService makerService, CategoryService categoryService, PhotoProductService photoProductService, ImageService imageService, BasketService basketService, UserService userService) {
        this.productService = productService;
        this.makerService = makerService;
        this.categoryService = categoryService;
        this.photoProductService = photoProductService;
        this.imageService = imageService;
        this.basketService = basketService;
        this.userService = userService;
    }

    @GetMapping("/{product}")
    public String getProduct(Model model, @PathVariable Product product, HttpSession httpSession, @AuthenticationPrincipal User user) {
        if (product == null) throw new ResourceNotFoundException("Продукт не найден");
        if (user != null) user = userService.findByEmail(user.getEmail());
        basketService.basketInit(model, httpSession, user);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/{product}/info")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public String getProductInfo(Model model, @PathVariable Product product, @AuthenticationPrincipal User user) {
        if (product == null) throw new ResourceNotFoundException("Продукт не найден");
        if (product.getProductInfo() == null) product.setProductInfo(new ProductInfo(product, user, user));
        model.addAttribute("info", product.getProductInfo());
        return "productInfo";
    }

    @GetMapping("/{product}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public String getUpdateProduct(@PathVariable Product product, Model model) {
        if (product == null) throw new ResourceNotFoundException("Продукт не найден");
        model.addAttribute("adminPage", true);
        model.addAttribute("product", product);
        model.addAttribute("productDto", new ProductDTO(product));
        model.addAttribute("makers", makerService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("products", productService.findAll());
        return "productUpdate";
    }

    @PostMapping("/{product}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public String updateProduct(Model model, @PathVariable Product product, @ModelAttribute("productDto") @Valid ProductDTO productDTO,
                                BindingResult bindingResult, @AuthenticationPrincipal User user) {
        model.addAttribute("adminPage", true);
        model.addAttribute("productDto", productDTO);
        model.addAttribute("product", product);
        model.addAttribute("makers", makerService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("products", productService.findAll());
        if (bindingResult.hasErrors()) {
            return "productUpdate";
        }
        Product updated = Product.fromDto(productDTO);
        if (!productService.validateProduct(product)) {
            model.addAttribute("other", "Минимальное значение не может быть больше максимального");
            return "productUpdate";
        }
        updated.setAgeCategory(productService.getAgeCategory(productDTO.getAge()));
        if (updated.getIsIndependent()) updated.setOriginal(null);
        updated.setId(product.getId());
        updated.setMainPhoto(product.getMainPhoto());
        updated.setPhotos(product.getPhotos());
        ProductInfo productInfo = product.getProductInfo();
        productInfo.setUpdateDate(Date.valueOf(LocalDate.now()));
        productInfo.setUpdater(user);
        updated.setProductInfo(productInfo);
        updated.setDelDate(product.getDelDate());
        updated.setCode(product.getCode());
        updated.setRate(product.getRate());
        productService.save(updated);
        return "productUpdate";
    }

    @PostMapping("/{product}/imageUpd")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public String imageUpdate(Model model, @PathVariable Product product, @AuthenticationPrincipal User user,
                              @ModelAttribute("imageDto") @Valid ImageDTO imageDTO) {
        if (product == null) throw new ResourceNotFoundException("Продукт не найден");
        model.addAttribute("adminPage", true);
        model.addAttribute("productDto", new ProductDTO(product));
        model.addAttribute("product", product);
        model.addAttribute("makers", makerService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("products", productService.findAll());
        if (imageDTO.getPhotoToDel() != null && imageDTO.getPhotoToDel().contains(imageDTO.getMainPhoto())) {
            model.addAttribute("imageError", "Главное изображение не может быть удалено");
            return "productUpdate";
        }
        ProductInfo productInfo = product.getProductInfo();
        productInfo.setUpdateDate(Date.valueOf(LocalDate.now()));
        productInfo.setUpdater(user);
        Set<PhotoProduct> oldPhotos = product.getPhotos();
        if (imageDTO.getNewPhotos() != null && (imageDTO.getNewPhotos().size() > 2 || !imageDTO.getNewPhotos().get(0).isEmpty())) {
            Iterable<PhotoProduct> iterable = photoProductService.saveAll(imageService.saveImages(imageDTO).stream().map((x) -> new PhotoProduct(product, x)).collect(Collectors.toSet()));
            iterable.forEach(oldPhotos::add);
        }
        if (imageDTO.getPhotoToDel() != null) imageDTO.getPhotoToDel().forEach(oldPhotos::remove);
        product.setPhotos(oldPhotos);
        product.setMainPhoto(imageDTO.getMainPhoto());
        productService.save(product);
        return "productUpdate";
    }
}
