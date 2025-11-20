package org.big5.shop.model;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:big5shop.db";
    private static boolean initialized = false;

    // ==================================================================================
    // INITIALIZATION
    // ==================================================================================

    static {
        initSchema();
    }

    private static void initSchema() {
        if (initialized) return;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA journal_mode=WAL");

            // 1. Users
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "is_admin INTEGER NOT NULL DEFAULT 0)");

            // 2. Items
            stmt.execute("CREATE TABLE IF NOT EXISTS items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "price DECIMAL(10,2) NOT NULL, " +
                    "description TEXT, " +
                    "image_path TEXT NOT NULL, " +
                    "sold INTEGER NOT NULL DEFAULT 0, " +
                    "category TEXT)");

            // 3. Cart
            stmt.execute("CREATE TABLE IF NOT EXISTS cart_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "item_id INTEGER NOT NULL, " +
                    "quantity INTEGER NOT NULL DEFAULT 1, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id), " +
                    "FOREIGN KEY (item_id) REFERENCES items(id))");

            // 4. Orders
            stmt.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "order_date TEXT NOT NULL, " +
                    "subtotal DECIMAL(10,2) NOT NULL, " +
                    "tax DECIMAL(10,2) NOT NULL, " +
                    "shipping_cost DECIMAL(10,2) NOT NULL, " +
                    "total DECIMAL(10,2) NOT NULL, " +
                    "shipping_method TEXT NOT NULL, " +
                    "shipping_first_name TEXT NOT NULL, " +
                    "shipping_last_name TEXT NOT NULL, " +
                    "shipping_street_address TEXT NOT NULL, " +
                    "shipping_apt_suite TEXT, " +
                    "shipping_city TEXT NOT NULL, " +
                    "shipping_state TEXT NOT NULL, " +
                    "shipping_zip_code TEXT NOT NULL, " +
                    "phone_number TEXT NOT NULL, " +
                    "card_last_four_digits TEXT NOT NULL, " +
                    "purchaser_email TEXT NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            // 5. Order Items
            stmt.execute("CREATE TABLE IF NOT EXISTS order_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "order_id INTEGER NOT NULL, " +
                    "item_id INTEGER NOT NULL, " +
                    "item_name TEXT NOT NULL, " +
                    "item_price DECIMAL(10,2) NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "FOREIGN KEY (order_id) REFERENCES orders(id))");

            // Indexes
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_users_email ON users(email)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_items_sold ON items(sold)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_cart_items_user_id ON cart_items(user_id)");

            initialized = true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }

    // ==================================================================================
    // INTERNAL HELPERS
    // ==================================================================================

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    @FunctionalInterface
    private interface DbOperation<T> {
        T execute(Connection conn) throws SQLException;
    }

    @FunctionalInterface
    private interface DbVoidOperation {
        void execute(Connection conn) throws SQLException;
    }

    private static <T> T transaction(DbOperation<T> op) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try {
                T result = op.execute(conn);
                conn.commit();
                return result;
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    private static void transactionVoid(DbVoidOperation op) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try {
                op.execute(conn);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    private static <T> T query(DbOperation<T> op) {
        try (Connection conn = getConnection()) {
            return op.execute(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Query failed: " + e.getMessage(), e);
        }
    }

    /**
     * SQLite specific helper to get the ID of the last inserted row.
     * This replaces getGeneratedKeys() which is flaky on some SQLite drivers.
     */
    private static long getLastInsertId(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Failed to retrieve last insert ID");
        }
    }

    // ==================================================================================
    // MODULE: USERS
    // ==================================================================================

    public static class User {
        public Long id;
        public String email;
        public String password;
        public boolean isAdmin;

        public User() {}
        public User(Long id, String email, String password, boolean isAdmin) {
            this.id = id;
            this.email = email;
            this.password = password;
            this.isAdmin = isAdmin;
        }
    }

    public static User createUser(String email, String password, boolean isAdmin) {
        return transaction(conn -> {
            if (userExistsByEmail(conn, email)) {
                throw new IllegalArgumentException("Email already exists");
            }

            String sql = "INSERT INTO users (email, password, is_admin) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                stmt.setInt(3, isAdmin ? 1 : 0);
                stmt.executeUpdate();

                // Use the manual query helper
                long id = getLastInsertId(conn);
                return new User(id, email, password, isAdmin);
            }
        });
    }

    public static Optional<User> findUserById(Long id) {
        return query(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
                return Optional.empty();
            }
        });
    }

    public static Optional<User> findUserByEmail(String email) {
        return query(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
                return Optional.empty();
            }
        });
    }

    private static boolean userExistsByEmail(Connection conn, String email) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public static Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOptional = findUserByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.password.equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private static User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("is_admin") == 1
        );
    }

    // ==================================================================================
    // MODULE: ITEMS
    // ==================================================================================

    public static class Item {
        public Long id;
        public String name;
        public BigDecimal price;
        public String description;
        public String imagePath;
        public boolean sold;
        public String category;

        public Item() {}
        public Item(Long id, String name, BigDecimal price, String description, String imagePath, boolean sold, String category) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.description = description;
            this.imagePath = imagePath;
            this.sold = sold;
            this.category = category;
        }
    }

    public static Item createItem(String name, BigDecimal price, String description, String imagePath, String category) {
        return transaction(conn -> {
            String sql = "INSERT INTO items (name, price, description, image_path, sold, category) VALUES (?, ?, ?, ?, 0, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setBigDecimal(2, price);
                stmt.setString(3, description);
                stmt.setString(4, imagePath);
                stmt.setString(5, category);
                stmt.executeUpdate();

                long id = getLastInsertId(conn);
                return new Item(id, name, price, description, imagePath, false, category);
            }
        });
    }

    public static Optional<Item> findItemById(Long id) {
        return query(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items WHERE id = ?")) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapItem(rs));
                }
                return Optional.empty();
            }
        });
    }

    public static List<Item> getAvailableItems() {
        return query(conn -> {
            List<Item> items = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items WHERE sold = 0 ORDER BY price DESC");
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) items.add(mapItem(rs));
            }
            return items;
        });
    }

    public static List<Item> searchItems(String keyword) {
        return query(conn -> {
            List<Item> items = new ArrayList<>();
            String sql = "SELECT * FROM items WHERE sold = 0 AND (LOWER(name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?)) ORDER BY price DESC";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                String pattern = "%" + keyword + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) items.add(mapItem(rs));
            }
            return items;
        });
    }

    private static void markItemAsSold(Connection conn, Long itemId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE items SET sold = 1 WHERE id = ?")) {
            stmt.setLong(1, itemId);
            stmt.executeUpdate();
        }
    }

    private static Item mapItem(ResultSet rs) throws SQLException {
        return new Item(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getString("description"),
                rs.getString("image_path"),
                rs.getInt("sold") == 1,
                rs.getString("category")
        );
    }

    // ==================================================================================
    // MODULE: CART
    // ==================================================================================

    public static class CartItem {
        public Long id;
        public Long userId;
        public Long itemId;
        public Integer quantity;
        public Item item;

        public CartItem() {}
        public CartItem(Long id, Long userId, Long itemId, Integer quantity) {
            this.id = id;
            this.userId = userId;
            this.itemId = itemId;
            this.quantity = quantity;
        }
    }

    public static void addToCart(Long userId, Long itemId) {
        transactionVoid(conn -> {
            Optional<Item> item = findItemById(itemId);
            if (item.isEmpty() || item.get().sold) {
                throw new IllegalArgumentException("Item not available");
            }

            String checkSql = "SELECT id, quantity FROM cart_items WHERE user_id = ? AND item_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setLong(1, userId);
                stmt.setLong(2, itemId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Long cartItemId = rs.getLong("id");
                    int newQuantity = rs.getInt("quantity") + 1;
                    try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE cart_items SET quantity = ? WHERE id = ?")) {
                        updateStmt.setInt(1, newQuantity);
                        updateStmt.setLong(2, cartItemId);
                        updateStmt.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO cart_items (user_id, item_id, quantity) VALUES (?, ?, 1)")) {
                        insertStmt.setLong(1, userId);
                        insertStmt.setLong(2, itemId);
                        insertStmt.executeUpdate();
                    }
                }
            }
        });
    }

    public static List<CartItem> getCartItems(Long userId) {
        return query(conn -> {
            List<CartItem> cartItems = new ArrayList<>();
            String sql = "SELECT c.id, c.user_id, c.item_id, c.quantity, i.name, i.price, i.description, i.image_path, i.sold, i.category " +
                    "FROM cart_items c JOIN items i ON c.item_id = i.id WHERE c.user_id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    CartItem cartItem = new CartItem(
                            rs.getLong("id"),
                            rs.getLong("user_id"),
                            rs.getLong("item_id"),
                            rs.getInt("quantity")
                    );
                    cartItem.item = new Item(
                            rs.getLong("item_id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            rs.getString("description"),
                            rs.getString("image_path"),
                            rs.getInt("sold") == 1,
                            rs.getString("category")
                    );
                    cartItems.add(cartItem);
                }
            }
            return cartItems;
        });
    }

    public static void removeFromCart(Long cartItemId) {
        transactionVoid(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM cart_items WHERE id = ?")) {
                stmt.setLong(1, cartItemId);
                stmt.executeUpdate();
            }
        });
    }

    public static void clearCart(Long userId) {
        transactionVoid(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM cart_items WHERE user_id = ?")) {
                stmt.setLong(1, userId);
                stmt.executeUpdate();
            }
        });
    }

    private static void clearCartInternal(Connection conn, Long userId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM cart_items WHERE user_id = ?")) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }

    // ==================================================================================
    // MODULE: ORDERS
    // ==================================================================================

    public static class Order {
        public Long id;
        public Long userId;
        public BigDecimal total;
        public String shippingMethod;
        public List<OrderItem> orderItems = new ArrayList<>();
    }

    public static class OrderItem {
        public Long id;
        public Long orderId;
        public Long itemId;
        public String itemName;
        public BigDecimal itemPrice;
        public Integer quantity;
    }

    public static Order createOrder(Long userId, String shippingMethod, String firstName, String lastName,
                                    String streetAddress, String aptSuite, String city, String state,
                                    String zipCode, String phoneNumber, String cardNumber) {
        return transaction(conn -> {
            List<CartItem> cartItems = getCartItems(userId);
            if (cartItems.isEmpty()) throw new IllegalArgumentException("Cart is empty");

            BigDecimal subtotal = BigDecimal.ZERO;
            for (CartItem ci : cartItems) {
                subtotal = subtotal.add(ci.item.price.multiply(BigDecimal.valueOf(ci.quantity)));
            }

            BigDecimal tax = subtotal.multiply(new BigDecimal("0.08"));

            BigDecimal shippingCost;
            String method = shippingMethod.toLowerCase();
            if (method.equals("express")) {
                shippingCost = new BigDecimal("12.99");
            } else if (method.equals("overnight")) {
                shippingCost = new BigDecimal("24.99");
            } else {
                shippingCost = new BigDecimal("5.99");
            }

            BigDecimal total = subtotal.add(tax).add(shippingCost);

            Optional<User> user = findUserById(userId);
            String email = user.map(u -> u.email).orElse("");

            String orderSql = "INSERT INTO orders (user_id, order_date, subtotal, tax, shipping_cost, total, " +
                    "shipping_method, shipping_first_name, shipping_last_name, shipping_street_address, " +
                    "shipping_apt_suite, shipping_city, shipping_state, shipping_zip_code, phone_number, " +
                    "card_last_four_digits, purchaser_email) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Long orderId;
            try (PreparedStatement stmt = conn.prepareStatement(orderSql)) {
                stmt.setLong(1, userId);
                stmt.setString(2, LocalDateTime.now().toString());
                stmt.setBigDecimal(3, subtotal);
                stmt.setBigDecimal(4, tax);
                stmt.setBigDecimal(5, shippingCost);
                stmt.setBigDecimal(6, total);
                stmt.setString(7, shippingMethod);
                stmt.setString(8, firstName);
                stmt.setString(9, lastName);
                stmt.setString(10, streetAddress);
                stmt.setString(11, aptSuite);
                stmt.setString(12, city);
                stmt.setString(13, state);
                stmt.setString(14, zipCode);
                stmt.setString(15, phoneNumber);
                stmt.setString(16, cardNumber.length() > 4 ? cardNumber.substring(cardNumber.length() - 4) : "0000");
                stmt.setString(17, email);
                stmt.executeUpdate();

                // Use manual helper
                orderId = getLastInsertId(conn);
            }

            String orderItemSql = "INSERT INTO order_items (order_id, item_id, item_name, item_price, quantity) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(orderItemSql)) {
                for (CartItem cartItem : cartItems) {
                    stmt.setLong(1, orderId);
                    stmt.setLong(2, cartItem.item.id);
                    stmt.setString(3, cartItem.item.name);
                    stmt.setBigDecimal(4, cartItem.item.price);
                    stmt.setInt(5, cartItem.quantity);
                    stmt.executeUpdate();

                    markItemAsSold(conn, cartItem.item.id);
                }
            }

            clearCartInternal(conn, userId);

            Order order = new Order();
            order.id = orderId;
            order.total = total;
            order.shippingMethod = shippingMethod;
            return order;
        });
    }

    // ==================================================================================
    // EXAMPLE USAGE
    // ==================================================================================

    public static void main(String[] args) {
        System.out.println("=== Database Demo ===\n");

        try {
            // User user = createUser("test@example.com", "password123", false);
            // System.out.println("✓ Created user: " + user.email);

            // Item item1 = createItem("Pokemon Card", new BigDecimal("99.99"), "Rare Charizard", "/img/card.jpg", "Toys");
            // Item item2 = createItem("Zelda Game", new BigDecimal("49.99"), "Switch Game", "/img/zelda.jpg", "Games");
            // System.out.println("✓ Created items.");

            // addToCart(user.id, item1.id);
            // addToCart(user.id, item2.id);
            // System.out.println("✓ Added items to cart.");

            // Order order = createOrder(user.id, "express", "John", "Doe", "123 Main St",
            //       "Apt 4", "Springfield", "IL", "62701", "555-1234", "4111222233334444");

            // System.out.println("✓ Order placed successfully!");
            // System.out.println("  Order ID: " + order.id);
            // System.out.println("  Total: $" + order.total);

            String value = String.valueOf(findUserByEmail("test@example.com").get().isAdmin);
            System.out.println(value);

            List<Item> items = getAvailableItems();
            for (Item item: items) {
                System.out.println(item.name);
                System.out.println(item.imagePath);
            }



        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}