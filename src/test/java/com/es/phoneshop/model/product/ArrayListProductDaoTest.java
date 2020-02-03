package com.es.phoneshop.model.product;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ArrayListProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
    }

    @After
    public void tearDown() {
        productDao.setProducts(new ArrayList<>());
    }

    @Test
    public void testGetProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        productDao.save(product);

        assertNotNull(productDao.getProduct(1L));
    }

    @Test
    public void testFindProducts() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal(20));
        product.setStock(31);
        productDao.save(product);

        assertEquals(1L, (long) productDao.findProducts().get(0).getId());
    }

    @Test
    public void testFindProductsNoResults() {
        assertTrue(productDao.findProducts().isEmpty());
    }

    @Test
    public void testFindNullPriceProducts() {
        Product product = new Product();
        product.setId(1L);
        product.setStock(20);

        assertEquals(0, productDao.findProducts().size());
    }

    @Test
    public void testFindZeroStockProducts() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal(42));

        assertEquals(0, productDao.findProducts().size());
    }

    @Test
    public void testSaveProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal(20));
        product.setStock(31);
        productDao.save(product);

        assertEquals(1, productDao.findProducts().size());
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product();
        product.setId(1L);
        productDao.save(product);
        productDao.delete(1L);

        assertEquals(0, productDao.findProducts().size());
    }
}
