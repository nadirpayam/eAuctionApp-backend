package com.project.eAuctionApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.eAuctionApp.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query(value = "SELECT * FROM product  where sold=0 ORDER BY product_id ASC LIMIT 3", nativeQuery = true)
	List<Product> findRandomProducts();

}

