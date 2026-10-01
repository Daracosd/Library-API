package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByMemberId(Long memberId);
    List<Loan> findByBookId(Long bookId);

    List<Loan> findByDueDateBefore(LocalDate date);
    List<Loan> findByReturnDateIsNull();
    List<Loan> findByDueDateBeforeAndReturnDateIsNull(LocalDate date);


    
}
