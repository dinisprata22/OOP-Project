package pt.iscte.poo.sokobanstarter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Scoreboard {

	private List<String> statsLevels;
	private int numOfLevels;
	
	Scoreboard( int num){
		this.statsLevels = new ArrayList<>();
		numOfLevels = num;
	}
	
	//atualiza o conteudo do ficheiro
	public void UpdateStats(int numLevel, String player, int moves) {
		File ficheiro = new File("Scoreboard.txt");
		if(statsLevels.isEmpty()) {
			inicializeStats(ficheiro);
		}
		
		List<String> newScore = new ArrayList<>();

		for (int i = 0; i < statsLevels.size(); i++) {
			if (i == 2 + 4 * numLevel) {
				List<String> list = new ArrayList<>();
				list.add("-> PLAYER: " + player + " MOVES: " + moves);
				list.add(statsLevels.get(i));
				list.add(statsLevels.get(++i));
				list.add(statsLevels.get(++i));
				DescendingComparator e = new DescendingComparator();
				list.sort(e);
				for (String s : list) {
					newScore.add(s);
				}
				newScore.remove(newScore.size() - 1);
			} else
				newScore.add(statsLevels.get(i));
		}

		statsLevels = newScore;
		try {
			PrintWriter print = new PrintWriter(ficheiro);
			for (String s : newScore)
				print.println(s);
			print.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error file not found" + ficheiro);
		}
	}
	
	
	/* prepara a lista
	 * se estiver vazio adiciona algum texto predefinido a lista
	 * se nao vai buscar o conteudo do ficheiro ficheiro
	 */ 
	private void inicializeStats(File ficheiro) {
		if (ficheiro.length() == 0) {
			statsLevels.add("SOKOBAN SCOREBOARD");
			
			for (int i = 0; i <= numOfLevels; i++) {
				statsLevels.add("LEVEL " + i);
				statsLevels.add("");
				statsLevels.add("");
				statsLevels.add("");
			}
		} else {
			try {
				Scanner scanner = new Scanner(ficheiro);
				while (scanner.hasNextLine()) {
					statsLevels.add(scanner.nextLine());
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				System.err.println("Error file not found" + ficheiro);
			}
		}
	}
}
