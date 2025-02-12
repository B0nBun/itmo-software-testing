package my.beloved.subject;

import java.util.List;

public class Bar {
    List<Person> clients;

    public Bar(List<Person> clients) {
        this.clients = clients;
    }

    public void emit(Event event) {
        for (Person client : clients) {
            client.reactTo(event);
        }
    }
}
