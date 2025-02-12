package my.beloved.subject;

import java.util.List;

public class App {
    public static void main(String[] args) {
       var ford = new Person.Ford();
       var arthur = new Person.Arthur();
       var stranger = new Person.Stranger();

       var bar = new Bar(List.of(ford, arthur, stranger));
       ford.reactTo(new Event.AskedForDrink(Drink.WHISKEY, stranger));
       bar.emit(Event.NoPayload.MUSIC);
       bar.emit(Event.NoPayload.CHATTER);
       ford.reactTo(new Event.AskedForDrink(Drink.WHISKEY, stranger));
       bar.emit(Event.NoPayload.RUMBLE);
    }
}

