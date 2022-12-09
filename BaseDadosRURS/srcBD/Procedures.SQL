-- Procedimento que insere um novo aluno na BD com o nome fornecido.
-- O novo aluno e colocado no ultimo lugar da hierarquia e 
-- retorna o id que lhe foi atribuido.
CREATE PROCEDURE insere_aluno 
    @nome         VARCHAR(80),
    @data_entrada DATE
AS
    DECLARE @id AS INT = (SELECT MAX(id) FROM aluno) + 1;
    DECLARE @hierarquia AS INT = (SELECT MAX(hierarquia) FROM aluno) + 1;
BEGIN
    INSERT INTO aluno VALUES (@id, @nome, @hierarquia, @data_entrada);
    SELECT @id;
END;

-- Procedimento que insere uma nova estadia de um aluno na BD com
-- os dados fornecidos e quotas a 0. Se ja existir uma estadia do 
-- mesmo aluno nesse ano e lancada uma excecao.
CREATE PROCEDURE insere_estadia 
    @ano VARCHAR(7),
    @aluno  INT,
    @quarto FLOAT
AS BEGIN
    BEGIN TRY
        INSERT INTO reside VALUES(@ano, @aluno, @quarto, 0);
    END TRY
    BEGIN CATCH
        SELECT ERROR_MESSAGE() AS MensagemDeErro;
    END CATCH;
END;

-- Procedimento que altera a posicao na hierarquia do aluno fornecido
-- para a posicao escolhida, ajustando as posicoes dos alunos adjacentes.
-- Realiza verificacoes para querys incompativeis.
CREATE PROCEDURE altera_posicao_hierarquia
    @aluno   INT,
    @posicao INT
AS 
    DECLARE @atual AS INT = (SELECT hierarquia FROM aluno WHERE id = @aluno);
    DECLARE @max   AS INT = (SELECT MAX(hierarquia) FROM aluno);
BEGIN
    IF @posicao <= @max AND @posicao >= 1
	BEGIN
        UPDATE aluno SET hierarquia = @posicao WHERE id = @aluno;
        IF @atual > @posicao
			BEGIN
			SET @atual = @atual-1;
            WHILE @atual >= @posicao
				BEGIN
					UPDATE aluno SET hierarquia = @atual+1 WHERE hierarquia = @atual AND id != @aluno;
					SET @atual = @atual-1;
				END;
			END;
        ELSE IF @atual < @posicao
			BEGIN;
            SET @atual = @atual+1;
            WHILE @atual <= @posicao
				BEGIN
					UPDATE aluno SET hierarquia = @atual-1 WHERE hierarquia = @atual AND id != @aluno;
                    SET @atual = @atual+1;
				END;
			END;
		END;
    ELSE
		BEGIN
        PRINT 'Posicao invalida'; 
		END;
END;


-- Procedimento que remove o aluno especificado da hierarquia, 
-- ajustando as posicoes dos outros alunos.
CREATE PROCEDURE remove_hierarquia
    @aluno   INT
AS
    DECLARE @max   AS INT = (SELECT MAX(hierarquia) FROM aluno);
    DECLARE @atual AS INT = (SELECT hierarquia FROM aluno where id = @aluno) + 1;
BEGIN
    UPDATE aluno SET hierarquia = NULL WHERE id = @aluno;
    WHILE @atual <= @max
        BEGIN
            UPDATE aluno SET hierarquia = hierarquia-1 WHERE hierarquia = @atual;
            SET @atual = @atual+1;
        END;
END;

-- Procedimento que permite um antigo aluno voltar a residencia,
-- sendo colocado no final da hierarquia
CREATE PROCEDURE volta_residencia
    @aluno        INT,
    @data_entrada DATE
AS
    DECLARE @novoMax AS INT = (SELECT MAX(hierarquia) FROM aluno) + 1;
BEGIN
    IF (SELECT hierarquia FROM aluno WHERE id = @aluno) IS NULL
        BEGIN
            UPDATE aluno SET hierarquia = @novoMax, data_entrada = @data_entrada WHERE id = @aluno;
        END;
    ELSE
        BEGIN
        PRINT 'Aluno ja esta na hierarquia!'
        END;
END;

-- Procedimento especifica o pagamento de quotas de um aluno no 
-- ano letivo especificado
CREATE PROCEDURE paga_quotas
    @aluno INT,
    @ano   VARCHAR(7)
AS BEGIN
    UPDATE reside SET quotas = 1 WHERE aluno = @aluno AND ano = @ano;
END;