package com.ecommerce;

import java.util.Scanner;

import com.ecommerce.exception.DuplicateProductException;
import com.ecommerce.exception.InvalidProductDataException;
import com.ecommerce.inventory.InventoryService;

public class InventoryManagementApp {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		InventoryService inventoryService = new InventoryService();

		while (true) {
			System.out.println("\nOptions:");
			System.out.println("1. Add Product");
			System.out.println("2. Get Products Sorted by Price");
			System.out.println("3. Get Products Below Specific Price");
			System.out.println("4. Update Product");
			System.out.println("5. Delete Product");
			System.out.println("6. Calculate Total Inventory Value");
			System.out.println("7. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline

			try {
				switch (choice) {
				case 1:
					addProduct(inventoryService, scanner);
					break;
				case 2:
					getProductsSortedByPrice(inventoryService);
					break;
				case 3:
					getProductsBelowSpecificPrice(inventoryService, scanner);
					break;
				case 4:
					updateProduct(inventoryService, scanner);
					break;
				case 5:
					deleteProduct(inventoryService, scanner);
					break;
				case 6:
					calculateTotalInventoryValue(inventoryService);
					break;
				case 7:
					System.out.println("Exiting...");
					scanner.close();
					System.exit(0);
				default:
					System.out.println("Invalid choice. Please enter a valid option.");
				}
			} catch (InvalidProductDataException | DuplicateProductException | IllegalArgumentException e) {
				System.out.println("Error: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("An unexpected error occurred: " + e.getMessage());
			}
		}
	}

	private static void addProduct(InventoryService inventoryService, Scanner scanner) {
		System.out.print("Enter product name: ");
		String name = scanner.nextLine();
		System.out.print("Enter product description: ");
		String description = scanner.nextLine();
		System.out.print("Enter product price: ");
		double price = scanner.nextDouble();
		System.out.print("Enter product quantity: ");
		int quantity = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		try {
			inventoryService.addProduct(name, description, price, quantity);
		} catch (InvalidProductDataException | DuplicateProductException e) {
			System.out.println("Error adding product: " + e.getMessage());
		}
	}

	private static void getProductsSortedByPrice(InventoryService inventoryService) {
		System.out.println("Products sorted by price:");
		inventoryService.getProductsSortedByPrice().forEach(System.out::println);
	}

	private static void getProductsBelowSpecificPrice(InventoryService inventoryService, Scanner scanner) {
		System.out.print("Enter price limit: ");
		double priceLimit = scanner.nextDouble();
		scanner.nextLine(); // Consume newline

		System.out.println("Products below price " + priceLimit + ":");
		inventoryService.getProductsBelowPrice(priceLimit).forEach(System.out::println);
	}

	private static void updateProduct(InventoryService inventoryService, Scanner scanner) {
		System.out.print("Enter product ID to update: ");
		String id = scanner.nextLine();
		System.out.print("Enter new price: ");
		double newPrice = scanner.nextDouble();
		System.out.print("Enter new quantity: ");
		int newQuantity = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		try {
			inventoryService.updateProduct(id, newPrice, newQuantity);
		} catch (InvalidProductDataException | IllegalArgumentException e) {
			System.out.println("Error updating product: " + e.getMessage());
		}
	}

	private static void deleteProduct(InventoryService inventoryService, Scanner scanner) {
		System.out.print("Enter product ID to delete: ");
		String id = scanner.nextLine();

		try {
			inventoryService.deleteProduct(id);
		} catch (IllegalArgumentException e) {
			System.out.println("Error deleting product: " + e.getMessage());
		}
	}

	private static void calculateTotalInventoryValue(InventoryService inventoryService) {
		double totalValue = inventoryService.calculateTotalInventoryValue();
		System.out.println("Total inventory value: " + totalValue);
	}
}
