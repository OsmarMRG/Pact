# Guia de Personalização - App EPACT

## 🎯 Como adicionar dados reais

### 1. Empresas Reais
**Ficheiro:** `app/src/main/java/com/example/epact/MainActivity.kt`
**Linhas:** 45-92

```kotlin
private val companies = listOf(
    Company(
        id = 1,
        name = "Nome da Empresa Real",
        category = "Categoria",
        city = "Cidade",
        shortDescription = "Descrição curta",
        fullDescription = "Descrição completa",
        tags = listOf("Tag1", "Tag2", "Tag3")
    ),
    // Adiciona mais empresas aqui
)
```

### 2. Métricas Atualizadas
**Linhas:** 34-39

```kotlin
private val metrics = listOf(
    Metric("Empresas", "55", "business"),  // Atualiza o número
    Metric("Postos de trabalho", "540", "groups"),
    Metric("Edifícios", "6", "apartment"),
    Metric("Projetos liderados", "5", "trend")
)
```

### 3. Adicionar Imagens

#### Opção A: Imagens locais
1. Coloca as imagens em `app/src/main/res/drawable/`
2. Usa: `painterResource(id = R.drawable.nome_imagem)`

#### Opção B: Imagens da internet
1. Adiciona dependência Coil no `app/build.gradle.kts`:
```kotlin
implementation("io.coil-kt:coil-compose:2.5.0")
```
2. Usa: `AsyncImage(model = "https://url-da-imagem.jpg", ...)`

### 4. Adicionar Vídeos

#### Vídeos locais:
1. Coloca vídeos em `app/src/main/res/raw/`
2. Usa `VideoView` ou `ExoPlayer`

#### Vídeos do YouTube:
1. Adiciona dependência:
```kotlin
implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
```
2. Usa o ID do vídeo do YouTube

### 5. Múltiplas Localizações no Mapa

**Função:** `MapScreen()`

```kotlin
// Adiciona mais marcadores
val locations = listOf(
    GeoPoint(38.5488092, -7.9111854) to "PACT",
    GeoPoint(38.5500000, -7.9100000) to "Empresa X"
)

locations.forEach { (point, title) ->
    val marker = Marker(mapView)
    marker.position = point
    marker.title = title
    mapView.overlays.add(marker)
}
```

## 🎨 Personalizar Cores

**Ficheiro:** `MainActivity.kt` (linhas 95-101)

```kotlin
private val pactGreen = Color(0xFF0A5C4A)        // Verde principal
private val pactGreenSoft = Color(0xFFE9F5F1)    // Verde suave
private val pactGreenDark = Color(0xFF063E32)    // Verde escuro
private val pactAccent = Color(0xFF23A26D)       // Verde destaque
```

Muda os códigos hexadecimais para as cores oficiais do PACT.

## 📊 Adicionar Base de Dados (Supabase)

Se quiseres gerir empresas dinamicamente:

1. Cria tabela no Supabase
2. Adiciona dependência:
```kotlin
implementation("io.github.jan-tennert.supabase:postgrest-kt:2.0.0")
implementation("io.ktor:ktor-client-android:2.3.0")
```
3. Faz fetch das empresas da API

## 🚀 Melhorias Futuras

- [ ] Adicionar autenticação (se necessário)
- [ ] Sistema de favoritos
- [ ] Partilhar empresas nas redes sociais
- [ ] Notificações de eventos
- [ ] Modo escuro
- [ ] Multi-idioma (PT/EN)
- [ ] Filtros avançados
- [ ] Integração com calendário de eventos

## 📱 Testar no Dispositivo Real

1. Ativa "Opções de programador" no telemóvel Android
2. Ativa "Depuração USB"
3. Liga o cabo USB ao computador
4. Seleciona o dispositivo no Android Studio
5. Clica em "Run"

## 🎓 Dicas para a Apresentação

1. **Mostra o fluxo completo:**
   - Início → métricas do ecossistema
   - Ecossistema → soluções oferecidas
   - Empresas → pesquisa e filtros funcionais
   - Detalhe de empresa → informação completa
   - Mapa → localização real do PACT
   - Media → estrutura para conteúdo futuro

2. **Destaca os pontos fortes:**
   - Interface profissional e moderna
   - Navegação intuitiva
   - Funcionalidade de pesquisa
   - Mapa interativo real
   - Pronto para expansão com dados reais

3. **Explica o próximo passo:**
   - Substituir empresas exemplo por dados reais
   - Adicionar logótipos das empresas
   - Integrar vídeos de drone
   - Adicionar galeria de fotos do espaço
   - Conexão com base de dados para gestão dinâmica

## 💡 Contacto e Suporte

Se precisares de ajuda adicional para implementar alguma funcionalidade, basta perguntar!

---

**A aplicação está completamente funcional e pronta para demonstração!** 🎉
