delivery
==========================
Tecnologias / Frameworks:
- Spring Core
- Spring JDBC
- Spring MVC
- Maven
- PostgreSQL

-----------------------------------------
Como executar o projeto:
 1. Utilizando o git, execute o comando "git clone https://github.com/rafazampieri/delivery.git"
 2. Acesse a pasta aonde está o pom.xml e execute mvn clean install, para que as bibliotecas sejam baixadas.
 3. O projeto foi criado utilizando como banco de dados o postgresql, acesse o caminho /delivery/documentation/database/postgresql/ e execute os scripts para inicializar a configuração do banco de dados. Obs: Os scripts estão numerados.
 4. Inicie o servidor de aplicação.

-----------------------------------------
Persistencia:
 O framework utilizado na camada de persistencia foi o Spring JDBC buscando melhor performance, evitando o uso de reflection como é feito com o Hibernate. A modelagem está dentro do projeto no caminho "/delivery/documentation/database/postgresql/modelagem.png".

-----------------------------------------
API REST:
 Foi exposto duas URI`s:
 1. /rest/delivery/addLocationToMap/{mapName}/{locationBegin}/{locationEnd}/{cost}
	Objetivo: Incluir novos caminhos no mapa.
	Parametros: 
		mapName: String: nome do mapa
		locationBegin: String: localizacao de partida
		locationEnd: String: localizacao de chegada
		cost: Integer: distancia entre os dois pontos
	Retorno: Boolean: true se o local foi cadastrado, false se ocorreu algum erro durante o cadastro
	Ex: http://localhost:8080/delivery/rest/delivery/addLocationToMap/SP/A/B/10

 2. /rest/delivery/calculateMinorPath/{mapName}/{locationBegin}/{locationEnd}/{fuelAutonomy}/{fuelCost}
	Objetivo: Consultar o menor caminho entre dois pontos
	Parametros: 
		mapName: String: nome do mapa
		locationBegin: String: localizacao de partida
		locationEnd: String: localizacao de chegada
		fuelAutonomy: Integer: autonomia de combustivel do caminhão por litro
		fuelCost: Integer: valor do custo da gasolina por litro
	Retorno: JSON: parametros:
					mapName: String: nome do mapa consultado
					distance: Integer: distancia total percorrida
					fuelAutonomy: Integer: autonomia de combustivel do caminhão por litro
					fuelCost: Integer: valor do custo do combustivel por litro
					listPaths: List<String>: caminhos percorridos da localizacao de inicio até o fim
					totalCost: Double: total do valor gasto com combustivel
	Ex: http://localhost:8080/delivery/rest/delivery/calculateMinorPath/SP/A/D/10/2.5
	Ex de Retorno : {"mapName":"SP","distance":15,"fuelAutonomy":10,"fuelCost":2.5,"listPaths":["A","B","F"],"totalCost":3.75}

-----------------------------------------
Algoritmo - Calculo do menor caminho:
 Para realizar o calculo, primeiro foi planejado como fazer o armazenamento no banco de dados dos caminhos possíveis entre um ponto e outro. Para solucionar esse problema foi criado uma tabela chamada Maps aonde é armazenado o nome do mapa e o id, e uma segunda tabela aonde é armazenado o nome da localização e um json contendo as arestas possíveis que são os caminhos aonde a partir daquele ponto é possível se deslocar. 
 Ex: 
 tabela Maps, coluna id = 1, coluna name = SP
 tabelas MapLocation, coluna id = 1, coluna name = A, coluna json_edges = {"B":10,C:20}

Então quando consultas A ele retorna um caminho/distancia até B e se consultamos B ele retorna um caminho/distancia também até A, então dessa maneira foi solucionado em poucos registro no banco de dados o primeiro problema que é o mapeamento correto das informações dentro do banco de dados, vamos mostrar abaixo um exemplo de entrada de dados e como isso é armazenado no banco.
 Entrada de dados:
 http://localhost:8080/delivery/rest/delivery/addLocationToMap/SP/A/B/10
 http://localhost:8080/delivery/rest/delivery/addLocationToMap/SP/B/D/10
 http://localhost:8080/delivery/rest/delivery/addLocationToMap/SP/A/C/10
 http://localhost:8080/delivery/rest/delivery/addLocationToMap/SP/C/D/10
 http://localhost:8080/delivery/rest/delivery/addLocationToMap/SP/B/E/10
 http://localhost:8080/delivery/rest/delivery/addLocationToMap/SP/D/E/10

Com essas chamadas REST o banco de dados ficou da seguinte maneira:

Maps

 id=1,name=SP

MapLocation

 id=1,location=A,json_edges={B:10,C:20}
 id=2,location=B,json_edges={A:10,D:15}
 id=3,location=C,json_edges={A:20,D:30}
 id=4,location=D,json_edges={B:15,C:30,E:30}
 id=5,location=E,json_edges={B:50,D:30}

Podemos perceber que toda localização mantém um vinculo com as suas arestas em poucos dados armazenados no banco de dados, com isso é possível realizar consultas mais eficazes e rápidas.

A segunda parte do problema foi realizar o calculo, então partindo do parametros passados na chamada REST o calculo é feito da seguinte maneira:

 http://localhost:8080/delivery/rest/delivery/calculateMinorPath/SP/A/D/10/2.5

Primeiro é feita a consulta no Maps para descobrir se o Mapa existe, depois no locationBegin e locationEnd para descobrir se as localizações consultadas são válidas. Após essas validações é convertido o JSON do locationBegin e realizado um consulta no banco de dados realizando in() com todas as arestas possíveis, com o retorno é inicio o looping que só termina quando é encontrado o menor caminho. O looping consiste em verificar cada aresta que ainda não foi consultada e realizar novas consultas utilizando essas arestas e sempre adicionando o custo entre cada aresta. 

Obs: Como o requisito inicial consta que deve conter o menor caminho, o algoritmo percorre todas as localizações existentes, no mapa.
