package com.example.ProjectSprintFrontend.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Handles two different JSON shapes from the backend:
 *
 * 1) LIST  — GET /orders?projection=orderView  (excerpt projection)
 *    Fields: orderDate, requiredDate, shippedDate, status, comments, customerNumber (flat int)
 *    NO orderNumber field — we parse it from _links.self.href
 *
 * 2) DETAIL — GET /orders/{id}  (no projection)
 *    Fields: orderNumber, orderDate, requiredDate, shippedDate, status, comments
 *    + nested "customer": { customerNumber, customerName, ... }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {

    // Present in DETAIL call (no projection)
    private Integer     orderNumber;

    private String      orderDate;
    private String      requiredDate;
    private String      shippedDate;
    private String      status;
    private String      comments;

    // Present in LIST projection as a flat integer field
    private Integer     customerNumber;

    // Present in DETAIL call as a nested object
    private CustomerIdDTO customer;

    // HAL links — used to extract orderNumber in LIST rows
    @JsonProperty("_links")
    private Links links;

    // ── orderNumber with fallback from self-link ─────────────────────────────
    public Integer getOrderNumber() {
        if (orderNumber != null) return orderNumber;
        if (links != null && links.getSelf() != null) {
            String href = links.getSelf().getHref();
            // strip template vars like {?projection} and query strings
            if (href.contains("{")) href = href.substring(0, href.indexOf('{'));
            if (href.contains("?")) href = href.substring(0, href.indexOf('?'));
            href = href.stripTrailing().replaceAll("/+$", "");
            String[] parts = href.split("/");
            try { return Integer.parseInt(parts[parts.length - 1]); }
            catch (NumberFormatException ignored) {}
        }
        return null;
    }
    public void setOrderNumber(Integer v) { this.orderNumber = v; }

    // ── getters / setters ────────────────────────────────────────────────────
    public String getOrderDate()        { return orderDate; }
    public void   setOrderDate(String v){ this.orderDate = v; }

    public String getRequiredDate()        { return requiredDate; }
    public void   setRequiredDate(String v){ this.requiredDate = v; }

    public String getShippedDate()        { return shippedDate; }
    public void   setShippedDate(String v){ this.shippedDate = v; }

    public String getStatus()        { return status; }
    public void   setStatus(String v){ this.status = v; }

    public String getComments()        { return comments; }
    public void   setComments(String v){ this.comments = v; }

    // Flat customerNumber from projection (list view)
    public Integer getCustomerNumber()        { return customerNumber; }
    public void    setCustomerNumber(Integer v){ this.customerNumber = v; }

    // Nested customer object from detail call (no projection)
    public CustomerIdDTO getCustomer()           { return customer; }
    public void        setCustomer(CustomerIdDTO v){ this.customer = v; }

    public Links getLinks()        { return links; }
    public void  setLinks(Links v) { this.links = v; }

    // ── HAL link helpers ─────────────────────────────────────────────────────
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
        private Href self;
        public Href getSelf()       { return self; }
        public void setSelf(Href v) { this.self = v; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Href {
        private String href;
        public String getHref()       { return href; }
        public void   setHref(String v){ this.href = v; }
    }
}
