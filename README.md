# atividade.guilherme
## Exercício 1 — Sistema de Controle de Aluguel de Quadra Esportiva

Tabelas Identificadas!

1. clientes
Armazena os dados dos clientes que alugam a quadra.

| Campo     | Tipo         | Descrição                        |
|-----------|--------------|----------------------------------|
| id        | INT (PK)     | Identificador único do cliente   |
| nome      | VARCHAR(100) | Nome completo do cliente         |
| telefone  | VARCHAR(20)  | Telefone de contato              |

2. horarios
Armazena os horários disponíveis para aluguel da quadra.

| Campo          | Tipo          | Descrição                              |
|----------------|---------------|----------------------------------------|
| id             | INT (PK)      | Identificador único do horário         |
| horario_inicio | TIME          | Hora de início do horário              |
| horario_fim    | TIME          | Hora de término do horário             |
| valor          | DECIMAL(10,2) | Valor cobrado pelo horário             |

3. alugueis
Registra cada aluguel realizado, vinculando cliente e horário.

| Campo      | Tipo          | Descrição                                       |
|------------|---------------|-------------------------------------------------|
| id         | INT (PK)      | Identificador único do aluguel                  |
| cliente_id | INT (FK)      | Referência ao cliente                           |
| horario_id | INT (FK)      | Referência ao horário reservado                 |
| data       | DATE          | Data do aluguel                                 |
| valor_pago | DECIMAL(10,2) | Valor efetivamente cobrado no aluguel           |
| pago       | BOOLEAN       | Indica se o pagamento foi realizado             |

Regras de Negócio Identificadas

1. Nome obrigatório: Não é permitido cadastrar um cliente com nome vazio.
2. Valor positivo: Não é permitido cadastrar um horário com valor negativo ou zero.
3. Sem sobreposição de reservas:Um mesmo horário não pode ser reservado por dois clientes na mesma data (horário ocupado não pode ser reservado novamente).
4. Cálculo automático do total diário: O sistema deve somar automaticamente o valor de todos os aluguéis de um mesmo cliente em um determinado dia.

## Exercício 2 — Sistema de Controle de Biblioteca Escolar

Tabelas Identificadas

 1. livros
Armazena os livros disponíveis na biblioteca.

| Campo               | Tipo         | Descrição                                    |
|---------------------|--------------|----------------------------------------------|
| id                  | INT (PK)     | Identificador único do livro                 |
| titulo              | VARCHAR(200) | Título do livro                              |
| autor               | VARCHAR(150) | Nome do autor                                |
| quantidade_total    | INT          | Quantidade total de exemplares               |
| quantidade_disponivel | INT        | Quantidade disponível para empréstimo        |

2. alunos
Armazena os dados dos alunos que podem pegar livros emprestados.

| Campo    | Tipo         | Descrição                      |
|----------|--------------|--------------------------------|
| id       | INT (PK)     | Identificador único do aluno   |
| nome     | VARCHAR(100) | Nome completo do aluno         |
| turma    | VARCHAR(20)  | Turma do aluno (opcional)      |

3. emprestimos
Registra cada empréstimo realizado.

| Campo          | Tipo     | Descrição                                         |
|----------------|----------|---------------------------------------------------|
| id             | INT (PK) | Identificador único do empréstimo                 |
| livro_id       | INT (FK) | Referência ao livro emprestado                    |
| aluno_id       | INT (FK) | Referência ao aluno que pegou o livro             |
| data_emprestimo | DATE    | Data em que o empréstimo foi realizado            |
| data_devolucao | DATE     | Data em que o livro foi devolvido (NULL se aberto)|


Regras de Negócio Identificadas

1. Título obrigatório: Não é permitido cadastrar um livro com título vazio.
2. Quantidade válida: Não é permitido cadastrar um livro com quantidade negativa.
3. Disponibilidade obrigatória para empréstimo: Um livro só pode ser emprestado se quantidade_disponivel for maior que zero.
4. atualização de estoque: Ao registrar um empréstimo, a quantidade_disponivel deve ser decrementada; ao registrar a devolução, deve ser incrementada.
5. Registro de devolução: O sistema deve permitir registrar a devolução de um livro, preenchendo a data_devolucao no empréstimo correspondente.

## Exercício 3 — Sistema de Controle de Pedidos em uma Lanchonete

Tabelas Identificadas

1. produtos
Armazena os produtos vendidos na lanchonete.

| Campo     | Tipo          | Descrição                          |
|-----------|---------------|------------------------------------|
| id        | INT (PK)      | Identificador único do produto     |
| nome      | VARCHAR(100)  | Nome do produto                    |
| descricao | TEXT          | Descrição do produto               |
| preco     | DECIMAL(10,2) | Preço unitário do produto          |

2. pedidos
Registra cada pedido realizado na lanchonete.

| Campo        | Tipo          | Descrição                                   |
|--------------|---------------|---------------------------------------------|
| id           | INT (PK)      | Identificador único do pedido               |
| data         | DATE          | Data em que o pedido foi realizado          |
| valor_total  | DECIMAL(10,2) | Valor total calculado automaticamente       |
| finalizado   | BOOLEAN       | Indica se o pedido foi finalizado           |

3. itens_pedido
Tabela intermediária que registra os produtos incluídos em cada pedido.

| Campo      | Tipo          | Descrição                                      |
|------------|---------------|------------------------------------------------|
| id         | INT (PK)      | Identificador único do item                    |
| pedido_id  | INT (FK)      | Referência ao pedido                           |
| produto_id | INT (FK)      | Referência ao produto                          |
| quantidade | INT           | Quantidade do produto no pedido                |
| preco_unit | DECIMAL(10,2) | Preço unitário no momento do pedido            |

Regras de Negócio Identificadas

1. Nome obrigatório: Não é permitido cadastrar um produto com nome vazio.
2. Preço positivo: Não é permitido cadastrar um produto com preço negativo ou zero.
3. Pedido com pelo menos um produto: Um pedido só pode ser finalizado se tiver pelo menos um produto adicionado (tabela itens_pedido deve ter ao menos um registro associado).
4. Cálculo automático do valor total: O sistema deve calcular e atualizar automaticamente o valor_total do pedido com base nos produtos e quantidades adicionados.
5. Consulta por data: Deve ser possível listar todos os pedidos realizados em uma data específica.
|
|
|
|
|
|
V
#Eu identifiquei todas as regras de negocio e suas tabelas, tentei fazelas de um jeito mais apresentavel e coloquei de um jeito que pareça banco de dados.
