package com.example.javaswing.Repository;

import com.example.javaswing.Entity.DoiSoat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoiSoatRepo extends JpaRepository<DoiSoat,String> {
}
