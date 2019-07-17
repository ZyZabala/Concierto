package concierto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

class Planner extends StackPane {
    SmallHeader title;
    SmallButton performers;
    SmallButton action;
    SmallButton back;
    SmallButton what;
    SmallButton seat;
    SmallButton time;
    SmallButton month;
    SmallButton day;
    SmallTexter year;
    SmallButton where;
    SmallButton paid;
    private final VBox wall = new VBox();
    private final VBox wall_left = new VBox();
    private final VBox wall_right = new VBox();
    private final HBox beam = new HBox();
    private final HBox when = new HBox();
    private final HBox head = new HBox();
    Concert event;
    User owner;
    private int taken;
    Planner(Double L, Double H, DataFile F, String name, String type, User U) {
        // Init Panels
        wall.setAlignment(Pos.TOP_CENTER);
        wall.setPadding(new Insets(15,0,0,0));
        wall.setPrefHeight(H);
        wall.setSpacing(80);
        wall_left.setAlignment(Pos.CENTER);
        wall_left.setSpacing(15);
        wall_right.setAlignment(Pos.CENTER);
        wall_right.setSpacing(15);
        beam.setAlignment(Pos.CENTER);
        beam.setSpacing(15);
        when.setAlignment(Pos.CENTER);
        when.setSpacing(15);
        head.setAlignment(Pos.CENTER);
        head.setSpacing(15);
        // Init Objects
        event = F.FindConcert("name", name);
        owner = U;
        taken = 80;
        title = new SmallHeader(new Rectangle(L-H/6-60,H/12), "", "Title");
        what = new SmallButton(new Rectangle(H/12,H/12), "?", "Title");
        back = new SmallButton(new Rectangle(H/12,H/12), "<", "Title");
        action = new SmallButton(new Rectangle(L-30,H/12), "", "Header");
        where = new SmallButton(new Rectangle(L/2-15,H/12), "", "Header");
        paid = new SmallButton(new Rectangle(L/2-15,H/12), "", "Header");
        performers = new SmallButton(new Rectangle(L/2-45,15+H/6), "Performers", "Header");
        seat = new SmallButton(new Rectangle(L/2-45,H/12), "", "Header");
        // When Objects
        time = new SmallButton(new Rectangle(L/8,H/12), "", "Texter");
        month = new SmallButton(new Rectangle(L/6,H/12), "", "Texter");
        day = new SmallButton(new Rectangle(L/20,H/12), "", "Texter");
        year = new SmallTexter(new Rectangle(L/9,H/12), "");
        // Compile
        when.getChildren().addAll(time,month,day,year);
        head.getChildren().addAll(back,title,what);
        wall_left.getChildren().addAll(when, where, paid);
        wall_right.getChildren().addAll(seat,performers);
        beam.getChildren().addAll(wall_left,wall_right);
        wall.getChildren().addAll(head,beam,action);
        getChildren().addAll(wall);
        // Controls Setup
        switch (type) {
            case "view":
                title.heading.setText(name);
                time.control.setDisable(true);
                time.control.setText(F.toWhen(event.date)[0]);
                month.control.setDisable(true);
                month.control.setText(F.toWhen(event.date)[1]);
                day.control.setDisable(true);
                day.control.setText(F.toWhen(event.date)[2]);
                year.input.setEditable(false);
                year.input.setText(F.toWhen(event.date)[3]);
                where.control.setDisable(true);
                where.control.setText(F.FindVenue("ID", event.venue).name);
                paid.control.setDisable(true);
                paid.control.setText("Php "+Double.toString(event.price));
                for (String s : event.seats) if (s.length()==19) taken--;
                seat.control.setText(Integer.toString(taken)+" Tickets Available");
                action.control.setText("Concert Options");
                break;
            case "edit":
                title.heading.setText(name);
                time.control.setDisable(false);
                time.control.setText(F.toWhen(event.date)[0]);
                month.control.setDisable(false);
                month.control.setText(F.toWhen(event.date)[1]);
                day.control.setDisable(false);
                day.control.setText(F.toWhen(event.date)[2]);
                year.input.setEditable(true);
                year.input.setText(F.toWhen(event.date)[3]);
                where.control.setDisable(false);
                where.control.setText(F.FindVenue("ID", event.venue).name);
                paid.control.setDisable(false);
                paid.control.setText("Php "+Double.toString(event.price));
                for (String s : event.seats) if (s.length()==19) taken--;
                seat.control.setText(Integer.toString(taken)+" Tickets Available");
                action.control.setText("Confirm Reorganization");
                break;
            case "make":
                title.heading.setText(name);
                time.control.setDisable(false);
                time.control.setText("Set Time");
                month.control.setDisable(false);
                month.control.setText("Month");
                day.control.setDisable(false);
                day.control.setText("Day");
                year.input.setEditable(true);
                year.input.setText("Type Year");
                where.control.setDisable(false);
                where.control.setText("Select Venue");
                paid.control.setDisable(false);
                paid.control.setText("Price");
                seat.control.setDisable(false);
                seat.control.setText("Give me a free ticket");
                action.control.setText("Organize Concert");
                break;
            case "ticket":
                title.heading.setText(name);
                time.control.setDisable(true);
                time.control.setText(F.toWhen(event.date)[0]);
                month.control.setDisable(true);
                month.control.setText(F.toWhen(event.date)[1]);
                day.control.setDisable(true);
                day.control.setText(F.toWhen(event.date)[2]);
                year.input.setEditable(false);
                year.input.setText(F.toWhen(event.date)[3]);
                where.control.setDisable(true);
                where.control.setText(F.FindVenue("ID", event.venue).name);
                paid.control.setDisable(true);
                paid.control.setText("Php "+event.price);
                seat.control.setDisable(true);
                for (int i=0; i<80; i++) {
                    if (event.seats.get(i).length()==19) {
                        if (owner.ID.equals(F.FindTicket("ID", event.seats.get(i)).owner)) {
                            if (F.FindTicket("ID", event.seats.get(i)).seat.equals(F.Seats.get(i))) seat.control.setText(F.Seats.get(i));
                            break;
                        }
                    }
                }
                action.control.setText("Ticket Options");
                break;
            case "book":
                title.heading.setText(name);
                time.control.setDisable(true);
                time.control.setText(F.toWhen(event.date)[0]);
                month.control.setDisable(true);
                month.control.setText(F.toWhen(event.date)[1]);
                day.control.setDisable(true);
                day.control.setText(F.toWhen(event.date)[2]);
                year.input.setEditable(false);
                year.input.setText(F.toWhen(event.date)[3]);
                where.control.setDisable(true);
                where.control.setText(F.FindVenue("ID", event.venue).name);
                paid.control.setDisable(true);
                paid.control.setText("Php "+event.price);
                for (String s : event.seats) if (s.length()==19) taken--;
                seat.control.setDisable(true);
                seat.control.setText(Integer.toString(taken)+" Tickets Available");
                action.control.setText("Reserve Tickets");
                break;
        }
    }
    void setMonth(String m) {
        month.control.setText(m);
    }
    void setTime(String t) {
        time.control.setText(t);
    }
    void setYear(String y) {
        year.input.setText(y);
    }
    void setDay(String d) {
        day.control.setText(d);
    }
    void setSeat(String s) {
        seat.control.setText(s);
    }
}