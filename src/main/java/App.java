import Connections.DAO.GenericDAO;
import Connections.DAO.ManagerDAO;
import Connections.MongoDB.GardenElementsMongoDB;
import Connections.MySQL.GardenElementsMySQL;
import Exceptions.NotValidOptionException;
import Exceptions.InputControl;
import FlowerStore.FlowerStore;
import FlowerStore.Interfaces.GardenElements;
import Ticket.ShopCart;

import java.util.*;



public class App {
    public static ManagerDAO managerDAO = new ManagerDAO(new GardenElementsMongoDB());
    private static FlowerStore flowerStore;
    private static ShopCart shopCart = ShopCart.getInstance();


    public static void runApp(){
        int opc = 0;
        System.out.println("Here are the available flower stores");
        System.out.println("0. Create or delete a FlowerStore");
        List<FlowerStore> listFlowerStores = showFlowerStores();
        if (listFlowerStores.isEmpty()) {
            System.out.println("You have not created any FlowerStore");
        }
        boolean validId = false;
        while (!validId) {
            try {
                opc = InputControl.requestIntData("Please indicate the ID of the flower shop you want to work with:") - 1;
                if (opc >= 0 && opc < listFlowerStores.size()) {
                    flowerStore = new FlowerStore(listFlowerStores.get(opc).getId(), listFlowerStores.get(opc).getName());
                    runProgram();
                    validId = true;
                }else if(opc == -1){
                    addDeleteFlowerShopMenu();
                    validId = true;
                }else {
                    throw new NotValidOptionException("The FlowerStore ID entered is not valid.");
                }
            } catch (NotValidOptionException e) {
                System.out.println(e.getMessage());
            }
        }
        if(opc == -1){
            runApp();
        }
    }
    private static void addDeleteFlowerShopMenu (){
        int opc;
        do {
            opc = InputControl.requestIntData("Choose an option:\n" +
                    "0. Return to choose a FlowerStore\n" +
                    "1. Create a new FlowerStore\n" +
                    "2. Remove a FlowerStore");
            switch (opc) {
                case 0:
                    System.out.println("Returning to main menu");
                    break;
                case 1:
                    createFlowerStore();
                    break;
                case 2:
                    removeFlowerStore();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid option: " + opc);
            }
        }while(opc != 0);
    }
    public static void runProgram() {
        System.out.println("Working with FlowerStore: " + flowerStore.getName());
        boolean seguirBucle;
        do {

            seguirBucle = menu(InputControl.requestIntData("Indicate number option:\n"
                    + "0.Exit\n"
                    + "1.Insert a tree, flower or decoration to stock\n"
                    + "2.Remove a tree, flower or decoration from stock\n"
                    + "3.Print the stock of all the trees, flowers and decorations\n"
                    + "4.Print the stock with quantities\n"
                    + "5.Print total value of flowerstore\n"
                    + "6.Create tickets with multiples objects\n"
                    + "7.Show a list of old purchases\n"
                    + "8.View the total money earned from all sales\n"));
        }while(seguirBucle);
    }
    private static boolean menu(int opcion) {
        boolean seguirBucle = true;
        switch (opcion){
            case 0:
                seguirBucle = false;
                System.out.println("You have exited the menu");
                break;
            case 1: insertProduct();
                break;
            case 2: removeProduct();
                break;
            case 3: printStock();
                break;
            case 4: printQuantity();
                break;
            case 5: valueTotal();
                break;
            case 6: createTicket();
                break;
            case 7: oldPurchasesList();
                break;
            case 8: totalMoneyEarned();
                break;
            default: throw new IllegalArgumentException("Invalid option: " + opcion);
        }
        return seguirBucle;

    }
    private static void insertProduct(){
        List<GardenElements> listaElements = managerDAO.showStockManager(flowerStore);
        System.out.println("We have these products: ");
        for(int i = 0; i < listaElements.size(); i++){
            System.out.println((i+1) + ". " + listaElements.get(i).toString());
        }
        try {
            int opc = InputControl.requestIntData("Indicate the product you want to update the stock:") -1;
            if (opc >= 0 && opc < listaElements.size()) {
                listaElements.get(opc).setQuantity(InputControl.requestIntData("Indicates the amount to add:") + listaElements.get(opc).getQuantity());
                double price = InputControl.requestDoubleData("Enter the sale price (enter 0 to not modify it)");
                if (price > 0) {
                    listaElements.get(opc).setPrice(price);
                }
                managerDAO.updateStockManager(flowerStore.getId(), listaElements.get(opc));
                System.out.println("Se han añadido " + listaElements.get(opc).getQuantity() + " productos al stock de la floristería " + flowerStore.getName());
                waitForContinue();

            } else {
                throw new NotValidOptionException("Incorrect option!");
            }
        } catch (NotValidOptionException e){
            System.out.println(e.getMessage());
        }
    }
    private static void removeProduct(){
        List<GardenElements> listaElements = managerDAO.showStockManager(flowerStore);
        System.out.println("We have these products: ");
        for(int i = 0; i < listaElements.size(); i++){
            System.out.println((i+1) + ". " + listaElements.get(i).toString());
        }

        try {
            int opc = InputControl.requestIntData("Indicate the product you want to remove the stock:") - 1;
            boolean flag = false;
            int stockRemove = 0;
            if (opc >= 0 && opc < listaElements.size()) {
                stockRemove = InputControl.requestIntData("Indicates the amount to remove:");
                if (listaElements.get(opc).getQuantity() >= stockRemove) {
                    listaElements.get(opc).setQuantity(listaElements.get(opc).getQuantity() - stockRemove);
                    flag = true;
                } else {
                    throw new NotValidOptionException("You can't delete more quantity than the existing stock");
                }
            } else {
                throw new NotValidOptionException("Incorrect option!");
            }
            managerDAO.updateStockManager(flowerStore.getId(), listaElements.get(opc));
            System.out.println(stockRemove + " products have been deleted from the " + flowerStore.getName() + " florist's stock.");
            waitForContinue();
        } catch (NotValidOptionException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private static void printStock(){
        List<GardenElements> listaStock = managerDAO.showStockManager(flowerStore);
        for(int i = 0; i < listaStock.size(); i++){
            System.out.println(listaStock.get(i).getClass().getSimpleName() + ": " + listaStock.get(i).getFeatures());
        }
        waitForContinue();
    }
    private static void printQuantity(){
        List<GardenElements> listaStock = managerDAO.showStockManager(flowerStore);
        for(int i = 0; i < listaStock.size(); i++){
            System.out.println(listaStock.get(i).getClass().getSimpleName() +" " + listaStock.get(i).getFeatures()+ ": " + listaStock.get(i).getQuantity());
        }
        waitForContinue();
    }
    private static void valueTotal(){
        List<GardenElements> listaStock = managerDAO.showStockManager(flowerStore);
        double total = 0;

        for(GardenElements prod : listaStock){
            total += prod.getPrice() * prod.getQuantity();

        }
        System.out.println("The value total of Flower store " + flowerStore.getName() + " is: " + total);
        waitForContinue();
    }
    private static void createTicket() {
        Scanner entrada = new Scanner(System.in);
        List<GardenElements> gardenElementsList = managerDAO.showStockManager(flowerStore);

        System.out.println("The garden elements available are: ");

        for (int i = 0; i < gardenElementsList.size(); i++) {
            System.out.println((i+1) + ". " + gardenElementsList.get(i));
        }


        boolean addInformation = true;

        while (addInformation) {
            int productId = -1;

            while(productId < 0 || productId >= gardenElementsList.size()){
                productId = InputControl.requestIntData("Enter product ID:") - 1;
            }

            int quantity = InputControl.requestIntData("Enter quantity:");

            if(quantity <= gardenElementsList.get(productId).getQuantity()){
                GardenElements tempGardenElement = gardenElementsList.get(productId);
                shopCart.addProductos(tempGardenElement,quantity);
                gardenElementsList.get(productId).setQuantity(gardenElementsList.get(productId).getQuantity()-quantity);
                managerDAO.updateStockManager(flowerStore.getId(),gardenElementsList.get(productId));
                System.out.println("Do you want to add more products to the ticket? (yes/no)");
                String respuesta = entrada.next();
                addInformation = respuesta.equalsIgnoreCase("yes");
            }
        }
        shopCart.printTicket();
        managerDAO.newTicketManager(flowerStore, shopCart.getProducts());
        System.out.println("Ticket created successfully.");
        waitForContinue();
    }

    private static void oldPurchasesList(){

        HashMap<Integer, Date> tickets = managerDAO.showAllTicketsManager(flowerStore.getId());
        if(tickets.isEmpty()){
            System.out.println("Don't have any ticket created");
        }else {
            System.out.println("Here you have the all tickets:");
            for (Integer ticketId : tickets.keySet()) {
                System.out.println("Ticket ID: " + ticketId + ", Date: " + tickets.get(ticketId));
            }
        }
        waitForContinue();
    }
    private static void totalMoneyEarned(){
        double totalMoneyEarned = managerDAO.totalPriceManager(flowerStore.getId());
        System.out.println("Total Money Earned from all sales: " + totalMoneyEarned);
        waitForContinue();
    }
    private static void createFlowerStore(){
        String nameStore = InputControl.askNameOnlyLetters("FlowerStore name: ");
        String id;
        id = managerDAO.newStoreManager(nameStore);
        flowerStore = new FlowerStore(id, nameStore);
        List<GardenElements> products = managerDAO.showStockManager(flowerStore);
        managerDAO.addStockManager(id, products);
        System.out.println("FlowerStore " + nameStore + "is created" );
        waitForContinue();
    }
    private static void removeFlowerStore(){
        List<FlowerStore> listFlowerStores = showFlowerStores();
        try {
            int opc = InputControl.requestIntData("Select the florist to delete") - 1;
            if(opc >= 0 && opc < listFlowerStores.size()) {
                managerDAO.removeFlowerStore(listFlowerStores.get(opc).getId());
                System.out.println("Flowershop eliminated.");
                waitForContinue();
            } else if (opc == 0){
                System.out.println("Empty flower store list");
            } else {
                throw new NotValidOptionException("The FlowerStore ID entered is not valid.");
            }
        } catch (NotValidOptionException e) {
            System.out.println(e.getMessage());
        }

    }
    private static List<FlowerStore> showFlowerStores(){
        List<FlowerStore> listFlowerStores = managerDAO.showFlowerStoreManager();
        for(int i = 0; i < listFlowerStores.size(); i++){
            System.out.println((i+1) + ". " + listFlowerStores.get(i).getName());
        }
        return listFlowerStores;

    }
    private static void waitForContinue(){
        Scanner sc = new Scanner(System.in);
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\nPress any key to continue...");
        sc.nextLine();
        System.out.println("\n\n\n\n");
    }



}
