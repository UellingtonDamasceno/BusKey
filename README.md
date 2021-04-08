# BusKeyFX
Um buscador que utiliza indice invertido e árvore AVL para armazenar arquivos que contenham uma determinada palavra.


|![Demonstração de pesquisa](https://github.com/UellingtonDamasceno/BusKeyFX/blob/master/resources/imagens/pesquisas.gif) | ![Demostração Top-K](https://github.com/UellingtonDamasceno/BusKeyFX/blob/master/resources/imagens/top-k.gif)|
|----|----|

# Motivação
O Google Search é um serviço da empresa Google através do qual é possível fazer 
pesquisas na internet sobre qualquer tipo de assunto ou conteúdo. Lançado pela
Google Inc em setembro de 1997, é atualmente o serviço de busca *online* mais 
utilizado em todo mundo. A popularidade do serviço é tão alta que é comum que
internautas utilizem a expressão "googlar" em conversas informais, um neologismo
que significa executar uma pesquisa na internet pelo motor de busca Google.

# User Stories

| User Story nº | Título | Breve Descrição | Status |
| :-----------: | ------ | --------------- | ------ |
| 01 | Buscar Páginas | A partir de palavra-chave inserida pelo usuário, o sistema deve apresentar todas as páginas que possuem tal palavra em seu conteúdo. Por padrão, as páginas devem ser apresentadas da mais relevante para a menos relevante*. | :white_check_mark: |
| 02 | Ordenar Resultados da Busca | É possível ordenar as páginas encontradas (por relevância crescente ou decresente). | :white_check_mark: |
| 03 | Top-K palavras mais buscadas | O sistema deve apresentar as K palavras mais buscadas pelos usuários. | :white_check_mark: |
| 04 | Top-K palavras menos buscadas | O sistema deve apresentar as K palavras menos buscadas. | :white_check_mark: | 
| 05 | Top-K Páginas mais visitadas | O sistema deve apresentar as K páginas mais visitadas pelos usuários. | :white_check_mark: |
| 06 | Top-K páginas menos visitadas | O sistema deve apresentar as K páginas menos visitadas pelos usuários. | :white_check_mark: |

  *A quantidade de ocorrencia de uma palavra foi o critério utlizado para definir a relevãncia da págia.

# Objetivo de aprendizagem

- Árvore AVL
- Busca binária
- Análise da complexidade de um algoritmo
- Indice invertido
- JavaFX

# Extras 

- Interface gráfica em utilizando JavaFX.
- Utilização do componente JavaFX TablePane para permitir multiplas buscas simutaneamente. 
- Pesquisas com multiplas palavras.
- Paginação de resultados de pesquisa.
- Permitir a criação de novas páginas.
- Permitir que o usuário altere o conteúdo de uma página que foi resultado de alguma pesquisa.

## Curiosidades

1. Primeiro produto com interface gráfica.
2. O produto completo era formado por um relatório e o código fonte. Mas o relatório não foi feito por que a interface gráfica valia 2 pontos extras e escrever código é mais divertido do que relatórios :)
3. Não era permitido o uso de construtores de telas.
4. O código funciona melhor no windows.
5. Houve um momento durante o desenvolvimento do _software_ que dava _stackoverflow_ e o problema foi resolvido utilizando o padrão de projeto `Facade`. 
6. O nome original do problema é "Feira Gugou".

----------

| :arrow_left: [Problema anterior](https://github.com/UellingtonDamasceno/BlackJack) |............................... :arrow_up: [Voltar ao topo](#BusKeyFX) :arrow_up: ...............................| [Próximo problema](https://github.com/UellingtonDamasceno/MyBook) :arrow_right: | 
| :----: |-----| :-----:| 
