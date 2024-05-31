package domain.service;

import domain.entity.Receipt;

public interface IReceiptService {
    Receipt decodeInput(String input);
    void calculateTotal(Receipt receipt);
    void printReceipt(Receipt receipt);
}
