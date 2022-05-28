# N2TSI

Projeto contendo:

Telas de Login no Firebase utilizando Firebase Auth:
- **MainActivity.java** para login principal;
- **RecuperaSenha.java** para envio de e-mail de recuperação de senha;
- **CriaUsuario.java** para criação de novo usuário;
- classe model **Usuario.java**

Após logado, o usuário tem acesso à uma tela com a listagem de filmes de uma API
- API utilizada: OMDB API (https://www.omdbapi.com/)
- Tela de listagem: **UsuarioLogado.java**
- Para a listagem foi utilizado um recyclerView configurado com **RecyclerAdapater.java**
- classe model **Filme.java**
- para a **renderização das imagens** a partir de uma URL foi utilizada a biblioteca **GLIDE** (https://github.com/bumptech/glide, necessário adicionar nas dependências)
- classe **DownloadDados.java** faz o download dos dados da API de forma assíncrona e faz o parse JSON para o Arraylist.
- ao clicar no item da lista: salvar o item no Firebase Realtime Database
- textView de link para a tela Gerenciar Favoritos (UsuarioFavoritos.java)

**UsuarioFavoritos.java**:
- tela similiar à tela de listagem acima, contudo dados vêm do Firebase, e não de uma API;

