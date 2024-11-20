package com.ecommerce.exception;

import static com.ecommerce.testutils.TestUtils.currentTest;
import static com.ecommerce.testutils.TestUtils.exceptionTestFile;
import static com.ecommerce.testutils.TestUtils.testReport;
import static com.ecommerce.testutils.TestUtils.yakshaAssert;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ecommerce.inventory.InventoryService;
import com.ecommerce.models.Product;

public class ExceptionTest {

	private InventoryService inventoryService;

	@BeforeEach
	public void setUp() {
		inventoryService = new InventoryService();
	}

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testAddProductWithInvalidData() throws IOException {
		try {
			inventoryService.addProduct("Invalid Product", "Invalid Description", -100.0, -5);
			yakshaAssert(currentTest(), false, exceptionTestFile);
		} catch (InvalidProductDataException ex) {
			yakshaAssert(currentTest(), true, exceptionTestFile);
		}
	}

	@Test
	public void testUpdateProductWithInvalidData() throws IOException {
		try {
			inventoryService.addProduct("Mock Product", "Mock Description", 100.0, 10);
			inventoryService.updateProduct("MOCK-ID", -100.0, -5); // Invalid price and quantity
			yakshaAssert(currentTest(), false, exceptionTestFile);
		} catch (InvalidProductDataException ex) {
			yakshaAssert(currentTest(), true, exceptionTestFile);
		}
	}

	@Test
	public void testUpdateProductNotFound() throws IOException {
		try {
			inventoryService.updateProduct("NON-EXISTENT-ID", 100.0, 10); // Non-existent product ID
			yakshaAssert(currentTest(), false, exceptionTestFile);
		} catch (IllegalArgumentException ex) {
			yakshaAssert(currentTest(), true, exceptionTestFile);
		}
	}

	@Test
	public void testDeleteProductNotFound() throws IOException {
		try {
			inventoryService.deleteProduct("NON-EXISTENT-ID"); // Non-existent product ID
			yakshaAssert(currentTest(), false, exceptionTestFile);
		} catch (IllegalArgumentException ex) {
			yakshaAssert(currentTest(), true, exceptionTestFile);
		}
	}

	@Test
	public void testRetrieveProductsBelowNegativePrice() throws IOException {
		try {
			List<Product> products = inventoryService.getProductsBelowPrice(-50.0); // Invalid price limit
			yakshaAssert(currentTest(), products != null && products.isEmpty(), exceptionTestFile);
		} catch (Exception ex) {
			yakshaAssert(currentTest(), false, exceptionTestFile);
		}
	}

	@Test
	public void testCalculateTotalInventoryValueWithNoProducts() throws IOException {
		try {
			double totalValue = inventoryService.calculateTotalInventoryValue(); // No products in inventory
			yakshaAssert(currentTest(), totalValue == 0.0, exceptionTestFile);
		} catch (Exception ex) {
			yakshaAssert(currentTest(), false, exceptionTestFile);
		}
	}
}