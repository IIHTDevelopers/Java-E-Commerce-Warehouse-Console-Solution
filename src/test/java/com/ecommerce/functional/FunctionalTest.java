package com.ecommerce.functional;

import static com.ecommerce.testutils.TestUtils.businessTestFile;
import static com.ecommerce.testutils.TestUtils.currentTest;
import static com.ecommerce.testutils.TestUtils.testReport;
import static com.ecommerce.testutils.TestUtils.yakshaAssert;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ecommerce.exception.InvalidProductDataException;
import com.ecommerce.inventory.InventoryService;
import com.ecommerce.models.Product;

public class FunctionalTest {

	private InventoryService inventoryService;
	private Product mockProduct;

	@BeforeEach
	public void setUp() {
		inventoryService = new InventoryService();
		mockProduct = new Product("MOCK-123", "Mock Product", "Mock Description", 100.0, 10);
		inventoryService.addProduct(mockProduct.getName(), mockProduct.getDescription(), mockProduct.getPrice(),
				mockProduct.getQuantity());
	}

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testAddProductSuccess() throws IOException {
		try {
			inventoryService.addProduct("New Product", "New Description", 50.0, 5);
			List<Product> products = inventoryService.getProductsSortedByPrice();
			yakshaAssert(currentTest(), products.size() == 2, businessTestFile);
		} catch (Exception ex) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testAddProductInvalidData() throws IOException {
		try {
			inventoryService.addProduct("Invalid Product", "Invalid Description", -10.0, 5);
			yakshaAssert(currentTest(), false, businessTestFile);
		} catch (InvalidProductDataException ex) {
			yakshaAssert(currentTest(), true, businessTestFile);
		}
	}

	@Test
	public void testGetProductsSortedByPrice() throws IOException {
		try {
			inventoryService.addProduct("Cheap Product", "Description", 10.0, 1);
			List<Product> products = inventoryService.getProductsSortedByPrice();
			yakshaAssert(currentTest(), products.get(0).getPrice() == 10.0, businessTestFile);
		} catch (Exception ex) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetProductsBelowSpecificPrice() throws IOException {
		try {
			inventoryService.addProduct("Expensive Product", "Description", 200.0, 1);
			List<Product> products = inventoryService.getProductsBelowPrice(150.0);
			yakshaAssert(currentTest(), products.size() == 1, businessTestFile);
		} catch (Exception ex) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateProductSuccess() throws IOException {
		try {
			String productId = inventoryService.getProductsSortedByPrice().stream()
					.filter(p -> p.getName().equals("Mock Product")).map(Product::getId).findFirst()
					.orElseThrow(() -> new RuntimeException("Product not found"));
			inventoryService.updateProduct(productId, 150.0, 20);
			Product updatedProduct = inventoryService.getProductsSortedByPrice().stream()
					.filter(p -> p.getId().equals(productId)).findFirst().orElse(null);
			yakshaAssert(currentTest(),
					updatedProduct != null && updatedProduct.getPrice() == 150.0 && updatedProduct.getQuantity() == 20,
					businessTestFile);
		} catch (Exception ex) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateProductInvalidData() throws IOException {
		try {
			inventoryService.updateProduct("MOCK-123", -50.0, 10);
			yakshaAssert(currentTest(), false, businessTestFile);
		} catch (InvalidProductDataException ex) {
			yakshaAssert(currentTest(), true, businessTestFile);
		}
	}

	@Test
	public void testDeleteProductSuccess() throws IOException {
		try {
			String productId = inventoryService.getProductsSortedByPrice().stream()
					.filter(p -> p.getName().equals("Mock Product")).map(Product::getId).findFirst()
					.orElseThrow(() -> new RuntimeException("Product not found"));
			inventoryService.deleteProduct(productId);
			List<Product> products = inventoryService.getProductsSortedByPrice();
			yakshaAssert(currentTest(), products.isEmpty(), businessTestFile);
		} catch (Exception ex) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testCalculateTotalInventoryValue() throws IOException {
		try {
			inventoryService.addProduct("Mock Product", "Mock Description", 100.0, 10);
			inventoryService.addProduct("Another Product", "Description", 50.0, 5);
			double totalValue = inventoryService.calculateTotalInventoryValue();
			yakshaAssert(currentTest(), totalValue == 2250.0, businessTestFile);
		} catch (Exception ex) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}
}
