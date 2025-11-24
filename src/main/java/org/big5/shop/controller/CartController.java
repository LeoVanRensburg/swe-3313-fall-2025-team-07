package org.big5.shop.controller;

import jakarta.servlet.http.HttpSession;
import org.big5.shop.model.Database;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CartController {

    //UserID
    private Long sessionUserId(HttpSession session){
        return (Long) session.getAttribute("userId");
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {

        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        List<Database.CartItem> cartItems = Database.getCartItems(sessionUserId(session));

        List<CartItemDTO> items = cartItems.stream().map(CartItemDTO::new).collect(Collectors.toList());

        double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();

        model.addAttribute("items", items);
        model.addAttribute("total", total);

        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("itemId") Long itemId, HttpSession session){

        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Database.addToCart(sessionUserId(session), itemId);

        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("cartItemId") Long cartItemId, HttpSession session){

        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Database.removeFromCart(cartItemId);

        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session){

        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Database.clearCart(sessionUserId(session));

        return "redirect:/cart";
    }


    public static class CartItemDTO{
        private Long id;
        private Long itemId;
        private String name;
        private double price;
        private int quantity;
        private String image;
        private String category;

        public CartItemDTO(Database.CartItem CartItem){
            this.id = CartItem.id;
            this.itemId = CartItem.itemId;
            this.quantity = CartItem.quantity != null ? CartItem.quantity : 1;

            if(CartItem.item != null){
                this.name = CartItem.item.name;
                this.price = CartItem.item.price.doubleValue();
                this.image = CartItem.item.imagePath;
                this.category = CartItem.item.category;
            }
        }

        public long getId(){ return id;}
        public long getItemId(){ return itemId;}
        public String getName(){ return name;}
        public double getPrice(){ return price;}
        public int getQuantity(){ return quantity;}
        public String getImage(){ return image;}
        public String getCategory(){ return category;}

    }

}
