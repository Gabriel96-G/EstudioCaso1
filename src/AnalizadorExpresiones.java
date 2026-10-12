

public class AnalizadorExpresiones {
    // Atributos para las variables registradas
    private String[] nombres;
    private double[] valoresVariables;
    private int cantidadVariables;

    // Constructor
    public AnalizadorExpresiones() {
        nombres = new String[30];
        valoresVariables = new double[30];
        cantidadVariables = 0;
    }

    // Valida nombres como total, precio2 o _iva
    public boolean nombreValido(String nombre) {
        if (nombre == null || nombre.length() == 0) return false;
        char inicial = nombre.charAt(0);
        if (!Character.isLetter(inicial) && inicial != '_') return false;
        for (int i = 1; i < nombre.length(); i++) {
            char c = nombre.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') return false;
        }
        return true;
    }

    public void registrarVariable(String nombre, double valor) {
        if (!nombreValido(nombre)) {
            throw new IllegalArgumentException("Nombre invalido. Ejemplos: precio, x, costo_2.");
        }
        if (Double.isInfinite(valor) || Double.isNaN(valor)) {
            throw new IllegalArgumentException("El valor de la variable debe ser finito.");
        }
        for (int i = 0; i < cantidadVariables; i++) {
            if (nombres[i].equals(nombre)) {
                valoresVariables[i] = valor;
                return;
            }
        }
        if (cantidadVariables == nombres.length) {
            throw new IllegalArgumentException("Limite de 30 variables alcanzado.");
        }
        nombres[cantidadVariables] = nombre;
        valoresVariables[cantidadVariables] = valor;
        cantidadVariables++;
    }

    private double buscarVariable(String nombre) {
        for (int i = 0; i < cantidadVariables; i++) {
            if (nombres[i].equals(nombre)) return valoresVariables[i];
        }
        throw new IllegalArgumentException("La variable '" + nombre + "' no esta definida.");
    }

    public void mostrarVariables() {
        if (cantidadVariables == 0) {
            System.out.println("No se han registrado variables.\n");
            return;
        }
        System.out.println("--- VARIABLES REGISTRADAS ---");
        for (int i = 0; i < cantidadVariables; i++) {
            System.out.println(nombres[i] + " = " + valoresVariables[i]);
        }
        System.out.println();
    }

    // Prioridad: signo negativo unario (~), luego * y /, luego + y -
    private int prioridad(String operador) {
        if (operador.equals("~")) return 3;
        if (operador.equals("*") || operador.equals("/")) return 2;
        if (operador.equals("+") || operador.equals("-")) return 1;
        return 0;
    }

    // Retira tokens respetando el orden de los operandos.
    private void aplicarOperacion(Pila operadores, Pila valores) {
        String operador = operadores.pop();
        if (operador == null || operador.equals("(")) {
            throw new IllegalArgumentException("Operacion invalida o parentesis sin cerrar.");
        }
        if (operador.equals("~")) {
            if (valores.getCantidad() < 1) {
                throw new IllegalArgumentException("Falta un valor para el signo negativo.");
            }
            double numero = Double.parseDouble(valores.pop());
            valores.push(String.valueOf(-numero));
            return;
        }
        if (valores.getCantidad() < 2) {
            throw new IllegalArgumentException("Faltan operandos para '" + operador + "'.");
        }
        double derecho = Double.parseDouble(valores.pop());
        double izquierdo = Double.parseDouble(valores.pop());
        double resultado;
        switch (operador) {
            case "+": resultado = izquierdo + derecho; break;
            case "-": resultado = izquierdo - derecho; break;
            case "*": resultado = izquierdo * derecho; break;
            case "/":
                if (derecho == 0.0) {
                    throw new ArithmeticException("No es posible dividir entre cero.");
                }
                resultado = izquierdo / derecho;
                break;
            default:
                throw new IllegalArgumentException("Operador no reconocido: " + operador);
        }
        if (Double.isInfinite(resultado) || Double.isNaN(resultado)) {
            throw new ArithmeticException("Resultado fuera del rango numerico permitido.");
        }
        valores.push(String.valueOf(resultado));
    }

    /**
     * Recorre la expresion caracter por caracter y procesa cada token.
     * Se usan pilas independientes de operadores y operandos.
     */
    public double evaluar(String expresion) {
        if (expresion == null || expresion.trim().isEmpty()) {
            throw new IllegalArgumentException("La expresion no puede estar vacia.");
        }
        Pila operadores = new Pila();
        Pila valores = new Pila();
        boolean esperandoOperando = true;
        int i = 0;

        while (i < expresion.length()) {
            char c = expresion.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // Token: numero literal, entero o decimal.
            if (Character.isDigit(c) || c == '.') {
                if (!esperandoOperando) {
                    throw new IllegalArgumentException("Falta un operador antes de la posicion " + (i + 1) + ".");
                }
                int inicio = i;
                int puntos = 0;
                while (i < expresion.length()) {
                    char actual = expresion.charAt(i);
                    if (Character.isDigit(actual)) {
                        i++;
                    } else if (actual == '.') {
                        puntos++;
                        i++;
                    } else {
                        break;
                    }
                }
                if (puntos > 1) {
                    throw new IllegalArgumentException("Numero decimal invalido en posicion " + (inicio + 1) + ".");
                }
                String numero = expresion.substring(inicio, i);
                try {
                    double valor = Double.parseDouble(numero);
                    if (Double.isInfinite(valor) || Double.isNaN(valor)) {
                        throw new IllegalArgumentException("Numero fuera de rango: " + numero);
                    }
                    valores.push(String.valueOf(valor));
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Numero invalido: " + numero);
                }
                esperandoOperando = false;
                continue;
            }

            // Token: nombre de variable.
            if (Character.isLetter(c) || c == '_') {
                if (!esperandoOperando) {
                    throw new IllegalArgumentException("Falta un operador antes de la posicion " + (i + 1) + ".");
                }
                int inicio = i;
                while (i < expresion.length()) {
                    char actual = expresion.charAt(i);
                    if (Character.isLetterOrDigit(actual) || actual == '_') i++;
                    else break;
                }
                String nombre = expresion.substring(inicio, i);
                valores.push(String.valueOf(buscarVariable(nombre)));
                esperandoOperando = false;
                continue;
            }

            // Token: parentesis de apertura.
            if (c == '(') {
                if (!esperandoOperando) {
                    throw new IllegalArgumentException("Falta operador antes de '('.");
                }
                operadores.push("(");
                i++;
                continue;
            }

            // Token: parentesis de cierre.
            if (c == ')') {
                if (esperandoOperando) {
                    throw new IllegalArgumentException("Parentesis cerrado sin un operando.");
                }
                while (!operadores.estaVacia() && !operadores.peek().equals("(")) {
                    aplicarOperacion(operadores, valores);
                }
                if (operadores.estaVacia()) {
                    throw new IllegalArgumentException("Hay un parentesis ')' sin apertura.");
                }
                operadores.pop(); // Retira el '('
                esperandoOperando = false;
                i++;
                continue;
            }

            // Token: operador matematico.
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (esperandoOperando) {
                    if (c == '-') {
                        operadores.push("~"); // Representa el menos unario.
                        i++;
                        continue;
                    }
                    if (c == '+') {
                        i++; // Mas unario: no altera el valor.
                        continue;
                    }
                    throw new IllegalArgumentException("Falta un operando antes de '" + c + "'.");
                }
                String nuevo = String.valueOf(c);
                while (!operadores.estaVacia()
                        && !operadores.peek().equals("(")
                        && prioridad(operadores.peek()) >= prioridad(nuevo)) {
                    aplicarOperacion(operadores, valores);
                }
                operadores.push(nuevo);
                esperandoOperando = true;
                i++;
                continue;
            }
            throw new IllegalArgumentException("Caracter no permitido '" + c + "' en posicion " + (i + 1) + ".");
        }

        if (esperandoOperando) {
            throw new IllegalArgumentException("La expresion termina sin un operando.");
        }
        while (!operadores.estaVacia()) {
            if (operadores.peek().equals("(")) {
                throw new IllegalArgumentException("Hay un parentesis '(' sin cerrar.");
            }
            aplicarOperacion(operadores, valores);
        }
        if (valores.getCantidad() != 1) {
            throw new IllegalArgumentException("La expresion contiene operandos sin relacion.");
        }
        return Double.parseDouble(valores.pop());
    }
}
