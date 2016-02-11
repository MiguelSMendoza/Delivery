
public class Warehouse {
	int id;

	Position position;

	int[] quantities;

	Warehouse(int r, int c, int P) {
		this.position = new Position(r, c);
		quantities = new int[P];
	}

	public int quantity(int id) {
		return quantities[id];
	}

	public boolean hasProduct(int id) {
		return quantities[id] > 0;
	}

	public int takeProduct(int id) {
		quantities[id]--;
		return quantities[id];
	}

}
