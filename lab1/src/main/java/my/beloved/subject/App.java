package my.beloved.subject;

import java.util.Map;

public class App {
    public static void main(String[] args) {
        var ford = new Person("Ford", Drink.WHISKEY.price * 3, 0.5f, Map.of(
            Sound.RUMBLE, Reaction.ANNOYANCE
        ));
        var arthur = new Person("Arthur", Drink.BEER.price * 3, 2f, Map.of(
            Sound.RUMBLE, Reaction.SHOCK
        ));
        var stranger = new Person("Stranger", 0, 0.75f, Map.of());

        ford.buy(Drink.WHISKEY);
        ford.buy(Drink.WHISKEY);
        ford.drink(Drink.WHISKEY);
        ford.drink(Drink.WHISKEY);

        for (int i = 0; i < 3; i ++) {
            arthur.buy(Drink.BEER);
            arthur.drink(Drink.BEER);
        }

        ford.buy(Drink.WHISKEY);
        ford.give(stranger, Drink.WHISKEY);
        stranger.drink(Drink.WHISKEY);
    }
}
