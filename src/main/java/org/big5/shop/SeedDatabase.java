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
        // Pokémon Collection
        SEED_DATA.put("pika3.png", new ItemData(
            "Pikachu",
            400.00,
            "One of the most beloved Pokémon, known for his electric abilities and loyal personality. A symbol of friendship and adventure, Pikachu continues to capture hearts around the world.",
            "Pokémon"
        ));
        SEED_DATA.put("puff.png", new ItemData(
            "Jigglypuff",
            150.00,
            "A charming and playful Pokémon, famous for her soothing lullabies and adorable round form. With a voice that can calm friend and foe alike, Jigglypuff brings both cuteness and quiet wherever it goes.",
            "Pokémon"
        ));
        SEED_DATA.put("charmander.png", new ItemData(
            "Charmander",
            800.00,
            "Charmander is a small Fire-type Pokémon known for the flame on its tail, which reflects its health and mood. Light on its feet and eager to grow stronger, it’s a dependable partner for trainers starting their journey.",
            "Pokémon"
        ));

        // The Legend of Zelda Collection
        SEED_DATA.put("shield.png", new ItemData(
            "Hylian Shield",
            1500.00,
            "A legendary emblem of courage, built to withstand even the fiercest attacks. Trusted by heroes across generations, its sturdy design and iconic crest make it a timeless protector in any adventure.",
            "The Legend of Zelda"
        ));
        SEED_DATA.put("mastersword.png", new ItemData(
            "Master Sword",
            1700.00,
            "A legendary, sacred blade said to be \"the sword that seals the darkness.\" Only a true hero can wield it. The Master Sword is infused with divine power and is known for its ability to repel and even destroy evil, especially forces like Ganon.",
            "The Legend of Zelda"
        ));
        SEED_DATA.put("fairybow.png", new ItemData(
            "Fairy Bow",
            400.00,
            "The Fairy Bow is a graceful yet powerful weapon, infused with sacred magic. Its enchanted arrows strike with precision, making it a trusted tool for heroes facing mystical foes and hidden dangers.",
            "The Legend of Zelda"
        ));

        // Super Mario Collection
        SEED_DATA.put("red.png", new ItemData(
            "Super Mushroom",
            400.00,
            "The Super Mushroom is an iconic power-up that boosts the user to new heights. It grants extra strength and resilience, turning small heroes into mighty adventurers.",
            "Super Mario"
        ));
        SEED_DATA.put("star.png", new ItemData(
            "Super Star",
            3000.00,
            "The Super Star is a dazzling power-up that grants brief invincibility and unstoppable speed. Shining with vibrant energy, it lets users charge through enemies and obstacles with effortless brilliance.",
            "Super Mario"
        ));
        SEED_DATA.put("flower.webp", new ItemData(
            "Fire Flower",
            800.00,
            "The Fire Flower grants the user the ability to throw controlled fireballs, adding reliable ranged offense to their toolkit. Practical and effective, it’s one of Mario’s most dependable power-ups.",
            "Super Mario"
        ));

        // Kirby Collection
        SEED_DATA.put("sword.png", new ItemData(
            "Sword",
            500.00,
            "Kirby’s Sword ability grants him classic hero flair, giving him swift, clean strikes and powerful spin attacks. With this trusty blade, Kirby becomes a tiny but fearless warrior ready to take on any challenge.",
            "Kirby"
        ));
        SEED_DATA.put("starrod.png", new ItemData(
            "Star Rod",
            1800.00,
            "The Star Rod is a legendary, dream-fueled wand capable of unleashing powerful bursts of light. As the source of Dream Land’s magic, it shines with cosmic energy and grants Kirby incredible strength when wielded.",
            "Kirby"
        ));
        SEED_DATA.put("hammer.png", new ItemData(
            "Hammer",
            900.00,
            "The Hammer is a heavy, hard-hitting weapon that delivers massive power with every swing. Known for smashing obstacles and enemies alike, it’s one of Kirby’s most formidable and iconic abilities.",
            "Kirby"
        ));

        // Kingdom Hearts Collection
        SEED_DATA.put("kingdomkey.png", new ItemData(
            "Kingdom Key",
            600.00,
            "The Kingdom Key is a classic, balanced Keyblade that embodies light and destiny. Reliable and iconic, it serves as the starting weapon for many heroes, unlocking both new paths and hidden potential.",
            "Kingdom Hearts"
        ));
        SEED_DATA.put("oblivion.png", new ItemData(
            "Oblivion",
            2200.00,
            "Oblivion is a dark and formidable Keyblade, steeped in shadow and power. Known for its sleek design and high strength, it symbolizes sacrifice and resolve, granting its wielder unmatched ferocity in battle.",
            "Kingdom Hearts"
        ));
        SEED_DATA.put("divinerose.png", new ItemData(
            "Divine Rose",
            1900.00,
            "Divine Rose is an elegant yet fiercely powerful Keyblade, inspired by enchanted beauty and inner strength. Its intricate design hides exceptional attack power, making it a favorite choice for heroes seeking both grace and might.",
            "Kingdom Hearts"
        ));

        // Minecraft Collection
        SEED_DATA.put("diamondsword.png", new ItemData(
            "Diamond Sword",
            700.00,
            "The Diamond Sword is a durable, high-powered weapon crafted from rare diamonds. Known for its sharpness and reliability, it stands as one of Minecraft’s most trusted tools for surviving tough battles and dangerous mobs.",
            "Minecraft"
        ));
        SEED_DATA.put("goldenapple.webp", new ItemData(
            "Golden Apple",
            900.00,
            "The Golden Apple is a rare and magical treat, prized for its powerful healing and protective effects. Shining with enchantment, it’s a lifesaving item for adventurers facing tough battles and perilous challenges.",
            "Minecraft"
        ));
        SEED_DATA.put("dragonegg.webp", new ItemData(
            "Dragon Egg",
            1500.00,
            "The Dragon Egg is a rare and mysterious trophy, left behind after defeating the Ender Dragon. Pulsing with ancient energy, it stands as a symbol of triumph and the ultimate achievement in any Minecraft world.",
            "Minecraft"
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
