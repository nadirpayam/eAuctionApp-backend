package com.project.eAuctionApp.services;


import java.io.File;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.lang.reflect.Field;

import com.project.eAuctionApp.entities.Product;
import com.project.eAuctionApp.repositories.ProductRepository;
import com.project.eAuctionApp.requests.ProductUpdateRequest;

import org.springframework.http.HttpStatus;

@Service
public class ProductService {

	private ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product updateOneProductById(Long productId, ProductUpdateRequest updateProduct) {
		Optional<Product> product = productRepository.findById(productId);
		if (product.isPresent()) {
			Product toUpdate = product.get();
			// toUpdate.setImagePath(updateProduct.getImagePath());
			toUpdate.setName(updateProduct.getName());
			toUpdate.setPrice(updateProduct.getPrice());
			toUpdate.setSold(updateProduct.getSold());
			productRepository.save(toUpdate);
			return toUpdate;
		} else {
			return null;
		}
	}
	


	public void deleteOneProduct(Long productId) {
		productRepository.deleteById(productId);
	}

	public void saveProduct(Product product) {
		productRepository.save(product);
	}

	public String createOneProduct(MultipartFile file, String name, double price, boolean sold) throws IOException {
		File convertFile = new File("C:\\Users\\nadir\\Tüm Projelerim\\StajProjesiKartaca\\Frontend\\e-auction-app\\public\\Images\\" + file.getOriginalFilename());
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(file.getBytes());
		fout.close();

		String imageUrl = "./Images/" + file.getOriginalFilename();

		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setSold(sold);
		product.setImageUrl(imageUrl);

		productRepository.save(product);

		return "File is uploaded successfully";
	}

	public ResponseEntity<Product> getOneProduct(Long productId) {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if (optionalProduct.isPresent()) {
			return ResponseEntity.ok().body(optionalProduct.get());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
		}
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();

	}
	
	public Product updateProductByFields(Long productId, Map<String, Object> fields) {
		Optional<Product> existingProduct = productRepository.findById(productId);

		if (existingProduct.isPresent()) {
			fields.forEach((key, value) -> {
				Field field = ReflectionUtils.findField(Product.class, key);
				field.setAccessible(true);
				ReflectionUtils.setField(field, existingProduct.get(), value);
			});
			return productRepository.save(existingProduct.get());
		}
		return null;
	}

	public List<Product> getRandomProducts() {
        return productRepository.findRandomProducts();

	}

}