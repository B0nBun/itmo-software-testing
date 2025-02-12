package my.beloved.subject;

import java.util.List;

import my.beloved.subject.bar.Bar;
import my.beloved.subject.bar.Drink;
import my.beloved.subject.bar.Event;
import my.beloved.subject.bar.Person;

public class App {
    public static void main(String[] args) {
        var ford = new Ford();
        var arthur = new Arthur();
        var stranger = new Stranger();

        var bar = new Bar(List.of(ford, arthur, stranger));
        ford.reactTo(new Event.AskedForDrink(Drink.WHISKEY, stranger));
        bar.emit(Event.NoPayload.MUSIC);
        bar.emit(Event.NoPayload.CHATTER);
        ford.reactTo(new Event.AskedForDrink(Drink.WHISKEY, stranger));
        bar.emit(Event.NoPayload.RUMBLE);
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
}
