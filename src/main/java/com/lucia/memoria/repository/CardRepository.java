package com.lucia.memoria.repository;

import com.lucia.memoria.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
