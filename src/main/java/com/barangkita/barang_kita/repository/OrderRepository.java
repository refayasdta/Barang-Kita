package com.barangkita.barang_kita.repository;

import com.barangkita.barang_kita.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.id_user = :userId")
    List<Order> findByUserId(@Param("userId") int id_user);
}