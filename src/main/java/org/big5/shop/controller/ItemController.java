package org.big5.shop.controller;

import jakarta.servlet.http.HttpSession;
import org.big5.shop.model.Database;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ItemController {

    @GetMapping("/product/{id}")
    public String itemDetail(@PathVariable Long id, Model model, HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Long userId = (Long) session.getAttribute("userId");
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

        Set<Long> cartItemIds = Database.getCartItems(userId).stream()
                .map(ci -> ci.itemId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        model.addAttribute("cartItemIds", cartItemIds);

        return "item-detail";
    }

    @GetMapping("/search")
    public String searchItems(@RequestParam(name = "q", required = false) String q, Model model, HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Long userId = (Long) session.getAttribute("userId");
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

        Set<Long> cartItemIds = Database.getCartItems(userId).stream()
                .map(ci -> ci.itemId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        model.addAttribute("cartItemIds", cartItemIds);
        return "search-results";
    }

    @GetMapping({"/categories"})
    public String categories(Model model, HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "categories";
    }

    @GetMapping("/category/{name}")
    public String category(@PathVariable("name") String name, Model model, HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Long userId = (Long) session.getAttribute("userId");

        List<Database.Item> items = Database.getAvailableItems().stream()
                .filter(i -> i.category != null && i.category.equalsIgnoreCase(name))
                .sorted(Comparator.comparing((Database.Item i) -> i.price).reversed())
                .collect(Collectors.toList());

        List<ItemDTO> itemDTOs = items.stream().map(ItemDTO::new).collect(Collectors.toList());

        model.addAttribute("categoryName", name);
        model.addAttribute("items", itemDTOs);

        Set<Long> cartItemIds = Database.getCartItems(userId).stream()
                .map(ci -> ci.itemId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        model.addAttribute("cartItemIds", cartItemIds);
        return "category";
    }

    @GetMapping("/items")
    public String allItems(Model model, HttpSession session) {
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Long userId = (Long) session.getAttribute("userId");
        List<Database.Item> allItems = new ArrayList<>(Database.getAvailableItems());

        List<ItemDTO> items = allItems.stream()
                .map(ItemDTO::new)
                .collect(Collectors.toList());

        model.addAttribute("items", items);

        Set<Long> cartItemIds = Database.getCartItems(userId).stream()
                .map(ci -> ci.itemId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        model.addAttribute("cartItemIds", cartItemIds);
        return "items";
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
