# 📋 Backlog da Sprint 1: Histórias de Usuário

## 🔹 US01: Configuração do Ecossistema Base e Massa de Dados
### História de Usuário
Como **desenvolvedor**, eu quero **inicializar o projeto Spring Boot com banco de dados e uma massa de dados pré-existente de usuários comuns e lojistas**, para que eu possa **simular transações sem precisar de uma tela de cadastro**.

### ☑️ Critérios de Aceite
- [ ] Projeto iniciado usando **Java 23+** e **Spring Boot 4.x**.
- [ ] Banco de dados (**H2 ou PostgreSQL**) configurado e rodando.
- [ ] Mapeamento das entidades `User ('users' no banco de dados, nome restrito ao SGBD)` (com campos nome, documento único, e-mail único, saldo e tipo) e `Transaction`.
- [ ] Um arquivo `import.sql` ou classe `CommandLineRunner` que insira pelo menos **2 usuários comuns** e **2 lojistas** com saldos iniciais ao subir o app.

---

## 🔹 US02: Implementação da Regra de Negócio de Transferência
### História de Usuário
Como **usuário comum** do PicPay Simplificado, eu quero **transferir dinheiro para outro usuário comum ou lojista**, para que eu possa **realizar pagamentos rapidamente**.

### ☑️ Critérios de Aceite
- [ ] Validar se o pagador (`payer`) é do tipo `COMMON` (**Lojistas não podem enviar dinheiro**).
- [ ] Validar se o pagador tem **saldo suficiente** antes de transferir.
- [ ] A operação de débito e crédito deve acontecer de forma **transacional** (se uma falhar, tudo volta ao estado original).

---

## 🔹 US03: Integração com Serviços Externos (Autorizador e Notificação)
### História de Usuário
Como **sistema de pagamentos**, eu quero **consultar um autorizador externo antes de concluir a transferência e disparar uma notificação após o sucesso**, para **garantir a segurança e a comunicação do processo**.

### ☑️ Critérios de Aceite
- [ ] Efetuar chamada HTTP GET para o mock da `devi.tools`. Se recusado, a transação deve sofrer **rollback**.
- [ ] Efetuar chamada HTTP POST **assíncrona ou resiliente** para a `devi.tools` após concluir a transferência.
- [ ] Se o serviço de notificação falhar, **logar o erro** sem derrubar a transferência concluída.

---

## 🔹 US04: Exposição do Endpoint REST e Contrato da API
### História de Usuário
Como **cliente da API**, eu quero **enviar os dados da transferência para um endpoint específico**, para **disparar o fluxo de pagamento do sistema**.

### ☑️ Critérios de Aceite
- [ ] Criar rota `POST /transfer` (ou endpoint equivalente de transferência).
- [ ] Validar o formato do contrato de dados recebido na requisição.
