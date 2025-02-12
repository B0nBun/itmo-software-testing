package my.beloved.subject.bar;

import java.util.ArrayList;
import java.util.List;

public class Bar {
    List<Person> clients;

    public Bar(List<Person> clients) {
        this.clients = new ArrayList<>(clients);
    }

    public void emit(Event event) {
        for (Person client : clients) {
            client.reactTo(event);
        }
    }
}
