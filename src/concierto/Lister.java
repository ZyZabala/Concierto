package concierto;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
      
class Lister extends StackPane {
    Band B;
    User U;
    Concert C;
    Ticket T;
    Venue V;
    ArrayList<SmallButton> data = new ArrayList<>();
    ScrollPane window;
    VBox list;
    VBox wall;
    HBox head;
    SmallButton back;
    SmallButton what;
    SmallHeader title;
    DataFile F;
    String[] S;
    Lister(Double L, Double H, DataFile File, String Type, String Name) { 
        // Init Panels
        wall = new VBox();
        wall.setAlignment(Pos.TOP_CENTER);
        wall.setPrefHeight(H);
        wall.setPadding(new Insets(15,0,0,0));
        wall.setSpacing(50);
        head = new HBox();
        head.setAlignment(Pos.CENTER);
        head.setSpacing(15);
        list = new VBox();
        list.setAlignment(Pos.CENTER);
        list.setSpacing(15);
        window = new ScrollPane();
        window.setPrefSize(L/2+L/4+20, H-H/6);
        window.setMaxSize(L/2+L/4+20, H-H/6);
        window.setHvalue(0.5);
        window.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        window.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        window.setVvalue(1.0);
        window.setPannable(true);
        // Init Objects
        F = File;
        title = new SmallHeader(new Rectangle(L-H/6-60,H/12), Type, "Title");
        what = new SmallButton(new Rectangle(H/12,H/12), "?", "Title");
        back = new SmallButton(new Rectangle(H/12,H/12), "<", "Title");
        // Init Array
        switch (Type) {
            case "Upcoming Concerts":
                for (int i=0; i<F.Concerts.size(); i++) {
                    data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), F.Concerts.get(i).name, "Texter"));
                    list.getChildren().add(data.get(i));
                }   
                break;
            case "Performers":
                for (int i=0; i<F.FindConcert("name", Name).performers.size(); i++) {
                    data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), F.FindBand("ID", F.FindConcert("name", Name).performers.get(i)).name, "Texter"));
                    list.getChildren().add(data.get(i));
                }
                break;
            case "External Contacts":
                data.add(new SmallButton(new Rectangle(L/2+L/4,H/3), "Bands","Header"));
                data.add(new SmallButton(new Rectangle(L/2+L/4,H/3), "Venues","Header"));
                for (SmallButton b : data) list.getChildren().add(b);
                break;
            case "Bands":
                for (int i=0; i<F.Bands.size(); i++) {
                    data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), F.Bands.get(i).name, "Texter"));
                    list.getChildren().add(data.get(i));
                }
                break;
            case "Venues":
                for (int i=0; i<F.Venues.size(); i++) {
                    data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), F.Venues.get(i).name, "Texter"));
                    list.getChildren().add(data.get(i));
                }
                break;
            case "Administrative Privileges":
                data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), "Pending Applications","Header"));
                data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), "Password Reset Requests","Header"));
                data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), "My Ticket Gallery","Header"));
                data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), "Retire","Header"));
                for (SmallButton b : data) list.getChildren().add(b);
                break;
            case "Pending Applications":
                for (User u : F.Users) {
                    if (u.type.equals("Applicant")) {
                        data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), "Review Applicant "+u.ID, "Texter"));
                        list.getChildren().add(data.get(data.size()-1));
                    }
                }
                break;

            case "Password Reset Requests":
                for (User u : F.Users) {
                    S = u.type.split(" ");
                    if (S[0].equals("Forgot")) {
                        data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), "Review Request by "+S[1]+" "+u.ID, "Texter"));
                        list.getChildren().add(data.get(data.size()-1));
                    }
                }
                break;
            case "My Tickets":
                for (Ticket t : F.Tickets) {
                    if (t.owner.equals(Name)) {
                        data.add(new SmallButton(new Rectangle(L/2+L/4,H/6), F.FindConcert("ID", t.event).name+"\t\tRef.No."+t.ID, "Texter"));
                        list.getChildren().add(data.get(data.size()-1));
                    }
                }
                break;
        }
        // Complain
        head.getChildren().addAll(back,title,what);
        window.setContent(list);
        wall.getChildren().addAll(head,window);
        getChildren().addAll(wall);
    }
}