import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Puente {
	private boolean libre = true; // El puente esta  libre
	private Queue<Integer> colaPuente = new LinkedList<>();

	public synchronized void cruzar(String direccionCoche) throws InterruptedException {
		while (!libre) { //libre = false 
			wait(); 
		}
		
		Random random = new Random();
		int numCoches = (random.nextInt(15) + 1);
		System.out.println("Coches entrando desde el " + direccionCoche + ": " + numCoches+". ");
		for (int i = 0; i < numCoches; i++) {
			colaPuente.add(random.nextInt(1000)); 
			System.out.println("Coche id : " + random.nextInt(100) + " 	posicion -> " + (i + 1));
		}
		libre = false; // El puente está ocupado		
		notify(); // Notifica a otros coches que el esta ocupado
	}

	public synchronized void saliendo() {
		
		
		colaPuente.clear(); 
		libre = true; // El puente esta libre 		
		notify(); // Notifica a otros coches que el puente está libre
		System.out.println("Todos los coches han salido del puente.");
	}

	public static void main(String[] args) {
		Puente puente = new Puente();
		Coches cocheNorte = new Coches("Norte", puente);
		Coches cocheSur = new Coches("Sur", puente);

		cocheNorte.start();
		cocheSur.start();

	}

}

class Coches extends Thread {
	private Puente puente;
	private String direccionCoche;

	public Coches(String direccionCoche, Puente puente) {
		this.direccionCoche = direccionCoche;
		this.puente = puente;
	}

	public void run() {
		while (true) {
			try {
				puente.cruzar(direccionCoche);
				sleep(1000); // Simula el tiempo que lleva cruzar el puente
				puente.saliendo();
				sleep(1000);  // Simula el tiempo de espera 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
