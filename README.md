
<div align="center">
<a href="#"><img src="https://readme-typing-svg.demolab.com?font=Fira+Code&size=33&pause=1000&color=8E84FF&background=26FFDE0F&center=true&vCenter=true&width=435&lines=ClickComercial!" alt="Typing SVG" /></a>
</div>

<div align="center">
  <h2>Informações técnicas | Requisitos | Execução</h2>
</div>

### Tecnolgias e suas Versões
- **Spring Boot**: Versão `3.4.1`
- **Java**: Versão `21`
- **Gradle**: Kotlin DSL-based build script (`build.gradle.kts`)

### Dependências
As seguintes dependências estão configuradas no projeto:
- **Spring Boot Starter Web**: desenvolvimento de API REST;
- **Spring Boot Security**: autenticação e autorização;
- **Oauth2 Resource Server**: simplifica os processos de configuração de autenticação e autorização;
- **Spring Boot Starter Data JPA**: acesso simplificado ao banco de dados;
- **Spring Boot Starter Test**: testes unitários e de integração;
- **Spring Boot DevTools**: otimização do ambiente de desenvolvimento;
- **H2 Database**: banco de dados em memória para desenvolvimento local;
- **Spring Dotenv**: gerenciamento de variáveis ​​de ambiente (`me.paulschwarz:spring-dotenv:4.0.0`) e
- **Springdoc OpenAPI**: documentação da API e interface do usuário do Swagger (`springdoc-openapi-starter-webmvc-ui:2.7.0`).

## Execução

### Requisitos
Certifique-se de que os seguintes itens estejam instalados em sua máquina:
- Java `21` ou superior;
- Gradle (opcional; incluído com o wrapper)
- Git.

#### 1. Variáveis ​​de Ambiente <br>
O projeto utiliza spring-dotenv para gerenciar variáveis ​​de ambiente. Crie e configure um arquivo .env na raiz (General/.env) com o seguinte modelo:
- ACTUAL_PROF = dev
- DB_URL = <*URL do seu BD*>
- DB_USER = <*Usuário do seu BD*>
- DB_PASSWORD =<*Senha do seu BD*>
- ACCESS_T = <valor em segundos para a expiração do token de acesso>
- REFRESH_T = <valor em segundos para a expiração do token de atualização>
- **Observação:** *No momento, a configuração padrão utiliza um banco de dados H2 em memória. Você pode modificar o aplicativo para se conectar a um banco de dados diferente.*

#### 2. Compilar o Projeto
```bash
./gradlew build
```

#### 3. Executar o Aplicativo
```bash
./gradlew bootRun
```

#### 4. Acessar a API
- Por padrão, o aplicativo é executado em: http://localhost:8081

#### 5. Documentação da API
- Acesse a interface do usuário do Swagger para explorar a API:
http://localhost:8081/swagger-ui.html