package domain.usecase;

import adapter.service.ReceiptService;
import domain.service.IReceiptService;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PrintReceiptByInputTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        "Location: CA, 1 book at 17.99, 1 potato chips at 3.99",
                        "item                     price            qty\n" +
                                "                                             \n" +
                                "book                    $17.99              1\n" +
                                "potato chips             $3.99              1\n" +
                                "subtotal:                              $21.98\n" +
                                "tax:                                    $1.80\n" +
                                "total:                                 $23.78\n"
                },
                {
                        "Location: NY, 1 book at 17.99, 3 pencils at 2.99",
                        "item                     price            qty\n" +
                                "                                             \n" +
                                "book                    $17.99              1\n" +
                                "pencils                  $2.99              3\n" +
                                "subtotal:                              $26.96\n" +
                                "tax:                                    $2.40\n" +
                                "total:                                 $29.36\n"
                },
                {
                        "Location: NY, 2 pencils at 2.99, 1 shirt at 29.99",
                        "item                     price            qty\n" +
                                "                                             \n" +
                                "pencils                  $2.99              2\n" +
                                "shirt                   $29.99              1\n" +
                                "subtotal:                              $35.97\n" +
                                "tax:                                    $0.55\n" +
                                "total:                                 $36.52\n"
                }
        });
    }

    @Parameterized.Parameter
    public String fInput;

    @Parameterized.Parameter(1)
    public String fExpected;


    private ByteArrayOutputStream testOut;

    private IReceiptService receiptService;

    @Before
    public void setUpStream() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @Before
    public void setUpDependency() {
        receiptService = new ReceiptService();
    }

    @Test
    public void testPrintReceiptByInput() {
        ReceiptUsecase.printReceiptByInput(receiptService, fInput);
        assertEquals(fExpected, testOut.toString());
    }
}
