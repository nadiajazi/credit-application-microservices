package com.example.aibouauth.payment.payment;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(EntityListeners.class)
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue
    private Integer id;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    private Integer userId;

    public Payment(Integer id, BigDecimal amount, PaymentMethod paymentMethod, LocalDateTime now, Integer user_id) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.createdDate = now;
        this.userId= user_id;

    }
}
