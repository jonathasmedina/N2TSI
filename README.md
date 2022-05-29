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
- clique no item para salvar nos favoritos configurado na classe **RecyclerAdapter.java**, método **inserirEm()**;

- textView de link para a tela Gerenciar Favoritos (UsuarioFavoritos.java)

**UsuarioFavoritos.java**:
- tela similiar à tela de listagem acima, contudo dados vêm do Firebase, e não de uma API;
- clique no item para exclusão configurado na classe **RecyclerAdapter.java**, método **removerEm()**;

- Conceitos Utilizados: Firebase Realtime Database, Firebase Authentication, API/JSON (OMDBAPI), RecyclerView, Glide, classes AsyncTask, interação e validação de elementos de formulário;
