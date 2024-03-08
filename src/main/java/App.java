import Connections.MySQL.GardenElementsMySQL;
import FlowerStore.FlowerStore;

import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    static Scanner input = new Scanner(System.in);
    private static GardenElementsMySQL gardenElementsDAO = null;

    private static FlowerStore flowerStore;
    static void runProgram() {
        boolean seguirBucle;
        do {

            seguirBucle = menu(pedirDatoInt("Indicate number option:\n"
                    + "0.Exit\n"
                    + "1.Create flowerstore\n"
                    + "2.Insert a tree, flower or decoration to stock\n"
                    + "3.Remove a tree, flower or decoration from stock\n"
                    + "4.Print the stock of all the trees, flowers and decorations\n"
                    + "5.Print the stock with quantities\n"
                    + "6.Print total value of flowerstore\n"
                    + "7.Create tickets with multiples objects\n"
                    + "8.Show a list of old purchases\n"
                    + "9.View the total money earned from all sales\n"));
        }while(seguirBucle);
    }
    static boolean menu(int opcion) {
        boolean seguirBucle = true;
        switch (opcion){
            case 0:
                seguirBucle = false;
                System.out.println("You have exited the menu");
                break;
            case 1: createFlowerStore();
                break;
            case 2: ;
                break;
            case 3: ;
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            default:
        }
        return seguirBucle;

    }
    static void createFlowerStore(){
        String nameStore = pedirNombre("Indicate the name of flowerStore");
        int id = gardenElementsDAO.createStore(nameStore);
        flowerStore = new FlowerStore(nameStore, id);
        //No necesita return, ya modifica el objeto global (edu)
    }


    static double pedirDato(String mensaje) {
        boolean correcto = true;
        double opcion = 0;
        while (correcto) {
            try {
                System.out.println(mensaje);
                opcion = input.nextDouble();
                correcto = false;
            } catch (InputMismatchException e) {
                System.out.println("No es un formato válido");
            }input.nextLine();

        }

        return opcion;
    }
    static int pedirDatoInt(String mensaje) {
        boolean correcto = true;
        int opcion = 0;
        while (correcto) {
            try {
                System.out.println(mensaje);
                opcion = input.nextInt();
                correcto = false;
            } catch (InputMismatchException e) {
                System.out.println("Introduce numeros enteros");
            }input.nextLine();

        }

        return opcion;
    }

    static String pedirNombreSoloLetras(String mensaje) {
        boolean seguirBucle = true;
        String nombre = "";
        while (seguirBucle) {
            try {
                System.out.println(mensaje);
                nombre = input.nextLine();
                for(int i = 0; i < nombre.length(); i++) {
                    char comprobante = nombre.charAt(i);
                    if (!Character.isAlphabetic(comprobante)) {//Compruebo que cada caracter sean letras
                        throw new Exception();
                    }
                }
                seguirBucle = false;
            } catch (Exception e) {
                System.out.println("Introduce solo letras");
            }
        }
        return nombre;
    }

    static String pedirNombre(String mensaje) {
        Scanner input = new Scanner(System.in);
        System.out.println(mensaje);
        String nombre = input.nextLine();
        return nombre;
    }


}
