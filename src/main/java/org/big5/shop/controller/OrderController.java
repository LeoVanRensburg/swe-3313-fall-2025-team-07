package org.big5.shop.controller;

import jakarta.servlet.http.HttpSession;
import org.big5.shop.model.Database;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    private long sessionUserId(HttpSession session){
        return (Long) session.getAttribute("userId");
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model, HttpSession session) {
        if(session.getAttribute("userId") == null){
            return "redirect:/login";
        }
        List<Database.CartItem> cartItems = Database.getCartItems(sessionUserId(session));

        List<CartController.CartItemDTO> items  = cartItems.stream().map(CartController.CartItemDTO::new).collect(Collectors.toList());

        double subtotal = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();

        //Adjust tax %
        double tax = subtotal * 0.01;

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
                             @RequestParam String lastname,
                             @RequestParam String streetAddress,
                             @RequestParam String aptSuite,
                             @RequestParam String city,
                             @RequestParam String state,
                             @RequestParam String zipcode,
                             @RequestParam String phoneNumber,
                             @RequestParam String cardNumber){
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        } try {
            Database.Order order = Database.createOrder(sessionUserId(session),
                    shippingMethod,
                    firstName,
                    lastname,
                    streetAddress,
                    aptSuite,
                    city,
                    state,
                    zipcode,
                    phoneNumber,
                    cardNumber);

            model.addAttribute("order", order);
            return "order-confirmation";
        }catch (IllegalArgumentException e){
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }
}
