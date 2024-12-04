-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 01/12/2024 às 14:13
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `banco_n2`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `agencias`
--

CREATE TABLE `agencias` (
  `numAgencia` int(11) NOT NULL,
  `nome` varchar(50) DEFAULT NULL,
  `endereco` varchar(50) NOT NULL,
  `cep` varchar(9) NOT NULL,
  `cidade` varchar(50) DEFAULT NULL,
  `estado` char(2) DEFAULT NULL,
  `telefone` varchar(14) NOT NULL,
  `gerenteResponsavel` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `agencias`
--

INSERT INTO `agencias` (`numAgencia`, `nome`, `endereco`, `cep`, `cidade`, `estado`, `telefone`, `gerenteResponsavel`) VALUES
(1, 'Agência Centro', 'Rua Principal, 100', '01000-000', 'São Paulo', 'SP', '(11) 1234-5678', 'Carlos Silva'),
(2, 'Agência Sul', 'Av. Sul, 250', '02000-000', 'Rio de Janeiro', 'RJ', '(21) 2345-6789', 'Ana Oliveira'),
(3, 'Agência Norte', 'Rua da Paz, 123', '03000-000', 'Belo Horizonte', 'MG', '(31) 3456-7890', 'Marcos Pereira'),
(4, 'Agência Oeste', 'Av. Oeste, 400', '04000-000', 'Porto Alegre', 'RS', '(51) 4567-8901', 'Luciana Costa'),
(5, 'Agência Leste', 'Rua Leste, 150', '05000-000', 'Salvador', 'BA', '(71) 5678-9012', 'Paulo Souza'),
(6, 'Agência Norte 2', 'Av. Norte, 550', '06000-000', 'Fortaleza', 'CE', '(85) 6789-0123', 'Renata Lima'),
(7, 'Agência Ibirapuera', 'Rua Ibirapuera, 320', '07000-000', 'São Paulo', 'SP', '(11) 7890-1234', 'Fernando Martins'),
(8, 'Agência Maracanã', 'Rua Maracanã, 99', '08000-000', 'Rio de Janeiro', 'RJ', '(21) 8901-2345', 'Sandra Rocha'),
(9, 'Agência Copacabana', 'Av. Atlântica, 500', '09000-000', 'Rio de Janeiro', 'RJ', '(21) 9012-3456', 'Eduardo Almeida'),
(10, 'Agência Campinas', 'Rua Campinas, 70', '10000-000', 'Campinas', 'SP', '(19) 1234-5678', 'Bruna Souza');

-- --------------------------------------------------------

--
-- Estrutura para tabela `cartoes`
--

CREATE TABLE `cartoes` (
  `id` int(11) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `numero` varchar(20) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `numero_seguranca` varchar(3) NOT NULL,
  `data_validade` varchar(5) NOT NULL,
  `senha` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `clientes`
--

CREATE TABLE `clientes` (
  `id` int(11) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  `telefone` varchar(14) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `nacionalidade` varchar(20) NOT NULL,
  `data_nasc` date NOT NULL,
  `endereco` varchar(100) DEFAULT NULL,
  `complemento` varchar(20) DEFAULT NULL,
  `cidade` varchar(50) DEFAULT NULL,
  `estado` char(2) DEFAULT NULL,
  `cep` varchar(9) DEFAULT NULL,
  `senha` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `clientes`
--

INSERT INTO `clientes` (`id`, `nome`, `cpf`, `telefone`, `email`, `nacionalidade`, `data_nasc`, `endereco`, `complemento`, `cidade`, `estado`, `cep`, `senha`) VALUES
(1, 'Gabriel Soares', '111.111.111-11', '(11)91234-1234', 'gabrsoaress1@gmail.com', 'Brasileiro', '2001-11-01', 'Rua 1', 'Casa 1', 'São Bernardo do Campo', 'SP', '13252-123', '$2a$12$olUMkTABhpta/8dFU5g3jOXsCjBxmlWT6I9FeC7f1YtwqAkCvXldm'),
(3, 'Thais Oliveira', '222.222.222-22', '(11)91232-1231', 'thatha.gos@gmail.com', 'Brasileira', '2002-10-30', 'Rua 2', 'Apto 300', 'Itu', 'SP', '13212-123', '$2a$12$UaOimyMZa1mQHFN/PgoIgOIkbgXFgrWaS7YGyxDBKUS0BXrkzkx6.'),
(5, 'Cliente Teste', '123.123.123-12', '(11)99123-1322', 'teste@testandoemail.com', 'Brasileiro', '2004-11-09', 'Rua teste', 'Teste', 'Teste', 'SP', '12341-123', '$2a$12$Q4MF5ZCIY1fElwLc0w8j5e./8kTBAVpkrGpR9KNNQc3Dg0af6LIUC');

-- --------------------------------------------------------

--
-- Estrutura para tabela `contas`
--

CREATE TABLE `contas` (
  `id` int(11) NOT NULL,
  `tipo` int(11) NOT NULL,
  `id_agencia` int(11) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `saldo` decimal(15,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `contas`
--

INSERT INTO `contas` (`id`, `tipo`, `id_agencia`, `id_cliente`, `saldo`) VALUES
(1, 1, 3, 1, 400.00),
(2, 2, 3, 1, 500.00),
(5, 1, 1, 3, 390.00),
(6, 2, 1, 3, 0.00),
(9, 1, 4, 5, 0.00),
(10, 2, 4, 5, 0.00);

-- --------------------------------------------------------

--
-- Estrutura para tabela `transacoes`
--

CREATE TABLE `transacoes` (
  `id` int(11) NOT NULL,
  `tipo_transacao` varchar(20) NOT NULL,
  `id_conta_remet` int(11) DEFAULT NULL,
  `id_conta_dest` int(11) DEFAULT NULL,
  `valor` decimal(15,2) NOT NULL,
  `data` date NOT NULL,
  `hora` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `transacoes`
--

INSERT INTO `transacoes` (`id`, `tipo_transacao`, `id_conta_remet`, `id_conta_dest`, `valor`, `data`, `hora`) VALUES
(2, 'Transferência', 5, 1, 100.00, '2024-11-28', '17:22:49'),
(4, 'Transferência', 1, NULL, 1.00, '2024-11-28', '18:56:00'),
(5, 'Transferência', NULL, 1, 1.00, '2024-11-28', '18:56:30');

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `agencias`
--
ALTER TABLE `agencias`
  ADD PRIMARY KEY (`numAgencia`);

--
-- Índices de tabela `cartoes`
--
ALTER TABLE `cartoes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_cliente` (`id_cliente`);

--
-- Índices de tabela `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `cpf` (`cpf`);

--
-- Índices de tabela `contas`
--
ALTER TABLE `contas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_cliente` (`id_cliente`);

--
-- Índices de tabela `transacoes`
--
ALTER TABLE `transacoes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_conta_remet` (`id_conta_remet`),
  ADD KEY `id_conta_dest` (`id_conta_dest`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `agencias`
--
ALTER TABLE `agencias`
  MODIFY `numAgencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de tabela `cartoes`
--
ALTER TABLE `cartoes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de tabela `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de tabela `contas`
--
ALTER TABLE `contas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de tabela `transacoes`
--
ALTER TABLE `transacoes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `cartoes`
--
ALTER TABLE `cartoes`
  ADD CONSTRAINT `cartoes_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id`) ON DELETE CASCADE;

--
-- Restrições para tabelas `contas`
--
ALTER TABLE `contas`
  ADD CONSTRAINT `contas_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id`) ON DELETE CASCADE;

--
-- Restrições para tabelas `transacoes`
--
ALTER TABLE `transacoes`
  ADD CONSTRAINT `transacoes_ibfk_1` FOREIGN KEY (`id_conta_remet`) REFERENCES `contas` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `transacoes_ibfk_2` FOREIGN KEY (`id_conta_dest`) REFERENCES `contas` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
