package concierto;

class Venue {
    
    String ID;
    String name;
    Double fee;
    
    Venue(String[] data) {
        ID = data[0];
        name = data[1];
        fee = Double.parseDouble(data[2]);
    }
    
    Venue(String i, String n, Double f) {
        ID = i;
        name = n;
        fee = f;
    }
}
