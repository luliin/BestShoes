package model;

import model.daos.*;

import java.util.*;
import java.util.stream.Collectors;



/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-23
 * Time: 19:44
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class ConsoleMain {
    private static Customer customer;
    private static final Scanner scanner = new Scanner(System.in);
    private static Order order;

    public static void main(String[] args) {
        //noinspection InfiniteLoopStatement
        while (true) {
            login();
            menu();
        }
    }

    private static void login() {
        System.out.print("Ange mail-adress: ");
        String email = scanner.nextLine().trim();

        System.out.print("Ange lösenord: ");
        String password = scanner.nextLine().trim();

        CustomerDao customerDao = new CustomerDao();
        customerDao.getAll().stream()
                .filter(customer -> customer.getEmail().equalsIgnoreCase(email))
                .filter(customer -> customer.getPassword().equals(password))
                .findFirst()
                .ifPresentOrElse(ConsoleMain::loginSuccessful, ConsoleMain::loginFailed);
    }

    private static void loginSuccessful(Customer activeCustomer) {
        customer = activeCustomer;
        System.out.printf("Välkommen%s!\n", customer.getShortName() != null ? " " + customer.getShortName() : "");
        order = Order.create(customer);

    }

    private static void loginFailed() {
        System.out.println("Fel användarnamn eller lösenord! Försök igen. ");
        login();
    }

    private static final String menuString = "\nVälj vad du vill göra\n\n" +
            "1. Lägg till i varukorgen\n" +
            "2. Se din varukorg\n" +
            "3. Betygsätt en produkt\n" +
            "4. Se betyg på produkter\n" +
            "5. Bekräfta köp\n" +
            "6. Avsluta\n";

    private static void menu() {
        System.out.println(menuString);
        var selection = handleIntInput(6);
        switch (selection) {
            case 1 -> startOrder();
            case 2 -> viewOrder();
            case 3 -> rateProduct();
            case 4 -> viewRatings();
            case 5 -> confirmPurchase();
            case 6 -> exitMenu();
        }
    }

    private static void startOrder() {
        System.out.println("Välj vilken produkt du vill beställa: \n");
        String productName = getProductNameFromUser(false);
        System.out.println("Du har valt: " + productName);
        orderItem(productName);
    }

    private static void viewOrder() {
        System.out.println("Ordernummer: " + order.getId());
        List<LineItem> lineItems = Dao.lineItem.getLineItemsFromOrderId(order.getId());

        order.setLineItems(lineItems);
        order.getLineItems().forEach(System.out::println);
        menu();
    }

    private static void rateProduct() {
        System.out.println("Vilken produkt vill du betygsätta? \n");
        String productName = getProductNameFromUser(true);

        Optional<Item> optionalItem = Dao.item.getFromProductName(productName);
        if (optionalItem.isEmpty()) {
            System.out.println("Produkten finns inte. ");
        } else {
            List<Grade> allGrades = Dao.grade.getAll();
            System.out.println("Möjliga betyg:");
            for (int i = 1; i <= allGrades.size(); i++) {
                System.out.println(i + " " + allGrades.get(i-1).getGrade());
            }
            System.out.println("Vilket betyg vill du sätta på produkten? ");
            int choice = handleIntInput(allGrades.size());

            System.out.println("Skriv kommentar, eller enter för att skippa:");
            String comment = scanner.nextLine();
            while (comment != null && comment.length() >= 1000) {
                System.out.println("Max 1000 tecken. Försök igen: ");
                comment = scanner.nextLine();
            }
            Dao.item.rate(customer.getId(), optionalItem.get().getProductId(), allGrades.get(choice-1).getId(), comment);
        }
        menu();
    }

    private static void viewRatings() {
        System.out.println("Vilken produkt vill du se betyg på? \n");
        String productName = getProductNameFromUser(true);

        Optional<Item> optionalItem = Dao.item.getFromProductName(productName);
        if (optionalItem.isEmpty()) {
            System.out.println("Produkten finns inte. ");
        } else {
            System.out.printf("Snittbetyg för %s är %.2f. %n%n", productName,
                    Dao.item.getAverageGrade(optionalItem.get().getProductId()));
            for (String review: Dao.review.getAllReviewsWithComments(optionalItem.get().getProductId())) {
                System.out.println(review);
            }
        }
        menu();
    }

    private static void confirmPurchase() {
        if (order.getId() != null) {
            System.out.println("Köp bekräftat. Tack för ditt köp!");
            order = Order.create(customer);
        } else {
            System.out.println("Varukorgen är tom");
        }
        menu();
    }

    private static void exitMenu() {
        if (order.getId() != null) {
            System.out.println("Order tas bort.");
        }
        Dao.order.delete(order);
        System.exit(0);
    }

    private static String getProductNameFromUser(boolean acceptOutOfStock) {
        List<Item> inventory = Dao.item.getAll().stream()
                .filter(item -> item.getQuantity() > 0 || acceptOutOfStock)
                .collect(Collectors.toList());

        Map<String, List<Item>> itemNames = inventory.stream()
                .collect(Collectors.groupingBy(Item::getName));

        int counter = 1;
        //TODO: ENTRIES? Alternativt parsea strängen (split vid komma)
        Map<Integer, Map.Entry<String, List<Item>>> indexMap = new HashMap<>();
        for(var set: itemNames.entrySet()) {
            indexMap.put(counter, set);
            System.out.println(counter++ + ": " + set.getValue().get(0).getNameAndBrand());
        }

        System.out.println();
        return indexMap.get(handleIntInput(indexMap.size())).getKey();
    }

    private static void orderItem(String productName) {

        System.out.println("Order skapad");

        List<Item> availableItems = Dao.item.getItemListFromName(productName);
        Map<Integer, Item> indexedMap;
        if(availableItems.stream().anyMatch(item -> item.getColor()==null)) {
            indexedMap = Dao.item.getIndexedMap(availableItems);
        } else {
            System.out.println("Välj vilken färg du vill ha: ");
            var colorOptions = Dao.item.getColorOptions(availableItems);
            int counter = 1;
            Map<Integer, Map.Entry<Color, List<Item>>> integerEntryMap = new HashMap<>();
            for (var set : colorOptions.entrySet()) {
                integerEntryMap.put(counter, set);
                System.out.println(counter++ + ": " + set.getKey());
            }
            int chosenColor = handleIntInput(integerEntryMap.size());
            indexedMap = Dao.item.getIndexedMap(integerEntryMap.get(chosenColor).getValue());
        }
        indexedMap.forEach((key, value) -> System.out.println(key + ":" + value));
        Item orderedItem = indexedMap.get(handleIntInput(indexedMap.size()));

        System.out.println("Hur många vill du ha?");
        int choice = handleIntInput();
        while (choice > 0) {
            order.setId(OrderDao.addToCart(customer, order, orderedItem));
            choice--;
        }
        System.out.println(orderedItem + " har lagts till i varukorgen\n");
        System.out.println("Ditt beställningsnummer är " + order.getId());

        chooseToContinue();
    }



    public static int handleIntInput() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Det är inte ett giltigt nummer. Försök igen:");
            }
        }
    }

    public static int handleIntInput(int maxAllowedNumber) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (input <= 0 || input > maxAllowedNumber) {
                    System.out.println("Bara siffror mellan 1 och " +  input + " är tillåtna. Försök igen.");
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.out.println("Det är inte ett giltigt nummer. Försök igen:");
            }
        }
    }

    public static int handleIntInput(String string) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException e) {
                System.out.println("Det är inte ett giltigt nummer. Försök igen:");
                return handleIntInput(scanner.nextLine().trim());
        }
    }


    public static void chooseToContinue() {

        var selection = getString("Vill du beställa mer? (j/n): ");
        while (!selection.equalsIgnoreCase("j") && !selection.equalsIgnoreCase("n")) {
            System.out.println("Ogiltigt val!");
          selection = getString("Vill du beställa mer? (j/n): ");
        }
        if (selection.equalsIgnoreCase("j")) {
            startOrder();
        } else {
            menu();
        }
    }

    public static String getString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
