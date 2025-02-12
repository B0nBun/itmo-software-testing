package my.beloved.subject.bar;

import java.util.ArrayList;

public class Person {
    public final String name;
    protected int intoxication = 0;
    protected ArrayList<Drink> hasDrinks = new ArrayList<>();

    public Person(String name) {
        this.name = name;
    }

    public void reactTo(Event event) {
        System.out.println(event.description(this));
    };

    public void buyDrink(Drink drink) {
        System.out.println(this.name + " buys a " + drink);
        this.hasDrinks.add(drink);
    }

    public void drink(Drink drink) {
        boolean deleted = this.hasDrinks.remove(drink);
        if (!deleted) {
            throw new DrinkNotFoundException();
        }
        System.out.println(this.name + " drank a " + drink);
        this.intoxication += 1;
    }

    public void giveDrink(Drink drink, Person reciever) {
        boolean deleted = this.hasDrinks.remove(drink);
        if (!deleted) {
            throw new DrinkNotFoundException();
        }
        System.out.println(this.name + " gave a " + drink + " to " + reciever.name);
        reciever.hasDrinks.add(drink);
    }

    public int getIntoxication() {
        return this.intoxication;
    }

    public boolean hasDrink(Drink drink) {
        return this.hasDrinks.contains(drink);
    }

    public static class DrinkNotFoundException extends RuntimeException {
        public DrinkNotFoundException() {}
    }
}
