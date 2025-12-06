package com.jonah.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "expense_type",nullable = false)
    private ExpenseType expenseType;

    @Column(name="date")
    private String date;

    @Column(name = "amount")
    private Double amount;

    @Column(name="category")
    private String category;

    @Column(name="account")
    private String account;

    @Column(name="note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore(true)
    private AppUser user;

}
