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
@Table(name = "pancakes")
public class Pancake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "pancake")
    private Set<PancakeIngredients> pancakeIngredients = new LinkedHashSet<>();
}
