package com.dersonlopes.picpaysimplificado.services;

import com.dersonlopes.picpaysimplificado.domain.transaction.Transaction;
import com.dersonlopes.picpaysimplificado.domain.user.User;
import com.dersonlopes.picpaysimplificado.domain.user.UserType;
import com.dersonlopes.picpaysimplificado.dtos.TransactionDTO;
import com.dersonlopes.picpaysimplificado.repositories.TransactionRepository;
import com.dersonlopes.picpaysimplificado.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    // Injeção de dependência via construtor (Boa prática que o mercado valoriza)
    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional // Garante o Rollback automático caso ocorra qualquer erro no meio do processo
    public Transaction createTransaction(TransactionDTO transactionData) throws Exception {

        // 1. Buscar quem envia e quem recebe no banco de dados
        User sender = userRepository.findById(transactionData.payer())
            .orElseThrow(() -> new Exception("Usuário pagador não encontrado."));

        User receiver = userRepository.findById(transactionData.payee())
            .orElseThrow(() -> new Exception("Usuário recebedor não encontrado."));

        // 2. Validar as Regras de Negócio do Desafio
        // Regra A: Lojista não pode enviar dinheiro
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuários do tipo Lojista não podem realizar transferências.");
        }

        // Regra B: Validação de saldo suficiente
        if (sender.getBalance().compareTo(transactionData.value()) < 0) {
            throw new Exception("Saldo insuficiente para realizar a transferência.");
        }

        // 3. Atualizar os saldos das carteiras temporariamente na memória
        sender.setBalance(sender.getBalance().subtract(transactionData.value()));
        receiver.setBalance(receiver.getBalance().add(transactionData.value()));

        // 4. Criar e registrar o histórico da transação
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transactionData.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        // 5. Salvar as alterações de saldo dos usuários e a nova transação no banco
        userRepository.save(sender);
        userRepository.save(receiver);
        return transactionRepository.save(newTransaction);
    }
}
