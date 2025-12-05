package org.big5.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.big5.shop.model.Database;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

import java.io.IOException;

@Controller
public class OrderController {

    private Long sessionUserId(HttpSession session){
        return (Long) session.getAttribute("userId");
    }

    private boolean isAdmin(HttpSession session){
        Boolean admin = (Boolean) session.getAttribute("isAdmin");
        return admin != null && admin;
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model, HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        List<Database.CartItem> cartItems = Database.getCartItems(sessionUserId(session));

        List<CartController.CartItemDTO> items  = cartItems.stream().map(CartController.CartItemDTO::new).collect(Collectors.toList());

        double subtotal = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        double tax = subtotal * 0.08;

        model.addAttribute("items", items);
        model.addAttribute("tax", tax);
        model.addAttribute("subtotal", subtotal);

        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(HttpSession session,
                             Model model,
                             @RequestParam String shippingMethod,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String streetAddress,
                             @RequestParam(required = false) String aptSuite,
                             @RequestParam String city,
                             @RequestParam String state,
                             @RequestParam String zipCode,
                             @RequestParam String phoneNumber,
                             @RequestParam String cardNumber){
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        Long userId = sessionUserId(session);

        try {
            List<Database.CartItem> cartItems = Database.getCartItems(userId);
            if(cartItems.isEmpty()){
                model.addAttribute("error", "Your cart is empty.");
                return "checkout";
            }

            BigDecimal subtotal = new BigDecimal(0);
            for (Database.CartItem item : cartItems) {
                if (item.item != null && item.item.price != null && item.quantity != null) {
                    subtotal = subtotal.add(item.item.price.multiply(new BigDecimal(item.quantity)));
                }
            }

            BigDecimal tax = subtotal.multiply(new BigDecimal("0.08"));
            BigDecimal orderShippingCost;
            String orderShippingMethod = shippingMethod.toLowerCase();

            if(orderShippingMethod.equals("express")){
                orderShippingCost = new BigDecimal("12.99");
            }else if (orderShippingMethod.equals("overnight")){
                orderShippingCost = new BigDecimal("24.99");
            }else {
                orderShippingCost = new BigDecimal("5.99");
            }

            BigDecimal total = subtotal.add(tax).add(orderShippingCost);

            Database.Order order = Database.createOrder(
                    userId,
                    shippingMethod,
                    firstName,
                    lastName,
                    streetAddress,
                    aptSuite,
                    city,
                    state,
                    zipCode,
                    phoneNumber,
                    cardNumber
            );

            model.addAttribute("order", order);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("tax", tax);
            model.addAttribute("shippingCost", orderShippingCost);
            model.addAttribute("total", total);

            return "order-confirmation";

        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "checkout";
        }
    }

    // Static until checkout is finished
    @GetMapping("/order-confirmation")
    public String showOrderConfirmation(/* HttpSession session */) {
//        if (session == null || session.getAttribute("userId") == null) {
//            return "redirect:/login";
//        }
        return "order-confirmation";
    }

    @GetMapping("/admin/sales-report")
    public String showSalesReport(Model model, HttpSession session, @RequestParam boolean isAdmin) {
        if (sessionUserId(session) == null || !isAdmin(session)) {
            return "redirect:/login";
        }



        return "sales-report";
    }

    @GetMapping("/admin/sales-report/download")
    public void downloadSalesReport(HttpSession session, HttpServletResponse response) throws IOException {
        if (sessionUserId(session) == null || !isAdmin(session)) {
            return;
        }



    }

}
