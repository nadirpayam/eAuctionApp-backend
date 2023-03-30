package com.project.eAuctionApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.eAuctionApp.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
