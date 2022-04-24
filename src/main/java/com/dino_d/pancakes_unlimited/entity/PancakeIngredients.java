package com.dino_d.pancakes_unlimited.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pancakes_with_ingredients")
public class PancakeIngredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pancake_id")
    private Pancake pancake;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public PancakeIngredients(Pancake pancake, Ingredient ingredient) {
        this.pancake = pancake;
        this.ingredient = ingredient;
    }
}
