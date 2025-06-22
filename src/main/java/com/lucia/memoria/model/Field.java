package com.lucia.memoria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fields")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String Id;

    @Column(name = "fields_id", nullable = false, unique = true, updatable = false)
    private UUID fieldId = UUID.randomUUID();

    private String content;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "card_id")
//    Card card;
////
////    @ManyToOne(fetch = FetchType.LAZY)
////    @JoinColumn(name = "field_template_id")
////    FieldTemplate fieldTemplate;
}
