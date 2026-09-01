-- 1. Tabela CLIENTE
CREATE TABLE cliente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf_cnpj VARCHAR(20) NOT NULL UNIQUE,
    telefone VARCHAR(20)
);

-- 2. Tabela VENDEDOR
CREATE TABLE vendedor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    salario DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (salario >= 0)
);

-- 3. Tabela FORNECEDOR
CREATE TABLE fornecedor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    razao_social VARCHAR(150) NOT NULL,
    cnpj VARCHAR(20) NOT NULL UNIQUE,
    telefone VARCHAR(20)
);

-- 4. Tabela PRODUTO
CREATE TABLE produto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    estoque INT NOT NULL DEFAULT 0 CHECK (estoque >= 0),
    preco DECIMAL(10,2) NOT NULL CHECK (preco >= 0),
    fornecedor_id INT NOT NULL,
    FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id)
);

-- 5. Tabela PEDIDO
CREATE TABLE pedido (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_pedido DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (valor_total >= 0),
    cliente_id INT NOT NULL,
    vendedor_id INT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (vendedor_id) REFERENCES vendedor(id)
);

-- 6. Tabela ITEM_PEDIDO
CREATE TABLE item_pedido (
    id INT PRIMARY KEY AUTO_INCREMENT,
    quantidade INT NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(10,2) NOT NULL CHECK (preco_unitario >= 0),
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    pedido_id INT NOT NULL,
    produto_id INT NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);





