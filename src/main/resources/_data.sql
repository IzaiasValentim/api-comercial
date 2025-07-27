INSERT INTO ROLES(name) VALUES ('ADMINISTRATOR');
INSERT INTO ROLES(name) VALUES ('MANAGER');
INSERT INTO ROLES(name) VALUES ('SELLER');
INSERT INTO ROLES(name) VALUES ('INTERN');

INSERT INTO USUARIOS (username, CPF, email, password)
VALUES ('ADM',
        '1231231',
        'adm@gmail.com',
        '$2a$10$GiseHkdvwOFr7A9KRWbeiOmg/PYPhWVjdm42puLfOzR/gIAQrsAGy'),
       ('Gerente',
        '1231232',
        'gerente@gmail.com',
        '$2a$10$GiseHkdvwOFr7A9KRWbeiOmg/PYPhWVjdm42puLfOzR/gIAQrsAGy'),
       ('Caixa',
        '1231233',
        'caixa@gmail.com',
        '$2a$10$GiseHkdvwOFr7A9KRWbeiOmg/PYPhWVjdm42puLfOzR/gIAQrsAGy'),
       ('Interno',
        '1231234',
        'int@gmail.com',
        '$2a$10$GiseHkdvwOFr7A9KRWbeiOmg/PYPhWVjdm42puLfOzR/gIAQrsAGy');

INSERT INTO USUARIO_API(user_id, phone, address, admission_date, is_admin, is_active, is_deleted)
VALUES (1, '8888888', 'casa', '2025-02-03', TRUE, TRUE, FALSE),
       (2, '88888889', 'casa', '2025-02-03', FALSE, TRUE, FALSE),
       (3, '88888889', 'casa', '2025-02-03', FALSE, TRUE, FALSE),
       (4, '88888889', 'casa', '2025-02-03', FALSE, TRUE, FALSE);


INSERT INTO tb_usuarios_roles(usuario_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4);

INSERT INTO Clientes (
    identification_number,
    name,
    email,
    address,
    phone_number,
    phone_number_reserve,
    payment,
    active,
    is_deleted
) VALUES (
             '123.456.789-00', -- Exemplo de CPF (ajuste para CNPJ se necess�rio)
             'Joao da Silva',
             'joao.silva@email.com',
             'Rua das Flores, 123, Centro, Cidade, UF',
             '+55 (11) 98765-4321',
             '+55 (11) 91234-5678',
             'Cartao de Credito', -- Ex: "Cart�o de Cr�dito", "Pix", "Boleto"
             TRUE,              -- Cliente ativo
             FALSE              -- Cliente n�o deletado logicamente
         );