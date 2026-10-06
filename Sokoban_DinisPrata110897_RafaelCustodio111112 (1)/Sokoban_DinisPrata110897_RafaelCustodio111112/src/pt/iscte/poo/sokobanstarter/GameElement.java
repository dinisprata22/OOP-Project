package pt.iscte.poo.sokobanstarter;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class GameElement implements ImageTile {
	
	private Point2D point2d; 		// Referencia para a posicao do objeto  
	private boolean occupied;		// Atributo para saber se esta ocupado (dependendo do objeto pode ou nao ser utilizado)
	private List<GameElement> remove;
	
	GameElement(Point2D point2d){
		this.point2d = point2d;
		this.occupied = false;
		remove = new ArrayList<>();
	}

	// Nome do objeto
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	// posicao do abjeto
	@Override
	public Point2D getPosition() {
		// TODO Auto-generated method stub
		return point2d;
	}
	
	// muda a posicao do objeto
	protected void setPoint2D(Point2D point2d) {
		this.point2d = point2d;
	}
	
	// verifica se a posicao esta ocupada
	public boolean isOccupied() {
		return occupied;
	}

	// muda o atributo occupied
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	// camada do objeto 
	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void remove(GameElement e) {
		remove.add(e);
	}
	
	public void remove(List<GameElement> r) {
		for(GameElement e : r)
		remove.add(e);
	}
	
	public List<GameElement> toRemove(){
		return remove;
	}

}
