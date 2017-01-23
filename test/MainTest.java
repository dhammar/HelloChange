import hammarstedt.hellochange.Bill;
import hammarstedt.hellochange.Register;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MainTest {

    private Register testReg;
    private HashMap<Bill, Integer> bills;

    @BeforeEach
    public void init() {
        testReg = new Register();
        bills = new HashMap<>();

    }

    @Test
    public void initialShow() {
        assertEquals("$0 0 0 0 0 0", testReg.show());
    }

    @Test
    public void putTest() {
        bills.put(Bill.ONE, 1);
        bills.put(Bill.TWO, 2);
        bills.put(Bill.FIVE, 3);
        bills.put(Bill.TEN, 4);
        bills.put(Bill.TWENTY, 5);
        assertEquals("$160 5 4 3 2 1", testReg.put(bills));
    }

    @Test
    public void ignoreNegativePut() {
        bills.put(Bill.FIVE, -3);
        assertEquals("sorry", testReg.put(bills));
    }

    @Test
    public void takeTest() {
        bills.put(Bill.ONE, 1);
        bills.put(Bill.TWO, 2);
        bills.put(Bill.FIVE, 3);
        testReg.put(bills);
        bills.replace(Bill.FIVE, 2);
        assertEquals("$5 0 0 1 0 0", testReg.take(bills));
    }

    @Test
    public void ignoreNegativeTakeTest() {
        bills.put(Bill.TWENTY, 3);
        testReg.put(bills);
        bills.replace(Bill.TWENTY, -2);
        assertEquals("sorry", testReg.take(bills));
    }

    @Test
    public void basicChangeTest() {
        bills.put(Bill.TWENTY, 3);
        testReg.put(bills);
        assertEquals("1 0 0 0 0",testReg.change(20));
    }

    @Test
    public void changeTest() {
        bills.put(Bill.TWENTY, 1);
        bills.put(Bill.FIVE, 3);
        bills.put(Bill.TWO, 4);
        testReg.put(bills);
        assertEquals("0 0 1 3 0", testReg.change(11));
    }

    @Test
    public void complexChangeTest() {
        bills.put(Bill.TWENTY, 20);
        bills.put(Bill.TEN, 10);
        bills.put(Bill.FIVE, 20);
        bills.put(Bill.TWO, 10);
        bills.put(Bill.ONE, 10);
        testReg.put(bills);
        assertEquals("10 1 1 1 1", testReg.change(218));
    }

    @Test
    public void noPossibleCombinationChangeTest() {
        bills.put(Bill.TWENTY, 1);
        bills.put(Bill.FIVE, 1);
        bills.put(Bill.TWO, 4);
        testReg.put(bills);
        assertEquals("sorry", testReg.change(21));
    }

    @Test
    public void registerContentsAfterChangeTest() {
        bills.put(Bill.TWENTY, 1);
        bills.put(Bill.FIVE, 3);
        bills.put(Bill.TWO, 4);
        testReg.put(bills);
        testReg.change(11);
        assertEquals("$32 1 0 2 1 0", testReg.show());
    }



}
