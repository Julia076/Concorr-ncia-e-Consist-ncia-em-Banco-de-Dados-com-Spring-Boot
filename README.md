# 🏦 Concorrência e Consistência em Banco de Dados

Trabalho prático sobre controle de concorrência em aplicações Java com Spring Boot e JPA.

## Alunas

| Nome | Responsabilidade |
|------|-----------------|
| Marielli Alves MAcedo | Parte 1 — Sem controle de concorrência |
| Julia Monteiro Rodrigues | Parte 2 — Controle com `@Version` |

---

## Sobre o Projeto

Este projeto demonstra na prática os problemas gerados pela falta de controle de concorrência em operações bancárias simultâneas, e como a anotação `@Version` do Hibernate resolve esse problema através do mecanismo de **bloqueio otimista**.

### O problema: Lost Update

Sem controle de concorrência, duas threads podem ler o mesmo saldo, aplicar operações independentes e salvar — fazendo com que uma das atualizações seja perdida silenciosamente.

```
Thread A lê saldo: R$ 100
Thread B lê saldo: R$ 100
Thread A deposita R$ 50 → salva R$ 150
Thread B deposita R$ 30 → salva R$ 130  ← sobrescreve Thread A!
Saldo correto deveria ser: R$ 180
```

### A solução: `@Version`

Com `@Version`, o Hibernate verifica se o registro foi modificado desde a última leitura. Se sim, lança `ObjectOptimisticLockingFailureException` e a operação é abortada — evitando dados corrompidos.

---

## 🚀 Tecnologias

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Banco H2 (em memória)
- Apache JMeter

---

## Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+

### Passos

```bash
# 1. Clonar o repositório
git clone URL_DO_REPOSITORIO

# 2. Entrar na pasta
cd nome-do-projeto

# 3. Executar
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

### Console H2

Acesse `http://localhost:8080/h2-console` com as configurações:

```
JDBC URL : jdbc:h2:mem:testdb
User     : sa
Password : (deixar em branco)
```

---

## Endpoints

### Parte 1 — Sem controle de concorrência

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/contas/{id}/deposito` | Realiza depósito na conta |
| `POST` | `/contas/{id}/saque` | Realiza saque na conta |

### Parte 2 — Com controle de versão

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/contas-versionadas/{id}/deposito` | Depósito com controle de versão |
| `POST` | `/contas-versionadas/{id}/saque` | Saque com controle de versão |

**Exemplo de body:**
```json
{
  "valor": 100.00
}
```

---

##  Testes de Concorrência (JMeter)

Os testes simulam múltiplos usuários realizando operações simultâneas na mesma conta.

### Cenário 1 — Sem `@Version`

- **Configuração:** N threads simultâneas sobre a mesma conta
- **Resultado esperado:** Inconsistências de saldo (Lost Update)
- **Observação:** O saldo final diverge do valor correto sem nenhum erro ser lançado

### Cenário 2 — Com `@Version`

- **Configuração:** Mesmas N threads sobre a conta versionada
- **Resultado esperado:** `ObjectOptimisticLockingFailureException` em operações conflitantes
- **Observação:** O saldo permanece consistente; conflitos são detectados e tratados

---

## Comparativo

| | Sem `@Version` | Com `@Version` |
|---|---|---|
| Integridade dos dados | ❌ Comprometida | ✅ Garantida |
| Lost Update | ❌ Ocorre silenciosamente | ✅ Detectado e rejeitado |
| Erro em conflito | ❌ Nenhum | ✅ `ObjectOptimisticLockingFailureException` |
| Consistência sob carga | ❌ Não | ✅ Sim |

---

## Conceitos Abordados

- **Lost Update** — problema clássico de concorrência onde uma atualização sobrescreve outra
- **Bloqueio Otimista** — estratégia que permite leituras livres e valida conflitos apenas na escrita
- **`@Version`** — anotação JPA/Hibernate que implementa bloqueio otimista via campo de versão
- **`ObjectOptimisticLockingFailureException`** — exceção lançada pelo Spring quando um conflito é detectado
