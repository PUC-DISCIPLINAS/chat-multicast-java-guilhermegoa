# chat-multicast-java-guilhermegoa
***
### Aluno
__Guilherme Oliveira Antonio__

###Professor
__Hugo Bastos De Paula__
***
## Descriçao
Este trabalho tem por objetivo praticar programação em redes utilizando sockets.

Deverão desenvolver uma aplicação de Chat em Java utilizando o protocolo Multicast.

Os requisitos básicos são:

1 - O servidor deve gerenciar múltiplas salas de bate papo.

2 - O cliente deve ser capaz de solicitar a lista de salas.

3 - O cliente deve ser capaz de solicitar acesso à uma das salas de bate papo.

3 - O servidor deve manter uma lista dos membros da sala.

4 - O cliente deve ser capaz de enviar mensagens para a sala.

5 - O cliente deve ser capaz de sair da sala de bate papo.
***
### Compilar
- Compilar o projeto use os comandos:
> javac Server.java
>
> javac Client.java

- Iniciar o server e o cliente
> java Server
> 
> java Client

### Comandos (e necessario digitar um numero seguido enter)
-  1 - Criar sala
  - Digitar o endereço da salar acima de 224 Ex.: 225.8.9.1

- 2 - Listar salas

- 3 - Entrar em uma sala
  - Ao entrar na sala, sao possiveis a seguintes açoes
    - Enviar mensagem: para enviar, mensagem basta digitar a mensagem
    - Listar membros: para listar, basta digitar "listar"
    - Sair: para sair: basta digitar "sair" 




