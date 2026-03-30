# Alterações para Integração com Strapi

## Resumo
Corrigi todos os erros de compilação e implementei a integração completa com o Strapi para substituir os dados hardcoded.

## Ficheiros Alterados

### 1. **app/build.gradle.kts**
- Adicionada dependência do Kotlin Coroutines:
  ```kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
  ```

### 2. **app/src/main/java/com/example/epact/model/Empresa.kt**
- Adicionados campos opcionais ao `EmpresaAttributes`:
  - `category: String?`
  - `city: String?`
  - `tags: List<String>?`
- Todos os campos agora têm valores default para evitar crashes

### 3. **app/src/main/java/com/example/epact/data/ApiStrapi.kt**
- Corrigido nome da função: `getEmpresa()` → `getEmpresas()`
- Removidos imports desnecessários

### 4. **app/src/main/java/com/example/epact/ui/screens/companies/CompaniesScreen.kt**
- **REFATORAÇÃO COMPLETA:**
  - Removida dependência de `AppData.companies` (hardcoded)
  - Agora usa `viewModel.companies` (dados da API)
  - Adicionado estado de loading com `CircularProgressIndicator`
  - Criada função `CompanyCardFromApi()` que converte `EmpresaData` → `Company`
  - Corrigido filtro de categorias para lidar com valores nulos
  - Corrigida sintaxe dos `items()` do LazyColumn

### 5. **app/src/main/java/com/example/epact/ui/screens/companies/CompanyDetailScreen.kt**
- Mudada assinatura de `CompanyDetailScreen(company: Company)` para `CompanyDetailScreen(companyId: Int)`
- Agora busca os dados do `viewModel` em vez de receber como parâmetro
- Converte `EmpresaData` → `Company` internamente
- Mostra mensagem "Empresa não encontrada" se o ID não existir

### 6. **app/src/main/java/com/example/epact/ui/app/EpactApp.kt**
- Removido parâmetro `companies = AppData.companies` da `CompaniesScreen`
- Mudado `CompanyDetailScreen` para receber apenas `companyId`
- Navegação agora passa apenas o ID, não o objeto completo

## Como Funciona Agora

### Fluxo de Dados:
1. **App inicia** → `CompanyViewModel.init()` → `loadCompanies()`
2. **loadCompanies()** faz chamada à API Strapi via Retrofit
3. Dados são guardados em `viewModel.companies` (estado reativo)
4. **CompaniesScreen** observa `viewModel.companies` e renderiza a lista
5. Ao clicar numa empresa → navega com o `companyId`
6. **CompanyDetailScreen** recebe o ID e busca a empresa no mesmo `viewModel`

### Mapeamento de Dados:
```kotlin
EmpresaData (API) → Company (UI)
  - empresaData.attributes.nome → company.name
  - empresaData.attributes.descricao → company.shortDescription
  - empresaData.attributes.category → company.category
  - empresaData.attributes.url → company.website
  - empresaData.attributes.tags → company.tags
```

## O Que Falta (Próximos Passos)

### No Strapi:
1. Adicionar campos `category`, `city`, `tags` ao Content Type "empresa"
2. Popular os dados das empresas
3. Garantir que o endpoint `http://10.1.1.39:1337/api/empresa` está a funcionar

### No Android (opcional):
1. **Imagens do Strapi:**
   - Usar Coil para carregar `logo.data.attributes.url`
   - Carregar galeria de `Galeria.data[].attributes.url`

2. **Tratamento de Erros:**
   - Mostrar mensagem quando a API falhar
   - Adicionar botão de retry

3. **Cache Local:**
   - Usar Room Database para guardar empresas offline
   - Sincronizar com API quando houver conexão

## Como Testar

1. **Certificar que o Strapi está a correr:**
   ```bash
   # No servidor Strapi
   npm run develop
   ```

2. **Verificar endpoint:**
   ```bash
   curl http://10.1.1.39:1337/api/empresa
   ```

3. **Buildar a app:**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Instalar no dispositivo:**
   ```bash
   ./gradlew installDebug
   ```

## Exemplo de Resposta Esperada do Strapi

```json
{
  "data": [
    {
      "id": 1,
      "attributes": {
        "nome": "TE Connectivity",
        "descricao": "Empresa global de conectividade",
        "url": "https://www.te.com",
        "ativo": true,
        "category": "Tecnologia",
        "city": "Évora",
        "tags": ["Conectividade", "Tecnologia"],
        "logo": {
          "data": {
            "id": 1,
            "attributes": {
              "url": "/uploads/te_logo.png",
              "name": "te_logo.png",
              "mime": "image/png"
            }
          }
        }
      }
    }
  ]
}
```

## Notas Importantes

- Os dados hardcoded em `AppData.companies` **ainda existem mas não são usados**
- Podes apagá-los depois de confirmar que a API está a funcionar
- O `RetrofitClient` está configurado para `http://10.1.1.39:1337/`
- Se mudares de IP/servidor, edita `app/src/main/java/com/example/epact/data/RetrofitClient.kt`
