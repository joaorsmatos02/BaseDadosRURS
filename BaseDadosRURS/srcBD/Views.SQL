-- Antigos alunos
CREATE VIEW antigos_alunos AS
SELECT a.id, a. nome, a.data_entrada
FROM aluno a
WHERE a.hierarquia IS NULL;


-- Hierarquia
CREATE VIEW hierarquia AS
SELECT a.id, a.nome, a.hierarquia, a.data_entrada
FROM aluno a
WHERE a.hierarquia IS NOT NULL;

-- Quartos Individuais
CREATE VIEW quarto_individual AS
SELECT a.id, a.nome 
FROM aluno a
WHERE EXISTS (SELECT *
			  FROM reside r, quarto q
			  WHERE r.aluno = a.id
              AND r.quarto = q.numero
			  AND q.individual = 1);

-- Layout 2022/23
CREATE VIEW planta_202223 AS
SELECT a.id, a.nome, r.quarto, r.quotas
FROM reside r, aluno a
WHERE a.id = r.aluno
AND r.ano LIKE '2022/23';