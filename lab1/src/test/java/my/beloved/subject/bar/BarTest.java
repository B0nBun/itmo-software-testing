package my.beloved.subject.bar;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BarTest {
    @Test
    public void eventEmittion() {
        var counter1 = new Counter();
        var counter2 = new Counter();

        var bar = new Bar(List.of(counter1, counter2));
        bar.emit(Event.NoPayload.CHATTER);
        bar.emit(Event.NoPayload.CHATTER);

        Assertions.assertEquals(2, counter1.counter);
        Assertions.assertEquals(2, counter2.counter);
    }

    @Test
    public void clientsAreCopied() {
        var counter1 = new Counter();
        var counter2 = new Counter();
        List<Person> clients = new ArrayList<>(List.of(counter1, counter2));

        var bar = new Bar(clients);
        clients.remove(0);
        bar.emit(Event.NoPayload.CHATTER);

        Assertions.assertEquals(1, counter1.counter);
        Assertions.assertEquals(1, counter2.counter);
    }
}

class Counter extends Person {
    public int counter = 0;
    
    public Counter() {
        super("counter");
    }

    @Override
    public void reactTo(Event event) {
        this.counter += 1;
    }
}
