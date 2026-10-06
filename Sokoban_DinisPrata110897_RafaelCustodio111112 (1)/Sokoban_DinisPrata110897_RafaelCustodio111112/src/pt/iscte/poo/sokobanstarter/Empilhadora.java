package pt.iscte.poo.sokobanstarter;

import java.util.List;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement {

	private String imageName;
	private int fuel;
	private boolean hasHammer;
	private int moves;

	public Empilhadora(Point2D initialPosition) {
		super(initialPosition);
		this.imageName = "Empilhadora_D";
		this.fuel = 100;
		this.hasHammer = false;
		moves = 0;
	}

	public int getMoves() {
		return moves;
	}

	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public void gainFuel(int fuel) {
		this.fuel += fuel;
	}

	public int getFuel() {
		return fuel;
	}

	public boolean hasHammer() {
		return hasHammer;
	}

	public void setHasHammer(boolean hasHammer) {
		this.hasHammer = hasHammer;
	}

	/*
	 * Faz a empilhadora andar com base na direcao verifacando antes se o pode fazer
	 */
	public void move(Direction direction, List<GameElement> gameElements) {

		Point2D newPosition = getPosition().plus(direction.asVector());
		String name = direction.name();
		imageName = "Empilhadora_" + name.charAt(0);

		for (GameElement e : gameElements) {
			if (e.getPosition().equals(newPosition)) { 		// se for um movable
				if (e instanceof Movable) {
					Movable m = (Movable) e;
					if (m.move(direction, gameElements)) {
						this.setPoint2D(newPosition);
						moves++;
						fuel -= 2;
					}
					remove(e.toRemove());
					return;
				} else if (e instanceof Teleporte) { 		// se for um teleporte
					Teleporte t = (Teleporte) e;
					if (t.teleport(this, gameElements)) {
						return;
					}

				} else if (e instanceof Absorvable) { 		// se for um absorvable
					Absorvable a = (Absorvable) e;
					a.purpose(this, direction);
					remove(e);
					this.setPoint2D(newPosition);
					fuel--;
					return;
				} else if (e instanceof Buraco) { 			// se for um buraco
					Buraco b = (Buraco) e;
					b.swallow(this);
					remove(b.toRemove());
					return;
				} else if (e instanceof Unmovable) { 		// se for um unmovable
					if (e instanceof ParedeRachada) {
						if (hasHammer) {
							remove(e);
							this.setPoint2D(newPosition);
							fuel--;
						}
					}
					return;
				}
			}
		}

		this.setPoint2D(newPosition);
		fuel--;
		moves++;
	}

}