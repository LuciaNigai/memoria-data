package com.lucia.memoria.repository;

import com.lucia.memoria.model.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTypeRepository extends JpaRepository<CardType, Long> {
}
