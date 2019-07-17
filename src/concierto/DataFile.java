package concierto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class DataFile {

    SimpleDateFormat format = new SimpleDateFormat("h:mm a MMMM d yyyy");
    DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("h:mm a MMMM d yyyy");
    ArrayList<Band> Bands;
    ArrayList<User> Users;
    ArrayList<Concert> Concerts;
    ArrayList<Ticket> Tickets;
    ArrayList<Venue> Venues;
    ArrayList<String> Seats;
    private static final String TEMPLATE = "♥♦♣♠";
    private static final String[] filename = {
        "Band",
        "User",
        "Concert",
        "Ticket",
        "Venue"
    };
    private static final String[] filehead = {
        "ID♥♦♣♠Band♥♦♣♠Fee\n",
        "ID♥♦♣♠Username♥♦♣♠Password♥♦♣♠Type\n",
        "ID♥♦♣♠Event♥♦♣♠Date♥♦♣♠Venue♥♦♣♠Price♥♦♣♠L1♥♦♣♠R1♥♦♣♠C1♥♦♣♠B1♥♦♣♠L2♥♦♣♠R2♥♦♣♠C2♥♦♣♠B2♥♦♣♠L3♥♦♣♠R3♥♦♣♠C3♥♦♣♠B3♥♦♣♠L4♥♦♣♠R4♥♦♣♠C4♥♦♣♠B4♥♦♣♠L5♥♦♣♠R5♥♦♣♠C5♥♦♣♠B5♥♦♣♠L6♥♦♣♠R6♥♦♣♠C6♥♦♣♠B6♥♦♣♠L7♥♦♣♠R7♥♦♣♠C7♥♦♣♠B7♥♦♣♠L8♥♦♣♠R8♥♦♣♠C8♥♦♣♠B8♥♦♣♠L9♥♦♣♠R9♥♦♣♠C9♥♦♣♠B9♥♦♣♠L10♥♦♣♠R10♥♦♣♠C10♥♦♣♠B10♥♦♣♠L11♥♦♣♠R11♥♦♣♠C11♥♦♣♠B11♥♦♣♠L12♥♦♣♠R12♥♦♣♠C12♥♦♣♠B12♥♦♣♠L13♥♦♣♠R13♥♦♣♠C13♥♦♣♠B13♥♦♣♠L14♥♦♣♠R14♥♦♣♠C14♥♦♣♠B14♥♦♣♠L15♥♦♣♠R15♥♦♣♠C15♥♦♣♠B15♥♦♣♠L16♥♦♣♠R16♥♦♣♠C16♥♦♣♠B16♥♦♣♠L17♥♦♣♠R17♥♦♣♠C17♥♦♣♠B17♥♦♣♠L18♥♦♣♠R18♥♦♣♠C18♥♦♣♠B18♥♦♣♠L19♥♦♣♠R19♥♦♣♠C19♥♦♣♠B19♥♦♣♠L20♥♦♣♠R20♥♦♣♠C20♥♦♣♠B20♥♦♣♠Performers\n",
        "ID♥♦♣♠Ticket♥♦♣♠Owner\n",
        "ID♥♦♣♠Venue♥♦♣♠Fee\n"
    };

    DataFile(ArrayList<Band> b, ArrayList<User> u, ArrayList<Concert> c, ArrayList<Ticket> t, ArrayList<Venue> v) {
        Bands = b;
        Users = u;
        Concerts = c;
        Tickets = t;
        Venues = v;
        Seater();
    }

    void Read(String line, String file) throws FileNotFoundException, IOException {
        if(file.equals("Files")) for(int i=0; i<5; i++) Read(line,filename[i]);
        else {
            try (BufferedReader reader = new BufferedReader(new FileReader(new File("src\\"+Concierto.class.getSimpleName()+"\\"+file+".dat").getAbsolutePath()))) {
                reader.readLine();
                switch (file) {
                    case "Band":
                        Bands.clear();
                        while ((line = reader.readLine()) != null) Bands.add(new Band(line.split(TEMPLATE)));
                        break;
                    case "User":
                        Users.clear();
                        while ((line = reader.readLine()) != null) Users.add(new User(line.split(TEMPLATE)));
                        break;
                    case "Concert":
                        Concerts.clear();
                        while ((line = reader.readLine()) != null) Concerts.add(new Concert(line.split(TEMPLATE)));
                        break;
                    case "Ticket":
                        Tickets.clear();
                        while ((line = reader.readLine()) != null) Tickets.add(new Ticket(line.split(TEMPLATE)));
                        break;
                    case "Venue":
                        Venues.clear();
                        while ((line = reader.readLine()) != null) Venues.add(new Venue(line.split(TEMPLATE)));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    void Save(String file) throws IOException {
        if(file.equals("Files")) for(int i=0; i<5; i++) Save(filename[i]);
        else {
            new File("src\\"+Concierto.class.getSimpleName()+"\\"+file+".dat").delete();
            try (BufferedWriter w = new BufferedWriter(new FileWriter(new File("src\\"+Concierto.class.getSimpleName()+"\\"+file+".dat").getAbsolutePath()))) {
                for(int i=0; i<filename.length; i++) if(file.equals(filename[i])) w.append(filehead[i]);
                switch(file) {
                    case "Band":
                        for (Band b : Bands) {
                            w.append(b.ID+TEMPLATE);
                            w.append(b.name+TEMPLATE);
                            w.append(Double.toString(b.fee));
                            w.append("\n");
                        }
                        break;
                    case "User":
                        for (User u : Users) {
                            w.append(u.ID+TEMPLATE);
                            w.append(u.name+TEMPLATE);
                            w.append(u.pass+TEMPLATE);
                            w.append(u.type);
                            w.append("\n");
                        }
                        break;
                    case "Concert":
                        for (Concert c : Concerts) {
                            w.append(c.ID+TEMPLATE);
                            w.append(c.name+TEMPLATE);
                            w.append(c.date+TEMPLATE);
                            w.append(c.venue+TEMPLATE);
                            w.append(c.price+TEMPLATE);
                            for (int i=0; i<80; i++) w.append(c.seats.get(i)+TEMPLATE);
                            for (int i=0; i<c.performers.size(); i++) {
                                if (i==c.performers.size()-1) w.append(c.performers.get(i));
                                else w.append(c.performers.get(i)+TEMPLATE);
                            }
                            w.append("\n");
                        }
                        break;
                    case "Ticket":
                        for (Ticket t : Tickets) {
                            w.append(t.ID+TEMPLATE);
                            w.append(t.seat+TEMPLATE);
                            w.append(t.owner+TEMPLATE);
                            w.append(t.event);
                            w.append("\n");
                        }
                        break;
                    case "Venue":
                        for (Venue v : Venues) {
                            w.append(v.ID+TEMPLATE);
                            w.append(v.name+TEMPLATE);
                            w.append(Double.toString(v.fee));
                            w.append("\n");
                        }
                        break;
                }
            }
            Read("",file);
        }
    }

    Band FindBand(String via, String token) {
        for (Band b : Bands) {
            switch (via) {
                case "ID":
                    if (b.ID.equals(token)) return b;
                    break;
                case "name":
                    if (b.name.equals(token)) return b;
                    break;
                case "fee":
                    if (b.fee.equals(Double.parseDouble(token))) return b;
                    break;
            }
        }
        return null;
    }
    User FindUser(String via, String token) {
        for (User u : Users) {
            switch (via) {
                case "ID":
                    if (u.ID.equals(token)) return u;
                    break;
                case "name":
                    if (u.name.equals(token)) return u;
                    break;
                case "fee":
                    if (u.pass.equals(token)) return u;
                    break;
                case "event":
                    if (u.type.equals(token)) return u;
                    break;
            }
        }
        return null;
    }
    Concert FindConcert(String via, String token) {
        for (Concert c : Concerts) {
            switch (via) {
                case "ID":
                    if (c.ID.equals(token)) return c;
                    break;
                case "name":
                    if (c.name.equals(token)) return c;
                    break;
                case "date":
                    if (c.date.equals(token)) return c;
                    break;
                case "venue":
                    if (c.venue.equals(token)) return c;
                    break;
                case "price":
                    if (c.price.equals(Double.parseDouble(token))) return c;
                    break;
            }
        }
        return null;
    }
    Ticket FindTicket(String via, String token) {
        for (Ticket t : Tickets) {
            switch (via) {
                case "ID":
                    if (t.ID.equals(token)) return t;
                    break;
                case "name":
                    if (t.seat.equals(token)) return t;
                    break;
                case "fee":
                    if (t.owner.equals(Double.parseDouble(token))) return t;
                    break;
            }
        }
        return null;
    }
    Venue FindVenue(String via, String token) {
        for (Venue v : Venues) {
            switch (via) {
                case "ID":
                    if (v.ID.equals(token)) return v;
                    break;
                case "name":
                    if (v.name.equals(token)) return v;
                    break;
                case "fee":
                    if (v.fee.equals(Double.parseDouble(token))) return v;
                    break;
            }
        }
        return null;
    }
    
    void TakeSeat(Concert c, String s, String a) {
        for (int i=0; i<80; i++) if (s.equals(c.seats.get(i))) c.seats.set(i, a);
    }
    private ArrayList<String> Seater() { 
        Seats = new ArrayList<>();
        for (int i=1; i<21; i++) {
            Seats.add("L"+i);
            Seats.add("R"+i);
            Seats.add("C"+i);
            Seats.add("B"+i);
        }
        return Seats;
    }

    String makeID(String array) {
        String ID = "";
        String a = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        for(int i=0; i<16; i++) {
            if (i>0 && i%4==0) ID=ID+" "+a.charAt(Math.abs((new Random().nextInt(36))));
            else ID=ID+a.charAt(Math.abs((new Random().nextInt(36))));
            
        }
        switch (array) {
            case "Band":
                for (Band b : Bands) if(b.ID.equals(ID)) makeID(array);
                break;
            case "User":
                for (User u : Users) if(u.ID.equals(ID)) makeID(array);
                break;
            case "Concert":
                for (Concert c : Concerts) if(c.ID.equals(ID)) makeID(array);
                break;
            case "Ticket":
                for (Ticket t : Tickets) if(t.ID.equals(ID)) makeID(array);
                break;
            case "Venue":
                for (Venue v : Venues) if(v.ID.equals(ID)) makeID(array);
                break;
            default:
                break;
        }
        return ID;
    }
    String[] toWhen(String date) {
        String[] data = date.split(" ");
        String[] token = {
            data[0]+" "+data[1],
            data[2],
            data[3],
            data[4],
        };
        return token;
    }
    String toDate(String time, String month, String day, String year) {
        return time+" "+month+" "+day+" "+year;
    }
    boolean dater(String s) throws ParseException {
        Date sched = format.parse(s);
        Date now = format.parse(dtformat.format(LocalDateTime.now()));
        return sched.after(now);
    }
}