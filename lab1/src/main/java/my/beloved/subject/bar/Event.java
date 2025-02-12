package my.beloved.subject.bar;

public interface Event {
    public String description(Person reciever);
    
    public static record AskedForDrink(Drink drink, Person asker) implements Event {
        public String description(Person reciever) {
            return reciever.name + " was asked for a " + drink + " by " + asker.name;
        }
    }

    public static enum NoPayload implements Event {
        RUMBLE,
        MUSIC,
        CHATTER;

        public String description(Person reciever) {
            return reciever.name + " heard " + this;
        }
    }
}
