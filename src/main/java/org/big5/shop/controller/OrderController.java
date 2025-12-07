package org.big5.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.big5.shop.model.Database;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.io.PrintWriter;

@Controller
public class OrderController {

    private Long sessionId(HttpSession session){
        return (Long) session.getAttribute("userId");
    }

    private boolean isAdmin(HttpSession session){
        Boolean admin = (Boolean) session.getAttribute("isAdmin");
        return admin != null && admin;
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model, HttpSession session,
                               @RequestParam(name = "shipping", required = false) String shipping) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        if (Database.getCartItems((Long) session.getAttribute("userId")).isEmpty()) {
            return "redirect:/cart";
        }

        List<Database.CartItem> cartItems = Database.getCartItems(sessionId(session));

        List<CartController.CartItemDTO> items  = cartItems.stream().map(CartController.CartItemDTO::new).collect(Collectors.toList());

        double subtotal = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        double tax = subtotal * 0.08;

        String shippingMethod = getShipping(shipping);
        double shippingCost = getShippingPrice(shippingMethod);

        model.addAttribute("items", items);
        model.addAttribute("tax", tax);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shippingMethod", shippingMethod);
        model.addAttribute("shippingCost", shippingCost);
        model.addAttribute("total", subtotal + tax + shippingCost);

        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(HttpSession session,
                             Model model,
                             @RequestParam String shippingMethod,
                             @RequestParam(required = false) String phoneNumber,
                             @RequestParam String cardNumber){
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        Long userId = sessionId(session);

        String firstName = clean((String) session.getAttribute("shipFirstName"));
        String lastName = clean((String) session.getAttribute("shipLastName"));
        String streetAddress = clean((String) session.getAttribute("shipStreetAddress"));
        String aptSuite = clean((String) session.getAttribute("shipAptSuite"));
        String city = clean((String) session.getAttribute("shipCity"));
        String state = cleanState((String) session.getAttribute("shipState"));
        String zipCode = clean((String) session.getAttribute("shipZipCode"));

        if (firstName == null || lastName == null || streetAddress == null || city == null || state == null || zipCode == null) {
            return "redirect:/checkout";
        }

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
            String shipping = getShipping(shippingMethod);
            BigDecimal orderShippingCost = new BigDecimal(String.format("%.2f", getShippingPrice(shipping)));

            BigDecimal total = subtotal.add(tax).add(orderShippingCost);

            Database.Order order = Database.createOrder(
                    userId,
                    shipping,
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

            List<CartController.CartItemDTO> items  = cartItems.stream()
                    .map(CartController.CartItemDTO::new)
                    .collect(Collectors.toList());

            String cardLast4 = (cardNumber != null && cardNumber.length() >= 4)
                    ? cardNumber.substring(cardNumber.length() - 4)
                    : "0000";

            String email = Database.findUserById(userId).map(u -> u.email).orElse("");

            String cardBrand = "";

            char first = 0;

            if (cardNumber != null) {
                first = cardNumber.trim().charAt(0);
            }
            if (first == '4') {
                cardBrand = "visa.png";
            } else if (first == '3') {
                cardBrand = "americanexpress.png";
            } else if (first == '5') {
                cardBrand = "mastercard2.png";
            }

            model.addAttribute("order", order);
            model.addAttribute("items", items);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("tax", tax);
            model.addAttribute("shippingCost", orderShippingCost);
            model.addAttribute("total", total);
            model.addAttribute("shippingMethod", shipping);

            model.addAttribute("streetAddress", streetAddress);
            model.addAttribute("aptSuite", aptSuite);
            model.addAttribute("city", city);
            model.addAttribute("state", state);
            model.addAttribute("zipCode", zipCode);

            model.addAttribute("cardLast4", cardLast4);
            model.addAttribute("cardBrand", cardBrand);
            model.addAttribute("email", email);

            session.removeAttribute("shipFirstName");
            session.removeAttribute("shipLastName");
            session.removeAttribute("shipStreetAddress");
            session.removeAttribute("shipAptSuite");
            session.removeAttribute("shipCity");
            session.removeAttribute("shipState");
            session.removeAttribute("shipZipCode");

            return "order-confirmation";

        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "checkout";
        }
    }

    private String clean(String s) {
        if (s == null) return null;
        String t = s.trim();
        
        t = t.replaceAll("^[,\\s]+", "");
        
        t = t.replaceAll("[,\\s]+$", "");
        
        t = t.replaceAll("\\s{2,}", " ");
        
        return t;
    }

    private String cleanState(String s) {
        String t = clean(s);
        if (t != null){
            t = t.toUpperCase();
        }
        return t;
    }

    @GetMapping("/checkout/payment")
    public String showCheckoutPayment(Model model, HttpSession session,
                                      @RequestParam(name = "shipping", required = false) String shipping) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        if (Database.getCartItems((Long) session.getAttribute("userId")).isEmpty()) {
            return "redirect:/cart";
        }

        Boolean paymentFlow = (Boolean) session.getAttribute("paymentFlow");
        if (paymentFlow == null || !paymentFlow) {
            return "redirect:/checkout";
        }

        session.removeAttribute("paymentFlow");

        List<Database.CartItem> cartItems = Database.getCartItems(sessionId(session));
        List<CartController.CartItemDTO> items  = cartItems.stream().map(CartController.CartItemDTO::new).collect(Collectors.toList());

        double subtotal = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        double tax = subtotal * 0.08;

        String shippingMethod = getShipping(shipping);
        double shippingCost = getShippingPrice(shippingMethod);

        model.addAttribute("items", items);
        model.addAttribute("tax", tax);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shippingMethod", shippingMethod);
        model.addAttribute("shippingCost", shippingCost);
        model.addAttribute("total", subtotal + tax + shippingCost);

        return "checkout-payment";
    }

    @PostMapping("/checkout/payment/start")
    public String beginPayment(@RequestParam(name = "shipping", required = false) String shipping,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String streetAddress,
                               @RequestParam(required = false) String aptSuite,
                               @RequestParam String city,
                               @RequestParam String state,
                               @RequestParam String zipCode,
                               HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        List<Database.CartItem> cart = Database.getCartItems(sessionId(session));
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        session.setAttribute("paymentFlow", Boolean.TRUE);

        session.setAttribute("shipFirstName", (firstName));
        session.setAttribute("shipLastName", (lastName));
        session.setAttribute("shipStreetAddress", (streetAddress));
        session.setAttribute("shipAptSuite", (aptSuite));
        session.setAttribute("shipCity", (city));
        session.setAttribute("shipState", (state));
        session.setAttribute("shipZipCode", (zipCode));

        String method = getShipping(shipping);
        String query = (method != null && !method.isBlank()) ? ("?shipping=" + method) : "";
        return "redirect:/checkout/payment" + query;
    }

    private String getShipping(String method) {
        if (method == null || method.isBlank()) return "Ground";
        String m = method.trim().toLowerCase();
        if (m.contains("overnight")) return "Overnight";
        if (m.contains("3") || m.contains("three")) return "3-Day";
        return "Ground";
    }

    private double getShippingPrice(String shipping) {
        if(Objects.equals(shipping, "Overnight")){
            return 29.00;
        }
        else if (Objects.equals(shipping, "3-Day")){
            return 19.00;
        }
        else {
            return 0.00;
        }
    }
}
