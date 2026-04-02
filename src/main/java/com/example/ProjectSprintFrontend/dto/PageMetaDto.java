package com.example.ProjectSprintFrontend.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageMetaDto {
    private int  size;
    private long totalElements;
    private int  totalPages;
    private int  number;          // current page (0-based)

    public int  getSize()               { return size; }
    public void setSize(int v)          { this.size = v; }

    public long getTotalElements()      { return totalElements; }
    public void setTotalElements(long v){ this.totalElements = v; }

    public int  getTotalPages()         { return totalPages; }
    public void setTotalPages(int v)    { this.totalPages = v; }

    public int  getNumber()             { return number; }
    public void setNumber(int v)        { this.number = v; }
}

