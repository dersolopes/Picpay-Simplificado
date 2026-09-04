package com.dersonlopes.picpaysimplificado.services;

import com.dersonlopes.picpaysimplificado.domain.transaction.Transaction;
import com.dersonlopes.picpaysimplificado.domain.user.User;
import com.dersonlopes.picpaysimplificado.domain.user.UserType;
import com.dersonlopes.picpaysimplificado.dtos.TransactionDTO;
import com.dersonlopes.picpaysimplificado.repositories.TransactionRepository;
import com.dersonlopes.picpaysimplificado.repositories.UserRepository;
import com.dersonlopes.picpaysimplificado.services.AuthorizationService;
import com.dersonlopes.picpaysimplificado.services.NotificationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    // Injeção das dependências de repositories e serviços
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authService; // <-- Novo
    private final NotificationService notificationService; // <-- Novo

    // Injeção de dependência via construtor
    public TransactionService(UserRepository userRepository,
                              TransactionRepository transactionRepository,
                              AuthorizationService authService,
                              NotificationService notificationService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.authService = authService;
        this.notificationService = notificationService;
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
        if (sender.getUserType() == com.dersonlopes.picpaysimplificado.domain.user.UserType.MERCHANT) {
            throw new Exception("Usuários do tipo Lojista não podem realizar transferências.");
        }

        // Regra B: Validação de saldo insuficiente
        if (sender.getBalance().compareTo(transactionData.value()) < 0) {
            throw new Exception("Saldo insuficiente.");
        }

        // 🛑 NOVA VALIDAÇÃO (US03): Consultar o serviço autorizador externo
        boolean isAuthorized = authService.authorizeTransaction(sender, transactionData.value());
        if (!isAuthorized) {
            throw new Exception("Transação não autorizada pelo serviço externo.");
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
        Transaction savedTransaction = transactionRepository.save(newTransaction);

        // 🛑 NOVA NOTIFICAÇÃO (US03): Envia a notificação após salvar com sucesso no banco
        notificationService.sendNotification(sender, "Sua transferência foi realizada com sucesso.");
        notificationService.sendNotification(receiver, "Você recebeu uma nova transferência.");

        return savedTransaction;
    }
}
