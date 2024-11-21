package com.ecommerce.inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.ecommerce.exception.DuplicateProductException;
import com.ecommerce.exception.InvalidProductDataException;
import com.ecommerce.models.Product;

public class InventoryService {
	// Inventory list to hold products
	private List<Product> inventory = new ArrayList<>();

//	// Add Product
//	public void addProduct(String name, String description, double price, int quantity) {
//		String id = UUID.randomUUID().toString();
//		Product product = new Product(id, name, description, price, quantity);
//
//		// Validate product data using Lambda Expression
//		Predicate<Product> isValidProduct = p -> p.getPrice() > 0 && p.getQuantity() > 0;
//		if (!isValidProduct.test(product)) {
//			System.out.println("Invalid product data. Price and quantity must be positive.");
//			return;
//		}
//
//		// Check for duplicate product IDs using Stream API
//		boolean idExists = inventory.stream().anyMatch(p -> p.getId().equals(id));
//		if (idExists) {
//			System.out.println("Duplicate ID detected, cannot add product.");
//			return;
//		}
//
//		// Add product using Method Reference
//		inventory.add(product);
//		System.out.println("Product added: " + product);
//	}

	public void addProduct(String name, String description, double price, int quantity) {
		String id = UUID.randomUUID().toString();
		Product product = new Product(id, name, description, price, quantity);

		// Validate product data using Lambda Expression
		Predicate<Product> isValidProduct = p -> p.getPrice() > 0 && p.getQuantity() > 0;
		if (!isValidProduct.test(product)) {
			throw new InvalidProductDataException("Invalid product data: Price and quantity must be positive.");
		}

		// Check for duplicate product IDs using Stream API
		boolean idExists = inventory.stream().anyMatch(p -> p.getId().equals(id));
		if (idExists) {
			throw new DuplicateProductException("Duplicate ID detected. Cannot add product.");
		}

		// Add product using Method Reference
		inventory.add(product);
		System.out.println("Product added: " + product);
	}

	// Get Products Sorted By Price
	public List<Product> getProductsSortedByPrice() {
		return inventory.stream().sorted(Comparator.comparingDouble(Product::getPrice)).collect(Collectors.toList());
	}

	// Retrieve Products Below Specific Price
	public List<Product> getProductsBelowPrice(double priceLimit) {
		return inventory.stream().filter(p -> p.getPrice() < priceLimit) // Filtering products below price limit
				.map(p -> new Product(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity())) // Mapping
																													// to
																													// a
																													// new
																													// product
																													// list
				.collect(Collectors.toList());
	}

//	// Update Product
//	public void updateProduct(String id, double newPrice, int newQuantity) {
//		// Validate new data using Lambda Expression
//		if (newPrice <= 0 || newQuantity < 0) {
//			System.out.println("Invalid update data. Price must be positive, and quantity cannot be negative.");
//			return;
//		}
//
//		// Locate and update the product using Stream API and Map
//		List<Product> updatedList = inventory.stream().map(p -> {
//			if (p.getId().equals(id)) {
//				p.setPrice(newPrice);
//				p.setQuantity(newQuantity);
//				System.out.println("Product updated: " + p);
//			}
//			return p;
//		}).collect(Collectors.toList());
//
//		inventory.clear();
//		inventory.addAll(updatedList);
//	}
	public void updateProduct(String id, double newPrice, int newQuantity) {
		// Validate new data using Lambda Expression
		if (newPrice <= 0 || newQuantity < 0) {
			throw new InvalidProductDataException(
					"Invalid update data: Price must be positive, and quantity cannot be negative.");
		}

		// Locate and update the product using Stream API
		System.out.println(id);
		System.out.println(inventory);

		boolean productFound = inventory.stream().filter(p -> p.getId().equals(id)).peek(p -> {
			p.setPrice(newPrice);
			p.setQuantity(newQuantity);
			System.out.println("Product updated: " + p);
		}).findFirst().isPresent();

		if (!productFound) {
			throw new IllegalArgumentException("Product with ID: " + id + " not found.");
		}
	}

//	// Delete Product
//	public void deleteProduct(String id) {
//		// Use Stream API with filter and Collectors to create a new list without the
//		// product
//		List<Product> updatedList = inventory.stream().filter(p -> !p.getId().equals(id)) // Exclude the product with
//																							// the given ID
//				.collect(Collectors.toList());
//
//		if (updatedList.size() == inventory.size()) {
//			System.out.println("Product not found with ID: " + id);
//		} else {
//			System.out.println("Product deleted with ID: " + id);
//		}
//
//		inventory.clear();
//		inventory.addAll(updatedList);
//	}
	public void deleteProduct(String id) {
		boolean productExists = inventory.stream().anyMatch(p -> p.getId().equals(id));

		if (!productExists) {
			throw new IllegalArgumentException("Product with ID: " + id + " not found.");
		}

		List<Product> updatedList = inventory.stream().filter(p -> !p.getId().equals(id)).collect(Collectors.toList());

		inventory.clear();
		inventory.addAll(updatedList);
		System.out.println("Product deleted with ID: " + id);
	}

	// Calculate Total Inventory Value
	public double calculateTotalInventoryValue() {
		return inventory.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()) // Lambda Expression for mapping to
																					// value
				.sum(); // Reduce operation to sum up values
	}
}