package concierto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.setUserAgentStylesheet;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Concierto extends Application {
    
    /**
     * Global Variables
     */
    Stage Concierto; 
    Double L;
    Double H;
    Root S;
    DataFile F;
    SmallDialog P;
    String T;

    /**
     * Interface Variables
     */
    String Last_View;
    Login login;
    Planner plan;
    Register reg;
    Menu menu;
    Seats seats;
    Month month;
    Day day;
    Time time;
    
    /**
     * Listing Variables
     */
    Lister concert;
    Lister performer;
    Lister tickets;
    
    Lister contact;
    Lister band;
    Lister venue;
    
    Lister admin;
    Lister applicant;
    Lister reseter;
    
    /**
     * Object Variables
     */
    Band bandnow;
    User usernow;
    Concert concertnow;
    Ticket ticketnow;
    Venue venuenow;
    
    /**
     * Interface Methods
     */
    void Start_Login() {
        // Init Login
        login = new Login(L,H,F);
        S.swap(login);
        Last_View = "Login";
        // Init Buttons
        login.signup.control.setOnAction((ActionEvent ae_signup) -> {
            Start_Register("Register");
            Last_View = "Start_Login";
            reg.numlabel.heading.setText("Customer ID");
            reg.title.heading.setText("Customer Registration");
            reg.action.control.setText("Submit Registration");
        });
        login.apply.control.setOnAction((ActionEvent ae_apply) -> {
            Start_Register("Register");
            Last_View = "Start_Login";
            reg.numlabel.heading.setText("Administrator ID");
            reg.title.heading.setText("Administrator Application");
            reg.action.control.setText("Submit Application");
        });
        login.exit.control.setOnAction((ActionEvent ae_exit) -> {
            Platform.exit();
        });
        login.signin.control.setOnAction((ActionEvent ae_signin) -> {
            switch (login.Verify()) {
                case "Administrator":
                case "Customer":
                    usernow = F.FindUser("name", login.usertext.input.getText());
                    Start_Menu();
                    break;
                case "Applicant":
                    login_applicant();
                    break;
                case "Forgot Customer":
                case "Forgot Administrator":
                case "Forgot Applicant":
                    login_forgot();
                    break;
                case "Reset Customer":
                case "Reset Administrator":
                case "Reset Applicant":
                    login_reset();
                    break;
                case "Rejected":
                    login_rejected();
                    break;
                case "Accepted":
                    login_accepted();
                    break;
                case "Incorrect":
                    login_incorrect();
                    break;
                default:
                    login_default();
                    break;
            }
        });
    }
    void Start_Register(String type) {
        // Init Register
        if (type.equals("Register")) reg = new Register(L,H,F,F.makeID("User"));
        else if (type.equals("Reset")) {
            reg = new Register(L,H,F,usernow.ID);
            reg.usertext.input.setText(login.usertext.input.getText());
            reg.usertext.input.setEditable(false);
            reg.passtext.input.setText("");
        }
        S.swap(reg);
        // Init Buttons
        reg.back.control.setOnAction((ActionEvent ae_reg_back) -> {
            if (Last_View.equals("Start_Login")) Start_Login();
            else if (Last_View.equals("Start_Menu")) Start_Menu();
        });
        reg.action.control.setOnAction((ActionEvent ae_reg_act) -> {
            switch (reg.action.control.getText()) {
                case "Reset Password":
                    if (reg.passtext.input.getText().isEmpty()) empty_pass();
                    else reg_reset();
                    break;
                case "Update Account":
                    if (usernow.type.equals("Customer")) reg.numlabel.heading.setText("Customer ID");
                    else if (usernow.type.equals("Administrator")) reg.numlabel.heading.setText("Administrator ID");
                    F.Users.remove(F.FindUser("card", usernow.ID));
                    usernow = reg.Change(usernow, reg.usertext.input.getText(), reg.passtext.input.getText());
                    F.Users.add(usernow);
                    try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                    Start_Menu();
                    menu_update();
                    break;
                default:
                    if (reg.usertext.input.getText().isEmpty()) empty_user();
                    else {
                        if (reg.passtext.input.getText().isEmpty()) empty_pass();
                        else {
                            if (F.FindUser("name", reg.usertext.input.getText()) == null) {
                                if (reg.action.control.getText().equals("Submit Application")) reg_receive();
                                else if (reg.action.control.getText().equals("Submit Registration")) reg_accept();
                                F.Users.add(new User(F.makeID("User"), reg.usertext.input.getText(), reg.passtext.input.getText(), T));
                                try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                                S.swap(login);
                            }
                            else reg_exist();
                        }
                    }   break;
            }
        });
    }
    void Start_Menu() {
        // Init Menu
        menu = new Menu(L,H,F,usernow);
        Concierto.getScene().getStylesheets().clear();
        if (usernow.type.equals("Administrator")) {
            S.Aesthetic.setFill(Color.CORNFLOWERBLUE);
            Concierto.getScene().getStylesheets().add(getClass().getResource("Administrator.css").toExternalForm());
        }
        else {
            S.Aesthetic.setFill(Color.MEDIUMVIOLETRED);
            Concierto.getScene().getStylesheets().add(getClass().getResource("Concierto.css").toExternalForm());
        }
        // Init Timeline
        EventHandler tick = (EventHandler<ActionEvent>) (ActionEvent ae_tick) -> {
            int x = F.Concerts.size();
            for (int i=0; i<x; i++) {
                try {
                    if (!F.dater(F.Concerts.get(i).date)) {
                        if (usernow!=null) {
                            menu_commence(F.Concerts.get(i));
                            while (F.FindTicket("event", F.Concerts.get(i).ID)!=null) F.Tickets.remove(F.FindTicket("event", F.Concerts.get(i).ID));
                            F.Concerts.remove(F.FindConcert("name", F.Concerts.get(i).name));
                            F.Save("Concert");
                            F.Save("Ticket");
                        }
                    }
                } catch (ParseException | IOException ex) {
                    Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);
                }
                x = F.Concerts.size();
            }
        };
        KeyFrame j = new KeyFrame(Duration.ZERO,tick);
        KeyFrame k = new KeyFrame(Duration.minutes(1));
        Timeline t = new Timeline(j,k);
        t.setCycleCount(INDEFINITE);
        t.play();
        S.swap(menu);
        // Init Buttons
        menu.back.control.setOnAction((ActionEvent ae_menu_out) -> {
            menu_logout();
        });
        menu.mine.control.setOnAction((ActionEvent ae_menu_me) -> {
            Last_View = "Start_Menu";
            Start_Register("Reset");
            reg.numtext.heading.setText(usernow.ID);
            reg.usertext.input.setText(usernow.name);
            reg.passtext.input.setText(usernow.pass);
            reg.action.control.setText("Update Account");
        });
        menu.make.control.setOnAction((ActionEvent ae_menu_make) -> {
            if (usernow.type.equals("Administrator")) List_Concert();
            else List_Tickets();
        });
        menu.view.control.setOnAction((ActionEvent ae_menu_view) -> {
            if (usernow.type.equals("Administrator")) Select_Contact();
            else List_Concert();
        });
        menu.mode.control.setOnAction((ActionEvent ae_menu_mode) -> {
            if (usernow.type.equals("Administrator")) Select_Admin();
            else menu_upgrade();
        });
    }
    void Start_Planner(String name, String type) {
        // Init Planner
        plan = new Planner(L,H,F,name,type,usernow);
        S.swap(plan);
        // Init Buttons
        plan.time.control.setOnAction((ActionEvent ae_plan_time) -> {
            Start_Time();
        });
        plan.month.control.setOnAction((ActionEvent ae_plan_month) -> {
            Start_Month();
        });
        plan.day.control.setOnAction((ActionEvent ae_plan_day) -> {
            if (plan.year.input.getText().matches("[0-9][0-9][0-9][0-9]") && Calendar.getInstance().get(Calendar.YEAR) <= Integer.parseInt(plan.year.input.getText()) && Calendar.getInstance().get(Calendar.YEAR)+2>=Integer.parseInt(plan.year.input.getText())) {
                if (!plan.month.control.getText().equals("Month")) {
                    Start_Day();
                }
                else plan_month_bad();
            }
            else plan_year_bad();
        });
        plan.action.control.setOnAction((ActionEvent ae_plan_action) -> {
            switch (plan.action.control.getText()) {
                case "Concert Options":
                    plan_concert_options(name);
                    break;
                case "Organize Concert":
                case "Confirm Reorganization":
                    if (!plan.time.control.getText().equals("Set Time")) {
                        if (!plan.month.control.getText().equals("Month")) {
                            if (!plan.day.control.getText().equals("Day")) {
                                if (plan.year.input.getText().matches("[0-9][0-9][0-9][0-9]")) {
                                    if (!plan.where.control.getText().equals("Select Venue")) {
                                        if (!plan.paid.control.getText().equals("Price")) {
                                            if (!F.FindConcert("name", plan.title.heading.getText()).performers.isEmpty()) {
                                                try {
                                                    if (F.dater(F.toDate(plan.time.control.getText(), plan.month.control.getText(), plan.day.control.getText(), plan.year.input.getText()))) {
                                                        if (plan.seat.control.getText().equals("Give me a free ticket")) Start_Seats();
                                                        else List_Concert();
                                                        F.FindConcert("name", plan.title.heading.getText()).date = F.toDate(plan.time.control.getText(), plan.month.control.getText(), plan.day.control.getText(), plan.year.input.getText());
                                                        try {F.Save("Concert");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                                                    }
                                                    else plan_date_bad();
                                                } catch (ParseException ex) {
                                                    Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                            else plan_perf_bad();
                                        }
                                        else plan_price_bad();
                                    }
                                    else plan_venue_bad();
                                }
                                else plan_year_bad();
                            }
                            else plan_day_bad();
                        }
                        else plan_month_bad();
                    }
                    else plan_time_bad();
                    break;
                case "Reserve Tickets":
                    Last_View = "Reservation";
                    Start_Seats();
                    break;
                case "Ticket Options":
                    plan_options();
                    break;
            }
        });
        plan.seat.control.setOnAction((ActionEvent ae_plan_seat) -> {
            if (plan.seat.control.getText().equals("Give me a free ticket")) {
                plan.seat.control.setText("Do not give me a free ticket");
            }
            else if (plan.seat.control.getText().equals("Do not give me a free ticket")) {
                plan.seat.control.setText("Give me a free ticket");
            }
            else if (plan.seat.control.getText().split(" ")[2].equals("Available")) {
                Start_Seats();
            }
        });
        plan.back.control.setOnAction((ActionEvent ae_plan_back) -> {
            if (Last_View.equals("Tickets")) List_Tickets();
            else Start_Menu();
        });
        plan.where.control.setOnAction((ActionEvent ae_plan_where) -> {
            List_Venue();
        });
        plan.paid.control.setOnAction((ActionEvent ae_plan_paid) -> {
            plan_price();
        });
        plan.performers.control.setOnAction((ActionEvent ae_plan_bands) -> {
            List_Performer(plan.title.heading.getText());
        });
    }
    void Start_Seats() {
        // Init Concert
        seats = new Seats(L,H,F,F.FindConcert("name", plan.title.heading.getText()));
        S.swap(seats);
        // Init Seats
        for (SmallButton seat : seats.seat) {
            seat.control.setOnAction((ActionEvent ae_seat_each) -> {
                ticketnow = new Ticket(F.makeID("Ticket"), seat.control.getText(), usernow.ID, F.FindConcert("name", plan.title.heading.getText()).ID);
                F.Tickets.add(ticketnow);
                F.TakeSeat(F.FindConcert("name", plan.title.heading.getText()), seat.control.getText(), ticketnow.ID);
                try {F.Save("Concert");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                try {F.Save("Ticket");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                Start_Seats();
            });
        }
        seats.back.control.setOnAction((ActionEvent ae_seat_back) -> {
            Start_Menu();
        });
    }

    /**
     * When Interfaces
     */
    void Start_Time() {
        // Init Day
        time = new Time(L,H,F);
        // Init Buttons
        time.select.control.setOnAction((ActionEvent ae_time_select) -> {
            plan.setTime(time.now[0]+":"+time.now[1]+" "+time.now[2]);
            S.swap(plan);
        });
        time.back.control.setOnAction((ActionEvent ae_time_back) -> {
            S.swap(plan);
        });
        S.swap(time);
    }
    void Start_Month() {
        // Init Month
        month = new Month(L,H,F);
        // Init Buttons
        month.back.control.setOnAction((ActionEvent ae_month_back) -> {
            S.swap(plan);
        });
        for (SmallButton m : month.month) {
            m.control.setOnAction((ActionEvent ae_plan_month_each) -> {
                S.swap(plan);
                plan.setMonth(m.control.getText());
            });
        }
        S.swap(month);
    }
    void Start_Day() {
        // Init Day
        day = new Day(L,H,F,plan.month.control.getText(),Integer.parseInt(plan.year.input.getText()));
        // Init Buttons
        for (SmallButton d : day.days) {
            d.control.setOnAction((ActionEvent ae_day_each) -> {
                plan.setDay(d.control.getText());
                S.swap(plan);
            });
        }
        day.back.control.setOnAction((ActionEvent ae_day_back) -> {
            S.swap(plan);
        });
        S.swap(day);
    }

    /**
     * Listing Methods
     */
    void List_Concert() {
        // Init Concert
        Last_View = "Upcoming";
        concert = new Lister(L,H,F,"Upcoming Concerts",null);
        S.swap(concert);
        // Init Items
        concert.back.control.setOnAction((ActionEvent ae_concert_back) -> {
            Start_Menu();
        });
        if (usernow.type.equals("Administrator")) concert.what.control.setText("+");
        else concert.what.control.setText("?");
        concert.what.control.setOnAction((ActionEvent ae_concert_what) -> {
            if (usernow.type.equals("Administrator")) concert_new();
            else {
                
            }
        });
        for (SmallButton b : concert.data) {
            b.control.setOnAction((ActionEvent ae_concert_data) -> {
                if (usernow.type.equals("Administrator")) {
                    Start_Planner(b.control.getText(), "view");
                }
                else {
                    Start_Planner(b.control.getText(), "book");
                }
            });
        }
    }
    void List_Performer(String event) {
        // Init Performer
        performer = new Lister(L,H,F,"Performers",event);
        S.swap(performer);
        // Init Items
        performer.back.control.setOnAction((ActionEvent ae_performer_back) -> {
            S.swap(plan);
        });
        if (Last_View.equals("Organize")) performer.what.control.setText("+");
        else if (Last_View.equals("Viewed")) performer.what.control.setText("?");
        performer.what.control.setOnAction((ActionEvent ae_performer_add) -> {
            if (performer.what.control.getText().equals("+")) List_Band();
        });
        for (SmallButton b : performer.data) {
            if (Last_View.equals("Organize")) {
                b.control.setDisable(false);
                b.control.setOnAction((ActionEvent ae_performer_each) -> {
                    performer.data.remove(performer.data.size()-1);
                    List_Performer(event);
                });
            }
            else b.control.setDisable(true);
        }
    }
    void List_Tickets() {
        // Init Tickets
        tickets = new Lister(L,H,F,"My Tickets",usernow.ID);
        S.swap(tickets);
        Last_View = "Tickets";
        // Init Items
        tickets.back.control.setOnAction((ActionEvent ae_tix_back) -> {
            if (usernow.type.equals("Administrator")) {
                Select_Admin();
            }
            else Start_Menu();
        });
        for (SmallButton b : tickets.data) {
            b.control.setOnAction((ActionEvent ae_tix_view) -> {
                Start_Planner(b.control.getText().split("\t\tRef.No.")[0], "ticket");
                ticketnow = F.FindTicket("ID", b.control.getText().split("\t\tRef.No.")[1]);
                plan.seat.control.setText(ticketnow.seat);
            });
        }
    }
    
    void List_Band() {
        // Init Band
        band = new Lister(L,H,F,"Bands",null);
        S.swap(band);
        // Init Items
        band.back.control.setOnAction((ActionEvent ae_band_back) -> {
            if (Last_View.equals("Organize")) List_Performer(plan.title.heading.getText());
            else Select_Contact();
        });
        band.what.control.setText("+");
        band.what.control.setOnAction((ActionEvent ae_band_add) -> {
            band_make();
        });
        for (SmallButton b : band.data) {
            if (Last_View.equals("Organize")) for (String s : F.FindConcert("name", plan.title.heading.getText()).performers) if (b.control.getText().equals(F.FindBand("ID", s).name)) b.setVisible(false);
            b.control.setOnAction((ActionEvent ae_band_each) -> {
                if (Last_View.equals("Organize")) {
                    F.FindConcert("name", plan.title.heading.getText()).performers.add(F.FindBand("name", b.control.getText()).ID);
                    List_Band();
                }
                else {
                    band_view(b.control.getText());
                }
            });
        }
    }
    void List_Venue() {
        // Init Venue
        venue = new Lister(L,H,F,"Venues",null);
        S.swap(venue);
        // Init Items
        venue.back.control.setOnAction((ActionEvent ae_venue_back) -> {
            if (Last_View.equals("Organize")) S.swap(plan);
            else Select_Contact();
        });
        venue.what.control.setText("+");
        venue.what.control.setOnAction((ActionEvent ae_venue_add) -> {
            venue_make();
        });
        for (SmallButton b : venue.data) {
            if (Last_View.equals("Select_Contact")) {
                b.control.setOnAction((ActionEvent ae_venue_view) -> {
                    venue_view(b.control.getText());
                });
            }
            else {
                b.control.setOnAction((ActionEvent ae_venue_get) -> {
                    plan.where.control.setText(b.control.getText());
                    F.FindConcert("name", plan.title.heading.getText()).venue = F.FindVenue("name", b.control.getText()).ID;
                    S.swap(plan);
                });
            }
        }
    }
    void Select_Contact() {
        // Init Contact
        contact = new Lister(L,H,F,"External Contacts",null);
        S.swap(contact);
        // Init Items
        contact.data.get(0).control.setOnAction((ActionEvent ae_contact_b) -> {
            Last_View = "Select_Contact";
            List_Band();
        });
        contact.data.get(1).control.setOnAction((ActionEvent ae_contact_v) -> {
            Last_View = "Select_Contact";
            List_Venue();
        });
        contact.back.control.setOnAction((ActionEvent ae_contact_back) -> {
            Start_Menu();
        });
        
    }
    
    void List_Applicant() {
        // Init Applicant
        applicant = new Lister(L,H,F,"Pending Applications",null);
        S.swap(applicant);
        // Init Items
        applicant.back.control.setOnAction((ActionEvent ae_applicant_back) -> {
            Select_Admin();
        });
        for (SmallButton d : applicant.data) {
            d.control.setOnAction((ActionEvent ae_applicant_each) -> {
                app_review(d.control.getText());
            });
        }
    }
    void List_Reseter() {
        // Init Reseter
        reseter = new Lister(L,H,F,"Password Reset Requests",null);
        S.swap(reseter);
        // Init Items
        reseter.back.control.setOnAction((ActionEvent ae_rstr_back) -> {
            Select_Admin();
        });
        for (SmallButton d : reseter.data) {
            d.control.setOnAction((ActionEvent ae_rstr_each) -> {
                rstr_review(d.control.getText());
            });
        }
    }
    void Select_Admin() {
        // Init Administrator
        admin = new Lister(L,H,F,"Administrative Privileges",null);
        S.swap(admin);
        // Init Items
        admin.data.get(0).control.setOnAction((ActionEvent ae_admin_applicants) -> {
            List_Applicant();
        });
        admin.data.get(1).control.setOnAction((ActionEvent ae_admin_resets) -> {
            List_Reseter();
        });
        admin.data.get(2).control.setOnAction((ActionEvent ae_admin_tix) -> {
            List_Tickets();
        });
        admin.data.get(3).control.setOnAction((ActionEvent ae_admin_down) -> {
            admin_down();
        });
        admin.back.control.setOnAction((ActionEvent ae_admin_back) -> {
            Start_Menu();
        });
    }

    /**
     * Dialog Methods
     */
    void empty_user() {
        P = new SmallDialog(
            reg,L,H,F,
            "This is not cool.\nYour username cannot be blank!\nNow, do your best to make it unique.",
            new SmallButton(new Rectangle(L-30,H/12), "Okay. Sorry!","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_reg_user_mt) -> {
            reg.getChildren().remove(reg.getChildren().size()-1);
        });
    }
    void empty_pass() {
        P = new SmallDialog(
            reg,L,H,F,
            "This is not cool at all.\nYour password cannot be blank!\nNow, do your best to make it memorable to you alone.",
            new SmallButton(new Rectangle(L-30,H/12), "Okay. Got it!","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_reg_pass_mt) -> {
            reg.getChildren().remove(reg.getChildren().size()-1);
        });
    }

    void login_applicant() {
        P = new SmallDialog(
            login,L,H,F,
            "What's up, "+login.usertext.input.getText()+"!\nJust relax for a moment.\nYour application is being reviewed.\nWe'll notify you immediately after!",
            new SmallButton(new Rectangle(L-30,H/12), "Okay. Take your time, guys! **crosses fingers**","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_action_yeah) -> {
            login.getChildren().remove(login.getChildren().size()-1);
        });
    }
    void login_forgot() {
        P = new SmallDialog(
            login,L,H,F,
            "Hol'up, "+login.usertext.input.getText()+"!\nYou just requested a password reset.\nWe are still working on it!",
            new SmallButton(new Rectangle(L-30,H/12), "Okay. Take your time.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_login_forgot_yeah) -> {
            login.getChildren().remove(login.getChildren().size()-1);
        });
    }
    void login_reset() {
        P = new SmallDialog(
            login,L,H,F,
            "Here you go, Boss "+login.usertext.input.getText()+"!\nWe see you forgot your password.\nReset your password by clicking Awesome!",
            new SmallButton(new Rectangle(L-30,H/12), "Awesome!","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_login_reset_yeah) -> {
            usernow = F.FindUser("name", login.usertext.input.getText());
            Start_Register("Reset");
            reg.action.control.setText("Reset Password");
        });
    }
    void login_rejected() {
        P = new SmallDialog(
            login,L,H,F,
            "We are truly sorry, Dear "+login.usertext.input.getText()+".\nWe cannot hire you right now.\nBut fret not!\nYou can still enjoy being a valued customer.",
            new SmallButton(new Rectangle(L-30,H/12), "No, it' okay. I understand. Really.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_login_reject_yeah) -> {
            F.FindUser("name", login.usertext.input.getText()).type = "Customer";
            try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            login.getChildren().remove(login.getChildren().size()-1);
        });
    }
    void login_accepted() {
        P = new SmallDialog(
            login,L,H,F,
            "Congratulations, Boss "+login.usertext.input.getText()+".\nWe are happy to welcome you...\nas one of Concierto's administrators!\nLogin to exercise your administrative privileges.",
            new SmallButton(new Rectangle(L-30,H/12), "Thank you so much! You won't regret this.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_login_accept_yeah) -> {
            login.getChildren().remove(login.getChildren().size()-1);
            F.FindUser("name", login.usertext.input.getText()).type = "Administrator";
            try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
        });
    }
    void login_incorrect() {
        P = new SmallDialog(
            login,L,H,F,
            "Hey, "+login.usertext.input.getText()+".\nThe password didn't match this time.\nDid you forget it or something?",
            new SmallButton(new Rectangle(L-30,H/12), "Yes. I would like to reset it. Please?","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "No. I simply mistyped it. Lemme try again.","Header"),
            null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_login_wrong_yeah) -> {
            login.getChildren().remove(login.getChildren().size()-1);
            F.FindUser("name", login.usertext.input.getText()).type = "Forgot "+F.FindUser("name", login.usertext.input.getText()).type;
            try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
        });
        P.nope.control.setOnAction((ActionEvent ae_login_wrong_nope) -> {
            login.getChildren().remove(login.getChildren().size()-1);
        });
    }
    void login_default() {
        P = new SmallDialog(
            login,L,H,F,
            "We're so sorry, "+login.usertext.input.getText()+".\nWe cannot find you in our database.\nWould you like to register now?",
            new SmallButton(new Rectangle(L-30,H/12), "As a customer? Yes, please! ","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "No. Thank you, though.","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "As an administrator, yeah sure!","Header"),
            false
        );
        P.yeah.control.setOnAction((ActionEvent ae_login_nothing_yeah) -> {
            login.getChildren().remove(login.getChildren().size()-1);
            Start_Register("Register");
            Last_View = "Start_Login";
            reg.numlabel.heading.setText("Customer ID");
            reg.title.heading.setText("Customer Registration");
            reg.action.control.setText("Submit Registration");
            reg.usertext.input.setText(login.usertext.input.getText());
            reg.passtext.input.setText(login.passtext.input.getText());
        });
        P.what.control.setOnAction((ActionEvent ae_login_nothing_what) -> {
            login.getChildren().remove(login.getChildren().size()-1);
            Start_Register("Register");
            Last_View = "Start_Login";
            reg.numlabel.heading.setText("Administrator ID");
            reg.title.heading.setText("Administrator Application");
            reg.action.control.setText("Submit Application");
            reg.usertext.input.setText(login.usertext.input.getText());
            reg.passtext.input.setText(login.passtext.input.getText());
        });
        P.nope.control.setOnAction((ActionEvent ae_login_nothing_nope) -> {
            login.getChildren().remove(login.getChildren().size()-1);
        });
    }
    void login_bye() {
        Start_Login();
        P = new SmallDialog(
            login,L,H,F,
            "Goodbye, "+usernow.name+".\nHope to see you, soon!",
            new SmallButton(new Rectangle(L-30,H/12), "Yeah, sure.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_menu_logout_yeah) -> {
            login.getChildren().remove(login.getChildren().size()-1);
            usernow = null;
        });
    }

    void reg_receive() {
        T = "Applicant";
        P = new SmallDialog(
            login,L,H,F,
            "This is cool, Boss "+reg.usertext.input.getText()+".\nWe have received your application.\nPlease await for our notification!",
            new SmallButton(new Rectangle(L-30,H/12), "Alright. Thanks!","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_reg_admin) -> {
            login.getChildren().remove(login.getChildren().size()-1);
        });
    }
    void reg_accept() {
        T = "Customer";
        P = new SmallDialog(
            login,L,H,F,
            "Congratulations, Dear "+reg.usertext.input.getText()+".\nYou are now one of our valued customers.\nEnjoy using Concierto!",
            new SmallButton(new Rectangle(L-30,H/12), "Oh, Great. Thanks!","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_reg_custom) -> {
            login.getChildren().remove(login.getChildren().size()-1);
        });
    }
    void reg_exist() {
        P = new SmallDialog(
            reg,L,H,F,
            "Sorry, dear.\nThe username "+reg.usertext.input.getText()+" is already taken.\nCome on, be more creative than "+reg.usertext.input.getText()+"!",
            new SmallButton(new Rectangle(L-30,H/12), "Ok, I'll try. I got this!","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_reg_exist) -> {
            reg.getChildren().remove(reg.getChildren().size()-1);
        });
    }
    void reg_reset() {
        P = new SmallDialog(
            reg,L,H,F,
            "Your password has been reset, "+reg.usertext.input.getText()+"!.\nJust login to verify and we're done.",
            new SmallButton(new Rectangle(L-30,H/12), "Yes, thank you so much!","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_reg_reset) -> {
            usernow.type = usernow.type.split(" ")[1];
            usernow.pass = reg.passtext.input.getText();
            F.Users.remove(F.FindUser("ID", usernow.ID));
            F.Users.add(usernow);
            try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            reg.getChildren().remove(reg.getChildren().size()-1);
            Start_Login();
        });
    }
    
    void menu_logout() {
        P = new SmallDialog(
            menu,L,H,F,
            "Hey, "+usernow.name+".\nYou are about to get logged out for now.\nYou sure about this?",
            new SmallButton(new Rectangle(L-30,H/12), "Yes.","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "No.","Header"),
            null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_menu_logout_yeah) -> {
            menu.getChildren().remove(menu.getChildren().size()-1);
            login_bye();
        });
        P.nope.control.setOnAction((ActionEvent ae_menu_logout_nope) -> {
            menu.getChildren().remove(menu.getChildren().size()-1);
        });
    }
    void menu_update() {
        P = new SmallDialog(
            menu,L,H,F,
            "Hey, "+usernow.name+".\nYou just updated your account details!",
            new SmallButton(new Rectangle(L-30,H/12), "Yeah, sure.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_menu_logout_yeah) -> {
            menu.getChildren().remove(menu.getChildren().size()-1);
            usernow = null;
        });
    }
    void menu_commence(Concert c) {
        Start_Menu();
        String all = "";
        for (String s : c.performers) all+=F.FindBand("ID", s).name+", ";
        P = new SmallDialog(
            menu,L,H,F,
            "Heads up, "+usernow.name+"!\nThe long-awaited "+c.name+" is happening right now.\n\n"+all+".\n\nThey're playing!",
            new SmallButton(new Rectangle(L-30,H/12), "Alright! Time to Party!","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_menu_play) -> {
            menu.getChildren().remove(menu.getChildren().size()-1);
        });
    }
    void menu_upgrade() {
        P = new SmallDialog(
            menu,L,H,F,
            "This a big step, "+usernow.name+".\nBy upgrading your account, you will become an administrator!\nBy agreeing to do so, we will review your application. ",
            new SmallButton(new Rectangle(L-30,H/12), "Bring it on. Upgrade me.","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "On second thought, I'd rather stay as a customer.","Header"),
            null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_menu_upgrade_yeah) -> {
            menu.getChildren().remove(menu.getChildren().size()-1);
            usernow.type = "Applicant";
            F.Users.remove(F.FindUser("name", usernow.name));
            F.Users.add(usernow);
            try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            usernow = null;
            login_bye();
        });
        P.nope.control.setOnAction((ActionEvent ae_menu_upgrade_nope) -> {
            menu.getChildren().remove(menu.getChildren().size()-1);
        });
    }
    
    void admin_down() {
        P = new SmallDialog(
            admin,L,H,F,
            "This a major step backwards, "+usernow.name+".\nNevertheless, I trust you understand what this means?",
            new SmallButton(new Rectangle(L-30,H/12), "I understand. I'm retiring anyways.","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Not really. I like working here!","Header"),
            null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_admin_down_yeah) -> {
            admin.getChildren().remove(admin.getChildren().size()-1);
            usernow.type = "Customer";
            try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            login_bye();
        });
        P.nope.control.setOnAction((ActionEvent ae_admin_down_nope) -> {
            admin.getChildren().remove(admin.getChildren().size()-1);
        });
    }
    
    void app_review(String a) {
        String[] q = a.split(" ");
        T = q[2]+" "+q[3]+" "+q[4]+" "+q[5];
        P = new SmallDialog(
            applicant,L,H,F,
            "Applicant ID\t:\t"+T+"\nUsername\t:\t"+F.FindUser("ID", T).name,
            new SmallButton(new Rectangle(L-30,H/12), "Accept "+F.FindUser("ID", T).name+".","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Reject "+F.FindUser("ID", T).name+".","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Review other applicants first.","Header"),
            false
        );
        P.yeah.control.setOnAction((ActionEvent ae_app_yeah) -> {
            applicant.getChildren().remove(applicant.getChildren().size()-1);
            F.FindUser("ID", T).type = "Accepted";
            try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            List_Applicant();
        });
        P.nope.control.setOnAction((ActionEvent ae_app_nope) -> {
            applicant.getChildren().remove(applicant.getChildren().size()-1);
            F.FindUser("ID", T).type = "Rejected";
            try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            List_Applicant();
        });
        P.what.control.setOnAction((ActionEvent ae_app_what) -> {
            applicant.getChildren().remove(applicant.getChildren().size()-1);
        });
    }
    
    void rstr_review(String a) {
        String[] q = a.split(" ");
        T = q[4]+" "+q[5]+" "+q[6]+" "+q[7];
        P = new SmallDialog(
            reseter,L,H,F,
            "Applicant ID\t:\t"+T+"\nUsername\t:\t"+F.FindUser("ID", T).name,
            new SmallButton(new Rectangle(L-30,H/12), "Reset Password for "+F.FindUser("ID", T).name+".","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Review other requests first.","Header"),
            null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_rstr_yeah) -> {
            reseter.getChildren().remove(reseter.getChildren().size()-1);
            F.FindUser("ID", T).type = "Reset "+q[3];
            try {F.Save("User");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            List_Reseter();
        });
        P.nope.control.setOnAction((ActionEvent ae_rstr_nope) -> {
            reseter.getChildren().remove(reseter.getChildren().size()-1);
        });
    }
    
    void concert_new() {
        Last_View = "Organize";
        P = new SmallDialog(
            concert,L,H,F,
            "You are about to create a new concert event.\nPlease type a unique concert name.",
            new SmallButton(new Rectangle(L-30,H/12), "Confirm Concert Name","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel New Concert","Header"),
            null,true
        );
        P.yeah.control.setOnAction((ActionEvent ae_concert_new) -> {
            if (!P.text.input.getText().isEmpty() && F.FindConcert("name", P.text.input.getText())==null) {
                F.Concerts.add(new Concert(F.makeID("Venue"), P.text.input.getText(), F.Seats));
                concert.getChildren().remove(concert.getChildren().size()-1);
                Start_Planner(P.text.input.getText(), "make");
            }
            else {
                concert.getChildren().remove(concert.getChildren().size()-1);
                concert_bad();
            }
        });
        P.nope.control.setOnAction((ActionEvent ae_venue_delete) -> {
            concert.getChildren().remove(concert.getChildren().size()-1);
            List_Concert();
        });
    }
    void concert_bad() {
        P = new SmallDialog(
            concert,L,H,F,
            "This name is not valid for a Concert.\nPlease try again.",
            new SmallButton(new Rectangle(L-30,H/12), "Ok, sorry.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_concert_bad_back) -> {
            concert.getChildren().remove(concert.getChildren().size()-1);
            concert_new();
        });
    }
    
    void venue_make() {
        P = new SmallDialog(
            venue,L,H,F,
            "You are about to add a new Venue to the database.\nPlease type the unique venue name.",
            new SmallButton(new Rectangle(L-30,H/12), "Confirm Venue Name","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel Venue Addition","Header"),
            null,true
        );
        P.yeah.control.setOnAction((ActionEvent ae_venue_make) -> {
            if (!P.text.input.getText().isEmpty() && F.FindVenue("name", P.text.input.getText())==null) {
                venuenow = new Venue(F.makeID("Venue"), P.text.input.getText(), 0.0);
                venue.getChildren().remove(venue.getChildren().size()-1);
                venue_make_fee(venuenow);
            }
            else {
                venue.getChildren().remove(venue.getChildren().size()-1);
                venue_make_bad();
            }
        });
        P.nope.control.setOnAction((ActionEvent ae_venue_no) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            List_Venue();
        });
    }
    void venue_make_bad() {
        P = new SmallDialog(
            venue,L,H,F,
            "This name is not valid for a venue.\nPlease try again.",
            new SmallButton(new Rectangle(L-30,H/12), "Ok, got it.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_venue_make_bad_back) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            venue_make();
        });
    }
    void venue_make_fee(Venue v) {
        P = new SmallDialog(
            venue,L,H,F,
            "Venue Name\t:\t"+v.name+"\n\nNow, just type how much is their booking fee.",
            new SmallButton(new Rectangle(L-30,H/12), "Confirm Booking Fee","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel Venue Addition","Header"),
            null,true
        );
        P.yeah.control.setOnAction((ActionEvent ae_venue_make_fee_good) -> {
            if (P.text.input.getText().isEmpty() || !P.text.input.getText().matches("[0-9]*\\.?[0-9]+")) {
                venue.getChildren().remove(venue.getChildren().size()-1);
                venue_edit_fee_bad(v);
            }
            else {
                v.fee = Double.parseDouble(P.text.input.getText());
                F.Venues.add(v);
                try {F.Save("Venue");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                venue.getChildren().remove(venue.getChildren().size()-1);
                List_Venue();
            }
        });
        P.nope.control.setOnAction((ActionEvent ae_venue_make_fee_back) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
        });
    }
    void venue_view(String a) {
        P = new SmallDialog(
            venue,L,H,F,
            "Venue ID\t\t:\t"+F.FindVenue("name", a).ID+"\nVenue Name\t:\t"+F.FindVenue("name", a).name+"\nBooking Fee\t:\tPhp "+F.FindVenue("name", a).fee,
            new SmallButton(new Rectangle(L-30,H/12), "Back to Venue List","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Delete Venue "+F.FindVenue("name", a).name,"Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Modify Venue "+F.FindVenue("name", a).name,"Header"),
            false
        );
        P.yeah.control.setOnAction((ActionEvent ae_venue_view) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            List_Venue();
        });
        P.nope.control.setOnAction((ActionEvent ae_venue_delete) -> {
            F.Venues.remove(F.FindVenue("name", a));
            try {F.Save("Venue");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            venue.getChildren().remove(venue.getChildren().size()-1);
            List_Venue();
        });
        P.what.control.setOnAction((ActionEvent ae_venue_modify) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            venue_edit(F.FindVenue("name", a));
        });
    }
    void venue_edit(Venue v) {
        P = new SmallDialog(
            venue,L,H,F,
            "What would you like to change for "+v.name+"?",
            new SmallButton(new Rectangle(L-30,H/12), "Nothing","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Venue Name","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Venue Fee","Header"),
            false
        );
        P.yeah.control.setOnAction((ActionEvent ae_venue_edit_back) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            venue_view(v.name);
        });
        P.nope.control.setOnAction((ActionEvent ae_venue_edit_name) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            venue_edit_name(v);
        });
        P.what.control.setOnAction((ActionEvent ae_venue_edit_fee) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            venue_edit_fee(v);
        });
    }
    void venue_edit_name(Venue v) {
        Last_View = "venue_edit_name";
        P = new SmallDialog(
            venue,L,H,F,
            "Venue Name\t:\t"+v.name+"\n\nType the new venue name below.",
            new SmallButton(new Rectangle(L-30,H/12), "Confirm Changes","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel Changes","Header"),
            null,true
        );
        P.yeah.control.setOnAction((ActionEvent ae_venue_edit_name_go) -> {
            if (P.text.input.getText().isEmpty() || F.FindVenue("name", P.text.input.getText())!=null) {
                venue.getChildren().remove(venue.getChildren().size()-1);
                venue_edit_name_bad(v);
            }
            else {
                v.name = P.text.input.getText();
                try {F.Save("Venue");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                venue.getChildren().remove(venue.getChildren().size()-1);
                venue_view(v.name);
            }
        });
        P.nope.control.setOnAction((ActionEvent ae_venue_edit_name_no) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            venue_edit(v);
        });
    }
    void venue_edit_name_bad(Venue v) {
        P = new SmallDialog(
            venue,L,H,F,
            "This name is not valid for a venue.\nPlease try again.",
            new SmallButton(new Rectangle(L-30,H/12), "Ok, sorry.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_venue_edit_name_go_back) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            venue_edit_name(v);
        });
    }
    void venue_edit_fee(Venue v) {
        Last_View = "venue_edit_fee";
        P = new SmallDialog(
            venue,L,H,F,
            "Booking Fee\t:\tPhp "+v.fee+"\n\nType the new venue fee below.",
            new SmallButton(new Rectangle(L-30,H/12), "Confirm Changes","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel Changes","Header"),
            null,true
        );
        P.yeah.control.setOnAction((ActionEvent ae_venue_edit_fee_go) -> {
            if (!P.text.input.getText().isEmpty() && P.text.input.getText().matches("[0-9]*\\.?[0-9]+")) {
                v.fee = Double.parseDouble(P.text.input.getText());
                try {F.Save("Venue");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                venue.getChildren().remove(venue.getChildren().size()-1);
                venue_view(v.name);
            }
            else {
                venue.getChildren().remove(venue.getChildren().size()-1);
                venue_edit_fee_bad(v);
            }
        });
        P.nope.control.setOnAction((ActionEvent ae_venue_edit_fee_no) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            venue_edit(v);
        });
    }
    void venue_edit_fee_bad(Venue v) {
        P = new SmallDialog(
            venue,L,H,F,
            "This fee is not valid for a venue.\nPlease try again.",
            new SmallButton(new Rectangle(L-30,H/12), "Ok, sorry.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_venue_edit_fee_bad_back) -> {
            venue.getChildren().remove(venue.getChildren().size()-1);
            if (Last_View.equals("venue_edit_fee")) venue_edit_fee(v);
            else venue_make_fee(v);
        });
    }
    
    void band_make() {
        P = new SmallDialog(
            band,L,H,F,
            "You are about to add a new Band to the database.\nPlease type the unique band name",
            new SmallButton(new Rectangle(L-30,H/12), "Confirm Band Name","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel Band Addition","Header"),
            null,true
        );
        P.yeah.control.setOnAction((ActionEvent ae_band_new) -> {
            if (!P.text.input.getText().isEmpty() && F.FindBand("name", P.text.input.getText())==null) {
                bandnow = new Band(F.makeID("Band"), P.text.input.getText(), 0.0);
                band.getChildren().remove(band.getChildren().size()-1);
                band_make_fee(bandnow);
            }
            else {
                band.getChildren().remove(band.getChildren().size()-1);
                band_make_bad();
            }
        });
        P.nope.control.setOnAction((ActionEvent ae_band_no) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            List_Band();
        });
    }
    void band_make_bad() {
        P = new SmallDialog(
            band,L,H,F,
            "This name is not valid for a band.\nPlease try again.",
            new SmallButton(new Rectangle(L-30,H/12), "Ok, got it.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_band_make_bad_back) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            band_make();
        });
    }
    void band_make_fee(Band b) {
        P = new SmallDialog(
            band,L,H,F,
            "Band Name\t:\t"+b.name+"\n\nNow, just type how much is their booking fee.",
            new SmallButton(new Rectangle(L-30,H/12), "Confirm Booking Fee","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel Band Addition","Header"),
            null,true
        );
        P.yeah.control.setOnAction((ActionEvent ae_band_make_fee_good) -> {
            if (P.text.input.getText().isEmpty() || !P.text.input.getText().matches("[0-9]*\\.?[0-9]+")) {
                band.getChildren().remove(band.getChildren().size()-1);
                band_edit_fee_bad(b);
            }
            else {
                b.fee = Double.parseDouble(P.text.input.getText());
                F.Bands.add(b);
                try {F.Save("Band");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                band.getChildren().remove(band.getChildren().size()-1);
                List_Band();
            }
        });
        P.nope.control.setOnAction((ActionEvent ae_band_make_fee_back) -> {
            band.getChildren().remove(band.getChildren().size()-1);
        });
    }
    void band_view(String a) {
        P = new SmallDialog(
            band,L,H,F,
            "Band ID\t\t:\t"+F.FindBand("name", a).ID+"\nBand Name\t:\t"+F.FindBand("name", a).name+"\nBooking Fee\t:\tPhp "+F.FindBand("name", a).fee,
            new SmallButton(new Rectangle(L-30,H/12), "Back to Band List","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Delete Band "+F.FindBand("name", a).name,"Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Modify Band "+F.FindBand("name", a).name,"Header"),
            false
        );
        P.yeah.control.setOnAction((ActionEvent ae_band_view) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            List_Band();
        });
        P.nope.control.setOnAction((ActionEvent ae_band_delete) -> {
            F.Bands.remove(F.FindBand("name", a));
            try {F.Save("Band");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            band.getChildren().remove(band.getChildren().size()-1);
            List_Band();
        });
        P.what.control.setOnAction((ActionEvent ae_band_modify) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            band_edit(F.FindBand("name", a));
        });
    }
    void band_edit(Band b) {
        P = new SmallDialog(
            band,L,H,F,
            "What would you like to change for "+b.name+"?",
            new SmallButton(new Rectangle(L-30,H/12), "Nothing","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Band Name","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Band Fee","Header"),
            false
        );
        P.yeah.control.setOnAction((ActionEvent ae_band_edit_back) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            band_view(b.name);
        });
        P.nope.control.setOnAction((ActionEvent ae_band_edit_name) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            band_edit_name(b);
        });
        P.what.control.setOnAction((ActionEvent ae_band_edit_fee) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            band_edit_fee(b);
        });
    }
    void band_edit_name(Band b) {
        Last_View = "band_edit_name";
        P = new SmallDialog(
            band,L,H,F,
            "Band Name\t:\t"+b.name+"\n\nType the new band name below.",
            new SmallButton(new Rectangle(L-30,H/12), "Confirm Changes","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel Changes","Header"),
            null,true
        );
        P.yeah.control.setOnAction((ActionEvent ae_band_edit_name_go) -> {
            if (P.text.input.getText().isEmpty() || F.FindBand("name", P.text.input.getText())!=null) {
                band.getChildren().remove(band.getChildren().size()-1);
                band_edit_name_bad(b);
            }
            else {
                b.name = P.text.input.getText();
                try {F.Save("Band");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                band.getChildren().remove(band.getChildren().size()-1);
                band_view(b.name);
            }
        });
        P.nope.control.setOnAction((ActionEvent ae_band_edit_name_no) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            band_edit(b);
        });
    }
    void band_edit_name_bad(Band b) {
        P = new SmallDialog(
            band,L,H,F,
            "This name is not valid for a band.\nPlease try again.",
            new SmallButton(new Rectangle(L-30,H/12), "Ok, sorry.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_band_edit_name_go_back) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            band_edit_name(b);
        });
    }
    void band_edit_fee(Band b) {
        Last_View = "band_edit_fee";
        P = new SmallDialog(
            band,L,H,F,
            "Booking Fee\t:\tPhp "+b.fee+"\n\nType the new band fee below.",
            new SmallButton(new Rectangle(L-30,H/12), "Confirm Changes","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel Changes","Header"),
            null,true
        );
        P.yeah.control.setOnAction((ActionEvent ae_band_edit_fee_go) -> {
            if (!P.text.input.getText().isEmpty() && P.text.input.getText().matches("[0-9]*\\.?[0-9]+")) {
                b.fee = Double.parseDouble(P.text.input.getText());
                try {F.Save("Band");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                band.getChildren().remove(band.getChildren().size()-1);
                band_view(b.name);
            }
            else {
                band.getChildren().remove(band.getChildren().size()-1);
                band_edit_fee_bad(b);
            }
        });
        P.nope.control.setOnAction((ActionEvent ae_band_edit_fee_no) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            band_edit(b);
        });
    }
    void band_edit_fee_bad(Band b) {
        P = new SmallDialog(
            band,L,H,F,
            "This fee is not valid for a band.\nPlease try again.",
            new SmallButton(new Rectangle(L-30,H/12), "Ok, sorry.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_band_edit_fee_bad_back) -> {
            band.getChildren().remove(band.getChildren().size()-1);
            if (Last_View.equals("band_edit_fee")) band_edit_fee(b);
            else band_make_fee(b);
        });
    }

    void plan_year_bad() {
        P = new SmallDialog(
            plan,L,H,F,
            "The year must be in the near future.\nLike, just up to 2 years from now.\nPlease try again.",
            new SmallButton(new Rectangle(L-30,H/12), "Ok, sorry.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_day_bad_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
        });
    }
    void plan_price() {
        P = new SmallDialog(
            plan,L,H,F,
            "Type your preferred ticket price.",
            new SmallButton(new Rectangle(L-30,H/12), "This look profitable.","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Wait. I still wanna add something.","Header"),
            null,true
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_price_good) -> {
            if (!P.text.input.getText().isEmpty() && P.text.input.getText().matches("[0-9]*\\.?[0-9]+")) {
                plan.paid.control.setText("Php "+P.text.input.getText());
                F.FindConcert("name", plan.title.heading.getText()).price = Double.parseDouble(P.text.input.getText());
                plan.getChildren().remove(plan.getChildren().size()-1);
            }
            else {
                plan.getChildren().remove(plan.getChildren().size()-1);
                plan_price_bad();
            }
        });
        P.nope.control.setOnAction((ActionEvent ae_plan_price_wait) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
        });
    }
    void plan_price_bad() {
        P = new SmallDialog(
            plan,L,H,F,
            "That is not a valid price.\nCome on, be more reasonable.\nYou want an affordable (but profitable) ticket, don't you!?",
            new SmallButton(new Rectangle(L-30,H/12), "Of course.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_day_bad_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
            plan_price();
        });
    }
    void plan_perf_bad() {
        P = new SmallDialog(
            plan,L,H,F,
            "This concert needs some performers!",
            new SmallButton(new Rectangle(L-30,H/12), "Of course.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_time_bad_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
        });
    }
    void plan_venue_bad() {
        P = new SmallDialog(
            plan,L,H,F,
            "The venue is not set properly.",
            new SmallButton(new Rectangle(L-30,H/12), "Of course.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_venue_bad_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
        });
    }
    void plan_day_bad() {
        P = new SmallDialog(
            plan,L,H,F,
            "The day is not set properly.",
            new SmallButton(new Rectangle(L-30,H/12), "Of course.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_day_bad_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
        });
    }
    void plan_month_bad() {
        P = new SmallDialog(
            plan,L,H,F,
            "The month is not set properly.",
            new SmallButton(new Rectangle(L-30,H/12), "Of course.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_month_bad_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
        });
    }
    void plan_time_bad() {
        P = new SmallDialog(
            plan,L,H,F,
            "The time is not set properly.",
            new SmallButton(new Rectangle(L-30,H/12), "Of course.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_time_bad_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
        });
    }
    void plan_date_bad() {
        P = new SmallDialog(
            plan,L,H,F,
            "You cannot schedule a concert in the past.\n We ain't got no time travel yet, Boss.",
            new SmallButton(new Rectangle(L-30,H/12), "Of course.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_date_bad_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
        });
    }
    void plan_options() {
        P = new SmallDialog(
            plan,L,H,F,
            "You can either cancel this ticket, or give it to someone else!\nJust type the name of a user.\nHe/She will get it if she/he is registered.",
            new SmallButton(new Rectangle(L-30,H/12), "Transfer Ticket","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel Reservation","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "I think I'll keep it.","Header"),
            true
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_options_give) -> {
            if (F.FindUser("name", P.text.input.getText())!=null) {
                F.FindTicket("ID", ticketnow.ID).owner = F.FindUser("name", P.text.input.getText()).ID;
                try {F.Save("Ticket");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
                plan.getChildren().remove(plan.getChildren().size()-1);
                List_Tickets();
            }
            else plan_options_bad();
        });
        P.nope.control.setOnAction((ActionEvent ae_plan_options_no) -> {
            F.Tickets.remove(ticketnow);
            for (int i=0; i<80; i++) if (F.FindConcert("name", plan.title.heading.getText()).seats.get(i).equals(ticketnow.ID)) F.FindConcert("name", plan.title.heading.getText()).seats.set(i,ticketnow.seat);
            try {F.Save("Ticket");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            try {F.Save("Concert");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            plan.getChildren().remove(plan.getChildren().size()-1);
            List_Tickets();
        });
        P.what.control.setOnAction((ActionEvent ae_plan_options_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
        });
    }
    void plan_options_bad() {
        P = new SmallDialog(
            plan,L,H,F,
            "Sorry, but that user does not exist.",
            new SmallButton(new Rectangle(L-30,H/12), "Okay, then.","Header"),
            null,null,false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_options_bad_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
            plan_options();
        });
    }
    void plan_concert_options(String name) {
        P = new SmallDialog(
            plan,L,H,F,
            "What would you like to do with this event?",
            new SmallButton(new Rectangle(L-30,H/12), "Just reorganize a bit.","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Cancel it. Regrettably.","Header"),
            new SmallButton(new Rectangle(L-30,H/12), "Nothing","Header"),
            false
        );
        P.yeah.control.setOnAction((ActionEvent ae_plan_options_edit) -> {
            Start_Planner(name,"edit");
        });
        P.nope.control.setOnAction((ActionEvent ae_plan_options_kill) -> {
            while (F.FindTicket("event", name)!=null) F.Tickets.remove(F.FindTicket("event", name));
            F.Concerts.remove(F.FindConcert("name", name));
            try {F.Save("Concert");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            try {F.Save("Ticket");} catch (IOException ex) {Logger.getLogger(Concierto.class.getName()).log(Level.SEVERE, null, ex);}
            plan.getChildren().remove(plan.getChildren().size()-1);
            List_Concert();
        });
        P.what.control.setOnAction((ActionEvent ae_plan_concert_options_back) -> {
            plan.getChildren().remove(plan.getChildren().size()-1);
        });
    }
    
    /**
     * Application Methods
     */
    private void preset(Path file) throws MalformedURLException {
        file.toFile().deleteOnExit();
        setUserAgentStylesheet(file.toUri().toURL().toString());
        Concierto.initStyle(StageStyle.UNDECORATED);
    }
    public static void main(String[] args) {
        launch(args);
    }
    @Override public void start(Stage stage) throws IOException {
        // Init Files
        F = new DataFile(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        F.Read("","Files");
        // Init Stage
        Concierto = stage;
        // CSS Reset
        preset(Files.createTempFile("prefix", "suffix"));
        // Launch Stage
        Concierto.show();
        Concierto.setMaximized(true);
        L = Concierto.getWidth();
        H = Concierto.getHeight();
        // Design Setup
        S = new Root(L,H);
        // Scene Setup
        Concierto.setScene(new Scene(S));
        Concierto.getScene().getStylesheets().add(getClass().getResource("Concierto.css").toExternalForm());
        // Formal Start
        Start_Login();
    }
}