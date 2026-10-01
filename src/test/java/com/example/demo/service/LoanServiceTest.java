package com.example.demo.service;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

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


@ExtendWith(MockitoExtension.class)
class LoanServiceTest {
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LoanService loanService;

    private Member activeMember;
    private Book availableBook;


    @BeforeEach
    void setUp() {
        activeMember = new Member();
        activeMember.setId(1L);
        activeMember.setMemberStatus(MemberStatus.ACTIVE);

        availableBook = new Book();
        availableBook.setId(1L);
        availableBook.setTitle("Test Book");
        availableBook.setAvailableCopies(5);
    }

    @Test 
    void testLoanBook_BookNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(activeMember));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            loanService.checkoutBook(1L, 1L);
        });
    }

    @Test 
    void testLoanBook_MemberNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> {
            loanService.checkoutBook(1L, 1L);
        });

    }

    @Test 
    void checkoutBook_throwsException_whenMemberNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> {
            loanService.checkoutBook(1L, 1L);
        });
    }

    @Test
    void checkoutBook_throwsExeption_whenMemberIsSuspended() {
        activeMember.setMemberStatus(MemberStatus.SUSPENDED);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(activeMember));

        assertThrows(MemberSuspendedException.class, () -> {
            loanService.checkoutBook(1L, 1L);
        });
    }

    @Test 
    void checkoutBook_throwsExeption_whenBookUnavailable() {
        availableBook.setAvailableCopies(0);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(activeMember));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(availableBook));

        assertThrows(BookNotAvailableException.class, () -> {
            loanService.checkoutBook(1L, 1L);
        });
    }

    @Test 
    void returnBook_throwsException_whenLoanNotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class, () -> {
            loanService.returnBook(1L);
        });
    }

    @Test 
    void returnBook_throwsException_whenLoanAlreadyReturned() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setReturnDate(LocalDate.now());
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(LoanAlreadyReturnedException.class, () -> {
            loanService.returnBook(1L);
        });
    }

    @Test 
    void returnBook_succeeds_incrementsAvailableCopies() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setBook(availableBook);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        loanService.returnBook(1L);

        assertEquals(6, availableBook.getAvailableCopies());
    }   

}
