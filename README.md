# 🍕 Pits A

Recentemente, diversas empresas do ramo alimentício têm se desvinculado dos grandes aplicativos de delivery. As causas
dessa tendência são diversas e vão desde a transformação no modo de operação de cada estabelecimento, até as taxas
abusivas das grandes plataformas.

Porém, em 2023, simplesmente não é viável voltar ao modo de trabalho “pré-Ifood”... Foi por isso que a pizzaria Pits A
decidiu desenvolver seu próprio aplicativo de delivery. E adivinha só… vocês foram escolhidos para ajudar!

Projeto acadêmico da disciplina de Projeto de Software (UFCG). API REST em Spring Boot para
um app de delivery de pizza: clientes, estabelecimentos, sabores, entregadores e pedidos.

## 🛠 Stack

Java 21 · Spring Boot 3.3 · Spring Data JPA · H2 (em memória) · Gradle · JUnit 5

## ▶️ Como rodar

Requer JDK 21. O wrapper do Gradle já vem no repositório.

```bash
./gradlew bootRun      # sobe a aplicação em http://localhost:8080
./gradlew test         # roda a suíte de testes
./gradlew build        # compila e gera o jar
```

O banco é H2 em memória e é recriado a cada inicialização (`ddl-auto=create-drop`), então não há
nenhum passo de setup de banco. Usuário e senha saem das variáveis `DB_USER` e `DB_PASSWORD`
(o padrão é `sa` sem senha).

> **Windows:** evite caminhos com acentos. O worker de testes do Gradle falha ao carregar as
> classes quando o caminho do projeto tem caracteres não-ASCII.

### 🔗 Endereços Úteis

- [Swagger](http://localhost:8080/swagger-ui/index.html)
- [H2 Console](http://localhost:8080/h2-console)
