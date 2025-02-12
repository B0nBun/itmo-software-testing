package my.beloved.subject;

import java.util.ArrayList;

public class Person {
    public final String name;
    int intoxication = 0;
    ArrayList<Drink> hasDrinks = new ArrayList<>();

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


    public static class Ford extends Person {
        public Ford() {
            super("Ford");
        }

        @Override
        public void reactTo(Event event) {
            super.reactTo(event);
            if (event == Event.NoPayload.MUSIC) {
                this.buyDrink(Drink.WHISKEY);
                this.drink(Drink.WHISKEY);
                return;
            }
            if (this.intoxication >= 1 && event instanceof Event.AskedForDrink) {
                var askedForDrink = (Event.AskedForDrink)event;
                Person otherClient = askedForDrink.asker();
                Drink drink = askedForDrink.drink();
                this.buyDrink(drink);
                this.giveDrink(drink, otherClient);
                return;
            }
            if (event == Event.NoPayload.RUMBLE) {
                System.out.println("Ford is annoyed");
            }
        }
    }

    public static class Arthur extends Person {
        public Arthur() {
            super("Arthur");
        }

        @Override
        public void reactTo(Event event) {
            super.reactTo(event);
            if (event == Event.NoPayload.MUSIC || event == Event.NoPayload.CHATTER) {
                this.buyDrink(Drink.BEER);
                this.drink(Drink.BEER);
                return;
            }
            if (event == Event.NoPayload.RUMBLE) {
                System.out.println("Arthur soberd up from shock");
                this.intoxication = 0;
            }
        }
    }

    public static class Stranger extends Person {
        public Stranger() {
            super("Stranger");
            this.intoxication = 5;
        }

        @Override
        public void reactTo(Event event) {
            super.reactTo(event);
            // Doesn't care about anything, just drinks
            while (!this.hasDrinks.isEmpty()) {
                this.drink(this.hasDrinks.get(0));
            }
        }
    }

    public static class DrinkNotFoundException extends RuntimeException {
        public DrinkNotFoundException() {}
    }
}
