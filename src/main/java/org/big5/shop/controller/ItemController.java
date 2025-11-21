package org.big5.shop.controller;

import org.big5.shop.model.Database;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ItemController {

    @GetMapping("/product/{id}")
    public String itemDetail(@PathVariable Long id, Model model) {
        Optional<Database.Item> itemOptional = Database.findItemById(id);

        if (itemOptional.isEmpty()) {
            return "redirect:/";
        }

        Database.Item item = itemOptional.get();

        // Get suggestions - items from the same category or random items
        List<Database.Item> allItems = Database.getAvailableItems();
        List<Database.Item> suggestions = allItems.stream()
            .filter(i -> !i.id.equals(id)) // Exclude current item
            .filter(i -> i.category != null && i.category.equals(item.category)) // Same category
            .limit(3)
            .collect(Collectors.toList());

        // If we don't have enough suggestions from the same category, add more random items
        if (suggestions.size() < 3) {
            suggestions = allItems.stream()
                .filter(i -> !i.id.equals(id))
                .limit(3)
                .collect(Collectors.toList());
        }

        // Create a DTO for Thymeleaf
        ItemDTO itemDTO = new ItemDTO(item);

        model.addAttribute("item", itemDTO);
        model.addAttribute("suggestions", suggestions.stream()
            .map(ItemDTO::new)
            .collect(Collectors.toList()));

        return "item-detail";
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
