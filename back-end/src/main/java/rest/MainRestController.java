package rest;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import models.Airport;
import models.Carrier;
import database.DatabaseConnector;

@RestController
public class MainRestController {
    private DatabaseConnector dbconn;
    
    public MainRestController() {
        try {
            dbconn = new DatabaseConnector();
        } catch (PropertyVetoException | SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    @RequestMapping("/airports")
    public List<Airport> airports() {
        try {
            return dbconn.getAirports();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @RequestMapping("/carriers")
    public List<Carrier> carriers() {
        try {
            return dbconn.getCarriers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
