package com.barangkita.barang_kita.repository;

import com.barangkita.barang_kita.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT c FROM Cart c WHERE c.id_user = :id_user")
    List<Cart> findByUserId(int id_user);
}