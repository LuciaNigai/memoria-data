package com.lucia.memoria.model;

import com.lucia.memoria.helper.FieldRole;
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
@Table(name = "field_templates")
public class FieldTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "field_template_id", nullable = false, unique = true, updatable = false)
    private UUID fieldTemplateId = UUID.randomUUID();

    private String name;

    private FieldRole fieldRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;
}
