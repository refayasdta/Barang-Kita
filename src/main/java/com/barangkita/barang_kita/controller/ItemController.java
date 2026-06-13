package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.entity.Item;
import com.barangkita.barang_kita.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


@Validated
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
    public ResponseEntity<?> createItem(
            @RequestParam("nama_item") @NotBlank(message = "Nama produk tidak boleh kosong") String namaItem,
            @RequestParam("harga") @Min(value = 0, message = "Harga tidak boleh negatif") Double harga,
            @RequestParam("deskripsi") String deskripsi,
            @RequestParam(value = "adminUsername", required = false) String adminUsername,
            @RequestParam(value = "stok", defaultValue = "1") Integer stok,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        

        Item item = new Item();
        item.setNama_item(namaItem);
        item.setHarga(harga);
        item.setDeskripsi(deskripsi);
        item.setAdminUsername(adminUsername != null ? adminUsername : "Admin");
        item.setStok(stok);

        // Image Saving Logic
        if (file != null && !file.isEmpty()) {
            try {
                // Generate a unique filename to prevent overwriting existing files
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                
                // Define the path where the image will be saved
                Path uploadPath = Paths.get("src/main/resources/static/images/");
                
                // Create the directory if it doesn't exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save the file
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);

                // Save the filename string to the database
                item.setGambar_item(fileName);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Gagal mengupload gambar: " + e.getMessage());
            }
        }

        // Save item using your existing service
        Item savedItem = itemService.saveItem(item); // Adjust this line if your service method is named differently
        return ResponseEntity.ok(savedItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(
            @PathVariable int id,
            @RequestParam("nama_item") @NotBlank(message = "Nama produk tidak boleh kosong") String namaItem,
            @RequestParam("harga") @Min(value = 0, message = "Harga tidak boleh negatif") Double harga,
            @RequestParam("deskripsi") String deskripsi,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        // 1. Fetch the existing item from the database
        Item existingItem = itemService.getItemById(id);
        if (existingItem == null) {
            return ResponseEntity.badRequest().body("Item tidak ditemukan");
        }

        // 2. Update the text fields
        existingItem.setNama_item(namaItem);
        existingItem.setHarga(harga);
        existingItem.setDeskripsi(deskripsi);

        // 3. Update the image ONLY if the admin uploaded a new one
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/images/");
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);

                existingItem.setGambar_item(fileName);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Gagal mengupload gambar: " + e.getMessage());
            }
        }

        // 4. Save and return
        Item savedItem = itemService.saveItem(existingItem);
        return ResponseEntity.ok(savedItem);
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