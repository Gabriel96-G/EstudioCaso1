
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static AnalizadorExpresiones analizador = new AnalizadorExpresiones();
    private static Pila pilaPrueba = new Pila();

    public static void main(String[] args) {
        menu();
        scanner.close();
    }

    public static void menu() {
        int opcion = 0;
        do {
            System.out.println("=================================================");
            System.out.println("  ANALIZADOR DE EXPRESIONES - ESTUDIO DE CASO 1  ");
            System.out.println("=================================================");
            System.out.println("1. Registrar o modificar una variable numerica");
            System.out.println("2. Mostrar variables registradas");
            System.out.println("3. Evaluar una expresion aritmetica");
            System.out.println("4. Probar operaciones de la pila (push/pop/peek)");
            System.out.println("5. Mostrar instrucciones y ejemplos");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opcion: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Escriba un numero de opcion.\n");
                continue;
            }
            System.out.println();
            switch (opcion) {
                case 1: registrarVariable(); break;
                case 2: analizador.mostrarVariables(); break;
                case 3: evaluarExpresion(); break;
                case 4: menuPila(); break;
                case 5: mostrarAyuda(); break;
                case 6: System.out.println("Fin del programa. Hasta luego."); break;
                default: System.out.println("Opcion no valida.\n");
            }
        } while (opcion != 6);
    }

    private static void registrarVariable() {
        System.out.println("--- REGISTRAR O ACTUALIZAR VARIABLE ---");
        System.out.print("Nombre (ej.: precio): ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Valor numerico: ");
        try {
            double valor = Double.parseDouble(scanner.nextLine().trim());
            analizador.registrarVariable(nombre, valor);
            System.out.println("Variable guardada: " + nombre + " = " + valor + "\n");
        } catch (NumberFormatException e) {
            System.out.println("[Error] Ingrese un numero valido.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("[Error] " + e.getMessage() + "\n");
        }
    }

    private static void evaluarExpresion() {
        System.out.println("--- EVALUACION ARITMETICA ---");
        System.out.print("Escriba la expresion: ");
        String expresion = scanner.nextLine();
        try {
            double resultado = analizador.evaluar(expresion);
            System.out.println("Expresion: " + expresion);
            System.out.println("Resultado: " + resultado + "\n");
        } catch (IllegalArgumentException e) {
            System.out.println("[Error] " + e.getMessage() + "\n");
        } catch (ArithmeticException e) {
            System.out.println("[Error] " + e.getMessage() + "\n");
        }
    }

    private static void menuPila() {
        int opcion = 0;
        do {
            System.out.println("--- DEMOSTRACION DE PILA ---");
            System.out.println("1. push: insertar un token");
            System.out.println("2. pop: extraer el ultimo token");
            System.out.println("3. peek: consultar la cima");
            System.out.println("4. Mostrar pila completa");
            System.out.println("5. Vaciar pila");
            System.out.println("6. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Ingrese una opcion numerica.\n");
                continue;
            }
            switch (opcion) {
                case 1:
                    System.out.print("Token a insertar: ");
                    String token = scanner.nextLine().trim();
                    if (token.length() == 0) {
                        System.out.println("El token no debe estar vacio.\n");
                    } else {
                        pilaPrueba.push(token);
                        System.out.println("Token insertado: " + token + "\n");
                    }
                    break;
                case 2:
                    String extraido = pilaPrueba.pop();
                    System.out.println(extraido == null ? "La pila esta vacia.\n" : "Token extraido: " + extraido + "\n");
                    break;
                case 3:
                    String cima = pilaPrueba.peek();
                    System.out.println(cima == null ? "La pila esta vacia.\n" : "Token en la cima: " + cima + "\n");
                    break;
                case 4: pilaPrueba.mostrarPila(); break;
                case 5:
                    pilaPrueba.vaciar();
                    System.out.println("Pila vaciada.\n");
                    break;
                case 6: System.out.println("Regresando al menu principal.\n"); break;
                default: System.out.println("Opcion no valida.\n");
            }
        } while (opcion != 6);
    }

    private static void mostrarAyuda() {
        System.out.println("--- INSTRUCCIONES Y EJEMPLOS ---");
        System.out.println("Se aceptan numeros, variables, +, -, *, / y parentesis.");
        System.out.println("Ejemplo 1: (8 + 2) * 3  -> 30.0");
        System.out.println("Ejemplo 2: 10 + 2 * 5   -> 20.0");
        System.out.println("Ejemplo 3: -3 * (2 + 1) -> -9.0");
        System.out.println("Ejemplo 4: precio * cantidad + impuesto");
        System.out.println("  Primero registre: precio=100, cantidad=3, impuesto=13.");
        System.out.println("  Entonces el resultado sera 313.0.");
        System.out.println("Use punto decimal: 2.5 (no 2,5).");
        System.out.println("No se permite division entre cero ni variables indefinidas.\n");
    }
}
