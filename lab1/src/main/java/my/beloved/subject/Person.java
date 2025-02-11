package my.beloved.subject;

import java.util.ArrayList;
import java.util.Map;


// enum Emotion
// enum Drink
// enum Stimuli

// class Person(String identoifier, int money, []Drink wantsDrinks, []Drink drinks, int intoxication)
//    addListener(TriFunction<Runnable next, intoxication, Stimuli, Emotion> func)

// class Bar([]Person people)
//     giveOut() Gives everyone a drink
//     emit(Stimuli) notifies every person
//

public class Person {
    public final String identifier;
    private int money;
    public final float susceptibility;
    private ArrayList<Drink> drinks = new ArrayList<>();
    private int intoxication = 0;
    private Map<Sound, Reaction> reactions;

    public Person(String identifier, int money, float susceptibility, Map<Sound, Reaction> reactions) {
        this.identifier = identifier;
        this.money = money;
        this.susceptibility = susceptibility;
        this.reactions = reactions;
    }

    public Reaction reactTo(Sound sound) {
        return this.reactions.getOrDefault(sound, Reaction.UNKNOWN);
    }

    public boolean canAfford(Drink drink) {
        return this.money > drink.price;
    }

    public void buy(Drink drink) {
        this.money -= drink.price;
        // TODO: Throw if no money
        this.drinks.add(drink);
    }

    public void drink(Drink drink) {
        // TODO: Throw if no drink
        this.intoxication += drink.intoxication * this.susceptibility;
        this.drinks.remove(drink);
    }

    public void give(Person person, Drink drink) {
        this.drinks.remove(drink);
        person.drinks.add(drink);
    }

    public int getIntoxicationDegree() {
        return this.intoxication;
    }
}
