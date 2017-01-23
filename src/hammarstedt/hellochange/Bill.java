package hammarstedt.hellochange;

public enum Bill {
    TWENTY(20), TEN(10), FIVE(5), TWO(2), ONE(1);
    public final int value;

    Bill(int cashValue){
            value = cashValue;
        }
}
