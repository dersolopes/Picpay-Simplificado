# 💸 Sistema de Pagamentos Simplificado

Este projeto consiste em uma plataforma simplificada de transferências financeiras entre usuários e lojistas, desenvolvida com o objetivo de aplicar conceitos avançados de arquitetura de software, resiliência física de microsserviços e **observabilidade aplicada em produção**.

---

## 🎯 Objetivo do Negócio

A aplicação gerencia o fluxo de depósitos e transferências financeiras entre dois tipos de contas cadastrados na base:

*   **Usuários Comuns:** Possuem carteira com saldo, realizam transferências para outros usuários comuns e para lojistas, e também podem receber valores.
*   **Lojistas (Merchants):** Possuem carteira com saldo, mas **apenas recebem** transferências. Eles não enviam dinheiro para ninguém no ecossistema.

### 🛑 Regras de Negócio Básicas
1. **Unicidade de Dados:** O sistema não permite a criação de mais de um usuário com o mesmo CPF/CNPJ ou endereço de e-mail.
2. **Validação de Saldo:** O usuário pagador deve ter saldo suficiente antes de qualquer transação ser autorizada.
3. **Consistência Transacional:** Toda transferência deve ocorrer sob o conceito de transação única (tudo ou nada). Em caso de qualquer inconsistência, a operação sofre *rollback* completo.
4. **Autorização Externa:** Antes de finalizar, o sistema precisa consultar um serviço autorizador externo via chamada síncrona `GET`.
5. **Notificação Assíncrona:** Após o recebimento, o recebedor deve ser notificado por um serviço de terceiro (SMS/E-mail). Este serviço opera de forma instável, exigindo resiliência do nosso lado.

---

## 📊 Diferencial do Projeto: Observabilidade (MTR)

Diferente de implementações convencionais, este repositório foi projetado com foco no monitoramento de sistemas críticos em produção utilizando o ecossistema **Spring Boot Actuator, Prometheus e Grafana**:

*   **Métricas de Negócio:** Gráficos em tempo real medindo a quantidade de transferências processadas com sucesso versus transações rejeitadas por falta de saldo.
*   **Métricas de Infraestrutura:** Telemetria da JVM (uso de memória Heap, comportamento do Garbage Collector, consumo de CPU e pool de conexões do banco de dados).
*   **Monitoramento de Integrações:** Gráficos dedicados para medir a taxa de erro e o tempo de resposta (*latency*) das APIs externas de Autorização e Notificação.

---

## 📋 Organização do Desenvolvimento (Backlog Ágil)

O desenvolvimento deste sistema foi estruturado simulando o fluxo de uma equipe de engenharia ágil (*Tribo/Squad*), quebrado nas seguintes Histórias de Usuário:

### 🔹 Sprint 1: Core da Transação & Infra base
*   **US01 - Configuração de Ecossistema e Massa de Dados:** Inicialização do Spring Boot 3 + Banco de Dados e uma classe de carga inicial de sementes (*seed data*) para popular o ambiente com usuários de teste (comuns e lojistas).
*   **US02 - Motor de Regras da Transferência:** Implementação das validações de saldo, restrição de lojista pagador e o controle transacional isolado na camada de serviços.
*   **US03 - Integração com APIs Externas (Clientes HTTP):** Construção da resiliência para consumo dos mocks externos autorizadores (`https://util.devi.tools/api/v2/authorize`) e de envio de notificações (`https://util.devi.tools/api/v1/notify`).
*   **US04 - Exposição do Endpoint REST:** Disponibilização da rota principal do contrato do sistema:
    ```http
    POST /transfer
    Content-Type: application/json

    {
      "value": 100.0,
      "payer": 4,
      "payee": 15
    }
    ```

### 🔹 Sprint 2: Observabilidade & Dockerização
*   **US05 - Ativação do Actuator & Micrometer:** Configuração fina dos endpoints operacionais expostos e conversão de métricas nativas para o formato Prometheus.
*   **US06 - Infraestrutura Docker:** Orquestração de containers via `docker-compose` para subir os servidores locais de Prometheus e Grafana sem a necessidade de instalações locais invasivas.
*   **US07 - Dashboard de Telemetria:** Importação e personalização do painel visual no Grafana com os principais KPIs operacionais do sistema.

---

## 🛠️ Tecnologias Utilizadas

*   **Java 17+** & **Spring Boot 3.x**
*   **Spring Data JPA** & Banco de Dados Relacional
*   **Spring Boot Actuator** & **Micrometer Prometheus**
*   **Docker** & **Docker Compose**
*   **Prometheus** (Métricas / Série temporal)
*   **Grafana** (Visualização e Dashboards)

---

## 🚀 Como Executar o Projeto

*(Instruções de inicialização do projeto via maven e docker-compose serão detalhadas conforme as histórias forem entregues e implementadas).*
