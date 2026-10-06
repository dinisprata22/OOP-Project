package pt.iscte.poo.sokobanstarter;

import java.util.List;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Palete extends GameElement implements Movable{
	
	Palete(Point2D point2d) {
		super(point2d);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Palete";
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean move(Direction direction, List<GameElement> gameElements) {

		Point2D newPoint = getPosition().plus(direction.asVector());

		for (GameElement e : gameElements) {
			if (e.getPosition().equals(newPoint)) {
				if(e instanceof Movable) {
					return false;
				}else if (e instanceof Buraco) {
					Buraco b = (Buraco) e;
					b.swallow(this);
					remove(b.toRemove());
				} else if (e instanceof Teleporte) {
					Teleporte t = (Teleporte) e;
					if (t.teleport(this, gameElements))
						return true;
				} else if (e instanceof Alvo) {
					setPoint2D(newPoint);
					e.setOccupied(true);
					return true;
				} else
					return false;
			}
		}

		setPoint2D(newPoint);
		return true;
	}
	
	
}
