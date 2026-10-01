package com.example.demo.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.BookNotAvailableException;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.LoanAlreadyReturnedException;
import com.example.demo.exception.LoanNotFoundException;
import com.example.demo.exception.MemberNotFoundException;
import com.example.demo.exception.MemberSuspendedException;
import com.example.demo.model.Book;
import com.example.demo.model.Loan;
import com.example.demo.model.Member;
import com.example.demo.model.MemberStatus;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.MemberRepository;

@Service
public class LoanService {

    // Standard loan period used by the library.
    private static final int LOAN_PERIOD_DAYS = 14;

    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, MemberRepository memberRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    // Checks out a book to a valid, active member and reduces the book's available copies.
    public Loan checkoutBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        if (member.getMemberStatus() == MemberStatus.SUSPENDED) {
            throw new MemberSuspendedException("Member is suspended and cannot checkout books.");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("Book is not available for checkout.");
        }

        Loan loan = new Loan();
        loan.setMember(member);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS));
        loan.setReturnDate(null);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    // Marks a loan as returned and returns the book to the available pool.
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + loanId));

        if (loan.getReturnDate() != null) {
            throw new LoanAlreadyReturnedException("This book has already been returned.");
        }

        loan.setReturnDate(LocalDate.now());

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    // Lists all loan records.
    public Iterable<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // Convenience method for saving a loan directly.
    public Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    // Allows a loan record to be updated manually.
    public Loan updateLoan(Long id, Loan updatedLoan) {
        return loanRepository.findById(id)
                .map(loan -> {
                    loan.setLoanDate(updatedLoan.getLoanDate());
                    loan.setDueDate(updatedLoan.getDueDate());
                    loan.setReturnDate(updatedLoan.getReturnDate());
                    return loanRepository.save(loan);
                })
                .orElse(null);
    }

    // Reads one loan by ID.
    public java.util.Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    // Returns all loans that are past due and not yet returned.
    public List<Loan> getOverdueLoans() {
        LocalDate today = LocalDate.now();
        return loanRepository.findByDueDateBeforeAndReturnDateIsNull(today);
    }

    // Deletes a loan from the database.
    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }
}
