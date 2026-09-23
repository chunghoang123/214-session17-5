package org.example.orderservice.service;

import org.example.orderservice.entity.Order;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Cacheable(value = "orders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Cacheable(value = "order", key = "#id")
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Caching(
            put = {@CachePut(value = "order", key = "#result.id")},
            evict = {@CacheEvict(value = "orders", allEntries = true)}
    )
    @Transactional
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Caching(
            put = {@CachePut(value = "order", key = "#id")},
            evict = {@CacheEvict(value = "orders", allEntries = true)}
    )
    @Transactional
    public Order updateOrder(Long id, Order orderDetails) {
        Order order = getOrderById(id);
        order.setCustomerName(orderDetails.getCustomerName());
        order.setProductName(orderDetails.getProductName());
        order.setQuantity(orderDetails.getQuantity());
        order.setPrice(orderDetails.getPrice());
        if (orderDetails.getStatus() != null) {
            order.setStatus(orderDetails.getStatus());
        }
        return orderRepository.save(order);
    }
    @Caching(evict = {
            @CacheEvict(value = "order", key = "#id"),
            @CacheEvict(value = "orders", allEntries = true)
    })
    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }
}
