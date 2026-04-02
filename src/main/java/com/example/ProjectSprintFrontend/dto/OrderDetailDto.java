package com.example.ProjectSprintFrontend.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetailDto {
    private String     productName;
    private Integer    quantityOrdered;
    private BigDecimal priceEach;
    private BigDecimal totalPrice;       // computed by projection: priceEach * qty
    private Integer    orderNumber;

    // ── getters / setters ──────────────────────────────────
    public String getProductName()       { return productName; }
    public void setProductName(String v) { this.productName = v; }

    public Integer getQuantityOrdered()       { return quantityOrdered; }
    public void setQuantityOrdered(Integer v) { this.quantityOrdered = v; }

    public BigDecimal getPriceEach()          { return priceEach; }
    public void setPriceEach(BigDecimal v)    { this.priceEach = v; }

    public BigDecimal getTotalPrice()         { return totalPrice; }
    public void setTotalPrice(BigDecimal v)   { this.totalPrice = v; }

    public Integer getOrderNumber()       { return orderNumber; }
    public void setOrderNumber(Integer v) { this.orderNumber = v; }
}

