package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.Product;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class ProductDemodataServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String demodata = servletContextEvent.getServletContext().getInitParameter("demodata");

        if (demodata.equalsIgnoreCase("enable")) {
            ProductDao productDao = ArrayListProductDao.getInstance();
            getSampleProducts().forEach(productDao::save);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private List<Product> getSampleProducts() {
        List<Product> products = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");
        products.add(new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 3000, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        products.add(new Product(2L, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        products.add(new Product(3L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        products.add(new Product(4L, "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        products.add(new Product(5L, "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        products.add(new Product(6L, "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        products.add(new Product(7L, "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        products.add(new Product(8L, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        products.add(new Product(9L, "nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        products.add(new Product(10L, "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        products.add(new Product(11L, "simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        products.add(new Product(12L, "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        products.add(new Product(13L, "simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));

        List<Map<LocalDate, BigDecimal>> priceHistories = Arrays.asList(
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2018, Month.APRIL, 27), new BigDecimal(140));
                    put(LocalDate.of(2019, Month.DECEMBER, 21), new BigDecimal(135));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2018, Month.MARCH, 18), new BigDecimal(215));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2017, Month.SEPTEMBER, 5), new BigDecimal(340));
                    put(LocalDate.of(2018, Month.FEBRUARY, 1), new BigDecimal(320));
                    put(LocalDate.of(2019, Month.AUGUST, 13), new BigDecimal(329));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2018, Month.NOVEMBER, 27), new BigDecimal(259));
                    put(LocalDate.of(2019, Month.FEBRUARY, 21), new BigDecimal(230));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2018, Month.MAY, 18), new BigDecimal(1130));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2018, Month.MAY, 8), new BigDecimal(350));
                    put(LocalDate.of(2019, Month.OCTOBER, 12), new BigDecimal(310));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2018, Month.JULY, 15), new BigDecimal(455));
                    put(LocalDate.of(2019, Month.DECEMBER, 3), new BigDecimal(449));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2018, Month.JUNE, 30), new BigDecimal(160));
                    put(LocalDate.of(2018, Month.SEPTEMBER, 28), new BigDecimal(149));
                    put(LocalDate.of(2019, Month.MARCH, 14), new BigDecimal(155));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2017, Month.APRIL, 19), new BigDecimal(115));
                    put(LocalDate.of(2019, Month.MAY, 1), new BigDecimal(85));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2019, Month.NOVEMBER, 23), new BigDecimal(195));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2019, Month.MARCH, 7), new BigDecimal(100));
                    put(LocalDate.of(2020, Month.JANUARY, 9), new BigDecimal(115));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2017, Month.SEPTEMBER, 18), new BigDecimal(95));
                    put(LocalDate.of(2018, Month.AUGUST, 3), new BigDecimal(90));
                    put(LocalDate.of(2019, Month.FEBRUARY, 26), new BigDecimal(95));
                }},
                new HashMap<LocalDate, BigDecimal>() {{
                    put(LocalDate.of(2018, Month.OCTOBER, 28), new BigDecimal(200));
                    put(LocalDate.of(2019, Month.MARCH, 25), new BigDecimal(165));
                }}
        );

        for (int i = 0; i < products.size(); i++) {

            if (priceHistories.size() > i) {
                priceHistories.get(i).put(LocalDate.of(2020, Month.FEBRUARY, 10), products.get(i).getPrice());
                products.get(i).setPriceHistory(priceHistories.get(i));
            }
        }

        return products;
    }
}
