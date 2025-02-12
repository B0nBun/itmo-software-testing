package my.beloved.subject.bar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import my.beloved.subject.bar.Person.DrinkNotFoundException;

public class PersonTest {
    @Test
    public void drinkingRaisesIntoxication() {
        var person = new Person("person");
        person.buyDrink(Drink.WHISKEY);
        person.drink(Drink.WHISKEY);
        Assertions.assertEquals(person.getIntoxication(), 1);
    }

    @Test
    public void drinkingNothingFails() {
        var person = new Person("person");
        Assertions.assertThrows(DrinkNotFoundException.class, () -> person.drink(Drink.WHISKEY));
        person.buyDrink(Drink.WHISKEY);
        Assertions.assertThrows(DrinkNotFoundException.class, () -> person.drink(Drink.BEER));
    }

    @Test
    public void givingDrinkTransfersIt() {
        var person1 = new Person("Person1");
        var person2 = new Person("Person2");
        person1.buyDrink(Drink.WHISKEY);
        person1.giveDrink(Drink.WHISKEY, person2);
        person2.drink(Drink.WHISKEY);
        Assertions.assertThrows(DrinkNotFoundException.class, () -> person1.drink(Drink.WHISKEY));
    }

    @Test
    public void givingNonExistingDrinkFails() {
        var person1 = new Person("Person1");
        var person2 = new Person("Person2");
        Assertions.assertThrows(DrinkNotFoundException.class, () -> person1.giveDrink(Drink.WHISKEY, person2));
        person1.buyDrink(Drink.WHISKEY);
        Assertions.assertThrows(DrinkNotFoundException.class, () -> person1.giveDrink(Drink.BEER, person2));
    }
}