O projeto BaseDadosRURS tem como objetivo proporcionar aos residentes da residência universitária Ribeiro Santos, da Universidade de Lisboa,
uma plataforma onde podem facilmente consultar informações relativas á administração da mesma, por parte da comissão de residentes.  Para
estes, serve também como uma ferramenta administrativa, que permite a gerência dos seus registos, com propósito final de criação e 
manutenção de um registo histórico dos alunos desta residência.

Este documento pretende servir como um guia de uso, manutenção e futura expansão deste projeto, estando dividido em três partes 
fundamentais, precedidas por um breve guia de instalação: Primeiramente, será apresentado um guia de uso por parte dos residentes, que 
deverá ser expandido ao serem adicionadas novas funcionalidades. De seguida, irá ser detalhado o uso e manutenção da plataforma por parte de 
futuras comissões, de forma simplificada que não exija conhecimentos técnicos profundos. Por fim, será apresentado um guia de carácter mais
técnico, descrevendo o funcionamento da aplicação com o propósito de facilitar futuras expansões e correções de eventuais bugs.


Instalação
------------------------------
Este projeto tem como pré-requisito a instalação da versão 8 do Java.Para fazer download do programa deve aceder á secção "Releases" deste repositório 
ou diretamente á pasta "ver" onde estão arquivadas todas as versões existentes. Após fazer download e extrair o ficheiro zip, o programa pode ser 
executado através do ficheiro launch.bat, não sendo necessário qualquer passo de instalação adicional. É possível criar um atalho para este ficheiro,
de forma a iniciar o programa a partir de outra localização.

Uso por residentes
------------------------------
Após iniciar a aplicação, residentes devem efetuar o seu login através do botão "Entrar como residente", não sendo requerida qualquer autenticação
adicional. Depois de um login bem sucedido terão acesso a todos os conteúdos presentes na base de dados, podendo consultar as vistas pré-definidas ou
executar uma query SQL personalizada. A única limitação deste tipo de acesso é a impossibilidade de modificar quaisquer dados presentes na base de 
dados, pelo que qualquer query executada deve ser do tipo "SELECT". Na eventualidade de ser encontrada alguma informação incorreta ou incompleta, a 
ocorrência deve ser comunicada aos membros competentes da comissão, para que possa ser retificada.

Uso pela comissão
------------------------------
Os membros da comissão devem fazer o seu login através de autenticação integrada (na máquina host da base de dados) ou através das suas credenciais SQL
Server. Este tipo de login dá acesso a todas as funcionalidades disponíveis aos residentes, bem como permissão para alterar os dados presentes na base
de dados. Para este efeito, é possível realizar qualquer query SQL válida e existe um menu de execução de procedimentos, bastando preencher os campos
corretamente. Para mais informação sobre cada procedimento deve consultar a documentação do codigo fonte da base de dados, presente no repositório git.

Numa eventual mudança de host, existem alguns passos que deverão ser seguidos para preparar uma nova máquina para receber a base de dados. A seguinte
lista aplica-se ao sistema operativo Windows 10, pelo que carece de adaptação a outros SOs e possiveis atualizações do software usado.

 - No host atual, deve fazer clique direito na base de dados RURS, aceder a Tasks e Generate Scripts...
 - No menu seguinte deve escolher toda a base de dados 
 - Sob "Set Scripting Options" deve escolher "Save as script file" e em "Advanced" alterar "Types of data to script" para "Schema and Data".
 - Quando o processo terminar será gerado um ficheiro .sql que deve ser fornecido ao novo host
 - Deve ser instalado Microsoft SQL Server, Microsoft SQL Server Management Studio (neste guia, usada a versão 18.12.21),  bem como o SQL Server Configuration Manager (19)
 - Após uma configuração inicial e criação da base de dados deve executar uma query com o conteúdo do ficheiro gerado no host antigo, replicando a base de dados na máquina atual
 - Quando a base de dados RURS estiver presente na nova máquina, deve aceder ás propriedades do servidor no SQL Server Manager e sob "Connections"
   habilitar a opção "Allow remote connections to this server".
 - Sob "Security" deve alterar a opção "Windows Authentication Mode" para "SQL Server and Windows Authentication Mode"
 - No Server Configuration Manager, sob "SQL Server Network Configuration" e "Protocols for MSSQLSERVER" habilitar o protocolo "TCP/IP" e escolher a 
   porta desejada nas propriedades TCP/IP na secção "IPAll" (recomenda-se a porta 1433)
 - Após estes passos é recomendado reiniciar a instância do servidor, que pode ser feito sob "SQL Server Services", reiniciando a opção "SQL Server
   (Instance Name)"
 - De seguida, menu iniciar do sistema operativo pesquisar "Firewall do Windows Defender com Segurança Avançada".
 - Em "Regras de Entrada" criar uma nova regra, de tipo "Porta"
 - No próximo menu escolher TCP e específicar a porta selecionada anteriormente (recomenda-se a porta 1433)
 - No menu seguinte escolher "Permitir a conexão"
 - De seguida habilitar todas as três opções e, por fim, dar um pequeno nome e descrição á regra criada.
 - No final desta configuração deve-se tomar nota do nome do servidor, no Server Management Studio ou do IP local da máquina.

Após uma migração de host, os residentes devem ser informados das credenciais da nova máquina para que possam atualizar os seus ficheiros properties. 
Como incêntivo á não redistribuição, este programa apenas se liga á base de dados com o nome "RURS", ignorando quaisquer outras presentes na máquina. 
Recomenda-se ainda a alteração da password SQL Server aquando de mudanças no corpo da comissão.

Funcionamento
------------------------------

Todo o projeto tem como base uma base de dados SQL Server que possui 3 tabelas, bem como várias views e procedures definidos em PL/SQL. Dado que este 
método não possuí uma interface gráfica intuitiva, criou-se um programa Java que se liga a esta base de dados e exibe, de uma forma elegante, os seus 
conteúdos. Esta camada do projeto (que engloba a todalidade do código Java, FXML e CSS) segue vários padrões GoF de forma a simplificar o seu 
funcionamento e implementa um padrão global de desenvolvimento para facilitar a sua expansão. Após fazer download do código fonte, é necessário ter em
atenção que é usada a versão 8 do Java e que, por se tratar de uma aplicação JavaFX, deve ser corrida com o argumento VM "--module-path "caminho\ 
para\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml".

O projeto encontra-se organizado em packages. O package main em particular contém as classes fundamentais para o funcionamento da aplicação, tais como
adaptadores a serviços externos. Este package contém ainda todo o código necessário para efetuar o login e iniciar o caso de uso pretendido. De resto,
deve existir um package para cada caso de uso, que deverá conter obrigatóriamente o handler desse caso de uso, bem como os controllers e ficheiros 
FXML da sua interface. 

O handler deve extender a classe abstrata HandlerTemplate e, com a exceção do método launchGUI (que inicializa a sua GUI), deverá apenas conter os
métodos fundamentais do caso de uso, nomeadamente aqueles que impliquem comunicação com as classes fundamentais do package main. É também aqui que 
deverão ser feitas quaisquer verificações e confirmações específicas do caso de uso e possívelmente lançadas exceções. Apesar de não implementarem o 
padrão singleton (devido a possuir uma interface), deverá sempre existir uma única instância de cada handler, pelo que este deverá ser passado através 
de "this" aos controllers quando necessário.

De seguida, nas classes controller deverá ser feita a ligação entre o respetivo handler e a sua interface. Estas classes devem ser sempre marcadas
como controller no ficheiro FXML respetivo, existindo um controller por cada FXML (que, por sua vez, corresponde a uma interface, relembrando que um
caso de uso pode ser várias interfaces para um só handler). Os ficheiros controller têm como atributos todos os elementos da interface relevantes, 
bem como quaisquer outros objetos que se revelem importantes á sua execução. Em termos de métodos, os controllers devem possuir um método que recebe 
os objetos necessários e que prepara a interface (que possui argumentos variáveis, pelo que não pode ser normalizado numa interface ou classe 
abstrata) com os elementos não especifícados em CSS ou FXML. A preparação de cada elemento deve ser feita através de métodos privados chamados pelo 
método de set up, de forma a produzir código mais legivel. Por fim, deve possuir também os métodos handler de cada elemento da interface que 
necessite, que por sua vez devem chamar métodos do handler, bem como outros métodos privados que se revelem úteis. As classes controller são ainda 
responsáveis por chamar novas interfaces e apanhar exceções, emitindo alertas se necessário.

Por fim existem os ficheiros FXML usados para definir o formato geral da interface, sendo dada preferência ao uso da base stackpane, e um ficheiro CSS
único na base src, usado para aplicar estilos a elementos, preferencialmente generalizados em toda a aplicação para manter uma imagem visual
consistente. Opcionalmente, os packages podem ainda conter outras classes relevantes ao caso de uso em questão.

Recomenda-se a futuros comissários que pretendam expandir a aplicação que sigam este padrão, mantendo o código consistente, relembrando que este breve
guia não dispensa a consulta dos ficheiros existentes, em especial a documentação javadoc contida nos mesmos e aconselhando a continuação da produção
de documentação que permita a constante melhoria desta ferramenta. Após uma atualização deve ser emitida uma nova release aos residentes, na forma de
um zip com um runnable JAR e todos os ficheiros necessários á execução do programa (DLLs, config, etc.), bem como um ficheiro batch que inicie a 
aplicação.
