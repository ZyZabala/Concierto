package concierto;

class Ticket {
    
    String ID;
    String seat;
    String owner;
    String event;
    
    Ticket(String[] data) {
        ID = data[0];
        seat = data[1];
        owner = data[2];
        event = data[3];
    }
    
    Ticket(String i, String t, String o, String e) {
        ID = i;
        seat = t;
        owner = o;
        event = e;
    }
}
