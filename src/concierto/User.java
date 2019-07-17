package concierto;

class User {
    
    String name;
    String pass;
    String ID;
    String type;
    
    User(String[] data) {
        ID = data[0];
        name = data[1];
        pass = data[2];
        type = data[3];
    }
    
    User(String i, String n, String p, String t) {
        ID = i;
        name = n;
        pass = p;
        type = t;
    }
}
