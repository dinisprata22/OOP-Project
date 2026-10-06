package pt.iscte.poo.sokobanstarter;

import java.util.List;
import pt.iscte.poo.utils.Point2D;

public class Teleporte extends GameElement {
	
	private GameElement twinTeleport;

	Teleporte(Point2D point2d) {
		super(point2d);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Teleporte";
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public GameElement twinTeleport(List<GameElement> gameElements) {
		for (GameElement e : gameElements) {
			if (e instanceof Teleporte && !getPosition().equals(e.getPosition()))
				twinTeleport = e;
		}
		return twinTeleport;
	}
	
	public boolean teleport(GameElement e, List<GameElement> gameElements ) {
		if(twinTeleport == null)
			twinTeleport(gameElements);
		
		for(GameElement h: gameElements) {
			if(h.getPosition().equals(getPosition()) && !( h instanceof Teleporte)) {
				return false;
			}
		}
		if(!twinTeleport.isOccupied()) { 
			e.setPoint2D(twinTeleport.getPosition());
			return true;
		}else 
			return false;
	}	
	
	

}
