package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.entity.Item;
import com.barangkita.barang_kita.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable int id) {
        return itemService.getItemById(id);
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemService.saveItem(item);
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable int id, @RequestBody Item item) {
        item.setId_item(id);
        return itemService.saveItem(item);
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
        return "Item deleted successfully";
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam String keyword) {
        return itemService.searchItem(keyword);
    }
}