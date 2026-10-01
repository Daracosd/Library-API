package com.example.demo.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CheckoutRequest;
import com.example.demo.model.Loan;
import com.example.demo.service.LoanService;


@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // Shows all loan records in the system.
    @GetMapping
    public Iterable<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    // Finds a specific loan using its ID.
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Optional<Loan> loan = loanService.getLoanById(id);
        return loan.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lists loans whose due date has passed and that have not yet been returned.
    @GetMapping("/overdue")
    public List<Loan> getOverdueLoans() {
        return loanService.getOverdueLoans();
    }

    // Handles the checkout action by creating a loan record and reducing available copies.
    @PostMapping
    public ResponseEntity<Loan> checkoutBook(@RequestBody CheckoutRequest request) {
        Loan loan = loanService.checkoutBook(request.getMemberId(), request.getBookId());
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    // Marks a loan as returned and restores availability of the book.
    @PutMapping("/{id}/return")
    public ResponseEntity<Loan> returnBook(@PathVariable Long id) {
        Loan returnedLoan = loanService.returnBook(id);
        return ResponseEntity.ok(returnedLoan);
    }

    // Updates loan metadata such as dates.
    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Loan updatedLoan) {
        Loan loan = loanService.updateLoan(id, updatedLoan);
        if (loan != null) {
            return ResponseEntity.ok(loan);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Removes a loan record from the database.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}
