package com.example.javaswing.Repository;

import com.example.javaswing.Entity.DoiSoat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoiSoatRepo extends JpaRepository<DoiSoat,String> {
    @Modifying
    @Query(value = "DELETE FROM DOISOAT d WHERE d.TDATE LIKE :tdate%",nativeQuery = true)
    void deleteDOISOAT(@Param("tdate") String tdate);

}
