package com.example.ProjectSprintFrontend.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerIdDTO {
    private Integer customerNumber;
    private String  customerName;
    private String  contactFirstName;
    private String  contactLastName;
    private String  phone;
    private String  city;
    private String  country;

    // ── getters / setters ──────────────────────────────────
    public Integer getCustomerNumber()    { return customerNumber; }
    public void setCustomerNumber(Integer v) { this.customerNumber = v; }

    public String getCustomerName()       { return customerName; }
    public void setCustomerName(String v) { this.customerName = v; }

    public String getContactFirstName()       { return contactFirstName; }
    public void setContactFirstName(String v) { this.contactFirstName = v; }

    public String getContactLastName()       { return contactLastName; }
    public void setContactLastName(String v) { this.contactLastName = v; }

    public String getPhone()       { return phone; }
    public void setPhone(String v) { this.phone = v; }

    public String getCity()       { return city; }
    public void setCity(String v) { this.city = v; }

    public String getCountry()       { return country; }
    public void setCountry(String v) { this.country = v; }

    public String getFullContact() {
        String first = contactFirstName != null ? contactFirstName : "";
        String last  = contactLastName  != null ? contactLastName  : "";
        String name  = (first + " " + last).trim();
        return name.isEmpty() ? "—" : name;
    }
}
