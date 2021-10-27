package com.example.appcraftmaster.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Response {
    private Long id;
    private ExecutorInfo executor;
    private BigDecimal price;
    private Long dateBeg;
    private Long dateEnd;
    private Float rating;
}
