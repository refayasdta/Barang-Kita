package com.barangkita.barang_kita.repository;

import com.barangkita.barang_kita.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("SELECT i FROM Item i WHERE i.nama_item LIKE %:keyword%")
    List<Item> searchByNama(@Param("keyword") String keyword);
}