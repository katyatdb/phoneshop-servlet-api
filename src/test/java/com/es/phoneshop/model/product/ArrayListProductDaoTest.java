package com.es.phoneshop.model.product;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {

    private ArrayListProductDao productDao;
    private Product product;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        product = new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), null, 100, null);

        productDao.save(product);
    }

    @After
    public void tearUp() {
        productDao.setProducts(new ArrayList<>());
    }

    @Test
    public void testGetProduct() {
        assertNotNull(productDao.getProduct(1L));
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetProductException() {
        productDao.getProduct(2L);
    }

    @Test
    public void testFindProducts() {
        assertEquals(1, productDao.findProducts().size());
    }

    @Test
    public void testFindProductsNoResults() {
        productDao.delete(1L);
        assertEquals(0, productDao.findProducts().size());
    }

    @Test
    public void testFindNullPriceProducts() {
        product.setPrice(null);
        assertTrue(productDao.findProducts().isEmpty());
    }

    @Test
    public void testFindZeroStockProducts() {
        product.setStock(0);
        assertTrue(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProduct() {
        Product productToSave = new Product(2L, "iphone6", "Apple iPhone 6S", new BigDecimal(300), null, 42, null);
        productDao.save(productToSave);
        assertEquals(2, productDao.findProducts().size());
    }

    @Test
    public void testSaveEqualIdProducts() {
        Product productToSave = new Product(1L, "iphone6", "Apple iPhone 6S", new BigDecimal(300), null, 42, null);
        productDao.save(productToSave);
        assertEquals(1, productDao.findProducts().size());
    }

    @Test
    public void testDeleteProduct() {
        productDao.delete(1L);
        assertTrue(productDao.findProducts().isEmpty());
    }
}
