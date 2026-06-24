package com.library.borrow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.borrow.DTO.BorrowRequest;
import com.library.borrow.DTO.BorrowResponse;
import com.library.borrow.model.Fine;
import com.library.borrow.service.BorrowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/borrows")
@Tag(name = "Borrows", description = "Book issuing, returning, extensions and fines")
public class BorrowController {

    @Autowired
    private BorrowService service;

    @Operation(description = "Admin only - returns all borrow records")
    @GetMapping
    public List<BorrowResponse> getAllBorrows() {
        return service.getAllBorrows();
    }

    @Operation(description = "Returns all borrows for a specific member ID")
    @GetMapping("/member/{memberId}")
    public List<BorrowResponse> getByMember(@PathVariable String memberId) {
        return service.getByMember(memberId);
    }

    @Operation(description = "Admin only - returns all fine records")
    @GetMapping("/fines")
    public List<Fine> getAllFines() {
        return service.getAllFines();
    }

    @Operation(description = "Returns all fines for a specific member ID")
    @GetMapping("/fines/member/{memberId}")
    public List<Fine> getFinesByMember(@PathVariable String memberId) {
        return service.getFinesByMember(memberId);
    }

    @Operation(description = "Issues a book to a member by memberId and book title")
    @PostMapping
    public BorrowResponse issue(@RequestBody BorrowRequest req) {
        return service.issue(req);
    }

    @Operation(description = "Returns a book using the borrow record ID")
    @PutMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        return service.returnBook(id);
    }

    @Operation(description = "Extends due date by 7 days - allowed only once per borrow")
    @PutMapping("/extend/{id}")
    public BorrowResponse extendDueDate(@PathVariable Long id) {
        return service.extendDueDate(id);
    }

    @Operation(description = "Admin only - marks fine as PAID and borrow as RETURNED")
    @PutMapping("/pay-fine/{fineId}")
    public String payFine(@PathVariable Long fineId) {
        return service.payFine(fineId);
    }

    @Operation(description = "Admin only - triggers manual check for overdue borrows")
    @PostMapping("/update-overdue")
    public String triggerOverdueUpdate() {
        service.updateOverdue();
        return "Overdue borrows updated successfully!!";
    }
}
