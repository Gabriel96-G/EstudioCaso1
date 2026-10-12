
public class Pila {
    // Atributos
    private NodoPila cima;
    private int cantidad;

    // Constructor
    public Pila() {
        cima = null;
        cantidad = 0;
    }

    // Operaciones fundamentales
    public boolean estaVacia() {
        return cima == null;
    }

    public void push(String token) {
        NodoPila nuevo = new NodoPila(token);
        nuevo.setSiguiente(cima);
        cima = nuevo;
        cantidad++;
    }

    public String pop() {
        if (estaVacia()) {
            return null;
        }
        String token = cima.getToken();
        cima = cima.getSiguiente();
        cantidad--;
        return token;
    }

    public String peek() {
        if (estaVacia()) {
            return null;
        }
        return cima.getToken();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void vaciar() {
        while (!estaVacia()) {
            pop();
        }
    }

    public void mostrarPila() {
        if (estaVacia()) {
            System.out.println("La pila esta vacia.\n");
            return;
        }
        System.out.println("--- PILA (DE CIMA A BASE) ---");
        NodoPila actual = cima;
        while (actual != null) {
            System.out.println("[ " + actual.getToken() + " ]");
            actual = actual.getSiguiente();
        }
        System.out.println("-----------------------------");
        System.out.println("Cantidad de tokens: " + cantidad + "\n");
    }
}
