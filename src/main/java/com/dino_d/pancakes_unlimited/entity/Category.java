package com.dino_d.pancakes_unlimited.entity;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Ingredient> ingredients = new LinkedHashSet<>();
}