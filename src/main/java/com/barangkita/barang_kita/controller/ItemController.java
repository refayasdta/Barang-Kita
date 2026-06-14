package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.entity.Item;
import com.barangkita.barang_kita.service.ItemService;
import com.barangkita.barang_kita.service.CloudinaryService; // Injecting your new Cloudinary Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
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

    // ☁️ Tell Spring Boot to use the Cloudinary Service
    @Autowired
    private CloudinaryService cloudinaryService; 

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

        // ☁️ CLOUDINARY UPLOAD MAGIC
        if (file != null && !file.isEmpty()) {
            try {
                // Upload directly to the cloud and get the secure HTTPS link back
                String imageUrl = cloudinaryService.uploadImage(file);
                
                // Save the secure link to your database instead of a local filename
                item.setGambar_item(imageUrl);

            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Gagal mengupload gambar ke Cloudinary: " + e.getMessage());
            }
        }

        Item savedItem = itemService.saveItem(item); 
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
                // Upload the new image to Cloudinary
                String imageUrl = cloudinaryService.uploadImage(file);
                
                // Overwrite the old database entry with the new secure link
                existingItem.setGambar_item(imageUrl);

            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Gagal mengupload gambar ke Cloudinary: " + e.getMessage());
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