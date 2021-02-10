# BusKeyFX
Um buscador que utiliza indice invertido e árvore AVL para armazenar arquivos que contenham uma determinada palavra.


# User Stories

| User Story nº | Título | Breve Descrição | Status |
| :-----------: | ------ | --------------- | ------ |
| 01 | Buscar Páginas | A partir de palavra-chave inserida pelo usuário, o sistema deve apresentar todas as páginas que possuem tal palavra em seu conteúdo. Por padrão, as páginas devem ser apresentadas da mais relevante para a menos relevante*. | ✅ |
| 02 | Ordenar Resultados da Busca | É possível ordenar as páginas encontradas (por relevância crescente ou decresente). | ✅ |
| 03 | Top-K palavras mais buscadas | O sistema deve apresentar as K palavras mais buscadas pelos usuários. | ✅ |
| 04 | Top-K palavras menos buscadas | O sistema deve apresentar as K palavras menos buscadas. | ✅ | 
| 05 | Top-K Páginas mais visitadas | O sistema deve apresentar as K páginas mais visitadas pelos usuários. | ✅ |
| 06 | Top-K páginas menos visitadas | O sistema deve apresentar as K páginas menos visitadas pelos usuários. | ✅ |

  *A quantidade de ocorrencia de uma palavra foi o critério utlizado para definir a relevãncia da págia.
