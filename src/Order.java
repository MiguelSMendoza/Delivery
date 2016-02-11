
public class Order implements Comparable<Order> {
	int id;
	Position position;
	int L;
	Product[] products;

	Order(int r, int c, int id) {
		position = new Position(r, c);
		this.id = id;
	}

	public void setL(int L) {
		this.L = L;
		products = new Product[L];
	}

	public int countProducts(int id) {
		int count = 0;
		for (int i = 0; i < products.length; i++) {
			if (products[id].id == id)
				count++;
		}
		return count;
	}

	public int getPeso() {
		int peso = 0;
		for (int i = 0; i < products.length; i++) {
			peso += products[i].peso;
		}
		return peso;
	}

	@Override
	public int compareTo(Order o) {
		if (this.getPeso() > o.getPeso())
			return -1;
		if (this.getPeso() < o.getPeso())
			return 1;
		return 0;
	}

}
