package my.beloved.subject;

public enum Drink {
    BEER(10, 1),
    WHISKEY(30, 3);

    public int price;
    public int intoxication;

    Drink(int price, int intoxication) {
        this.price = price;
        this.intoxication = intoxication;
    }
}
