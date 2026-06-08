# Concorrência e Consistência em Banco de Dados com Spring Boot

Este projeto demonstra e analisa os impactos da concorrência em sistemas transacionais, evidenciando o problema clássico de **Lost Update (Atualização Perdida)** e aplicando a solução de **Controle de Versão Otimista** com JPA/Hibernate e banco de dados H2.

---

## Alunas

| Aluno | Responsabilidade |
|-------|-----------------|
| **Aluno A** – Marielli Alves MAcedo| Implementação da Parte 1 – Cenário Sem Bloqueio, entidade `ContaBancaria`, regras de negócio iniciais e testes de estresse no Apache JMeter para evidenciar a inconsistência de saldo. |
| **Aluno B** – Júlia Monteiro Rodrigues | Implementação da Parte 2 – Solução Otimista, entidade `ContaBancariaVersionada` com `@Version`, interceptação de `ObjectOptimisticLockingFailureException` retornando HTTP `409 Conflict` e validação final com JMeter. |

---

## Como Executar

### Pré-requisitos

- Java 17+
- Apache JMeter (para execução dos planos de teste)

### Passos

1. Clone este repositório em sua máquina local:
   ```bash
   git clone https://github.com/Julia076/concorrencia-e-consistencia-spring-boot.git

2. Execute a aplicação na raiz do projeto:
   ```bash
   ./mvnw spring-boot:run
   ```

3. A aplicação estará disponível em `http://localhost:8080`.

4. O console do banco H2 pode ser acessado em `http://localhost:8080/h2-console`:
   - **JDBC URL:** `jdbc:h2:mem:bancodb`
   - **Usuário:** `sa`
   - **Senha:** *(deixar em branco)*

---

## Relatório de Análise Comparativa

Os testes foram executados com **100 threads simultâneas** no JMeter, todas disparando a operação de depósito na mesma conta corrente.

---

### 🔴 Cenário 1 — Sem Bloqueio (Lost Update)

**Comportamento:** Duas ou mais requisições leram o mesmo saldo inicial concorrentemente, calcularam o novo valor em memória e sobrescreveram o resultado uma da outra.

**Resultado no JMeter:** 100% das requisições retornaram `200 OK` (sucesso aparente).

**Inconsistência:** Apesar dos 100 depósitos, o saldo final ficou corrompido e defasado — prova de que as escritas simultâneas causaram perdas massivas de dados.

#### Evidências

**Relatório de Amostras (0% de erro aparente):**

![Relatório de Amostras](../imagens/relatorio-amostras.png)

**Árvore de Resultados (todas as requisições verdes):**

![Árvore de Resultados](../imagens/arvore-resultados.png)

**Console H2 (saldo final inconsistente):**

![Console H2](../imagens/console-h2-corrompido.png)
---

### 🟢 Cenário 2 — Com Controle Otimista (`@Version`)

**Comportamento:** O Hibernate passou a verificar o número de versão do registro antes de cada commit. A primeira transação incrementou a versão; as demais, baseadas em uma versão desatualizada, foram rejeitadas imediatamente.

**Resultado no JMeter:** Apenas as requisições sem conflito retornaram sucesso. As demais falharam de forma intencional e controlada (~64% de conflitos interceptados).

**Tratamento:** A exceção `ObjectOptimisticLockingFailureException` foi capturada no Controller, retornando `409 Conflict` ao cliente e garantindo integridade total do saldo.

#### Evidências

**Árvore de Resultados (bloqueios retornando falha controlada em vermelho):**

![Árvore de Resultados - Bloqueio Ativo](../imagens/arvore-bloqueio-ativo.png)

**Relatório de Erros (64% de concorrência interceptada):**

![Relatório de Erros](../imagens/relatorio-erros-64.png)

**Console H2 (saldo protegido e coluna VERSION incrementada corretamente):**

![Console H2 - Versão](../imagens/console-h2-versao.png)
---

## Resumo Comparativo

| Indicador | 🔴 Cenário 1 (Sem Controle) | 🟢 Cenário 2 (Com `@Version`) |
|-----------|----------------------------|-------------------------------|
| **Status HTTP no JMeter** | 100% `200 OK` (sucesso falso) | Falhas controladas `409 Conflict` |
| **Integridade dos Dados** | Corrompida (saldo inconsistente) | Totalmente preservada |
| **Comportamento do Banco** | Sobrescrita cega de registros | Validação de versão antes do commit |

---

> 📁 O arquivo de plano de testes do JMeter (`.jmx`) encontra-se na raiz do repositório para fins de auditoria e reprodução dos experimentos.
