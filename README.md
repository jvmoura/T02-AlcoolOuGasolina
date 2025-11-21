# Álcool ou Gasolina Estendido
## Descrição
Um aplicativo Android desenvolvido com Kotlin e Jetpack Compose que permite motoristas decidirem entre abastecer com Álcool ou Gasolina e salvar seus postos de combustível favoritos.

## ✨ Funcionalidades

### Calculadora
* Comparação de preços entre Álcool e Gasolina.
* **Configuração Flexível**: Alternância entre taxa de rendimento de **70%** ou **75%**.
* Persistência da preferência do usuário.

### Gerenciamento de Postos
* **Criar**: Adicione novos postos com nome, preços e data automática.
* **Ler**: Visualize a lista completa de postos salvos.
* **Atualizar**: Edite informações de postos já cadastrados.
* **Deletar**: Remova postos da lista com diálogo de confirmação.
* **Persistência**: Dados salvos localmente utilizando `SharedPreferences` e `JSON`.

### Localização e Mapas
* **GPS Automático**: Captura a latitude e longitude atual ao cadastrar um posto (requer permissão).
* **Integração com Mapas**: Botão dedicado para abrir o Google Maps na localização do posto.

### Internacionalização (i18n)
* Suporte nativo para **Português (pt-BR)** e **Inglês (en-US)**.
* O idioma muda automaticamente conforme a configuração do dispositivo.

## Prints
- Listagem de Postos
<img width="308" height="688" alt="Captura de tela 2025-11-20 224240" src="https://github.com/user-attachments/assets/f93ebb6b-b5e9-463c-ba43-3600d14e0332" />

- Adicionar/Editar posto
<img width="308" height="688" alt="Captura de tela 2025-11-20 225842" src="https://github.com/user-attachments/assets/b6a7792c-e6fa-4cb2-9d8c-19450ad89608" />

- Detalhes do posto
<img width="306" height="643" alt="Captura de tela 2025-11-20 224304" src="https://github.com/user-attachments/assets/d63fade6-5bff-4c4e-8240-014050099794" />



