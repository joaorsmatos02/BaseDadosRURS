DROP TABLE reside;
DROP TABLE aluno;
DROP TABLE quarto;

CREATE TABLE quarto(
	numero	   FLOAT,
	andar	   INT    CONSTRAINT nn_andar_andar NOT NULL,
	lado	   CHAR   CONSTRAINT nn_andar_lado NOT NULL,
	individual BIT	  CONSTRAINT nn_andar_individual NOT NULL,
	--
	CONSTRAINT ck_andar_quarto
		CHECK (andar > -1 AND andar < 4),
	--
	CONSTRAINT ck_lado_quarto
		CHECK (lado = 'E' OR lado = 'D'),
	-- 
	CONSTRAINT pk_quarto
		PRIMARY KEY (numero)
);

CREATE TABLE aluno(
	ID		     INT,
	nome	     VARCHAR(80) CONSTRAINT nn_nome_aluno NOT NULL,
	hierarquia   INT,
	data_entrada DATE
	--
	CONSTRAINT un_ID_aluno
		UNIQUE (ID),
	--
	CONSTRAINT ck_ID_aluno
		CHECK (ID > 0),
	--
	CONSTRAINT pk_aluno
		PRIMARY KEY (ID)
);

CREATE TABLE reside(
	ano     VARCHAR(7) CONSTRAINT nn_ano_reside NOT NULL, -- formato 'XXXX/XX', ex: '2021/22'
	aluno 	INT,
	quarto	FLOAT,
	quotas  BIT        CONSTRAINT nn_quotas_reside NOT NULL, 
	--
	CONSTRAINT fk_aluno_reside
		FOREIGN KEY (aluno) REFERENCES aluno(ID),
	--
	CONSTRAINT fk_quarto_reside
		FOREIGN KEY (quarto) REFERENCES quarto(numero),
	--
	CONSTRAINT pk_reside
		PRIMARY KEY (ano, aluno)
);

-- ----------------------------------------------------------------------------
-- Quartos
-- ----------------------------------------------------------------------------

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(0.001, 0, 'E', 0);

-- ----------------------------------------------------------------------------

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(1.001, 1, 'D', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(1.002, 1, 'D', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(1.003, 1, 'D', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(1.004, 1, 'D', 1);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(1.005, 1, 'E', 1);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(1.006, 1, 'E', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(1.007, 1, 'E', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(1.008, 1, 'E', 0);

-- ----------------------------------------------------------------------------

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(2.001, 2, 'D', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(2.002, 2, 'D', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(2.003, 2, 'D', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(2.004, 2, 'D', 1);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(2.005, 2, 'E', 1);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(2.006, 2, 'E', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(2.007, 2, 'E', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(2.008, 2, 'E', 0);

-- ----------------------------------------------------------------------------

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(3.001, 3, 'D', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(3.002, 3, 'D', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(3.003, 3, 'D', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(3.004, 3, 'D', 1);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(3.005, 3, 'E', 1);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(3.006, 3, 'E', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(3.007, 3, 'E', 0);

INSERT INTO quarto (numero, andar, lado, individual)
	VALUES(3.008, 3, 'E', 0);

-- ----------------------------------------------------------------------------
-- Alunos
-- ----------------------------------------------------------------------------

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (1, 'Dália Teixeira', 1);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (2, 'Luísa Caldeira', 2);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (3, 'Bernardo Marrafa', 3);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (4, 'Letícia Salgueiro', 4);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (5, 'Daniela Nunes', 5);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (6, 'Rúben Sousa', 6);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (7, 'Ana Canário', 7);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (8, 'Carolina Dias', 8);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (9, 'António Pereira', 9);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (10, 'Samuel Pinheiro', 10);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (11, 'Henrique Rosado', 11);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (12, 'Alexandre Costa', 12);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (13, 'Ana Godinho', 13);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (14, 'Felipe Cunha', 14);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (15, 'João Filipe', 15);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (16, 'Maria Sebastião', 16);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (17, 'Flávia Maia', 17);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (18, 'Samuel Teixeira', 18);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (19, 'João Matos', 19);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (20, 'Joana Lopes', 20);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (21, 'Joel Esteves', 21);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (22, 'Afonso Serra', 22);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (23, 'Henrique Carvalinho',23);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (24, 'Patrícia Coelho', 24);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (25, 'Ana Oliveira', 25);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (26, 'Maria Silva', 26);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (27, 'Mariana Atalaia', 27);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (28, 'Sofia Cabral', 28);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (29, 'Vanessa Robalo', 29);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (30, 'Teresa Fontes', 30);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (31, 'Beatriz Ferreira', 31);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (32, 'Laura Vieira', 32);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (33, 'Carlos Guerra', 33);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (34, 'Pedro Monteiro', 34);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (35, 'Tomás Camolas', 35);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (36, 'João Lima', 36);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (37, 'Maria Silva', 37);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (38, 'Emil', NULL);

INSERT INTO aluno(ID, nome, hierarquia)
	VALUES (39, 'Luxithan', NULL);

-- ----------------------------------------------------------------------------
-- 1º andar 2021/22
-- ----------------------------------------------------------------------------
INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 23, 1.008); -- Substituir nome pelo ID

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 3, 1.008);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 14, 1.006);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22' , 21, 1.006);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 12, 1.005);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 22, 1.001);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 18, 1.001);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 34, 1.002);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 35, 1.002);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 9, 1.003);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 11, 1.004);
-- ----------------------------------------------------------------------------
-- 2º andar 2021/22
-- ----------------------------------------------------------------------------
INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 19, 2.008);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 15, 2.008);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 33, 2.007);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 36, 2.007);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 38, 2.006);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 39, 2.006);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 10, 2.005);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 13, 2.001);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 20, 2.001);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 28, 2.002);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 37, 2.002);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 24, 2.003);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 25, 2.003);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 7, 2.004);
-- ----------------------------------------------------------------------------
-- 3º andar 2021/22
-- ----------------------------------------------------------------------------
INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 27, 3.005);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 1, 3.005);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 17, 3.004);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 32, 3.006);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 31, 3.006);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 5, 3.007);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 26, 3.007);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 8, 3.003);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 30, 3.001);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 29, 3.001);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 16, 3.002);

INSERT INTO reside(ano, aluno, quarto)
	VALUES('2021/22', 2, 3.002);