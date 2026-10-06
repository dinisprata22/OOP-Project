package pt.iscte.poo.sokobanstarter;

import java.util.Comparator;

public class DescendingComparator implements Comparator<String>{

	@Override
	public int compare(String o1, String o2) { // Player: ... , Moves: ...
		// TODO Auto-generated method stub
		String[] um = o1.split(":");
		String[] dois = o2.split(":");
		if (um.length < 3) {
			if (dois.length < 3) {
				return 0;
			}else return 1;
		}else if(dois.length <3) return 0;
		return Integer.parseInt(um[2].strip())-Integer.parseInt(dois[2].strip());
	}
}
