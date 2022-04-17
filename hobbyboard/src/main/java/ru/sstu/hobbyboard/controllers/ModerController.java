package ru.sstu.hobbyboard.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sstu.hobbyboard.entities.*;
import ru.sstu.hobbyboard.entities.dto.CategoryDTO;
import ru.sstu.hobbyboard.entities.dto.MakerDTO;
import ru.sstu.hobbyboard.entities.dto.ProductDTO;
import ru.sstu.hobbyboard.exceptionsOLD.MyBadRequestException;
import ru.sstu.hobbyboard.exceptionsOLD.ResourceNotFoundException;
import ru.sstu.hobbyboard.servicies.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ModerController {

    private final ProductService productService;
    private final MakerService makerService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final PhotoProductService photoProductService;
    private final PurchaseService purchaseService;

    @GetMapping
    public String getInfo(Model model) {
        model.addAttribute("adminPage", true);
        return "moder";
    }

    @GetMapping("/productsTable")
    public String getProductsTable(Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("adminPage", true);
        model.addAttribute("products", productService.findAllPageable(PageRequest.of(page - 1, 10)));
        return "productTable";
    }

    @GetMapping("/createProduct")
    public String createProductForm(Model model) {
        model.addAttribute("adminPage", true);
        model.addAttribute("productDto", new ProductDTO());
        model.addAttribute("makers", makerService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("products", productService.findAll());
        return "createProduct";
    }

    @PostMapping("/createProduct")
    public String createProduct(Model model, @ModelAttribute("productDto") @Valid ProductDTO productDTO,
                                BindingResult bindingResult, @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("makers", makerService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("products", productService.findAll());
            return "createProduct";
        }
        Product product = Product.fromDto(productDTO);
        if (!productService.validateProduct(product)) {
            model.addAttribute("other", "Минимальное значение не может быть больше максимального");
            model.addAttribute("makers", makerService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("products", productService.findAll());
            return "createProduct";

        }
        product.setAgeCategory(productService.getAgeCategory(product.getAge()));
        if (product.getIsIndependent()) product.setOriginal(null);
        product.setCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        Product saved = productService.save(product);
        saved.setProductInfo(new ProductInfo(saved, user, user));
        Iterable<PhotoProduct> iterable = photoProductService.saveAll(imageService.saveImages(productDTO).stream().map((x) -> new PhotoProduct(product, x)).collect(Collectors.toSet()));
        Set<PhotoProduct> photoProducts = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toSet());
        product.setPhotos(photoProducts);
        product.setMainPhoto(photoProducts.iterator().next());
        productService.save(saved);
        return "redirect:/product/" + saved.getId();
    }

    @GetMapping("/createMaker")
    public String createMaker(Model model) {
        model.addAttribute("makerDto", new MakerDTO());
        return "createMaker";
    }

    @PostMapping("/createMaker")
    public String createMakerPost(Model model, @ModelAttribute("makerDto") @Valid MakerDTO makerDto,
                                  BindingResult bindingResult) {
        if (makerDto == null) throw new MyBadRequestException("Invalid request");
        if (bindingResult.hasErrors()) {
            model.addAttribute("makerDto", makerDto);
            return "createMaker";
        }
        Maker maker = new Maker(makerDto);
        maker.setLogo(imageService.saveImages(makerDto).get(0));
        maker = makerService.save(maker);
        return "redirect:/maker/" + maker.getId();
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam Integer product) {
        if (product == null || productService.findById(product) == null)
            throw new ResourceNotFoundException("Продукт не найден");
        productService.deleteById(product);
        return "redirect:/admin/productsTable";
    }

    @GetMapping("/createCategory")
    public String createCategory(Model model) {
        model.addAttribute("categoryDto", new CategoryDTO());
        return "createCategory";
    }

    @PostMapping("/createCategory")
    public String createCategoryPost(Model model, @ModelAttribute("categoryDto") @Valid CategoryDTO categoryDto,
                                     BindingResult bindingResult) {
        if (categoryDto == null) throw new MyBadRequestException("Invalid request");
        if (bindingResult.hasErrors()) {
            model.addAttribute("makerDto", categoryDto);
            return "createCategory";
        }
        Category category = new Category(categoryDto);
        categoryService.save(category);
        return "redirect:/admin/";
    }

    @GetMapping("/purchases")
    public String getPurchases(Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("adminPage", true);
        model.addAttribute("purchases", purchaseService.findAllPageable(PageRequest.of(page - 1, 10)));
        return "purchases";
    }

    @GetMapping("/statistics")
    public String statistics(){
        return "statistics";
    }
}
