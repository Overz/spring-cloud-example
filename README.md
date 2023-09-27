# spring-cloud-example

Exemplo simples de utilização do spring-cloud config-server

---

## Step 1 - Serviço Consumer

Adicionar a dependência ao serviço que resgatara a configuração de um possível serviço centralizado:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId> <!-- STARTER -->
</dependency>
```

Separar os serviços por `NOME` & realizar o import:

```yaml
spring:
  application:
    name: "nome-da-aplicacao"
  config:
    import:
      - configserver:https://<provider>/<context-path>/
  cloud:
    config:
      profile: "profile-2" # Profile encontrado no serviço de configuração que reucperou do repositório
      label: "master" # possível branch onde esta a configuração, não obrigatório
```

---

## Step 2 - Serviço Provider

Adicionar a depenência ao serviço que ira prover as configurações:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>  <!-- CONFIGURATION -->
</dependency>
```

Realizar o import do serviço que puxa as configurações:

```yaml
spring:
  application:
    name: "configuration-service"
  cloud:
    config:
      server:
        git:
          uri: "https://github.com/Overz/spring-cloud-example" # URI do repositório
          default-label: "master" # Branch principal
          search-paths:
            # Caminhos onde o spring-config-server deve procurar os profiles.
            # No caso, o repositório possui uma pasta `properties` com sub-diretorios.
            # Cada sub-diretório é o NOME de cada projeto, utilizado para identificação, boa pratica mas não obrigatório.
            - "properties/{application}/*"
```

- `label`: branch - OPCIONAL caso esteja na branch principal
- `name`: aplicação
- `profile`: profile do arquivo

```http request
GET http://localhost:4000/<context-path>/<name>/<profile>/<branch>
```

Exemplo de Resposta:

```json
{
  "name": "service-one",
  "profiles": [
    "profile-1"
  ],
  "label": null,
  "version": "6fd1b8a051d1ae712805a6bd218ad45e2e168499",
  "state": null,
  "propertySources": [
    {
      "name": "https://github.com/Overz/spring-cloud-example/properties/service-one/config/application-profile-1.yaml",
      "source": {
        "spring.application.name": "application-service",
        "management.endpoints.web.exposure.include[0]": "*",
        "properties-example.greeting": "Hello ",
        "properties-example.default-value": "Profile 1"
      }
    },
    {
      "name": "https://github.com/Overz/spring-cloud-example/properties/service-one/config/application.yaml",
      "source": {
        "spring.application.name": "default-configuration",
        "properties-example.greeting": "Hello ",
        "properties-example.default-value": "Default Profile"
      }
    }
  ]
}
```

---

## Step 3 - Repositório

Crie o repositório da forma que preferir mas tenha em mente que a propriedade `search-paths` do serviço `provider` deve
ser alterada para que consiga encontrar todos os arquivos necessários.

Neste caso, segue o padrão:

```text
properties/
  ├── service-one/
  │   └── config/
  │       ├── application-profile-1.yml
  │       ├── application-profile-2.yml
  │       └── application.yml
  ├── service-two/ # caso tenha a aplicação possua mais serviços, sera identificado aqui pelo nome identico
  │   └── config/
  │       ├── application-profile-1.yml
  │       ├── application-profile-2.yml
  │       └── application.yml

```