HTTP Client em Java: O Automatizador de Relatórios da Citadela
Fazer pedidos um a um no Bruno para saber quem está vivo ou morto no universo de Rick & Morty é trabalho de estagiário!
Vamos usar o Java para analisar os primeiros 20 cidadãos do universo automaticamente.
Modifiquem o código base em grupo para cumprir estes 3 objetivos:

O Varredor de Portais (O Loop)
O vosso programa deve analisar automaticamente os primeiros 20 cidadãos do universo. Criem um ciclo que faça o código Java disparar 20 pedidos HTTP seguidos, alterando o ID no fim do URL de forma dinâmica (do ID 1 ao 20).

O Censo Demográfico (Lógica de Contagem)
Queremos estatísticas reais. O programa deve analisar o texto de cada resposta (JSON) e contar quantos cidadãos estão vivos e quantos estão mortos.
No final do programa (fora do loop), imprimam o relatório final na consola:
=> CENSO: Detetados X personagens VIVOS e Y personagens MORTOS nos primeiros 20 registos.

Alerta de Segurança: Ameaça Alienígena
A Citadela precisa de monitorizar riscos biológicos. Se o vosso programa detetar um cidadão que seja da espécie Alien e que esteja Morto, deve imprimir um alerta imediato na consola:
[PERIGO] Um Alien foi encontrado morto com o ID X!

Se o vosso programa detetar um alien morto, deve iniciar uma investigação após o alerta para descobrir onde ele foi visto pela última vez.
Isolem o URL do episódio e façam o Java disparar um segundo pedido HTTP para o URL do episódio que acabaram de descobrir.
Extraiam o nome desse episódio e mostrem o veredicto no ecrã com este formato:
[ALERTA FORENSE] O último registo do alien morto foi no episódio: '...'.

A - Servlet do Censo

Criar uma servlet que disponibiliza o relatório através do caminho "/census" (contagem de vivos/mortos, alerta de alien morto e análise forense do episódio relativo ao perigo biológico).

Devolver o relatório em HTML em vez de escrever na consola.

Permitir que o pedido ao servlet defina o intervalo de personagens que devem ser analisados. (?offset=x&limit=y, x=1 e y=20 por defeito)

Permitir que o pedido defina se o alerta de ameaça biológica alienígena deve ou não ser gerado. (?showAlerts=true/false, true por defeito)

B - Validação de parâmetros e erros HTTP

Nem todos os pedidos do utilizador vão chegar corretos.
Regras de Validação:
O parâmetro limit não pode ser negativo nem superior a 50.
Os parâmetros offset e limit não podem ser texto.
O parâmetro showAlerts só pode ser true ou false.
Ação: Em caso de erro de validação, o Servlet deve:
Definir o código de estado HTTP para 400 Bad Request.
Retornar um JSON de erro estruturado, por exemplo:
{
"status": 400,
"error": "Bad Request",
"message": "O parâmetro 'limit' deve ser um número inteiro entre 1 e 50."
}

C - Desafios Extra
Criar um ficheiro HTML com um formulário para receber os parâmetros offset, limit e showAlerts.
Criar uma pasta no cliente HTTP Bruno para guardar todos os testes de validação.

Exercício do Log Criar um log para o nosso automatizador de censos. Cada vez que o Servlet /census for executado com sucesso, o programa deve adicionar (append) uma nova linha no ficheiro de log local (citadela_audit.log). Para que o programa escreva uma linha nova e não reescreva todo o ficheiro podem usar o método Files.writeString, mas têm que lhe passar uma opção específica, tentem descobrir qual é. Desafio extra Cada nova linha de log deve começar com com um "time stamp": [AAAA-MM-DDTHH:MM:SS.SSS]

CRUD

Criar uma classe chamada Personagem. Esta classe deve ter os seguintes campos (com getters, setters e construtores):
private String nome;
private String especie;
private String comidaFavorita;

Criar um endpoint ("/personagem") que permita fazer operações CRUD:
Usar o método POST para criar uma nova personagem.
Usar o método GET para ler todas as personagens.

CSR

Reestruturem o vosso código para respeitar o padrão de desenho Controller-Service-Repository.

- Controller só deve ser responsável por receber os pedidos HTTP e reencaminhar para o Service.
- Repository só deve ter código relacionado com a persistência de dados.
- Service deve estabelecer a comunicação entre os outros componentes e qualquer lógica de processamento que seja necessária.

Para o Service ter alguma lógica pela qual é responsável, façam com que qualquer criação de personagem que não inclua a "comidaFavorita" atribua automaticamente um valor a esse campo (uma comida ao vosso critério).
