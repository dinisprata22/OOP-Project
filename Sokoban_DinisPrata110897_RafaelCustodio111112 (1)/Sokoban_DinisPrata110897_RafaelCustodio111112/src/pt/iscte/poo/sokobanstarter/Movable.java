package pt.iscte.poo.sokobanstarter;

import java.util.List;

import pt.iscte.poo.utils.Direction;

public interface Movable {

	public boolean move(Direction direcao, List<GameElement> gameElements);
}
