
public class Drone {
	Position position;
	int load, payload, movement;

	Drone(int r, int c, int P, int M) {
		position = new Position(r, c);
		load = 0;
		movement = M;
		payload = P;
	}

	void load(int peso) {
		load = load + peso;
	}

	void unload(int peso) {
		load = load - peso;
	}

	void move(int r, int c) {
		int distancia = getDistance(position.R, position.C, r, c);
		movement = movement - (distancia+1);
		this.position.R = r;
		this.position.C = c;
	}

	public static int getDistance(int r, int c, int x, int z) {
		int distance = 0;
		distance = (int) Math.ceil(Math.sqrt(((r - x) * (r - x)) + ((c - z) * (c - z))));
		return distance+1;
	}

}
