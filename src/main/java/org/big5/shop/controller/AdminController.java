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
public class AdminController {

    private Long sessionId(HttpSession session){
        return (Long) session.getAttribute("userId");
    }


    private boolean isAdmin(HttpSession session){
        Boolean admin = (Boolean) session.getAttribute("isAdmin");
        return admin != null && admin;
    }

    //If a user enters this page, redirect
    private String ifNotAdmin(HttpSession session){
        return (sessionId(session) == null || !isAdmin(session)) ? "redirect:/login" : null;
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session){
        if (ifNotAdmin(session) != null){
            return ifNotAdmin(session);
        }
        return "admin-dashboard";
    }

    // =======
    //ADD ITEM
    // =======
    @GetMapping("/admin/add-items")
    public String addItem(Model model,
                          HttpSession session,
                          @RequestParam String name,
                          @RequestParam String category,
                          @RequestParam String price,
                          @RequestParam String imagePath,
                          @RequestParam String description){

        if (ifNotAdmin(session) != null){
            return ifNotAdmin(session);
        }

        try{
            BigDecimal priceValue = new BigDecimal(price);

            Database.createItem(name, priceValue, description, imagePath, category);

            return "redirect:/admin";

        } catch (NumberFormatException e){
            model.addAttribute("error", "Invalid price");
            model.addAttribute("name", name);
            model.addAttribute("price", price);
            model.addAttribute("description", description);
            model.addAttribute("imagePath", imagePath);
            model.addAttribute("category", category);
            return "add-items";
        }
    }

    // ===========
    //MANAGE USERS
    // ===========
    @GetMapping("/admin/manage-users")
    public String manageUsers(Model model, HttpSession session){
        if (ifNotAdmin(session) != null){
            return ifNotAdmin(session);
        }
        List<Database.User> users = Database.getAllUsers();
        model.addAttribute("users", users);
        return "manage-users";
    }

    @PostMapping("/admin/manage-users/{id}/promote")
    public String promoteUser(@PathVariable("id") Long id, HttpSession session){
        if (ifNotAdmin(session) != null){
            return ifNotAdmin(session);
        }
        Database.updateAdminStatus(id, true);
        return "redirect:/admin/manage-users";
    }

    @PostMapping("admin/manage-users/{id}/demote")
    public String demoteAdmin(@PathVariable("id") Long id, HttpSession session){
        if (ifNotAdmin(session) != null){
            return ifNotAdmin(session);
        }
        Database.updateAdminStatus(id, false);
        return "redirect:/admin/manage-users";
    }

    // ============
    //SALES REPORT
    // ============
    @GetMapping("/admin/sales-report")
    public String showSalesReport(Model model, HttpSession session) {
        if (sessionId(session) == null || !isAdmin(session)) {
            return "redirect:/login";
        }

        List<Database.SalesReportItem> rows = Database.getSalesReport();
        model.addAttribute("rows", rows);

        return "sales-report";
    }

    @GetMapping("/admin/sales-report/{orderId}")
    public String viewReceipt(@PathVariable Long orderId, Model model, HttpSession session) {
        if (sessionId(session) == null || !isAdmin(session)) {
            return "redirect:/login";
        }

        Optional<Database.OrderReceipt> receiptOptional = Database.getReceipt(orderId);
        if (receiptOptional.isPresent()) {
            model.addAttribute("receipt", receiptOptional.get());
            return "sales-receipt";
        }

        return "redirect:/admin/sales-report";
    }

    @GetMapping("/admin/sales-report/download")
    public void downloadSalesReport(HttpSession session, HttpServletResponse response) throws IOException {
        if (sessionId(session) == null || !isAdmin(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Not authorized to download sales-report");
            return;
        }

        List<Database.SalesReportItem> rows = Database.getSalesReport();
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=sales-report.csv");

        try(PrintWriter writter = response.getWriter()){
            writter.println("orderID,orderDate,purchaserEmail,itemName,itemPrice,quantity,lineTotal");

            for (Database.SalesReportItem item : rows) {
                BigDecimal lineTotal = item.itemPrice.multiply(new BigDecimal(item.quantity));

                writter.printf(
                        "%s,%s,%s,\"%s\",%s,%s,%s%n",
                        item.orderId,
                        item.date,
                        item.purchaserEmail,
                        item.itemName.replace("\"", "'"),
                        item.itemPrice.toPlainString(),
                        item.quantity,
                        lineTotal.toPlainString()
                );
            }
        }

    }
}
