package com.example.ProjectSprintFrontend.controller;



import com.example.ProjectSprintFrontend.dto.OrderDetailDto;
import com.example.ProjectSprintFrontend.dto.OrderDto;
import com.example.ProjectSprintFrontend.dto.OrdersPageResponse;
import com.example.ProjectSprintFrontend.dto.PageMetaDto;
import com.example.ProjectSprintFrontend.service.OrderApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class OrderViewController {

    private final OrderApiService apiService;

    public OrderViewController(OrderApiService apiService) {
        this.apiService = apiService;
    }

    // ── Orders list ────────────────────────────────────────────────────────
    @GetMapping("/ui/orders")
    public String ordersList(
            @RequestParam(defaultValue = "0")           int     page,
            @RequestParam(defaultValue = "10")          int     size,
            @RequestParam(defaultValue = "orderNumber") String  sort,
            @RequestParam(defaultValue = "asc")         String  dir,
            @RequestParam(required = false)             String  status,
            @RequestParam(required = false)             String  search,
            @RequestParam(required = false)             Boolean addOrder,
            Model model
    ) {
        OrdersPageResponse response;

        if (search != null && !search.isBlank()) {
            try {
                int customerNumber = Integer.parseInt(search.trim());
                response = apiService.searchByCustomer(customerNumber, page, size, sort, dir);
            } catch (NumberFormatException e) {
                response = apiService.getOrders(page, size, sort, dir, status);
                model.addAttribute("searchWarn", "Enter a numeric Customer # to search.");
            }
        } else {
            response = apiService.getOrders(page, size, sort, dir, status);
        }

        PageMetaDto meta          = response.getPage();
        int         totalPages    = meta != null ? meta.getTotalPages()    : 0;
        long        totalElements = meta != null ? meta.getTotalElements() : 0;

        long statShipped   = apiService.countByStatus("Shipped");
        long statInProcess = apiService.countByStatus("In Process")
                + apiService.countByStatus("Processing");
        long statCancelled = apiService.countByStatus("Cancelled");

        model.addAttribute("orders",        response.getOrders());
        model.addAttribute("currentPage",   page);
        model.addAttribute("totalPages",    totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("sort",          sort);
        model.addAttribute("dir",           dir);
        model.addAttribute("status",        status);
        model.addAttribute("size",          size);
        model.addAttribute("search",        search);
        model.addAttribute("addOrder",      Boolean.TRUE.equals(addOrder));
        model.addAttribute("statShipped",   statShipped);
        model.addAttribute("statInProcess", statInProcess);
        model.addAttribute("statCancelled", statCancelled);

        return "list";
    }

    // ── Create order ───────────────────────────────────────────────────────
    @PostMapping("/ui/orders/create")
    public String createOrder(
            @RequestParam Integer orderNumber,
            @RequestParam Integer customerNumber,
            @RequestParam String  orderDate,
            @RequestParam String  requiredDate,
            @RequestParam(required = false) String shippedDate,
            @RequestParam String  status,
            @RequestParam(required = false) String comments,
            RedirectAttributes redirectAttrs
    ) {
        try {
            apiService.createOrder(orderNumber, customerNumber, orderDate, requiredDate,
                    shippedDate, status, comments);
            redirectAttrs.addFlashAttribute("successMsg",
                    "Order #" + orderNumber + " created successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg",
                    "Could not create order: " + e.getMessage());
            redirectAttrs.addFlashAttribute("addOrder", true);
        }
        return "redirect:/ui/orders";
    }

    // ── Update order ───────────────────────────────────────────────────────
    @PostMapping("/ui/orders/{id}/update")
    public String updateOrder(
            @PathVariable Integer id,
            @RequestParam String  orderDate,
            @RequestParam String  requiredDate,
            @RequestParam(required = false) String shippedDate,
            @RequestParam String  status,
            @RequestParam(required = false) String comments,
            RedirectAttributes redirectAttrs
    ) {
        try {
            apiService.updateOrder(id, orderDate, requiredDate, shippedDate, status, comments);
            redirectAttrs.addFlashAttribute("successMsg", "Order #" + id + " updated successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg",
                    "Could not update order: " + e.getMessage());
            return "redirect:/ui/orders/" + id + "?edit=true";
        }
        return "redirect:/ui/orders/" + id;
    }

    // ── Order detail ───────────────────────────────────────────────────────
    @GetMapping("/ui/orders/{id}")
    public String orderDetail(
            @PathVariable Integer id,
            @RequestParam(required = false) Boolean edit,
            Model model
    ) {
        OrderDto order = apiService.getOrder(id);
        if (order == null) return "redirect:/ui/orders";

        List<OrderDetailDto> details = apiService.getOrderDetails(id);
        BigDecimal orderTotal = details.stream()
                .map(d -> d.getTotalPrice() != null ? d.getTotalPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("order",      order);
        model.addAttribute("details",    details);
        model.addAttribute("orderTotal", orderTotal);
        model.addAttribute("edit",       Boolean.TRUE.equals(edit));

        return "details";
    }
}
