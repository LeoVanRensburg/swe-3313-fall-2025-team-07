package org.big5.shop.controller;
import jakarta.servlet.http.HttpSession;
import org.big5.shop.model.Database;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("cartCount")
    public int getCartCount(HttpSession session) {
        if (session.getAttribute("userId") == null){
            return 0;
        }
        Long userId = (Long) session.getAttribute("userId");
        List<Database.CartItem> items = Database.getCartItems(userId);
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.size();
    }
}
