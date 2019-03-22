package database;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import models.Airport;
import models.Carrier;
import models.ExtraStatistic;
import models.Statistic;

public class DatabaseConnector {
    // Database URL, and database credentials.
    private final String DB_URL = "jdbc:mysql://192.168.178.22:3306/webengdb";
    private final String USER = "webenguser";
    private final String PASS = "webengpass123";

    ComboPooledDataSource cpds;

    public DatabaseConnector() throws PropertyVetoException, SQLException {
        cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl(DB_URL);
        cpds.setUser(USER);                                  
        cpds.setPassword(PASS);

        createAirportsTable();
        createCarriersTable();
        createStatsTable();
        createExtraStatsTable();
    }

    private void createAirportsTable() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        String query = "CREATE TABLE IF NOT EXISTS airports ("
                     + "code CHAR(3) PRIMARY KEY,"
                     + "name VARCHAR(255)"
                     + ");";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createCarriersTable() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        String query = "CREATE TABLE IF NOT EXISTS carriers ("
                     + "code CHAR(2) PRIMARY KEY,"
                     + "name VARCHAR(255)"
                     + ");";

        try {  
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createStatsTable() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        String query = "CREATE TABLE IF NOT EXISTS stats ("
                     + "airport CHAR(3),"
                     + "carrier CHAR(2),"
                     + "year YEAR,"
                     + "month TINYINT(2) UNSIGNED,"

                     + "cancelledFlightCount MEDIUMINT UNSIGNED NOT NULL,"
                     + "onTimeFlightCount MEDIUMINT UNSIGNED NOT NULL,"
                     + "delayedFlightCount MEDIUMINT UNSIGNED NOT NULL,"
                     + "divertedFlightCount MEDIUMINT UNSIGNED NOT NULL,"
                     + "totalFlightCount MEDIUMINT UNSIGNED NOT NULL,"

                     + "lateAircraftDelayCount MEDIUMINT UNSIGNED NOT NULL,"
                     + "weatherDelayCount MEDIUMINT UNSIGNED NOT NULL,"
                     + "securityDelayCount MEDIUMINT UNSIGNED NOT NULL,"
                     + "nationalAviationSystemDelayCount MEDIUMINT UNSIGNED NOT NULL,"
                     + "carrierDelayCount MEDIUMINT UNSIGNED NOT NULL,"

                     + "lateAircraftDelayTime MEDIUMINT UNSIGNED NOT NULL,"
                     + "weatherDelayTime MEDIUMINT UNSIGNED NOT NULL,"
                     + "securityDelayTime MEDIUMINT UNSIGNED NOT NULL,"
                     + "nationalAviationSystemDelayTime MEDIUMINT UNSIGNED NOT NULL,"
                     + "carrierDelayTime MEDIUMINT UNSIGNED NOT NULL,"
                     + "totalDelayTime MEDIUMINT UNSIGNED NOT NULL,"

                     + "PRIMARY KEY (airport, carrier, year, month)"
                     + ");";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createExtraStatsTable() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        String query = "CREATE TABLE IF NOT EXISTS extraStats ("
                     + "airport1 CHAR(3),"
                     + "airport2 CHAR(3),"
                     + "carrier CHAR(2),"
                     + "year YEAR,"
                     + "month TINYINT(2) UNSIGNED,"

                     + "lateAircraftDelaysTimedMean SMALLINT UNSIGNED NOT NULL,"
                     + "lateAircraftDelaysTimedMed SMALLINT UNSIGNED NOT NULL,"
                     + "lateAircraftDelaysTimedSd FLOAT UNSIGNED NOT NULL,"
                     + "carrierAircraftDelaysTimedMean SMALLINT UNSIGNED NOT NULL,"
                     + "carrierAircraftDelaysTimedMed SMALLINT UNSIGNED NOT NULL,"
                     + "carrierAircraftDelaysTimedSd FLOAT UNSIGNED NOT NULL,"

                     + "PRIMARY KEY (airport1, airport2, carrier, year, month)"
                     + ");";

        try {  
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addAirport(Airport airport) throws SQLException {
        List<Airport> airports = new ArrayList<>();
        airports.add(airport);
        addAirports(airports);
    }

    public void addAirports(List<Airport> airports) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "INSERT INTO airports (code, name)"
                     + "VALUES (?, ?);";

        try { 
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);

            int i = 0;
            for (Airport airport : airports) {
                stmt.setString(1, airport.getCode());
                stmt.setString(2, airport.getName());

                stmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == airports.size()) {
                    stmt.executeBatch();
                }
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Airport> getAirports() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Airport> airports = new ArrayList<>();

        String query = "SELECT * "
                     + "FROM airports;";

        try {  
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while(rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                Airport airport = new Airport(code, name);
                airports.add(airport);
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return airports;
    }


    public void addCarrier(Carrier carrier) throws SQLException {
        List<Carrier> carriers = new ArrayList<>();
        carriers.add(carrier);
        addCarriers(carriers);
    }

    public void addCarriers(List<Carrier> carriers) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "INSERT INTO carriers (code, name)"
                     + "VALUES (?, ?);";

        try { 
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);

            int i = 0;
            for (Carrier carrier : carriers) {
                stmt.setString(1, carrier.getCode());
                stmt.setString(2, carrier.getName());

                stmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == carriers.size()) {
                    stmt.executeBatch();
                }
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Carrier> getCarriers() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Carrier> carriers = new ArrayList<>();

        String query = "SELECT * "
                     + "FROM carriers;";

        try {  
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while(rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                Carrier carrier = new Carrier(code, name);
                carriers.add(carrier);
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return carriers;
    }

    public List<Carrier> getCarriersAtAirport(Airport airport) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Carrier> carriers = new ArrayList<>();

        String query = "SELECT DISTINCT carriers.code, carriers.name "
                     + "FROM carriers "
                     + "INNER JOIN stats "
                     + "ON carriers.code = stats.carrier "
                     + "WHERE stats.airport = ?;";

        try {  
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);
            stmt.setString(1, airport.getCode());
            rs = stmt.executeQuery();

            while(rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                Carrier carrier = new Carrier(code, name);
                carriers.add(carrier);
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return carriers;
    }

    public void addStatistic(Statistic statistic) throws SQLException {
        List<Statistic> statistics = new ArrayList<>();
        statistics.add(statistic);
        addStatistics(statistics);
    }

    public void addStatistics(List<Statistic> statistics) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "INSERT INTO stats (airport, carrier, year, month,"
                     + "cancelledFlightCount, onTimeFlightCount, delayedFlightCount, divertedFlightCount, totalFlightCount,"
                     + "lateAircraftDelayCount, weatherDelayCount, securityDelayCount, nationalAviationSystemDelayCount, carrierDelayCount,"
                     + "lateAircraftDelayTime, weatherDelayTime, securityDelayTime, nationalAviationSystemDelayTime, carrierDelayTime, totalDelayTime)"
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);

            int i = 0;
            for (Statistic statistic : statistics) {
                stmt.setString(1, statistic.getAirport().getCode());
                stmt.setString(2, statistic.getCarrier().getCode());
                stmt.setInt(3, statistic.getYearMonth().getYear());
                stmt.setInt(4, statistic.getYearMonth().getMonthValue());
                
                stmt.setInt(5, statistic.getCancelledFlightCount());
                stmt.setInt(6, statistic.getOnTimeFlightCount());
                stmt.setInt(7, statistic.getDelayedFlightCount());
                stmt.setInt(8, statistic.getDivertedFlightCount());
                stmt.setInt(9, statistic.getTotalFlightCount());
                
                stmt.setInt(10, statistic.getLateAircraftDelayCount());
                stmt.setInt(11, statistic.getWeatherDelayCount());
                stmt.setInt(12, statistic.getSecurityDelayCount());
                stmt.setInt(13, statistic.getNationalAviationSystemDelayCount());
                stmt.setInt(14, statistic.getCarrierDelayCount());
                
                stmt.setInt(15, statistic.getLateAircraftDelayTime());
                stmt.setInt(16, statistic.getWeatherDelayTime());
                stmt.setInt(17, statistic.getSecurityDelayTime());
                stmt.setInt(18, statistic.getNationalAviationSystemDelayTime());
                stmt.setInt(19, statistic.getCarrierDelayTime());
                stmt.setInt(20, statistic.getTotalDelayTime());

                stmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == statistics.size()) {
                    stmt.executeBatch();
                }
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateStatistic(Statistic statistic) throws SQLException {
        List<Statistic> statistics = new ArrayList<>();
        statistics.add(statistic);
        updateStatistics(statistics);
    }

    public void updateStatistics(List<Statistic> statistics) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "UPDATE stats "
                     + "SET cancelledFlightCount = ?, onTimeFlightCount = ?, delayedFlightCount = ?, divertedFlightCount = ?, totalFlightCount = ?,"
                     + "lateAircraftDelayCount = ?, weatherDelayCount = ?, securityDelayCount = ?, nationalAviationSystemDelayCount = ?, carrierDelayCount = ?,"
                     + "lateAircraftDelayTime = ?, weatherDelayTime = ?, securityDelayTime = ?, nationalAviationSystemDelayTime = ?, carrierDelayTime = ?, totalDelayTime = ? "
                     + "WHERE airport = ? AND carrier = ? AND year = ? AND month = ?;";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);

            int i = 0;
            for (Statistic statistic : statistics) {
                stmt.setInt(1, statistic.getCancelledFlightCount());
                stmt.setInt(2, statistic.getOnTimeFlightCount());
                stmt.setInt(3, statistic.getDelayedFlightCount());
                stmt.setInt(4, statistic.getDivertedFlightCount());
                stmt.setInt(5, statistic.getTotalFlightCount());
                
                stmt.setInt(6, statistic.getLateAircraftDelayCount());
                stmt.setInt(7, statistic.getWeatherDelayCount());
                stmt.setInt(8, statistic.getSecurityDelayCount());
                stmt.setInt(9, statistic.getNationalAviationSystemDelayCount());
                stmt.setInt(10, statistic.getCarrierDelayCount());
                
                stmt.setInt(11, statistic.getLateAircraftDelayTime());
                stmt.setInt(12, statistic.getWeatherDelayTime());
                stmt.setInt(13, statistic.getSecurityDelayTime());
                stmt.setInt(14, statistic.getNationalAviationSystemDelayTime());
                stmt.setInt(15, statistic.getCarrierDelayTime());
                stmt.setInt(16, statistic.getTotalDelayTime());
                
                stmt.setString(17, statistic.getAirport().getCode());
                stmt.setString(18, statistic.getCarrier().getCode());
                stmt.setInt(19, statistic.getYearMonth().getYear());
                stmt.setInt(20, statistic.getYearMonth().getMonthValue());

                stmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == statistics.size()) {
                    stmt.executeBatch();
                }
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteStatistic(Airport airport, Carrier carrier, YearMonth yearMonth) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "DELETE FROM stats "
                     + "WHERE airport = ? AND carrier = ? AND year = ? AND month = ?;";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);
            stmt.setString(1, airport.getCode());
            stmt.setString(2, carrier.getCode());
            stmt.setInt(3, yearMonth.getYear());
            stmt.setInt(4, yearMonth.getMonthValue());
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteStatistics(List<Statistic> statistics) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "DELETE FROM stats "
                     + "SET cancelledFlightCount = ?, onTimeFlightCount = ?, delayedFlightCount = ?, divertedFlightCount = ?, totalFlightCount = ?,"
                     + "lateAircraftDelayCount = ?, weatherDelayCount = ?, securityDelayCount = ?, nationalAviationSystemDelayCount = ?, carrierDelayCount = ?,"
                     + "lateAircraftDelayTime = ?, weatherDelayTime = ?, securityDelayTime = ?, nationalAviationSystemDelayTime = ?, carrierDelayTime = ?, totalDelayTime = ? "
                     + "WHERE airport = ? AND carrier = ? AND year = ? AND month = ?;";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);

            int i = 0;
            for (Statistic statistic : statistics) {
                stmt.setInt(1, statistic.getCancelledFlightCount());
                stmt.setInt(2, statistic.getOnTimeFlightCount());
                stmt.setInt(3, statistic.getDelayedFlightCount());
                stmt.setInt(4, statistic.getDivertedFlightCount());
                stmt.setInt(5, statistic.getTotalFlightCount());
                
                stmt.setInt(6, statistic.getLateAircraftDelayCount());
                stmt.setInt(7, statistic.getWeatherDelayCount());
                stmt.setInt(8, statistic.getSecurityDelayCount());
                stmt.setInt(9, statistic.getNationalAviationSystemDelayCount());
                stmt.setInt(10, statistic.getCarrierDelayCount());
                
                stmt.setInt(11, statistic.getLateAircraftDelayTime());
                stmt.setInt(12, statistic.getWeatherDelayTime());
                stmt.setInt(13, statistic.getSecurityDelayTime());
                stmt.setInt(14, statistic.getNationalAviationSystemDelayTime());
                stmt.setInt(15, statistic.getCarrierDelayTime());
                stmt.setInt(16, statistic.getTotalDelayTime());
                
                stmt.setString(17, statistic.getAirport().getCode());
                stmt.setString(18, statistic.getCarrier().getCode());
                stmt.setInt(19, statistic.getYearMonth().getYear());
                stmt.setInt(20, statistic.getYearMonth().getMonthValue());

                stmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == statistics.size()) {
                    stmt.executeBatch();
                }
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private List<Statistic> getStatistics(Airport airport, Carrier carrier, Integer year, Integer month) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Statistic> statistics = new ArrayList<>();

        String query;
        
        if (year != null && month != null) {
            query = "SELECT * "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND year = ? AND month = ?;";
        } else if (year != null) {
            query = "SELECT * "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND year = ?;";
        } else if (month != null) {
            query = "SELECT * "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND month = ?;";
        } else {
            query = "SELECT * "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ?;";
        }
        
        try {  
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);
            stmt.setString(1, airport.getCode());
            stmt.setString(2, carrier.getCode());
            
            if (year != null) stmt.setInt(3, year);
            if (month != null && year == null) {
                stmt.setInt(3, month);
            } else if (month != null) {
                stmt.setInt(4, month);
            }
            
            rs = stmt.executeQuery();

            while (rs.next()) {
                YearMonth yearMonth = YearMonth.of(rs.getInt("year"), rs.getInt("month"));
                Statistic statistic = new Statistic(airport, carrier, yearMonth);

                statistic.setCancelledFlightCount(rs.getInt("cancelledFlightCount"));
                statistic.setOnTimeFlightCount(rs.getInt("onTimeFlightCount"));
                statistic.setDelayedFlightCount(rs.getInt("delayedFlightCount"));
                statistic.setDivertedFlightCount(rs.getInt("divertedFlightCount"));
                statistic.setTotalFlightCount(rs.getInt("totalFlightCount"));

                statistic.setLateAircraftDelayCount(rs.getInt("lateAircraftDelayCount"));
                statistic.setWeatherDelayCount(rs.getInt("weatherDelayCount"));
                statistic.setSecurityDelayCount(rs.getInt("securityDelayCount"));
                statistic.setNationalAviationSystemDelayCount(rs.getInt("nationalAviationSystemDelayCount"));
                statistic.setCarrierDelayCount(rs.getInt("carrierDelayCount"));

                statistic.setLateAircraftDelayTime(rs.getInt("lateAircraftDelayTime"));
                statistic.setWeatherDelayTime(rs.getInt("weatherDelayTime"));
                statistic.setSecurityDelayTime(rs.getInt("securityDelayTime"));
                statistic.setNationalAviationSystemDelayTime(rs.getInt("nationalAviationSystemDelayTime"));
                statistic.setCarrierDelayTime(rs.getInt("carrierDelayTime"));
                statistic.setTotalDelayTime(rs.getInt("totalDelayTime"));

                statistics.add(statistic);
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return statistics;
    }

    public Statistic getStatistic(Airport airport, Carrier carrier, YearMonth yearMonth) throws SQLException {
        List<Statistic> statistics = getStatistics(airport, carrier, yearMonth.getYear(), yearMonth.getMonthValue());
        Statistic statistic = statistics.get(0);

        return statistic;
    }
    
    public List<Statistic> getStatisticsInYear(Airport airport, Carrier carrier, int year) throws SQLException {
        return getStatistics(airport, carrier, year, null);
    }
    
    public List<Statistic> getStatisticsInMonth(Airport airport, Carrier carrier, int month) throws SQLException {
        return getStatistics(airport, carrier, null, month);
    }

    public List<Statistic> getStatistics(Airport airport, Carrier carrier) throws SQLException {
        return getStatistics(airport, carrier, null, null);
    }


    private List<Statistic> getStatisticsFlights(Airport airport, Carrier carrier, Integer year, Integer month) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Statistic> statistics = new ArrayList<>();

        String query;

        if (year != null && month != null) {
            query = "SELECT flights "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND year = ? AND month = ? AND reason = ?;";
        } else if (year != null) {
            query = "SELECT flights "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND year = ? AND reason = ?;";
        } else if (month != null) {
            query = "SELECT flights "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND month = ? AND reason = ?;";
        } else {
            query = "SELECT flights "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND reason = ?;";
        }

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);
            stmt.setString(1, airport.getCode());
            stmt.setString(2, carrier.getCode());

            if (year != null) stmt.setInt(3, year);
            if (month != null && year == null) {
                stmt.setInt(3, month);
            } else if (month != null) {
                stmt.setInt(4, month);
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                YearMonth yearMonth = YearMonth.of(rs.getInt("year"), rs.getInt("month"));
                Statistic statistic = new Statistic(airport, carrier, yearMonth);

                statistic.setCancelledFlightCount(rs.getInt("cancelledFlightCount"));
                statistic.setOnTimeFlightCount(rs.getInt("onTimeFlightCount"));
                statistic.setDelayedFlightCount(rs.getInt("delayedFlightCount"));
                statistic.setDivertedFlightCount(rs.getInt("divertedFlightCount"));
                statistic.setTotalFlightCount(rs.getInt("totalFlightCount"));


                statistics.add(statistic);
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return statistics;
    }

    public Statistic getStatisticFlights(Airport airport, Carrier carrier, YearMonth yearMonth) throws SQLException {
        List<Statistic> statistics = getStatisticsFlights(airport, carrier, yearMonth.getYear(), yearMonth.getMonthValue());
        Statistic statistic = statistics.get(0);

        return statistic;
    }

    public List<Statistic> getStatisticsInYearFlights(Airport airport, Carrier carrier, int year) throws SQLException {
        return getStatisticsFlights(airport, carrier, year, null);
    }

    public List<Statistic> getStatisticsInMonthFlights(Airport airport, Carrier carrier, int month) throws SQLException {
        return getStatisticsFlights(airport, carrier, null, month);
    }

    public List<Statistic> getStatisticsFlights(Airport airport, Carrier carrier) throws SQLException {
        return getStatisticsFlights(airport, carrier, null, null);
    }

    private List<Statistic> getStatisticsDelayTimes(Airport airport, Carrier carrier, Integer year, Integer month) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Statistic> statistics = new ArrayList<>();

        String query;

        if (year != null && month != null) {
            query = "SELECT minutes-delayed "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND year = ? AND month = ? AND reason = ?;";
        } else if (year != null) {
            query = "SELECT minutes-delayed"
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND year = ? AND reason = ?;";
        } else if (month != null) {
            query = "SELECT minutes-delayed "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND month = ? AND reason = ?;";
        } else {
            query = "SELECT minutes-delayed "
                    + "FROM stats "
                    + "WHERE airport = ? AND carrier = ? AND reason = ?;";
        }

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);
            stmt.setString(1, airport.getCode());
            stmt.setString(2, carrier.getCode());

            if (year != null) stmt.setInt(3, year);
            if (month != null && year == null) {
                stmt.setInt(3, month);
            } else if (month != null) {
                stmt.setInt(4, month);
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                YearMonth yearMonth = YearMonth.of(rs.getInt("year"), rs.getInt("month"));
                Statistic statistic = new Statistic(airport, carrier, yearMonth);

                statistic.setLateAircraftDelayTime(rs.getInt("lateAircraftDelayTime"));
                statistic.setWeatherDelayTime(rs.getInt("weatherDelayTime"));
                statistic.setSecurityDelayTime(rs.getInt("securityDelayTime"));
                statistic.setNationalAviationSystemDelayTime(rs.getInt("nationalAviationSystemDelayTime"));
                statistic.setCarrierDelayTime(rs.getInt("carrierDelayTime"));
                statistic.setTotalDelayTime(rs.getInt("totalDelayTime"));


                statistics.add(statistic);
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return statistics;
    }

    public Statistic getStatisticDelayTimes(Airport airport, Carrier carrier, YearMonth yearMonth) throws SQLException {
        List<Statistic> statistics = getStatisticsFlights(airport, carrier, yearMonth.getYear(), yearMonth.getMonthValue());
        Statistic statistic = statistics.get(0);

        return statistic;
    }

    public List<Statistic> getStatisticsInYearDelayTimes(Airport airport, Carrier carrier, int year) throws SQLException {
        return getStatisticsDelayTimes(airport, carrier, year, null);
    }

    public List<Statistic> getStatisticsInMonthDelayTimes(Airport airport, Carrier carrier, int month) throws SQLException {
        return getStatisticsDelayTimes(airport, carrier, null, month);
    }

    public List<Statistic> getStatisticsDelayTimes(Airport airport, Carrier carrier) throws SQLException {
        return getStatisticsDelayTimes(airport, carrier, null, null);
    }

    public List<ExtraStatistic> getExtraStatistics(Airport airport1, Airport airport2, Carrier carrier) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ExtraStatistic> extraStatistics = new ArrayList<>();

        String query;

        if (carrier != null) {
            query = "SELECT *"
                    + "FROM extraStats"
                    + "WHERE airport1 = ? AND airport2 = ? AND carrier = ?;";
        } else {
            query = "SELECT *"
                    + "FROM extraStats"
                    + "WHERE airport1 = ? AND airport2 = ?;";
        }


        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);
            stmt.setString(1, airport1.getCode());
            stmt.setString(2, airport2.getCode());

            if (carrier != null) {
                stmt.setString(2, carrier.getCode());
            }


            rs = stmt.executeQuery();

            while (rs.next()) {
                ExtraStatistic extraStatistic = new ExtraStatistic(airport1, airport2, carrier);

                extraStatistic.setLateAircraftDelaysTimedMean(rs.getInt("lateAircraftDelaysTimedMean"));
                extraStatistic.setLateAircraftDelaysTimedMed(rs.getInt("lateAircraftDelaysTimedMed"));
                extraStatistic.setLateAircraftDelaysTimedSd(rs.getInt("lateAircraftDelaysTimedSd"));
                extraStatistic.setCarrierAircraftDelaysTimedMean(rs.getInt("carrierAircraftDelaysTimedMean"));
                extraStatistic.setCarrierAircraftDelaysTimedMed(rs.getInt("carrierAircraftDelaysTimedMed"));
                extraStatistic.setCarrierAircraftDelaysTimedSd(rs.getInt("carrierAircraftDelaysTimedSd"));

                extraStatistics.add(extraStatistic);
            }
        } catch (SQLException e) {
            // Handle errors for JDBC.
            throw e;
        } finally {
            // finally block used to close resources.
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return extraStatistics;
    }

    public void close() {
        cpds.close();
    }
}
