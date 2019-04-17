package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import converters.StatisticDataSelectorHelper;
import models.*;

public class DatabaseConnector {
    // Database URL, and database credentials.
    private final String DB_URL = "jdbc:mysql://localhost:3306/webengdb?serverTimezone=" + TimeZone.getDefault().getID();
    private final String USER = "webenguser";
    private final String PASS = "webengpass123";

    private Map<String, String> airports;
    private Map<String, String> carriers;

    private ComboPooledDataSource cpds;

    public DatabaseConnector() throws SQLException {
        cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl(DB_URL);
        cpds.setUser(USER);                                  
        cpds.setPassword(PASS);

        createAirportsTable();
        createCarriersTable();
        createStatsTable();
        createExtraStatsTable();

        airports = new HashMap<>();
        for (Airport airport : getAirports()) {
            airports.put(airport.getCode(), airport.getName());
        }

        carriers = new HashMap<>();
        for (Carrier carrier : getCarriers()) {
            carriers.put(carrier.getCode(), carrier.getName());
        }
    }

    private void createAirportsTable() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        String query = "CREATE TABLE IF NOT EXISTS airports (" +
                "code CHAR(3) PRIMARY KEY, " +
                "name VARCHAR(255)" +
                ");";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
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

        String query = "CREATE TABLE IF NOT EXISTS carriers (" +
                "code CHAR(2) PRIMARY KEY," +
                "name VARCHAR(255)" +
                ");";

        try {  
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
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

        String query = "CREATE TABLE IF NOT EXISTS stats (" +
                "airport CHAR(3)," +
                "carrier CHAR(2)," +
                "year YEAR," +
                "month TINYINT(2) UNSIGNED," +

                "cancelledCount MEDIUMINT UNSIGNED NOT NULL," +
                "onTimeCount MEDIUMINT UNSIGNED NOT NULL," +
                "delayedCount MEDIUMINT UNSIGNED NOT NULL," +
                "divertedCount MEDIUMINT UNSIGNED NOT NULL," +
                "totalCount MEDIUMINT UNSIGNED NOT NULL," +

                "lateAircraftCount MEDIUMINT UNSIGNED NOT NULL," +
                "carrierCount MEDIUMINT UNSIGNED NOT NULL," +
                "weatherCount MEDIUMINT UNSIGNED NOT NULL," +
                "securityCount MEDIUMINT UNSIGNED NOT NULL," +
                "nationalAviationSystemCount MEDIUMINT UNSIGNED NOT NULL," +

                "lateAircraftTime MEDIUMINT UNSIGNED NOT NULL," +
                "carrierTime MEDIUMINT UNSIGNED NOT NULL," +
                "weatherTime MEDIUMINT UNSIGNED NOT NULL," +
                "securityTime MEDIUMINT UNSIGNED NOT NULL," +
                "nationalAviationSystemTime MEDIUMINT UNSIGNED NOT NULL," +
                "totalTime MEDIUMINT UNSIGNED NOT NULL," +

                "PRIMARY KEY (airport, carrier, year, month)" +
                ");";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
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

        String query = "CREATE TABLE IF NOT EXISTS extraStats (" +
                "airport1 CHAR(3)," +
                "airport2 CHAR(3)," +
                "carrier CHAR(2)," +

                "lateAircraftTimeMean FLOAT UNSIGNED NOT NULL," +
                "lateAircraftTimeMedian MEDIUMINT UNSIGNED NOT NULL," +
                "lateAircraftTimeSd FLOAT UNSIGNED NOT NULL," +

                "carrierTimeMean FLOAT UNSIGNED NOT NULL," +
                "carrierTimeMedian MEDIUMINT UNSIGNED NOT NULL," +
                "carrierTimeSd FLOAT UNSIGNED NOT NULL," +

                "PRIMARY KEY (airport1, airport2, carrier)" +
                ");";

        try {  
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
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

    public void addAirports(List<Airport> airports) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "INSERT INTO airports (code, name) " +
                "VALUES (?, ?);";

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
        ResultSet rs;
        List<Airport> airports = new ArrayList<>();

        String query = "SELECT * " +
                "FROM airports;";

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

    public void addCarriers(List<Carrier> carriers) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "INSERT INTO carriers (code, name) " +
                "VALUES (?, ?);";

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
        ResultSet rs;
        List<Carrier> carriers = new ArrayList<>();

        String query = "SELECT * " +
                "FROM carriers;";

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

    public boolean hasAirport(Airport airport) {
        return airports.containsKey(airport.getCode());
    }

    public boolean hasCarrier(Carrier carrier) {
        return carriers.containsKey(carrier.getCode());
    }

    public List<Carrier> getCarriersAtAirport(Airport airport) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs;
        List<Carrier> carriers = new ArrayList<>();

        String query = "SELECT DISTINCT carriers.code, carriers.name " +
                "FROM carriers " +
                "INNER JOIN stats " +
                "ON carriers.code = stats.carrier " +
                "WHERE stats.airport = ?;";

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

    public void addStatistics(List<Statistic> statistics) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "INSERT INTO stats (airport, carrier, year, month, cancelledCount, onTimeCount, delayedCount, " +
                "divertedCount, totalCount, lateAircraftCount, carrierCount, weatherCount, securityCount, " +
                "nationalAviationSystemCount, lateAircraftTime, carrierTime, weatherTime, securityTime, " +
                "nationalAviationSystemTime, totalTime) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);

            int i = 0;
            for (Statistic statistic : statistics) {
                if (!hasAirport(statistic.getAirport())) {
                    // Airport doesn't exist.
                    throw new SQLException();
                }

                if (!hasCarrier(statistic.getCarrier())) {
                    // Carrier doesn't exist.
                    throw new SQLException();
                }

                FlightData flightData = statistic.getFlightData();
                DelayData delayData = statistic.getDelayData();
                DelayTimeData delayTimeData = statistic.getDelayTimeData();

                stmt.setString(1, statistic.getAirport().getCode());
                stmt.setString(2, statistic.getCarrier().getCode());
                stmt.setInt(3, statistic.getYear());
                stmt.setInt(4, statistic.getMonth());
                
                stmt.setInt(5, flightData.getCancelledCount());
                stmt.setInt(6, flightData.getOnTimeCount());
                stmt.setInt(7, flightData.getDelayedCount());
                stmt.setInt(8, flightData.getDivertedCount());
                stmt.setInt(9, flightData.getTotalCount());
                
                stmt.setInt(10, delayData.getLateAircraftCount());
                stmt.setInt(11, delayData.getCarrierCount());
                stmt.setInt(12, delayData.getWeatherCount());
                stmt.setInt(13, delayData.getSecurityCount());
                stmt.setInt(14, delayData.getNationalAviationSystemCount());
                
                stmt.setInt(15, delayTimeData.getLateAircraftTime());
                stmt.setInt(16, delayTimeData.getCarrierTime());
                stmt.setInt(17, delayTimeData.getWeatherTime());
                stmt.setInt(18, delayTimeData.getSecurityTime());
                stmt.setInt(19, delayTimeData.getNationalAviationSystemTime());
                stmt.setInt(20, delayTimeData.getTotalTime());

                stmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == statistics.size()) {
                    stmt.executeBatch();
                }
            }
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

    public List<Statistic> getStatistics(Airport airport, Carrier carrier, Integer year, Integer month,
                                         StatisticDataSelectorHelper dataSelector) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs;

        List<Statistic> statistics = new ArrayList<>();

        String query = "SELECT ";

        if (dataSelector.withAirport() && airport == null) {
            query += "airport, ";
        }

        if (dataSelector.withCarrier() && carrier == null) {
            query += "carrier, ";
        }

        if (dataSelector.withYear() && year == null) {
            query += "year, ";
        }

        if (dataSelector.withMonth() && month == null) {
            query += "month, ";
        }

        if (dataSelector.withFlightData()) {
            if (dataSelector.withCancelledCount()) {
                query += "cancelledCount, ";
            }

            if (dataSelector.withOnTimeCount()) {
                query += "onTimeCount, ";
            }

            if (dataSelector.withDelayedCount()) {
                query += "delayedCount, ";
            }

            if (dataSelector.withDivertedCount()) {
                query += "divertedCount, ";
            }

            if (dataSelector.withTotalCount()) {
                query += "totalCount, ";
            }
        }

        if (dataSelector.withDelayData()) {
            if (dataSelector.withLateAircraftCount()) {
                query += "lateAircraftCount, ";
            }

            if (dataSelector.withCarrierCount()) {
                query += "carrierCount, ";
            }

            if (dataSelector.withWeatherCount()) {
                query += "weatherCount, ";
            }

            if (dataSelector.withSecurityCount()) {
                query += "securityCount, ";
            }

            if (dataSelector.withNationalAviationSystemCount()) {
                query += "nationalAviationSystemCount, ";
            }
        }

        if (dataSelector.withDelayTimeData()) {
            if (dataSelector.withLateAircraftTime()) {
                query += "lateAircraftTime, ";
            }

            if (dataSelector.withCarrierTime()) {
                query += "carrierTime, ";
            }

            if (dataSelector.withWeatherTime()) {
                query += "weatherTime, ";
            }

            if (dataSelector.withSecurityTime()) {
                query += "securityTime, ";
            }

            if (dataSelector.withNationalAviationSystemTime()) {
                query += "nationalAviationSystemTime, ";
            }

            if (dataSelector.withTotalTime()) {
                query += "totalTime, ";
            }
        }

        // Remove last space and comma from string.
        query = query.substring(0, query.length() - 2);

        query += " FROM stats WHERE ";

        if (airport != null) {
            query += "airport = ? AND ";
        }

        if (carrier != null) {
            query += "carrier = ? AND ";
        }

        if (year != null) {
            query += "year = ? AND ";
        }

        if (month != null) {
            query += "month = ?";
        } else {
            // Remove space and AND.
            query = query.substring(0, query.length() - 5);
            query += ";";
        }

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);

            int i = 1;

            if (airport != null) {
                stmt.setString(i++, airport.getCode());
            }

            if (carrier != null) {
                stmt.setString(i++, carrier.getCode());
            }

            if (year != null) {
                stmt.setInt(i++, year);
            }

            if (month != null) {
                stmt.setInt(i, month);
            }

            rs = stmt.executeQuery();

            Airport tempAirport = (dataSelector.withAirport()) ? airport : null;
            Carrier tempCarrier = (dataSelector.withCarrier()) ? carrier : null;
            Integer tempYear = (dataSelector.withYear()) ? year : null;
            Integer tempMonth = (dataSelector.withMonth()) ? month : null;

            while (rs.next()) {
                FlightData flightData = null;
                DelayData delayData = null;
                DelayTimeData delayTimeData = null;

                if (dataSelector.withAirport() && airport == null) {
                    String code = rs.getString("airport");
                    tempAirport = new Airport(code, airports.get(code));
                }

                if (dataSelector.withCarrier() && carrier == null) {
                    String code = rs.getString("carrier");
                    tempCarrier = new Carrier(code, carriers.get(code));
                }

                if (dataSelector.withYear() && year == null) {
                    tempYear = rs.getInt("year");
                }

                if (dataSelector.withMonth() && month == null) {
                    tempMonth = rs.getInt("month");
                }

                if (dataSelector.withFlightData()) {
                    int cancelledCount = 0;
                    int onTimeCount = 0;
                    int delayedCount = 0;
                    int divertedCount = 0;
                    int totalCount = 0;

                    if (dataSelector.withCancelledCount()) {
                        cancelledCount = rs.getInt("cancelledCount");
                    }

                    if (dataSelector.withOnTimeCount()) {
                        onTimeCount = rs.getInt("onTimeCount");
                    }

                    if (dataSelector.withDelayedCount()) {
                        delayedCount = rs.getInt("delayedCount");
                    }

                    if (dataSelector.withDivertedCount()) {
                        divertedCount = rs.getInt("divertedCount");
                    }

                    if (dataSelector.withTotalCount()) {
                        totalCount = rs.getInt("totalCount");
                    }

                    flightData = new FlightData(cancelledCount, onTimeCount, delayedCount, divertedCount, totalCount);
                }

                if (dataSelector.withDelayData()) {
                    int lateAircraftCount = 0;
                    int carrierCount = 0;
                    int weatherCount = 0;
                    int securityCount = 0;
                    int nationalAviationSystemCount = 0;

                    if (dataSelector.withLateAircraftCount()) {
                        lateAircraftCount = rs.getInt("lateAircraftCount");
                    }

                    if (dataSelector.withCarrierCount()) {
                        carrierCount = rs.getInt("carrierCount");
                    }

                    if (dataSelector.withWeatherCount()) {
                        weatherCount = rs.getInt("weatherCount");
                    }

                    if (dataSelector.withSecurityCount()) {
                        securityCount = rs.getInt("securityCount");
                    }

                    if (dataSelector.withNationalAviationSystemCount()) {
                        nationalAviationSystemCount = rs.getInt("nationalAviationSystemCount");
                    }

                    delayData = new DelayData(lateAircraftCount, carrierCount, weatherCount, securityCount,
                            nationalAviationSystemCount);
                }

                if (dataSelector.withDelayTimeData()) {
                    int lateAircraftTime = 0;
                    int carrierTime = 0;
                    int weatherTime = 0;
                    int securityTime = 0;
                    int nationalAviationSystemTime = 0;
                    int totalTime = 0;

                    if (dataSelector.withLateAircraftTime()) {
                        lateAircraftTime = rs.getInt("lateAircraftTime");
                    }

                    if (dataSelector.withCarrierTime()) {
                        carrierTime = rs.getInt("carrierTime");
                    }

                    if (dataSelector.withWeatherTime()) {
                        weatherTime = rs.getInt("weatherTime");
                    }

                    if (dataSelector.withSecurityTime()) {
                        securityTime = rs.getInt("securityTime");
                    }

                    if (dataSelector.withNationalAviationSystemTime()) {
                        nationalAviationSystemTime = rs.getInt("nationalAviationSystemTime");
                    }

                    if (dataSelector.withTotalTime()) {
                        totalTime = rs.getInt("totalTime");
                    }

                    delayTimeData = new DelayTimeData(lateAircraftTime, carrierTime, weatherTime, securityTime,
                            nationalAviationSystemTime, totalTime);
                }

                Statistic statistic = new Statistic(tempAirport, tempCarrier, tempYear, tempMonth, flightData, delayData,
                        delayTimeData);

                statistics.add(statistic);
            }
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

    public void updateStatistics(List<Statistic> statistics) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "INSERT INTO stats (airport, carrier, year, month, cancelledCount, onTimeCount, delayedCount, " +
                "divertedCount, totalCount, lateAircraftCount, carrierCount, weatherCount, securityCount, " +
                "nationalAviationSystemCount, lateAircraftTime, carrierTime, weatherTime, securityTime, " +
                "nationalAviationSystemTime, totalTime) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE cancelledCount = ?, onTimeCount = ?, delayedCount = ?, divertedCount = ?, " +
                "totalCount = ?, lateAircraftCount = ?, carrierCount = ?, weatherCount = ?, securityCount = ?, " +
                "nationalAviationSystemCount = ?, lateAircraftTime = ?, carrierTime = ?, weatherTime = ?, " +
                "securityTime = ?, nationalAviationSystemTime = ?, totalTime = ?;";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);

            int i = 0;
            for (Statistic statistic : statistics) {
                if (!airports.containsKey(statistic.getAirport().getCode())) {
                    // Airport doesn't exist.
                    throw new SQLException();
                }

                if (!carriers.containsKey(statistic.getCarrier().getCode())) {
                    // Carrier doesn't exist.
                    throw new SQLException();
                }

                FlightData flightData = statistic.getFlightData();
                DelayData delayData = statistic.getDelayData();
                DelayTimeData delayTimeData = statistic.getDelayTimeData();

                stmt.setString(1, statistic.getAirport().getCode());
                stmt.setString(2, statistic.getCarrier().getCode());
                stmt.setInt(3, statistic.getYear());
                stmt.setInt(4, statistic.getMonth());

                stmt.setInt(5, flightData.getCancelledCount());
                stmt.setInt(6, flightData.getOnTimeCount());
                stmt.setInt(7, flightData.getDelayedCount());
                stmt.setInt(8, flightData.getDivertedCount());
                stmt.setInt(9, flightData.getTotalCount());

                stmt.setInt(10, delayData.getLateAircraftCount());
                stmt.setInt(11, delayData.getCarrierCount());
                stmt.setInt(12, delayData.getWeatherCount());
                stmt.setInt(13, delayData.getSecurityCount());
                stmt.setInt(14, delayData.getNationalAviationSystemCount());
                
                stmt.setInt(15, delayTimeData.getLateAircraftTime());
                stmt.setInt(16, delayTimeData.getCarrierTime());
                stmt.setInt(17, delayTimeData.getWeatherTime());
                stmt.setInt(18, delayTimeData.getSecurityTime());
                stmt.setInt(19, delayTimeData.getNationalAviationSystemTime());
                stmt.setInt(20, delayTimeData.getTotalTime());

                stmt.setInt(21, flightData.getCancelledCount());
                stmt.setInt(22, flightData.getOnTimeCount());
                stmt.setInt(23, flightData.getDelayedCount());
                stmt.setInt(24, flightData.getDivertedCount());
                stmt.setInt(25, flightData.getTotalCount());

                stmt.setInt(26, delayData.getLateAircraftCount());
                stmt.setInt(27, delayData.getCarrierCount());
                stmt.setInt(28, delayData.getWeatherCount());
                stmt.setInt(29, delayData.getSecurityCount());
                stmt.setInt(30, delayData.getNationalAviationSystemCount());

                stmt.setInt(31, delayTimeData.getLateAircraftTime());
                stmt.setInt(32, delayTimeData.getCarrierTime());
                stmt.setInt(33, delayTimeData.getWeatherTime());
                stmt.setInt(34, delayTimeData.getSecurityTime());
                stmt.setInt(35, delayTimeData.getNationalAviationSystemTime());
                stmt.setInt(36, delayTimeData.getTotalTime());

                stmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == statistics.size()) {
                    stmt.executeBatch();
                }
            }
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

    public void deleteStatistics(Airport airport, Carrier carrier, Integer year, Integer month) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query;
        if (year != null && month != null) {
            query = "DELETE FROM stats " +
                    "WHERE airport = ? AND carrier = ? AND year = ? AND month = ?;";
        } else if (year != null) {
            query = "DELETE FROM stats " +
                    "WHERE airport = ? AND carrier = ? AND year = ?;";
        } else if (month != null) {
            query = "DELETE FROM stats " +
                    "WHERE airport = ? AND carrier = ? AND month = ?;";
        } else {
            query = "DELETE FROM stats " +
                    "WHERE airport = ? AND carrier = ?;";
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

    public void addExtraStatistics(List<ExtraStatistic> extraStatistics) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "INSERT INTO extraStats (airport1, airport2, carrier, lateAircraftTimeMean, " +
                "lateAircraftTimeMedian, lateAircraftTimeSd, carrierTimeMean, carrierTimeMedian, carrierTimeSd) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);

            int i = 0;
            for (ExtraStatistic extraStatistic : extraStatistics) {
                if (!hasAirport(extraStatistic.getAirport1()) || !hasAirport(extraStatistic.getAirport2())) {
                    // Airport 1 or 2 doesn't exist.
                    throw new SQLException();
                }

                if (!hasCarrier(extraStatistic.getCarrier())) {
                    // Carrier doesn't exist.
                    throw new SQLException();
                }

                stmt.setString(1, extraStatistic.getAirport1().getCode());
                stmt.setString(2, extraStatistic.getAirport2().getCode());
                stmt.setString(3, extraStatistic.getCarrier().getCode());

                stmt.setFloat(4, extraStatistic.getLateAircraftTimeMean());
                stmt.setFloat(5, extraStatistic.getLateAircraftTimeMedian());
                stmt.setFloat(6, extraStatistic.getLateAircraftTimeSd());

                stmt.setFloat(7, extraStatistic.getCarrierTimeMean());
                stmt.setFloat(8, extraStatistic.getCarrierTimeMedian());
                stmt.setFloat(9, extraStatistic.getCarrierTimeSd());

                stmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == extraStatistics.size()) {
                    stmt.executeBatch();
                }
            }
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

    public List<ExtraStatistic> getExtraStatistics(Airport airport1, Airport airport2, Carrier carrier)
            throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs;
        List<ExtraStatistic> extraStatistics = new ArrayList<>();

        if (airport1 == null || airport2 == null) {
            throw new SQLException();
        }

        String query;
        if (carrier != null) {
            query = "SELECT * " +
                    "FROM extraStats " +
                    "WHERE airport1 = ? and airport2 = ? AND carrier = ?;";
        } else {
            query = "SELECT * " +
                    "FROM extraStats " +
                    "WHERE airport1 = ? and airport2 = ?;";
        }

        try {
            // Get connection from pool.
            conn = cpds.getConnection();

            // Execute query.
            stmt = conn.prepareStatement(query);
            stmt.setString(1, airport1.getCode());
            stmt.setString(2, airport2.getCode());
            if (carrier != null) {
                stmt.setString(3, carrier.getCode());
            }

            rs = stmt.executeQuery();

            Carrier tempCarrier = null;

            while (rs.next()) {
                if (carrier == null) {
                    String code = rs.getString("carrier");
                    tempCarrier = new Carrier(code, carriers.get(code));
                }

                ExtraStatistic extraStatistic = new ExtraStatistic(airport1, airport2, tempCarrier);

                extraStatistic.setLateAircraftTimeMean(rs.getFloat("lateAircraftTimeMean"));
                extraStatistic.setLateAircraftTimeMedian(rs.getFloat("lateAircraftTimeMedian"));
                extraStatistic.setLateAircraftTimeSd(rs.getFloat("lateAircraftTimeSd"));

                extraStatistic.setCarrierTimeMean(rs.getFloat("carrierTimeMean"));
                extraStatistic.setCarrierTimeMedian(rs.getFloat("carrierTimeMedian"));
                extraStatistic.setCarrierTimeSd(rs.getFloat("carrierTimeSd"));

                extraStatistics.add(extraStatistic);
            }
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
