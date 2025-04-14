

# API de Transferências Bancárias

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-EA6E1F?style=for-the-badge&logo=lombok&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-4A4A55?style=for-the-badge&logo=apachemaven&logoColor=white)
![H2](https://img.shields.io/badge/H2-4479A1?style=for-the-badge&logo=h2&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-45C6B0?style=for-the-badge&logo=mockito&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white)


> Status do Projeto: :heavy_check_mark: Concluído


### Tópicos 

:small_blue_diamond: [Descrição do Projeto](#descrição-do-projeto)

:small_blue_diamond: [Tecnologias Utilizadas](#tecnologias-utilizadas)

:small_blue_diamond: [Estrutura do Projeto](#estrutura-do-projeto)

:small_blue_diamond: [Endpoints](#Endpoints)

:small_blue_diamond: [Pré-requisitos](#pré-requisitos)

:small_blue_diamond: [Como Executar](#como-executar)

:small_blue_diamond: [Testes](#testes)

:small_blue_diamond: [Controle de Concorrência](#controle-de-concorrência)

:small_blue_diamond: [Tratamento de Erros](#tratamento-de-erros)

:small_blue_diamond: [Desenvolvedores](#desenvolvedores)

:small_blue_diamond: [Licença](#licença)


## Descrição do Projeto

A *API de Transferências Bancárias* é uma aplicação desenvolvida com Java 17 e Spring Boot. O objetivo é simular operações bancárias como cadastro de clientes, consulta de contas e realização de transferências entre contas.

## Tecnologias Utilizadas

- **Java 17+** (Linguagem principal)
- **Spring Boot** (Framework para desenvolvimento de aplicações REST)
- **Spring Data JPA** (Framework de abstração da camada de persistência)
- **Lombok** (Biblioteca para reduzir boilerplate com anotações)
- **Maven** (Gerenciamento de dependências e build)
- **H2 Database** (Banco de Dados *in memory*)
- **JUnit** (Framework para testes)
- **Mockito** (Simulação de comportamentos em testes)
- **Swagger** (Utilizado para gerar e disponibilizar a documentação interativa da API)

## Estrutura do Projeto

A estrutura principal do projeto é a seguinte:

```bash
📦 src
├── 📂 main
│   ├── 📂 java
│   │   └── 📂 com
│   │       └── 📂 itau
│   │           └── 📂 api_transferencia_bancaria
│   │               ├── 📂 controller
│   │               ├── 📂 dto
│   │               ├── 📂 exception
│   │               ├── 📂 model
│   │               ├── 📂 repository
│   │               ├── 📂 service
│   │               └── 📄 ApiTransferenciaBancariaApplication.java
│   └── 📂 resources
│       └── 📄 application.properties
├── 📂 test
│   ├── 📂 java
│   │   └── 📂 com
│   │       └── 📂 itau
│   │           └── 📂 api_transferencia_bancaria
│   │               ├── 📂 integration
│   │               │   └── 📂 controller
│   │               ├── 📂 unit
│   │               │   └── 📂 service
│   │               └── 📄 ApiTransferenciaBancariaApplicationTests.java
│   └── 📂 resources
│       └── 📄 application-test.properties

```                                                                                                                                                         

## Endpoints

:heavy_check_mark: *Cadastro de Cliente:* Realiza o cadastro captando as informações de Nome, Número da Conta (único) e Saldo em Conta, posteriormente gerando um ID único.

:heavy_check_mark: *Listar Todos os Clientes Cadastrados:* Retorna uma lista de todos os clientes cadastrados, com informações de ID único, Nome e Número da Conta (único).

:heavy_check_mark:  *Buscar Cliente pelo Número da Conta:* Retorna informações da conta filtrada, como Nome, Número da Conta (único) e Saldo em Conta. 

:heavy_check_mark:  *Transferência entre duas contas:* Realiza a transferência de saldo em conta, desde que: a conta origem possua saldo suficiente para a transação; o valor a ser transferido seja de até 100,00 reais.

:heavy_check_mark:  *Buscar transferências relacionadas à uma conta:* Retorna um histórico de todas as transações de transferências em ordem decrescente, captando também as que não tiveram sucesso.

## Pré-requisitos

- *Java 17 ou superior:* [Download do JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- *Maven 3.6.0 ou superior:* [Download do Maven](https://maven.apache.org/download.cgi)

## Como Executar

1. *Clonar o Repositório:*

   ```bash
   git clone https://github.com/nicolleBor/api-transferencia-bancaria.git
   cd api-transferencia-bancaria
   ```
2. *Compilar o projeto:*

   ```bash
    mvn clean install
   ```

3. *Executar a Aplicação:*

   ```bash
   mvn spring-boot:run
   ```

4. *Acesse a Documentação da API:*

   - [Swagger UI](http://localhost:8081/swagger-ui/index.html#)

## Testes

O projeto possui testes unitários na *Service* e testes de integração na *Controller* utilizando JUnit e Mockito. Para executar os testes, rode:

```bash
mvn test
```

## Controle de Concorrência

O controle de concorrência é feito com @Version no modelo Cliente, garantindo que duas transferências simultâneas não causem conflitos de saldo. Em caso de concorrência, uma exceção personalizada é lançada para tratamento adequado.

## Tratamento de Erros

O projeto inclui tratamento centralizado de exceções via @ControllerAdvice, retornando mensagens claras e status HTTP apropriados para:
- Dados Inválidos
- Recursos Não Encontrados
- Erros de Concorrência
- Exceções Genéricas

## Desenvolvedores

| [<img src="https://avatars.githubusercontent.com/u/155683365?s=400&u=18f0d539c431028608d0b599db0753f81a95f282&v=4" width=115><br><sub>Nicolle Borges</sub>](https://github.com/nicolleBor) |  
| :---: 

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

Copyright :copyright: 2025 - api-transferencia-bancaria
