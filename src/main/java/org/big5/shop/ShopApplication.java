package org.big5.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.big5.shop.model.Database;

import java.util.List;

@RestController
@SpringBootApplication
public class ShopApplication {
    @RequestMapping("/")
	String home() {
		String rtn = "";
		List<Database.Item> items = Database.getAvailableItems();
		for (Database.Item item: items) {
			rtn = rtn + item.name + " ";
		}
		return rtn;
	}

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}
}
