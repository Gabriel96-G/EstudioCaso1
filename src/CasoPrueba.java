

public class CasoPrueba {
    private static int correctas = 0;
    private static int ejecutadas = 0;

    private static void igual(AnalizadorExpresiones a, String expresion, double esperado) {
        ejecutadas++;
        double real = a.evaluar(expresion);
        if (Math.abs(real - esperado) > 0.0000001) {
            throw new AssertionError(expresion + " -> esperado " + esperado + ", obtenido " + real);
        }
        correctas++;
        System.out.println("OK resultado: " + expresion + " = " + real);
    }

    private static void error(AnalizadorExpresiones a, String expresion) {
        ejecutadas++;
        try {
            a.evaluar(expresion);
            throw new AssertionError("Se esperaba error en: " + expresion);
        } catch (IllegalArgumentException | ArithmeticException esperado) {
            correctas++;
            System.out.println("OK error controlado: " + expresion + " -> " + esperado.getMessage());
        }
    }

    public static void main(String[] args) {
        AnalizadorExpresiones a = new AnalizadorExpresiones();
        a.registrarVariable("precio", 100);
        a.registrarVariable("cantidad", 3);
        a.registrarVariable("impuesto", 13);
        a.registrarVariable("precio", 100); // Actualizacion de variable.
        igual(a, "(8 + 2) * 3", 30);
        igual(a, "10 + 2 * 5", 20);
        igual(a, "3.5 + .5", 4);
        igual(a, "precio * cantidad + impuesto", 313);
        igual(a, "-(2+3)*2", -10);
        igual(a, "-3*-2", 6);
        igual(a, "5-8-2", -5);
        igual(a, "8/2/2", 2);
        igual(a, "(4+(3*2))/2", 5);
        igual(a, "(1+(2*(3+4)))", 15);
        igual(a, "2--3", 5);
        igual(a, "1+-2", -1);
        igual(a, "-(-3)", 3);
        igual(a, "2.5*4", 10);
        igual(a, "  6  / ( 2 + 1 )  ", 2);
        igual(a, "(1+2)+(3+4)", 10);
        error(a, "");
        error(a, "2+");
        error(a, "2**3");
        error(a, "3 + noExiste");
        error(a, "(1+2");
        error(a, "1+2)");
        error(a, "2(4)");
        error(a, "2 2");
        error(a, "3/0");
        error(a, "()");
        error(a, "1..2");
        error(a, "2&3");
        error(a, "1+() ");
        Pila pila = new Pila();
        if (!pila.estaVacia() || pila.pop() != null || pila.peek() != null) {
            throw new AssertionError("Pila vacia incorrecta");
        }
        pila.push("A"); pila.push("+"); pila.push("B");
        if (!"B".equals(pila.peek()) || !"B".equals(pila.pop()) || pila.getCantidad()!=2) {
            throw new AssertionError("Operaciones LIFO incorrectas");
        }
        pila.vaciar();
        if (!pila.estaVacia()) throw new AssertionError("Vaciar pila incorrecto");
        System.out.println("TOTAL: " + correctas + "/" + ejecutadas + " casos de expresiones correctos; pila LIFO comprobada.");
    }
}
