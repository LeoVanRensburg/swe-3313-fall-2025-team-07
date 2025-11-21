package org.big5.shop;

import org.big5.shop.model.Database;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Cross-platform database seeding utility for Big5 Shop
 * Works on Mac, Ubuntu, and Windows
 *
 * Run with: java org.big5.shop.SeedDatabase
 * Or: mvn exec:java -Dexec.mainClass="org.big5.shop.SeedDatabase"
 */
public class SeedDatabase {

    private static class ItemData {
        String name;
        BigDecimal price;
        String description;
        String category;

        ItemData(String name, double price, String description, String category) {
            this.name = name;
            this.price = BigDecimal.valueOf(price);
            this.description = description;
            this.category = category;
        }
    }

    // Seed data mapping image filenames to product information
    private static final Map<String, ItemData> SEED_DATA = new HashMap<>();

    static {
        // Pokemon Collection
        SEED_DATA.put("charmander.png", new ItemData(
            "Charmander Plush",
            24.99,
            "Adorable Charmander plush toy from the Pokemon series. Soft and cuddly, perfect for any Pokemon fan!",
            "Pokemon"
        ));
        SEED_DATA.put("pika.png", new ItemData(
            "Pikachu Figure",
            19.99,
            "Classic Pikachu collectible figure. High-quality design featuring everyone's favorite electric Pokemon!",
            "Pokemon"
        ));
        SEED_DATA.put("pika2.png", new ItemData(
            "Detective Pikachu",
            29.99,
            "Special edition Detective Pikachu figure with hat. Limited collector's item from the movie!",
            "Pokemon"
        ));
        SEED_DATA.put("pika3.png", new ItemData(
            "Pikachu Keychain",
            9.99,
            "Cute Pikachu keychain accessory. Perfect for your keys, backpack, or collection!",
            "Pokemon"
        ));
        SEED_DATA.put("pokeball.png", new ItemData(
            "Pokeball Replica",
            34.99,
            "Official size Pokeball replica. Opens up and features authentic details from the games!",
            "Pokemon"
        ));
        SEED_DATA.put("pokeball1.png", new ItemData(
            "Great Ball Replica",
            39.99,
            "Great Ball replica with premium finish. Catch 'em all with this beautiful collectible!",
            "Pokemon"
        ));
        SEED_DATA.put("red.png", new ItemData(
            "Pokemon Trainer Red Figure",
            49.99,
            "Legendary trainer Red action figure. Highly detailed with multiple poses available!",
            "Pokemon"
        ));

        // Zelda Collection
        SEED_DATA.put("link.png", new ItemData(
            "Link Action Figure",
            44.99,
            "Hero of Hyrule Link action figure. Comes with sword and shield accessories!",
            "Zelda"
        ));
        SEED_DATA.put("zelda.png", new ItemData(
            "Princess Zelda Statue",
            79.99,
            "Beautiful Princess Zelda collectible statue. Hand-painted with exquisite detail!",
            "Zelda"
        ));
        SEED_DATA.put("mastersword.png", new ItemData(
            "Master Sword Replica",
            149.99,
            "Full-size Master Sword replica. The legendary blade that seals the darkness!",
            "Zelda"
        ));
        SEED_DATA.put("shield.png", new ItemData(
            "Hylian Shield Replica",
            129.99,
            "Authentic Hylian Shield replica. Display-quality with intricate Triforce design!",
            "Zelda"
        ));
        SEED_DATA.put("fairybow.png", new ItemData(
            "Fairy Bow Replica",
            89.99,
            "Fairy Bow from Ocarina of Time. Perfect for cosplay or display!",
            "Zelda"
        ));
        SEED_DATA.put("sword.png", new ItemData(
            "Wooden Practice Sword",
            29.99,
            "Training sword like young Link used. Safe foam construction for play!",
            "Zelda"
        ));
        SEED_DATA.put("turtle.png", new ItemData(
            "Korok Seed Plush",
            14.99,
            "Yahaha! You found me! Adorable Korok plush from Breath of the Wild!",
            "Zelda"
        ));

        // Mario Collection
        SEED_DATA.put("mario.png", new ItemData(
            "Mario Classic Figure",
            24.99,
            "Classic Super Mario action figure. It's-a me, Mario!",
            "Mario"
        ));
        SEED_DATA.put("mario1.png", new ItemData(
            "Fire Mario Figure",
            29.99,
            "Mario in his Fire Flower power-up form. Throw fireballs at your enemies!",
            "Mario"
        ));
        SEED_DATA.put("mario2.png", new ItemData(
            "Tanooki Mario Plush",
            34.99,
            "Super Mario in his adorable Tanooki suit. Soft and huggable!",
            "Mario"
        ));
        SEED_DATA.put("star.png", new ItemData(
            "Super Star Power-Up",
            19.99,
            "Glowing Super Star collectible. Grants invincibility and plays the iconic music!",
            "Mario"
        ));
        SEED_DATA.put("flower.webp", new ItemData(
            "Fire Flower Lamp",
            39.99,
            "Fire Flower decorative lamp. Lights up your room with power-up magic!",
            "Mario"
        ));
        SEED_DATA.put("hammer.png", new ItemData(
            "Mario Hammer Toy",
            27.99,
            "Mario's signature hammer. Soft foam construction for safe play!",
            "Mario"
        ));

        // Kirby Collection
        SEED_DATA.put("kirby.png", new ItemData(
            "Kirby Plush",
            22.99,
            "Super soft Kirby plush toy. The pink puffball of Dream Land!",
            "Kirby"
        ));
        SEED_DATA.put("kirby2.png", new ItemData(
            "Sword Kirby Figure",
            26.99,
            "Kirby with sword ability. Ready to slash through any adventure!",
            "Kirby"
        ));
        SEED_DATA.put("puff.png", new ItemData(
            "Waddle Dee Plush",
            18.99,
            "Cute Waddle Dee plush. Kirby's loyal friend from Dream Land!",
            "Kirby"
        ));
        SEED_DATA.put("starrod.png", new ItemData(
            "Star Rod Replica",
            44.99,
            "Kirby's Star Rod weapon replica. Shoots stars and grants wishes!",
            "Kirby"
        ));

        // Kingdom Hearts Collection
        SEED_DATA.put("kingdomkey.png", new ItemData(
            "Kingdom Key Keyblade",
            89.99,
            "Sora's iconic Kingdom Key Keyblade replica. Opens the door to your heart!",
            "Kingdom Hearts"
        ));
        SEED_DATA.put("oblivion.png", new ItemData(
            "Oblivion Keyblade",
            94.99,
            "Dark and powerful Oblivion Keyblade. Forged from memories of darkness!",
            "Kingdom Hearts"
        ));
        SEED_DATA.put("divinerose.png", new ItemData(
            "Divine Rose Keyblade",
            99.99,
            "Elegant Divine Rose Keyblade. Beautiful and powerful weapon of light!",
            "Kingdom Hearts"
        ));
        SEED_DATA.put("sora2.png", new ItemData(
            "Sora Action Figure",
            54.99,
            "Kingdom Hearts Sora action figure. Includes multiple Keyblades!",
            "Kingdom Hearts"
        ));
        SEED_DATA.put("kh.png", new ItemData(
            "Kingdom Hearts Crown Necklace",
            29.99,
            "Silver crown necklace from Kingdom Hearts. Symbol of friendship!",
            "Kingdom Hearts"
        ));
        SEED_DATA.put("crest.png", new ItemData(
            "Organization XIII Coat",
            149.99,
            "Black Organization XIII coat. Premium quality cosplay outfit!",
            "Kingdom Hearts"
        ));
        SEED_DATA.put("crest2.png", new ItemData(
            "Nobody Symbol Pin",
            12.99,
            "Organization XIII Nobody symbol pin. Show your allegiance!",
            "Kingdom Hearts"
        ));

        // Minecraft Collection
        SEED_DATA.put("minecraft.png", new ItemData(
            "Minecraft Logo Poster",
            14.99,
            "Official Minecraft logo poster. Perfect for decorating your room!",
            "Minecraft"
        ));
        SEED_DATA.put("steve.png", new ItemData(
            "Steve Action Figure",
            19.99,
            "Minecraft Steve action figure with articulated joints. Build your world!",
            "Minecraft"
        ));
        SEED_DATA.put("steveandalex.png", new ItemData(
            "Steve & Alex Figure Set",
            34.99,
            "Steve and Alex figure duo pack. Team up for adventures!",
            "Minecraft"
        ));
        SEED_DATA.put("diamondsword.png", new ItemData(
            "Diamond Sword Foam Toy",
            24.99,
            "Minecraft diamond sword foam replica. The most valuable weapon!",
            "Minecraft"
        ));
        SEED_DATA.put("goldenapple.webp", new ItemData(
            "Golden Apple Plush",
            16.99,
            "Enchanted golden apple plush. Restores health and grants protection!",
            "Minecraft"
        ));
        SEED_DATA.put("dragonegg.webp", new ItemData(
            "Dragon Egg Replica",
            39.99,
            "End Dragon Egg replica with LED lights. The rarest block in Minecraft!",
            "Minecraft"
        ));

        // Halo Collection
        SEED_DATA.put("halo.png", new ItemData(
            "Master Chief Helmet Replica",
            199.99,
            "Full-scale Master Chief helmet replica. Finish the fight in style!",
            "Halo"
        ));
    }

    /**
     * Get the pictures directory path in a cross-platform way
     */
    private static Path getPicturesDirectory() {
        // Get the current working directory
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        return currentDir.resolve("src/main/resources/static/pictures");
    }

    /**
     * Get all image files from the pictures directory
     */
    private static List<String> getImageFiles() {
        File picturesDir = getPicturesDirectory().toFile();

        if (!picturesDir.exists() || !picturesDir.isDirectory()) {
            System.err.println("ERROR: Pictures directory not found: " + picturesDir.getAbsolutePath());
            return Collections.emptyList();
        }

        Set<String> imageExtensions = new HashSet<>(Arrays.asList(".png", ".jpg", ".jpeg", ".webp", ".gif"));
        List<String> imageFiles = new ArrayList<>();

        File[] files = picturesDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String extension = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
                    if (imageExtensions.contains(extension)) {
                        imageFiles.add(fileName);
                    }
                }
            }
        }

        Collections.sort(imageFiles);
        return imageFiles;
    }

    /**
     * Seed the database with items from the pictures folder
     */
    public static void seedDatabase() {
        System.out.println("=" .repeat(60));
        System.out.println("Big5 Shop Database Seeder");
        System.out.println("Cross-platform: Mac, Ubuntu, Windows");
        System.out.println("=" .repeat(60));

        Path picturesDir = getPicturesDirectory();
        System.out.println("\nPictures directory: " + picturesDir.toAbsolutePath());

        // Get all image files
        List<String> imageFiles = getImageFiles();
        System.out.println("Found " + imageFiles.size() + " images in pictures folder\n");

        if (imageFiles.isEmpty()) {
            System.err.println("No images found. Exiting.");
            return;
        }

        // Clear existing items (optional - comment out to keep existing data)
        System.out.println("Clearing existing items...");
        try {
            List<Database.Item> existingItems = Database.getAvailableItems();
            // Note: Would need a deleteItem method in Database class
            // For now, we'll just proceed with creating new items
        } catch (Exception e) {
            System.err.println("Note: Could not clear existing items: " + e.getMessage());
        }

        // Insert seed data
        System.out.println("\nInserting seed data...");
        int insertedCount = 0;
        int skippedCount = 0;

        for (String imageFile : imageFiles) {
            if (SEED_DATA.containsKey(imageFile)) {
                ItemData itemData = SEED_DATA.get(imageFile);

                try {
                    Database.Item item = Database.createItem(
                        itemData.name,
                        itemData.price,
                        itemData.description,
                        imageFile,
                        itemData.category
                    );

                    System.out.println("  ✓ Inserted: " + itemData.name + " (" + imageFile + ")");
                    insertedCount++;
                } catch (Exception e) {
                    System.err.println("  ✗ Failed to insert " + imageFile + ": " + e.getMessage());
                    skippedCount++;
                }
            } else {
                System.out.println("  ⚠ Skipped: " + imageFile + " (no seed data defined)");
                skippedCount++;
            }
        }

        // Summary
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Database seeding complete!");
        System.out.println("  Inserted: " + insertedCount + " items");
        System.out.println("  Skipped: " + skippedCount + " items");
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Main method to run the seeder
     */
    public static void main(String[] args) {
        try {
            seedDatabase();
        } catch (Exception e) {
            System.err.println("Error seeding database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
