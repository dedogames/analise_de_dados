package com.dedogames.summary.shared.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "summary_by_regions")
public class SummaryByRegions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String state;

    private double percent;

    private String products;

    private LocalDateTime created_at;

    public SummaryByRegions() {
    }

    public SummaryByRegions(String state, double percent, String products, LocalDateTime created_at) {
        this.state = state;
        this.percent = percent;
        this.products = products;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.created_at = createdAt;
    }

}