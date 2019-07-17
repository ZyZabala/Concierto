package concierto;

class Band {

    String ID;
    String name;
    Double fee;

    Band(String[] data) {
        ID = data[0];
        name = data[1];
        fee = Double.parseDouble(data[2]);
    }

    Band(String i, String n, Double f) {
        ID = i;
        name = n;
        fee = f;
    }
}