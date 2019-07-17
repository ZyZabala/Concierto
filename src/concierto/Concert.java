package concierto;

import java.util.ArrayList;

class Concert {

    String ID;
    String name;
    String date;
    String venue;
    Double price;
    ArrayList<String> seats = new ArrayList<>();
    ArrayList<String> performers = new ArrayList<>();

    Concert(String[] data) {
        ID = data[0];
        name = data[1];
        date = data[2];
        venue = data[3];
        price = Double.parseDouble(data[4]);
        for (int i=5; i<85; i++) seats.add(data[i]);
        for (int i=85; i<data.length; i++) performers.add(data[i]);
    }
    
    Concert(String id, String n, ArrayList<String> s) {
        ID = id;
        name = n;
        date = "";
        venue = "";
        price = 0.0;
        seats = s;
    }
}