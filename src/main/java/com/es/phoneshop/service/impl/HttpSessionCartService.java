package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.CartItem;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.service.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Optional;

public class HttpSessionCartService implements CartService {

    private static final String SESSION_CART_KEY = "cartKey";
    private ProductDao productDao;

    private HttpSessionCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static HttpSessionCartService getInstance() {
        return CartServiceHolder.instance;
    }

    private static class CartServiceHolder {
        private static final HttpSessionCartService instance = new HttpSessionCartService();
    }

    public Cart getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(SESSION_CART_KEY);

        if (cart == null) {
            cart = new Cart();
            session.setAttribute(SESSION_CART_KEY, cart);
        }

        return cart;
    }

    @Override
    public void add(Cart cart, long productId, int quantity) throws OutOfStockException, IllegalArgumentException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid value");
        }

        Product product = productDao.getProduct(productId);

        Optional<CartItem> cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        int quantityInCart = cartItem.map(CartItem::getQuantity).orElse(0);

        if (product.getStock() < quantity + quantityInCart) {
            throw new OutOfStockException("There are not enough products in stock. Number of products: " +
                    product.getStock());
        }

        if (cartItem.isPresent()) {
            cartItem.get().setQuantity(cartItem.get().getQuantity() + quantity);
        } else {
            cart.getCartItems().add(new CartItem(product, quantity));
        }

        recalculateTotalPrice(cart);
    }

    @Override
    public void update(Cart cart, long productId, int quantity) throws OutOfStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid value");
        }

        Product product = productDao.getProduct(productId);

        Optional<CartItem> cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (product.getStock() < quantity) {
            throw new OutOfStockException("There are not enough products in stock. Number of products: " +
                    product.getStock());
        }

        if (cartItem.isPresent()) {
            cartItem.get().setQuantity(quantity);
            recalculateTotalPrice(cart);
        }
    }

    @Override
    public void delete(Cart cart, long productId) {
        cart.getCartItems().removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));
        recalculateTotalPrice(cart);
    }

    void recalculateTotalPrice(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice);
    }
}
