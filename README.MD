<h1>Gestão de Biblioteca - API</h1>
<h2>Pré-requisitos</h2>
<p>Para rodar a aplicação, certifique-se de ter os seguintes softwares instalados:</p>
<ul>
    <li><strong>Java 21+</strong></li>
    <li><strong>Maven</strong> - Gerenciamento de dependências</li>
    <li><strong>Node.js e npm</strong> - Para a parte frontend em React</li>
    <li><strong>PostgreSQL</strong> (caso não opte pelo Docker)</li>
    <li><strong>Docker e Docker Compose</strong> (para rodar com Docker)</li>
</ul>

<h2>Configuração do Banco de Dados</h2>
<p>Você pode rodar a aplicação utilizando Docker ou configurando o PostgreSQL localmente. Escolha uma das opções abaixo:</p>

<h3>Rodando com Docker</h3>
<ol>
    <li>Se ainda não tiver, <a href="https://docs.docker.com/get-docker/">instale o Docker</a> e o <a href="https://docs.docker.com/compose/install/">Docker Compose</a>.</li>
    <li>Na raiz do projeto, rode o comando:</li>
    <pre><code>docker-compose up --build</code></pre>
    <li>Isso iniciará a aplicação, o banco de dados PostgreSQL, e a aplicação frontend em React. A aplicação está disponível em:</li>
    <pre><code>http://localhost/</code></pre>
</ol>

<h3>Rodando sem Docker (configuração manual do PostgreSQL e React)</h3>
<p>Siga as instruções abaixo para configurar o PostgreSQL e rodar a aplicação manualmente:</p>
<ol>
    <li>Instale o PostgreSQL. Você pode baixar <a href="https://www.postgresql.org/download/">aqui</a>.</li>
    <li>Crie um banco de dados chamado <code>gestaobiblioteca</code>:</li>
    <pre><code>CREATE DATABASE gestaobiblioteca;</code></pre>
    <li>Atualize as configurações de conexão com o banco de dados no arquivo <code>application.properties</code>:</li>
    <pre><code>
spring.datasource.url=jdbc:postgresql://localhost:5432/gestaobiblioteca
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    </code></pre>
    <li>Instale as dependências do Maven:</li>
    <pre><code>mvn clean install</code></pre>
    <li>Entre na pasta do frontend:</li>
    <pre><code>cd src/main/frontend</code></pre>
    <li>Instale as dependências do React:</li>
    <pre><code>npm install</code></pre>
    <li>Volte à raiz do projeto e inicie a aplicação backend:</li>
    <pre><code>mvn spring-boot:run</code></pre>
    <li>Acesse a aplicação em <code>http://localhost:8080</code>.</li>
</ol>

<h2>Swagger - Documentação da API</h2>
<p>A documentação da API está disponível no Swagger. Acesse a interface do Swagger na URL:</p>
<p><a href="http://localhost:8080/swagger-ui.html" target="_blank">http://localhost:8080/swagger-ui.html</a></p>

<h2>Dependências</h2>
<p>Abaixo estão listadas as principais dependências utilizadas no projeto:</p>
<ul>
    <li><strong>Spring Boot</strong> - Framework Java</li>
    <li><strong>PostgreSQL</strong> - Banco de dados </li>
    <li><strong>Spring Data JPA</strong> - Abstração de persistência de dados</li>
    <li><strong>Spring WebFlux</strong> - Programação reativa</li>
    <li><strong>Lombok</strong> - Redução de boilerplate no código Java</li>
    <li><strong>Jacoco</strong> - Ferramenta de cobertura de testes</li>
    <li><strong>SpringDoc OpenAPI</strong> - Geração automática de documentação Swagger</li>
    <li><strong>React</strong> - Biblioteca JavaScript para frontend</li>
</ul>

<h2>Instruções para Testes</h2>
<ol>
    <li>Os testes estão configurados com <strong>JUnit</strong> e cobertos com <strong>Jacoco</strong>. Para rodar os testes:</li>
    <pre><code>mvn test</code></pre>
    <li>Os relatórios de cobertura gerados pelo Jacoco estarão disponíveis no diretório <code>target/site/jacoco</code>.</li>
    <li>Possui um arquivo index.html para o detalhamento</li>
</ol>
