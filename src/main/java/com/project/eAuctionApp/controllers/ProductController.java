package com.project.eAuctionApp.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.eAuctionApp.entities.Product;
import com.project.eAuctionApp.requests.ProductUpdateRequest;
import com.project.eAuctionApp.services.ProductService;


@RestController
@RequestMapping("/products")
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/{productId}")
	public ResponseEntity<Product> getOneProduct(@PathVariable Long productId) {
		return productService.getOneProduct(productId);

	}
	


	@GetMapping
	public List<Product> getAllProducts() {
		return productService.getAllProducts();
	}
	
	@GetMapping("/rand")
	 public List<Product> getRandomProducts() {
	        return productService.getRandomProducts();
	  }

	@PostMapping
	public String createPro(@RequestParam("file") MultipartFile file, @RequestParam("name") String name,
			@RequestParam("price") double price, @RequestParam("sold") boolean sold) throws IOException {

		return productService.createOneProduct(file, name, price, sold);
	}

	@PutMapping("/{productId}")
	public Product updateOneProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest updateProduct) {
		return productService.updateOneProductById(productId, updateProduct);
	}
	
	@PatchMapping("/{productId}")
	public Product updateProductFields(@PathVariable Long productId,@RequestBody Map<String, Object> fields) {
		return productService.updateProductByFields(productId, fields);
	}

	@DeleteMapping("/{productId}")
	public void deleteOneProduct(@PathVariable Long productId) {
		productService.deleteOneProduct(productId);
	}

}
