package domain.usecase;

import domain.entity.Receipt;
import domain.service.IReceiptService;

public class ReceiptUsecase {
    public static void printReceiptByInput(IReceiptService receiptService, String input) {
        Receipt receipt = receiptService.decodeInput(input);
        receiptService.calculateTotal(receipt);
        receiptService.printReceipt(receipt);
    }
}
