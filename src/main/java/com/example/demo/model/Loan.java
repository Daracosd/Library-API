package com.example.demo.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    // Unique transaction ID for a book checkout.
    @Id @GeneratedValue
    private Long id;

    // Loan lifecycle dates.
    private java.time.LocalDate loanDate;
    private java.time.LocalDate dueDate;
    private java.time.LocalDate returnDate;

    // Links a loan to the member who borrowed the book.
    @ManyToOne
    private Member member;

    // Links a loan to the specific book that was borrowed.
    @ManyToOne
    private Book book;
}
