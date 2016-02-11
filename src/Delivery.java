import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Delivery {
	static int R, S, D, P, W, C, T, Payload;
	static Product[] products;
	static Warehouse[] warehouses;
	static Order[] orders;
	static Drone[] drones;

	static String[] files = { "busy_day.in", "mother_of_all_warehouses.in", "redundancy.in" };

	public static void main(String[] args) {
		for (String name : files) {
			try {
				File file = new File(name);

				Scanner input = new Scanner(file);
				String[] data = input.nextLine().split(" ");
				R = Integer.parseInt(data[0]);
				S = Integer.parseInt(data[1]);
				D = Integer.parseInt(data[2]);
				T = Integer.parseInt(data[3]);
				Payload = Integer.parseInt(data[4]);
				P = input.nextInt();
				input.nextLine();
				products = new Product[P];
				data = input.nextLine().split(" ");
				for (int i = 0; i < P; i++) {
					products[i] = new Product(i, Integer.valueOf(data[i]));
				}
				W = input.nextInt();
				input.nextLine();
				warehouses = new Warehouse[W];
				for (int i = 0; i < W; i++) {
					data = input.nextLine().split(" ");
					warehouses[i] = new Warehouse(Integer.valueOf(data[0]), Integer.valueOf(data[1]), P);
					data = input.nextLine().split(" ");
					for (int j = 0; j < P; j++) {
						warehouses[i].quantities[j] = Integer.valueOf(data[j]);
					}
				}
				C = input.nextInt();
				input.nextLine();
				orders = new Order[C];
				for (int i = 0; i < C; i++) {
					data = input.nextLine().split(" ");
					orders[i] = new Order(Integer.valueOf(data[0]), Integer.valueOf(data[1]), i);
					int L = input.nextInt();
					orders[i].setL(L);
					input.nextLine();
					data = input.nextLine().split(" ");
					for (int j = 0; j < L; j++) {
						int id = Integer.valueOf(data[j]);
						orders[i].products[j] = products[id];
					}
				}
				input.close();
				drones = new Drone[D];
				for (int i = 0; i < D; i++) {
					drones[i] = new Drone(warehouses[0].position.R, warehouses[0].position.C, Payload, T);
				}
				Arrays.sort(orders, Collections.reverseOrder());
				for (int i = 0; i < C; i++) {
					Order actual = orders[i];
					for (int j = 0; j < actual.L; j++) {
						Warehouse ware;
						int warehouse = getNearestWarehouse(actual.position.R, actual.position.C,
								actual.products[j].id);
						ware = warehouses[warehouse];
						int drone = getNearestDron(ware.position.R, ware.position.C, actual.products[j].id);
						Drone dron = drones[drone];
						write(drone + " L " + warehouse + " " + actual.products[j].id + " 1", true);
						ware.takeProduct(actual.products[j].id);
						dron.move(ware.position.R, ware.position.C);
						dron.load(actual.products[j].peso);
						write(drone + " D " + actual.id + " " + actual.products[j].id + " 1", true);
						dron.move(actual.position.R, actual.position.C);
						dron.unload(actual.products[j].peso);
					}
				}
				writeResults(name.replace(".in", ".out"));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static int getNearestDron(int r, int c, int product) {
		int min = Integer.MAX_VALUE;
		int dron = -1;
		for (int i = 0; i < D; i++) {
			int distancia = getDistance(drones[i].position.R, drones[i].position.C, r, c);
			if ((drones[i].load + products[product].peso) <= Payload && drones[i].movement - (distancia + 1) > 0) {
				if (distancia < min) {
					min = distancia;
					dron = i;
				}
			}
		}
		return dron;
	}

	public static int getNearestWarehouse(int r, int c, int product) {
		int min = Integer.MAX_VALUE;
		int warehouse = -1;
		for (int i = 0; i < warehouses.length; i++) {
			if (warehouses[i].hasProduct(product)) {
				int distancia = getDistance(warehouses[i].position.R, warehouses[i].position.C, r, c);
				if (distancia < min) {
					min = distancia;
					warehouse = i;
				}
			}
		}
		return warehouse;
	}

	public static int getDistance(int r, int c, int x, int z) {
		int distance = 0;
		distance = (int) Math.ceil(Math.sqrt(((r - x) * (r - x)) + ((c - z) * (c - z))));
		return distance;
	}

	public static void write(String str, boolean append) {
		if (str == "")
			return;
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileOutputStream(new File("commands.out"), append));
			writer.println(str);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean writeResults(String solution) {
		try {
			int lines = countLines("commands.out");
			File file = new File("commands.out");
			Scanner input = new Scanner(file);
			PrintWriter writer = new PrintWriter(solution);
			writer.println(lines);
			while (input.hasNextLine()) {
				writer.println(input.nextLine());
			}
			writer.close();
			input.close();
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public static int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

}
