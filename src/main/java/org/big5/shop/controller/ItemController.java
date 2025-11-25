package org.big5.shop.controller;

import jakarta.servlet.http.HttpSession;
import org.big5.shop.model.Database;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class ItemController {

    @GetMapping("/product/{id}")
    public String itemDetail(@PathVariable Long id, Model model, HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Optional<Database.Item> itemOptional = Database.findItemById(id);

        if (itemOptional.isEmpty()) {
            return "redirect:/";
        }

        Database.Item item = itemOptional.get();

        // Get suggestions - items from the same category or random items
        List<Database.Item> allItems = new ArrayList<>(Database.getAvailableItems());
        allItems.removeIf(i -> i.id != null && i.id.equals(id));
        Collections.shuffle(allItems);

        List<Database.Item> suggestions = allItems.stream()
                .filter(i -> Objects.equals(i.category, item.category))
                .limit(3)
                .collect(Collectors.toList());

        // If we don't have enough suggestions from the same category, add more random items
        if (suggestions.size() < 3) {
            int remaining = 3 - suggestions.size();
            List<Long> chosenIds = suggestions.stream().map(i -> i.id).collect(Collectors.toList());
            List<Database.Item> fillers = allItems.stream()
                    .filter(i -> !chosenIds.contains(i.id))
                    .limit(remaining)
                    .collect(Collectors.toList());
            suggestions.addAll(fillers);
        }

        // Create a DTO for Thymeleaf
        ItemDTO itemDTO = new ItemDTO(item);

        model.addAttribute("item", itemDTO);
        model.addAttribute("suggestions", suggestions.stream()
            .map(ItemDTO::new)
            .collect(Collectors.toList()));

        return "item-detail";
    }

    @GetMapping("/search")
    public String searchItems(@RequestParam(name = "q", required = false) String q, Model model, HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        List<Database.Item> results;
        if (q == null || q.isBlank()) {
            return "redirect:/";
        } else {
            results = Database.searchItems(q);
        }

        List<ItemDTO> items = results.stream()
            .map(ItemDTO::new)
            .collect(Collectors.toList());

        model.addAttribute("items", items);
        model.addAttribute("query", q);
        return "search-results";
    }

    // DTO to expose fields to Thymeleaf
    public static class ItemDTO {
        private Long id;
        private String name;
        private String price;
        private String description;
        private String image;
        private String category;

        public ItemDTO(Database.Item item) {
            this.id = item.id;
            this.name = item.name;
            this.price = item.price.toString();
            this.description = item.description;
            this.image = item.imagePath;
            this.category = item.category;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getPrice() { return price; }
        public String getDescription() { return description; }
        public String getImage() { return image; }
        public String getCategory() { return category; }
    }
}
