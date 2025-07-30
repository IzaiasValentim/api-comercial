-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS db_clickcomercial;
USE db_clickcomercial;

-- Tabela para armazenar informações de clientes.
-- 'id' é uma chave primária auto-incrementada.
-- Campos como 'email' e 'identification_number' (CPF/CNPJ) são únicos.
CREATE TABLE IF NOT EXISTS clientes (
    id SERIAL PRIMARY KEY,
    active BOOLEAN,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    identification_number VARCHAR(255) NOT NULL UNIQUE,
    is_deleted BOOLEAN,
    name VARCHAR(255) NOT NULL,
    payment VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    phone_number_reserve VARCHAR(255)
);

-- Tabela principal para autenticação de usuários.
-- 'id' é a chave primária auto-incrementada.
-- 'cpf', 'email' e 'username' são campos únicos para identificação.
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY, -- 'BIGSERIAL' para chaves primárias BIGINT auto-incrementáveis.
    cpf VARCHAR(255) UNIQUE, -- Garante que cada CPF seja único.
    email VARCHAR(255) UNIQUE, -- Garante que cada email seja único.
    password VARCHAR(255),
    username VARCHAR(255) UNIQUE -- Garante que cada username seja único.
);

-- Tabela para definir os papéis ou perfis de acesso (ex: ADMIN, USER).
-- 'role_id' é a chave primária auto-incrementada.
CREATE TABLE IF NOT EXISTS roles (
    role_id BIGSERIAL PRIMARY KEY, -- 'BIGSERIAL' para chaves primárias BIGINT auto-incrementáveis.
    name VARCHAR(255)
);

-- Tabela para armazenar informações detalhadas de usuários da API.
-- 'id' é a chave primária auto-incrementada. 'user_id' é um campo único,
-- provavelmente referenciando o ID do usuário na tabela 'usuarios'.
CREATE TABLE IF NOT EXISTS usuario_api (
    id BIGSERIAL PRIMARY KEY, -- 'BIGSERIAL' para chaves primárias BIGINT auto-incrementáveis.
    address VARCHAR(255),
    admission_date DATE,
    is_active BOOLEAN,
    is_admin BOOLEAN,
    is_deleted BOOLEAN,
    phone VARCHAR(255),
    shutdows_date DATE,
    user_id BIGINT UNIQUE NOT NULL, -- Garante que cada usuário da tabela 'usuarios' tenha apenas um registro em 'usuario_api'.
    CONSTRAINT fk_user_usuario_api FOREIGN KEY (user_id) REFERENCES usuarios (id)
);

-- Tabela de junção para mapear usuários a múltiplos papéis (N:M).
-- A chave primária é composta por 'usuario_id' e 'role_id'.
-- Contém chaves estrangeiras para 'usuarios' e 'roles'.
CREATE TABLE IF NOT EXISTS tb_usuarios_roles (
    usuario_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, role_id),
    CONSTRAINT fk_usuario_roles FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT fk_role_usuarios FOREIGN KEY (role_id) REFERENCES roles (role_id)
);

-- Tabela para armazenar tokens de atualização (refresh tokens) para autenticação.
-- 'id' é a chave primária. 'token' e 'user_id' são campos únicos.
-- Vinculado a um 'usuarios' através de uma chave estrangeira.
CREATE TABLE IF NOT EXISTS refresh_token (
    id BIGINT NOT NULL PRIMARY KEY,
    expiry_date TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE, -- Garante que cada token seja único.
    user_id BIGINT UNIQUE NOT NULL, -- Garante que cada usuário tenha apenas um refresh token ativo (ou último token).
    CONSTRAINT fk_user_refresh_token FOREIGN KEY (user_id) REFERENCES usuarios (id)
);

-- Tabela para itens agregados, representando o agrupamento de um produto como todo o seu estoque válido.
-- 'id' é a chave primária. Armazena o nome e o código do item, e a quantidade em estoque.
CREATE TABLE IF NOT EXISTS item_agregado (
    id BIGINT NOT NULL PRIMARY KEY,
    item_code VARCHAR(255) NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    stock FLOAT(53)
);

-- Tabela para itens individuais (por exemplo, produtos em estoque com detalhes específicos).
-- 'id' é a chave primária. 'batch' é um campo único, útil para rastreamento de lotes.
-- Possui uma chave estrangeira para 'item_agregado', conectando-o a um item genérico.
CREATE TABLE IF NOT EXISTS item (
    id BIGINT NOT NULL PRIMARY KEY,
    batch VARCHAR(255) UNIQUE, -- Garante a unicidade do lote do item.
    code VARCHAR(255),
    has_validity BOOLEAN,
    is_deleted BOOLEAN NOT NULL,
    name VARCHAR(255),
    price FLOAT(53),
    quantity FLOAT(53),
    type VARCHAR(255),
    validity TIMESTAMP(6),
    item_agregado_id BIGINT,
    CONSTRAINT fk_item_agregado FOREIGN KEY (item_agregado_id) REFERENCES item_agregado (id)
);

-- Tabela para registrar vendas.
-- 'id' é um UUID como chave primária.
-- 'usuario_api_id' é único, indicando que uma venda é associada a um único usuário da API.
-- Possui chaves estrangeiras para 'clientes' e 'usuario_api'.
CREATE TABLE IF NOT EXISTS venda (
    id UUID NOT NULL PRIMARY KEY,
    hired_date TIMESTAMP(6),
    is_deleted BOOLEAN,
    payment_method VARCHAR(255),
    realization_date TIMESTAMP(6),
    status_id INTEGER,
    total FLOAT(53),
    cliente_id INTEGER NOT NULL,
    usuario_api_id BIGINT NOT NULL,
    CONSTRAINT fk_cliente_venda FOREIGN KEY (cliente_id) REFERENCES clientes (id),
    CONSTRAINT fk_usuario_api_venda FOREIGN KEY (usuario_api_id) REFERENCES usuario_api (id)
);

-- Tabela de junção para itens incluídos em uma compra.
-- 'id' é um UUID (Identificador Único Universal) como chave primária.
-- 'item_agregado_id' agora permite múltiplos usos do mesmo item agregado em diferentes linhas de item_compra.
-- Vincula um 'item agregado' a uma 'venda' (compra).
CREATE TABLE IF NOT EXISTS item_compra (
    id UUID NOT NULL PRIMARY KEY,
    quantity BIGINT NOT NULL,
    item_agregado_id BIGINT NOT NULL,
    compra_id UUID NOT NULL,
    CONSTRAINT fk_item_agregado_compra FOREIGN KEY (item_agregado_id) REFERENCES item_agregado (id),
    CONSTRAINT fk_compra FOREIGN KEY (compra_id) REFERENCES venda (id)
);











