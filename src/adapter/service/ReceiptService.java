package adapter.service;

import domain.constant.ReceiptConstant;
import domain.entity.Item;
import domain.entity.Location;
import domain.entity.Receipt;
import domain.service.IReceiptService;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReceiptService implements IReceiptService {
    public Receipt decodeInput(String input) {
        Receipt receipt = new Receipt();

        String[] inputs = input.split(",");
        for (String i : inputs) {
            try{
                if (i.contains("Location")) {
                    String location = i.split(":")[1].trim();
                    switch (location) {
                        case "CA":
                            receipt.setLocation(Location.CA);
                            break;
                        case "NY":
                            receipt.setLocation(Location.NY);
                            break;
                    }
                } else if (i.contains("at")) {
                    String[] item = i.trim().split(" ");

                    int qty = Integer.parseInt(item[0]);

                    String[] name = new String[0];
                    for (int j = 1; j < item.length - 2; j++) {
                        name = Arrays.copyOfRange(item, 1, item.length - 2);
                    }

                    float price = Float.parseFloat(item[item.length - 1]);

                    receipt.addItem(new Item(String.join(" ", name), price, qty));
                }
            } catch (Exception e){
                System.out.println("Invalid input");
            }
        }

        return receipt;
    }

    public void calculateTotal(Receipt receipt) {
        float taxRate = 0;
        String[] exemptList = new String[0];

        switch (receipt.getLocation()) {
            case CA:
                taxRate = ReceiptConstant.CA_SALES_TAX_RATE;
                exemptList = Stream.of(ReceiptConstant.FOOD_LIST).toArray(String[]::new);
                break;
            case NY:
                taxRate = ReceiptConstant.NY_SALES_TAX_RATE;
                exemptList = Stream.of(ReceiptConstant.FOOD_LIST, ReceiptConstant.CLOTHING_LIST).flatMap(Stream::of).toArray(String[]::new);
                break;
        }

        float subtotal = 0;
        float tax = 0;

        for (Item i : receipt.getItems()) {
            float price = i.getPrice();
            int qty = i.getQuantity();
            float itemTotal = price * qty;
            subtotal += itemTotal;

            if (Arrays.stream(exemptList).anyMatch(i.getName()::contains)) {
                tax += 0;
            } else {
                tax += (float) (Math.ceil(itemTotal * taxRate * 20) / 20);
            }
        }

        receipt.setSubtotal(subtotal);
        receipt.setTax(tax);
        receipt.setTotal(subtotal + tax);
    }

    public void printReceipt(Receipt receipt) {
        final Object[][] table = new String[5 + receipt.getItems().length][];

        table[0] = new String[]{"item", "price", "qty"};
        table[1] = new String[]{"", "", ""};

        for (int i = 0; i < receipt.getItems().length; i++) {
            table[i + 2] = new String[]{receipt.getItems()[i].getName(), "$" + receipt.getItems()[i].getPrice(), Integer.toString(receipt.getItems()[i].getQuantity())};
        }

        table[receipt.getItems().length + 2] = new String[]{"subtotal:", "", "$" + String.format("%.2f", receipt.getSubtotal())};
        table[receipt.getItems().length + 3] = new String[]{"tax:", "", "$" + String.format("%.2f", receipt.getTax())};
        table[receipt.getItems().length + 4] = new String[]{"total:", "", "$" + String.format("%.2f", receipt.getTotal())};

        for (final Object[] row : table) {
            System.out.format("%-20s%10s%15s%n", row);
        }
    }
}
