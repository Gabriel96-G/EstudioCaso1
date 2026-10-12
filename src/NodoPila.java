
public class NodoPila {
    // Atributos
    private String token;
    private NodoPila siguiente;

    // Constructor
    public NodoPila(String token) {
        this.token = token;
        this.siguiente = null;
    }

    // Getters y setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public NodoPila getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoPila siguiente) {
        this.siguiente = siguiente;
    }
}
