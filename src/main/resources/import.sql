-- Inserindo Usuários Comuns (Eles ganharão automaticamente as IDs 1 e 2)
INSERT INTO users (first_name, last_name, document, email, password, balance, user_type) VALUES ('Gabriel', 'Silva', '12345678901', 'gabriel@email.com', '123456', 500.00, 'COMMON');
INSERT INTO users (first_name, last_name, document, email, password, balance, user_type) VALUES ('Amanda', 'Oliveira', '98765432100', 'amanda@email.com', '654321', 300.00, 'COMMON');

-- Inserindo Lojista (Ele ganhará automaticamente a ID 3)
INSERT INTO users (first_name, last_name, document, email, password, balance, user_type) VALUES ('Lanchonete', 'Dev', '12345678000199', 'contato@lanchedev.com', 'lanche123', 0.00, 'MERCHANT');
