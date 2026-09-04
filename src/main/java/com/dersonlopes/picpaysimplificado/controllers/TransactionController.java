package com.dersonlopes.picpaysimplificado.controllers;

import com.dersonlopes.picpaysimplificado.domain.transaction.Transaction;
import com.dersonlopes.picpaysimplificado.dtos.TransactionDTO;
import com.dersonlopes.picpaysimplificado.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransactionController {

    private final TransactionService transactionService;

    // Injeção de dependência padrão via construtor
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionData) throws Exception {
        // Dispara o motor de regras da US02 e integrações da US03
        Transaction newTransaction = this.transactionService.createTransaction(transactionData);

        // Retorna status 201 Created com os dados consolidados da transferência
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }
}
