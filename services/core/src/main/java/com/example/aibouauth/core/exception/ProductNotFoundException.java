package com.example.aibouauth.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ProductNotFoundException {
    private final String msg;
}
