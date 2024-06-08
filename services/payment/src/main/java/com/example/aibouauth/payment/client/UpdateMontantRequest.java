package com.example.aibouauth.payment.client;

import java.math.BigDecimal;

public record UpdateMontantRequest(Integer userId, BigDecimal newMontant) {
}
