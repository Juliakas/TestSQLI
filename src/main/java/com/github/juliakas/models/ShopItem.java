package com.github.juliakas.models;

import lombok.Data;

@Data
public class ShopItem {
    private Long itemId;
    private String name;
    private String description;
    private Double price;
}
