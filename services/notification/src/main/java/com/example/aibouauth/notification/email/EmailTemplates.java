package com.example.aibouauth.notification.email;

import lombok.Getter;

@Getter
public enum EmailTemplates {

    PAYMENT_CONFIRMATION("payment-confirmation.html", "Payment successfully processed"),
    PURCHASE_CONFIRMATION("purchase-confirmation.html", "Purchase confirmation")
    ;

    private final String template;
    private final String subject;


    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}