package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

// Note que esta classe e' um exemplo - nao pretende ser o inicio do projeto, 
// embora tambem possa ser usada para isso.
//
// No seu projeto e' suposto haver metodos diferentes.
// 
// As coisas que comuns com o projeto, e que se pretendem ilustrar aqui, sao:
// - GameEngine implementa Observer - para  ter o metodo update(...)  
// - Configurar a janela do interface grafico (GUI):
//        + definir as dimensoes
//        + registar o objeto GameEngine ativo como observador da GUI
//        + lancar a GUI
// - O metodo update(...) e' invocado automaticamente sempre que se carrega numa tecla
//
// Tudo o mais podera' ser diferente!

public class GameEngine implements Observer {

	// Dimensoes da grelha de jogo
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;

	private static GameEngine INSTANCE; 		// Referencia para o unico objeto GameEngine (singleton)
	private ImageMatrixGUI gui; 				// Referencia para ImageMatrixGUI (janela de interface com o utilizador)
	private Empilhadora bobcat; 				// Referencia para a empilhadora
	private List<GameElement> gameElements;
	private int numLevel; 						// Numero do nivel onde o jogador esta
	private String player; 						// Nome do jogador
	private int numAlvos;
	private int numCaixas;
	private int maxLevel; 						// Numero de niveis que existem
	private Scoreboard scoreboard; 				// Referencia para iniciar o Scoreboard

	// Construtor - inicializa a lista de GameElements, o numLevel, o maxlevel e a scoreboard
	private GameEngine() {
		gameElements = new ArrayList<>();
		numLevel = 0;
		maxLevel = new File("levels").list().length;
		scoreboard = new Scoreboard(maxLevel);
	}

	// Implementacao do singleton para o GameEngine
	public static GameEngine getInstance() {
		if (INSTANCE == null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	// Inicio
	public void start() {

		Scanner scanner = new Scanner(System.in);
		System.out.println("To Start the Game insert your name ");
		System.out.println("DONT PUT ':' IN YOUR NAME, please");
		System.out.print("-> Name: ");
		player = scanner.nextLine();
		while(player.contains(":")) {
					System.out.println("I SAID NO ':'");
					System.out.print("-> Name: ");
					player = scanner.nextLine();
		}
		scanner.close();

		// Setup inicial da janela que faz a interface com o utilizador

		gui = ImageMatrixGUI.getInstance(); 		// 1. obter instancia ativa de ImageMatrixGUI
		gui.setSize(GRID_HEIGHT, GRID_WIDTH); 		// 2. configurar as dimensoes
		gui.registerObserver(this); 				// 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go(); 									// 4. lancar a GUI

		// Criar o cenario de jogo
		drawLevel(numLevel); 						// desenha o nivel com base no ficheiro

		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage("Sokoban  " + "   Player:  " + player + "   Level: " + numLevel + "   Moves: "
				+ bobcat.getMoves() + "   Fuel: " + bobcat.getFuel());

		gui.update();
	}

	// O metodo update() e invocado automaticamente sempre que o utilizador carrega numa tecla
	// no argumento do metodo e passada uma referencia para o objeto observado (neste caso a GUI)
	@Override
	public void update(Observed source) {

		int key = gui.keyPressed(); 				// obtem o codigo da tecla pressionada
		if (key == KeyEvent.VK_R || bobcat.getFuel() <1) { // reinicia o Nivel se a Tecla R for carregada
			redrawGui(numLevel);
			return;
		}
		if (numLevel == maxLevel) { 				// acaba o jogo se nao existir mais niveis
			endOfLevels();
			return;
		}

		if (Direction.isDirection(key) && !areTargetsOccupied()) { 	// verifica se a tecla pressionada e uma direcao
																	// e se os alvos estao ocupados

			Direction direcao = Direction.directionFor(key);

			bobcat.move(direcao, gameElements);
			remove(bobcat.toRemove());
			isOccupied(gameElements, g -> g instanceof Alvo, f -> !f.equals(bobcat) && !(f instanceof Palete));
			isOccupied(gameElements, g -> g instanceof Teleporte, h -> true);
			numCaixas = countCaixas();
		}
		
		if(numCaixas < numAlvos) {
			redrawGui(numLevel);
			return;
		}

		gui.setStatusMessage("Level:" + numLevel + "  Player: " + player + "  moves:" + bobcat.getMoves() + "  Fuel: "
				+ bobcat.getFuel());
		gui.update(); 			// redesenha a lista de ImageTiles na GUI,
								// tendo em conta as novas posicoes dos objetos
	}

	// remove os GameElements dados
	private void remove(List<GameElement> remove) {
		for (GameElement e : remove) {
			if(e instanceof Buraco || e instanceof Palete) {
				gameElements.remove(e);
			}else if(e instanceof Empilhadora){
				redrawGui(numLevel);
				return;
			}else{
				gameElements.remove(e);				
				gui.removeImage(e);
			}
		
		}
	}
	
	// metodo para contar o numero de caixas que existem
	private int countCaixas() {
		int total = 0;
		for (GameElement e : gameElements) {
			if(e instanceof Caixote)
				total++;
		}
		return total;
	}
	
	/*
	 * verifica se os alvos estao ocupados pelas caixa se estiverem todos ocupados,
	 * atualiza a scoreboard e muda de nivel, caso nao existam mais niveis acaba o
	 * jogo
	 */
	private boolean areTargetsOccupied() {
		for (GameElement e : gameElements) {
			if (e instanceof Alvo && !e.isOccupied()) {
				return false;
			}
		}
		scoreboard.UpdateStats(numLevel, player, bobcat.getMoves());
		if (numLevel < maxLevel - 1) {
			redrawGui(++numLevel);
		} else {
			endOfLevels();
			numLevel++;
		}
		return true;
	}

	/*
	 * verifica se existe algum objeto com a mesma posicao de um outro objeto obtido
	 * pelo primeiro filtro e se existir um objeto que tenha a mesma posicao e que
	 * nao seja igual ao objeto do primeiro filtro e se tambem verifica o segundo
	 * filtro mete o primeiro objeto como ocupado
	 */
	private void isOccupied(List<GameElement> list, Predicate<GameElement> pred, Predicate<GameElement> pred2) {
		for (GameElement e : gameElements)
			if (pred.test(e)) {
				for (GameElement g : list)
					if (e.getPosition().equals(g.getPosition()) && !pred.test(g) && pred2.test(g)) {
						e.setOccupied(true);
						break;
					} else {
						e.setOccupied(false);
					}
			}
	}

	// Criacao da planta do armazem,ou seja, o chao, os alvos, os vazios e as
	// paredes

	private void createWarehouse(String level, List<ImageTile> tileList) {
		File ficheiro = new File(level);
		try {
			Scanner scanner = new Scanner(ficheiro);
			for (int y = 0; y < GRID_HEIGHT; y++) {
				String linha = scanner.nextLine();
				for (int x = 0; x < GRID_HEIGHT; x++) {
					if (linha.charAt(x) == 'X') {
						GameElement alvo = new Alvo(new Point2D(x, y));
						tileList.add(alvo);
						gameElements.add(alvo);
						numAlvos++;
					} else if (linha.charAt(x) == '#') {
						GameElement parede = new Parede(new Point2D(x, y));
						tileList.add(parede);
						gameElements.add(parede);
					} else if (linha.charAt(x) == '=') {
						GameElement vazio = new Vazio(new Point2D(x, y));
						tileList.add(vazio);
						gameElements.add(vazio);
					} else if (linha.charAt(x) == 'O') {
						GameElement Buraco = new Buraco(new Point2D(x, y));
						tileList.add(Buraco);
						gameElements.add(Buraco);
					} else if (linha.charAt(x) == 'T') {
						GameElement teleport = new Teleporte(new Point2D(x, y));
						tileList.add(teleport);
						gameElements.add(teleport);
					} else {
						tileList.add(new Chao(new Point2D(x, y)));
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found " + ficheiro);
		}

	}

	// Criacao dos objetos com que podemos interagir
	private void createMoreStuff(String level, List<ImageTile> tileList) {

		File ficheiro = new File(level);
		try {
			Scanner scanner = new Scanner(ficheiro);
			for (int y = 0; y < GRID_HEIGHT; y++) {
				String linha = scanner.nextLine();
				for (int x = 0; x < GRID_HEIGHT; x++) {
					if (linha.charAt(x) == 'C') {
						GameElement box = new Caixote(new Point2D(x, y));
						tileList.add(box);
						gameElements.add(box);
						numCaixas++;
					} else if (linha.charAt(x) == 'E') {
						bobcat = new Empilhadora(new Point2D(x, y));
						tileList.add(bobcat);
						gameElements.add(bobcat);
					} else if (linha.charAt(x) == 'B') {
						GameElement bateria = new Bateria(new Point2D(x, y));
						tileList.add(bateria);
						gameElements.add(bateria);
					} else if (linha.charAt(x) == 'P') {
						GameElement palete = new Palete(new Point2D(x, y));
						tileList.add(palete);
						gameElements.add(palete);
					} else if (linha.charAt(x) == 'M') {
						GameElement martelo = new Martelo(new Point2D(x, y));
						tileList.add(martelo);
						gameElements.add(martelo);
					} else if (linha.charAt(x) == '%') {
						GameElement paredeRachada = new ParedeRachada(new Point2D(x, y));
						tileList.add(paredeRachada);
						gameElements.add(paredeRachada);
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found " + ficheiro);
		}
	}

	// Acaba o jogo
	private void endOfLevels() {
		gui.clearImages();
		gameElements.clear();
		gui.setStatusMessage("CONGRATULATION YOU HAVE PASSED ALL LEVELS");
		gui.update();
	}

	// atualiza a gui com base no nivel dado
	private void redrawGui(int num) {
		gui.clearImages();
		gameElements.clear();
		numAlvos = 0;
		numCaixas=0;
		drawLevel(num);
	}

	// desenha o nivel
	private void drawLevel(int num) {
		String level = "levels/level" + num + ".txt";
		List<ImageTile> tileList = new ArrayList<>();
		createWarehouse(level, tileList);
		createMoreStuff(level, tileList);
		sendImagesToGUI(tileList); 			// enviar as imagens para a GUI
		gui.setStatusMessage("Sokoban  " + "   Player:  " + player + "   Level: " + numLevel + "   Moves: "
				+ bobcat.getMoves() + "   Fuel: " + bobcat.getFuel());
		gui.update();
	}

	// Envio das mensagens para a GUI - note que isto so' precisa de ser feito no inicio
	private void sendImagesToGUI(List<ImageTile> tileList) {
		gui.addImages(tileList);
	}

}
