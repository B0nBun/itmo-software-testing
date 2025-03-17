package my.beloved.subject;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import my.beloved.subject.bar.Drink;
import my.beloved.subject.bar.Event;
import my.beloved.subject.bar.Person;

public class AppTest {

    @Test
    public void playScene() {
        var ford = new MockPerson("Ford");
        var arthur = new MockPerson("Arthur");
        var stranger = new MockPerson("Stranger");
    
        App.playScene(ford, arthur, stranger);

        Assertions.assertIterableEquals(
            List.of(
                new Event.AskedForDrink(Drink.WHISKEY, stranger),
                Event.NoPayload.MUSIC,
                Event.NoPayload.CHATTER,
                new Event.AskedForDrink(Drink.WHISKEY, stranger),
                Event.NoPayload.RUMBLE
            ),
            ford.recordedEvents
        );

        Assertions.assertIterableEquals(
            List.of(
                Event.NoPayload.MUSIC,
                Event.NoPayload.CHATTER,
                Event.NoPayload.RUMBLE
            ),
            arthur.recordedEvents
        );

        Assertions.assertIterableEquals(
            List.of(
                Event.NoPayload.MUSIC,
                Event.NoPayload.CHATTER,
                Event.NoPayload.RUMBLE
            ),
            stranger.recordedEvents
        );
    }

    @Nested
    class FordTest {
        @Test
        public void drinksWhenMusicPlays() {
            var ford = new App.Ford();
            Assertions.assertEquals(0, ford.getIntoxication());
            ford.reactTo(Event.NoPayload.MUSIC);
            Assertions.assertEquals(1, ford.getIntoxication());
        }

        @Test
        public void generousWhenDrunk() {
            var ford = new App.Ford();
            var person = new Person("person");
            ford.reactTo(new Event.AskedForDrink(Drink.WHISKEY, person));
            Assertions.assertFalse(person.hasDrink(Drink.WHISKEY));
            ford.buyDrink(Drink.WHISKEY);
            ford.drink(Drink.WHISKEY);
            ford.reactTo(new Event.AskedForDrink(Drink.WHISKEY, person));
            Assertions.assertTrue(person.hasDrink(Drink.WHISKEY));
        }
    }

    @Nested
    class Arthur {
        @Test
        public void drinksAfterMusicOrChatter() {
            var arthur = new App.Arthur();
            Assertions.assertEquals(0, arthur.getIntoxication());
            arthur.reactTo(Event.NoPayload.MUSIC);
            Assertions.assertEquals(1, arthur.getIntoxication());    
            arthur.reactTo(Event.NoPayload.CHATTER);
            Assertions.assertEquals(2, arthur.getIntoxication());    
        }

        @Test
        public void sobersUpWhenHearsRumble() {
            var arthur = new App.Arthur();
            arthur.buyDrink(Drink.BEER);    
            arthur.drink(Drink.BEER);
            Assertions.assertEquals(1, arthur.getIntoxication());    
            arthur.reactTo(Event.NoPayload.RUMBLE);
            Assertions.assertEquals(0, arthur.getIntoxication());    
        }
    }

    @Nested
    class Stranger {
        @Test
        public void initiallyDrunk() {
            var stranger = new App.Stranger();
            
            stranger.reactTo(Event.NoPayload.MUSIC);
            Assertions.assertEquals(5, stranger.getIntoxication());
        }

        @Test
        public void drinksEverythingWhenAnythingHappens() {
            var stranger = new App.Stranger();
            
            stranger.reactTo(Event.NoPayload.MUSIC);
            Assertions.assertEquals(5, stranger.getIntoxication());
            
            stranger.buyDrink(Drink.WHISKEY);
            stranger.buyDrink(Drink.BEER);
            stranger.buyDrink(Drink.WHISKEY);
            stranger.reactTo(Event.NoPayload.RUMBLE);
            Assertions.assertEquals(8, stranger.getIntoxication());
            
            stranger.buyDrink(Drink.WHISKEY);
            stranger.reactTo(Event.NoPayload.CHATTER);
            Assertions.assertEquals(9, stranger.getIntoxication());
        }
    }
}

class MockPerson extends Person {
    public List<Event> recordedEvents = new ArrayList<>();
    
    public MockPerson(String name) {
        super(name);
    }

    @Override
    public void reactTo(Event event) {
        this.recordedEvents.add(event);
    }
}