import Connections.MySQL.GardenElementsMySQL;
import FlowerStore.FlowerStore;
import FlowerStore.Interfaces.GardenElements;

import java.sql.SQLException;
import java.util.*;

public class App {
    static Scanner input = new Scanner(System.in);
    public static GardenElementsMySQL gardenElementsMySQL = new GardenElementsMySQL();
    private static FlowerStore flowerStore;
    private static int flowerStoreId;
    private static HashMap<Integer, String> listaFlowerStores = new HashMap<>();

    static HashMap<Integer, String> showFlowerStores(){

        listaFlowerStores = gardenElementsMySQL.showFlowerStore();
        Set<Integer> listaId = listaFlowerStores.keySet();
        System.out.println("Here are the available flower stores");
        for(Integer id : listaId){
            String value = listaFlowerStores.get(id);
            System.out.println("El id: " + id + " Nombre: " + value);
        }
        return listaFlowerStores;

    }
    static void runApp(){
        showFlowerStores();
        if (listaFlowerStores.isEmpty()) {
            System.out.println("You have not created any FlowerStore");
            createFlowerStore();
        } else {
            flowerStoreId = pedirDatoInt("Please indicate the ID of the flower shop you want to work with:");
        }

    }

    static void runProgram() {
        System.out.println("Working with FlowerStore ID: " + flowerStoreId);
        boolean seguirBucle;
        do {

            seguirBucle = menu(pedirDatoInt("Indicate number option:\n"
                    + "0.Exit\n"
                    + "1.Insert a tree, flower or decoration to stock\n"
                    + "2.Remove a tree, flower or decoration from stock\n"
                    + "3.Print the stock of all the trees, flowers and decorations\n"
                    + "4.Print the stock with quantities\n"
                    + "5.Print total value of flowerstore\n"
                    + "6.Create tickets with multiples objects\n"
                    + "7.Show a list of old purchases\n"
                    + "8.View the total money earned from all sales\n"
                    + "9.Remove FlowerStore\n"));
        }while(seguirBucle);
    }
    static boolean menu(int opcion) {
        boolean seguirBucle = true;
        switch (opcion){
            case 0:
                seguirBucle = false;
                System.out.println("You have exited the menu");
                break;
            case 1: insertProduct();
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
            case 9: removeFlowerStore();
                break;
            default:
        }
        return seguirBucle;

    }
    static void createFlowerStore(){
        String nameStore = pedirNombreSoloLetras("Dime un nombre para la floristeria");
        int id = 0;
        id = gardenElementsMySQL.createStore(nameStore);
        flowerStore = new FlowerStore(nameStore, id);
        List<GardenElements> products = gardenElementsMySQL.allGardenElements(id);
        gardenElementsMySQL.addStock(id, products);
        System.out.println("FlowerStore " + nameStore + "is created" );
        //gardenElementsMySQL.close();
    }
    static void insertProduct(){
        int num = 1;
        List<GardenElements> listaElements = new ArrayList<>();
        listaElements = gardenElementsMySQL.allGardenElements(flowerStoreId);
        System.out.println("Disponemos de estos productos: ");
        for(GardenElements element : listaElements){
            System.out.println(element);
          ;
        }
        int idProduct = pedirDatoInt("Indica el idProduct que quieres añadir al stock:");
        int quantity = pedirDatoInt("Ahora añade la cantidad:");
        //gardenElementsMySQL.updateStock(idProduct, quantity, price);
        System.out.println("Se han añadido " + quantity + " productos al stock de la floristería " + flowerStoreId);

    }
    static void removeFlowerStore(){
        showFlowerStores();
        try {
            gardenElementsMySQL.removeFlowerStore(pedirDatoInt("Qué id quieres borrar?"));

        } catch (SQLException e) {
            throw new RuntimeException("Error removing FlowerStore", e);
        }
    }


    static double pedirDatoDouble(String mensaje) {
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
