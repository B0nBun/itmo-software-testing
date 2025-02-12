package my.beloved.subject.bar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import my.beloved.subject.bar.Person.DrinkNotFoundException;

public class PersonTest {
    @Test
    public void drinkingRaisesIntoxication() {
        var foo = new Foo();
        foo.buyDrink(Drink.WHISKEY);
        foo.drink(Drink.WHISKEY);
        Assertions.assertEquals(foo.getIntoxication(), 1);
    }

    @Test
    public void drinkingNothingFails() {
        var foo = new Foo();
        Assertions.assertThrows(DrinkNotFoundException.class, () -> foo.drink(Drink.WHISKEY));
        foo.buyDrink(Drink.WHISKEY);
        Assertions.assertThrows(DrinkNotFoundException.class, () -> foo.drink(Drink.BEER));
    }

    @Test
    public void givingDrinkTransfersIt() {
        var foo1 = new Foo();
        var foo2 = new Foo();
        foo1.buyDrink(Drink.WHISKEY);
        foo1.giveDrink(Drink.WHISKEY, foo2);
        foo2.drink(Drink.WHISKEY);
        Assertions.assertThrows(DrinkNotFoundException.class, () -> foo1.drink(Drink.WHISKEY));
    }

    @Test
    public void givingNonExistingDrinkFails() {
        var foo1 = new Foo();
        var foo2 = new Foo();
        Assertions.assertThrows(DrinkNotFoundException.class, () -> foo1.giveDrink(Drink.WHISKEY, foo2));
        foo1.buyDrink(Drink.WHISKEY);
        Assertions.assertThrows(DrinkNotFoundException.class, () -> foo1.giveDrink(Drink.BEER, foo2));
    }
}

class Foo extends Person {
    public Foo() {
        super("Foo");
    }

    public int getIntoxication() {
        return this.intoxication;
    }
}
