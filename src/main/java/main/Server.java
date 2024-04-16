package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import pms.database.DatabaseConnection;

public class Server extends UnicastRemoteObject implements ClientRequests {

    private static final int port = 3333;
    private static final String requestEndPoint = "ClientRequest";

    public Server() throws RemoteException {
    }

    public static void main(String[] args) {
        startServer();
        initializeUI();
    }

    public static void initializeUI() {
        // Display the IP Address of the server
        ServerUI UI = new ServerUI();

        String ipAddress = getServerIp();
        UI.lbIpAddress.setText(ipAddress);
        UI.lbIpAddress2.setText(ipAddress);
        UI.setVisible(true);

        DatabaseConfig dcg = new DatabaseConfig();
        if (!DatabaseConfig.isConnectionSuccess) {
            dcg.setVisible(true);
        }
    }

    private static void startServer() {

        try {
            Server obj = new Server();
            Registry Registry = LocateRegistry.createRegistry(port);
            Registry.rebind(requestEndPoint, obj);
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }

    private static String getServerIp() {
        String ipAddress = "localhost";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr.getHostAddress().length() <= 15) {//if a proper ip adress is found
                        ipAddress = addr.getHostAddress();
                    }
                    //System.out.println(iface.getDisplayName() + " " + ipAddress);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return ipAddress;
    }

    public String[] getDashboardInfo(int companyID) {
        try {
            String sql
                    = "SELECT * FROM \n"
                    + "(SELECT COUNT(*) IN_VEHICLES FROM PARKED_CAR WHERE COMPANY_ID = " + companyID + " AND DATE_EXIT IS NULL) A,  /*IN VEHICLES*/\n"
                    + "(SELECT COUNT(*) OUT_VEHICLES FROM PARKED_CAR WHERE COMPANY_ID = " + companyID + " AND DATE_EXIT IS NOT NULL /*AND DATE(DATE_ENTRY)=CURDATE()*/) B,  /*OUT VEHICLES*/\n"
                    + "(SELECT COUNT(*) IN_OUT_VEHICLES FROM PARKED_CAR WHERE COMPANY_ID = " + companyID + " /*AND DATE(DATE_ENTRY)=CURDATE()*/ /* IN & OUT VEHICLES*/) C,"
                    + "(SELECT IFNULL(FLOOR(FREE/TOTAL*100),0) PERCENT FROM ( "
                    + "SELECT COUNT(*) TOTAL "
                    + "FROM PARKING_LOT "
                    + "WHERE COMPANY_ID = " + companyID + ") A, "
                    + "(SELECT COUNT(*) FREE "
                    + "FROM PARKING_LOT "
                    + "WHERE COMPANY_ID = " + companyID + " "
                    + "  AND ID NOT IN (SELECT PARKING_LOT_ID "
                    + "             FROM PARKED_CAR WHERE COMPANY_ID = " + companyID + " "
                    + "             AND DATE_EXIT IS NULL) ) B ) D ";
            System.out.println(sql);
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new String[]{rs.getString("IN_VEHICLES"), rs.getString("OUT_VEHICLES"), rs.getString("IN_OUT_VEHICLES"), rs.getString("PERCENT")};
            }
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public String[] getDashboardAdmin(int companyID) {
        try {
            String sql
                    = "SELECT * FROM "
                    + "/* ACTIVE AND PASSIVE */ "
                    + "(SELECT SUM(CASE WHEN STATUS = TRUE THEN TOTAL ELSE 0 END ) AS ACTIVE, "
                    + "       SUM(CASE WHEN STATUS = FALSE THEN TOTAL ELSE 0 END )AS DISABLED, "
                    + "       SUM(IFNULL(TOTAL,0)) AS TOTAL FROM ( "
                    + "SELECT STATUS, COUNT(*) TOTAL FROM USER "
                    + "GROUP BY STATUS) V) USERS, "
                    + "/*** PARKING FACILITY AND SPACE ***/ "
                    + "(SELECT COMPANIES, PARKING FROM "
                    + "    (SELECT IFNULL(COUNT(*),0) COMPANIES FROM COMPANY) A, "
                    + "    (SELECT IFNULL(COUNT(*),0) PARKING FROM PARKING_LOT) B ) COMP ";
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new String[]{rs.getString("ACTIVE"), rs.getString("DISABLED"), rs.getString("TOTAL"), rs.getString("COMPANIES"), rs.getString("PARKING")};
            }
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public String greeting(int hour) {
        if (hour >= 5 && hour < 12) {
            return ("Good Morning,");
        } else if (hour >= 12 && hour < 18) {
            return ("Good Afternoon,");
        } else {
            return ("Good Evening,");
        }
    }

    public int registerParkingLot(String companyID, String parkingLotNO, String descrption) {
        String sql = "INSERT INTO PARKING_LOT (COMPANY_ID, PARKING_LOT_NO, DESCRIPTION) VALUES (" + companyID + ", '" + parkingLotNO + "', '" + descrption + "')";
        int result;
        try {
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            result = ps.executeUpdate();
            ps.close();

            return result;
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int parkACar(String companyID, String vehicleId, String parkingLotNO, String entryTime) {
        String sql = "insert into parked_car (company_id, vehicle_id, parking_lot_id, date_entry, date_exit) "
                + "values (" + companyID + ", " + vehicleId + ", (select max(id) from parking_lot where parking_lot.company_id = parked_car.company_id and "
                + "PARKING_LOT_NO = '" + parkingLotNO + "'), '" + entryTime + "', null)";
        int result;
        try {
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            result = ps.executeUpdate();
            ps.close();

            return result;
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int registerClient(String firstName, String middleName, String lastName, String gender, String address, String mobile, String dateOfBirth, String jobTitle, String tin, String email,
            String code, String licensePlate, String manufacturer, String model, String year) {

        String sqlDriver = "INSERT INTO DRIVER (FIRST_NAME, MIDDLE_NAME, LAST_NAME, GENDER, ADDRESS, MOBILE, DATE_OF_BIRTH, JOB_TITLE, TIN, EMAIL)"
                + "VALUES('" + firstName + "','" + middleName + "','" + lastName + "','" + gender + "','" + address + "','" + mobile + "', " + dateOfBirth + ",'" + jobTitle + "','" + tin + "','" + email + "')";
        int result1, result2;

        try {
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps1 = DatabaseConnection.getInstance().getConnection().prepareStatement(sqlDriver, Statement.RETURN_GENERATED_KEYS);
            result1 = ps1.executeUpdate();

            if (result1 == 1) {
                ResultSet generatedKeys = ps1.getGeneratedKeys();

                if (generatedKeys.next()) {
                    String sqlVehicle = "INSERT INTO VEHICLE (code, license_plate, manufacturer, model, year, driver_id)"
                            + "VALUES ( '" + code + "','" + licensePlate + "','" + manufacturer + "','" + model + "','" + year + "'," + generatedKeys.getInt(1) + ")";
                    PreparedStatement ps2 = DatabaseConnection.getInstance().getConnection().prepareStatement(sqlVehicle);
                    result2 = ps2.executeUpdate();
                    ps2.close();
                    if (result2 == 0) {/*If vehicle cannot be created Delete the Driver record*/
                        String sqlDeleteDriver = "DELETE FROM DRIVER WHERE ID = " + generatedKeys.getInt(1);
                        PreparedStatement ps3 = DatabaseConnection.getInstance().getConnection().prepareStatement(sqlDeleteDriver);
                        result2 = ps3.executeUpdate();
                        ps3.close();
                        return 0;
                        /* Return no success*/
                    }
                }
            }

            ps1.close();

            return result1;
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int registerUser(String username, String fullname, String password, String mobile, String role, String status, String company) {
        int result;
        status = status.equals("Enabled") ? "true" : "false";
        String sql = "insert into user (company_id, user_id, full_name, password, mobile, role, status) VALUES "
                + "((select id from company where company_name= '" + company + "'), '" + username + "', '" + fullname + "', 'NEW', '"
                + mobile + "', '" + role + "', " + status + ")";
        System.out.println(sql);
        try {
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            result = ps.executeUpdate();

            if (result == 1) {
                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()) {
                    String tempSql = "insert into user_detail (user_id, temp_password, temp_pass_flag, wrong_pass_count) values"
                            + "(" + generatedKeys.getInt(1) + ", md5('" + password + "'),true, 0) ";
                    PreparedStatement ps2 = DatabaseConnection.getInstance().getConnection().prepareStatement(tempSql);
                    int result2 = ps2.executeUpdate();
                    ps2.close();
                    if (result2 == 0) {/*If Temp pass cannot be created Delete the user record*/
                        String deleteUserSql = "DELETE FROM USER WHERE ID = " + generatedKeys.getInt(1);
                        PreparedStatement ps3 = DatabaseConnection.getInstance().getConnection().prepareStatement(deleteUserSql);
                        result2 = ps3.executeUpdate();
                        ps3.close();
                        return 0;
                        /* Return no success*/
                    }
                    return result2;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public int registerCompany(String companyName, String tinNo, String address1, String address2, String phoneNo, String feePerHr) {
        int result;
        String sql = "insert into company (company_name, tin_number, Address_1, Address_2, fee_per_hr, phone_no) values "
                + "('" + companyName + "', '" + tinNo + "', '" + address1 + "', '" + address2 + "', " + feePerHr + ", '" + phoneNo + "')";
        System.out.println(sql);
        try {
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            result = ps.executeUpdate();

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public int getTotalCount(String what, String[] parameters) {
        String sql;
        ResultSet rs;
        int count = 0;

        if (what.equals("parking_lot")) {

            sql = "select COUNT(*) total "
                    + "from parking_lot "
                    + "where company_id = " + parameters[0] + " "
                    + "and IFNULL(PARKING_LOT_NO,'') like '%" + parameters[1] + "%' "
                    + "and IFNULL(description,'') like '%" + parameters[2] + "%'  ";
        } else if (what.equals("customer")) {
            sql = "select COUNT(*) total from driver d "
                    + "join vehicle v on d.id = v.driver_id "
                    + "where CONCAT(first_name,' ',middle_name,' ',last_name) like '%" + parameters[0] + "%' "
                    + "and IFNULL(mobile,'') like '%" + parameters[1] + "%' "
                    + "and IFNULL(email,'') like '%" + parameters[2] + "%' "
                    + "and IFNULL(code,'') like '%" + parameters[3] + "%' "
                    + "and IFNULL(license_plate,'') like '%" + parameters[4] + "%' "
                    + "and IFNULL(manufacturer,'') like '%" + parameters[5] + "%' "
                    + "and IFNULL(model,'') like '%" + parameters[6] + "%' "
                    + "and IFNULL(year,'') like '%" + parameters[7] + "%' ";
        } else if (what.equals("parked")) {
            sql = "select COUNT(*) total  from parked_car pc "
                    + "join vehicle v on pc.vehicle_id = v.id "
                    + "join driver d on v.driver_id = d.id "
                    + "where pc.company_id = " + parameters[0] + " "
                    + "and CONCAT(first_name,' ',middle_name,' ',last_name) like '%" + parameters[1] + "%' "
                    + "and IFNULL(code,'') like '%" + parameters[2] + "%' "
                    + "and IFNULL(license_plate,'') like '%" + parameters[3] + "%' "
                    + "and IFNULL(year,'') like '%" + parameters[4] + "%' "
                    + "and date_exit is null";
        } else if (what.equals("exited")) {
            sql = "select COUNT(*) total  from parked_car pc "
                    + "join vehicle v on pc.vehicle_id = v.id "
                    + "join driver d on v.driver_id = d.id "
                    + "where pc.company_id = " + parameters[0] + " "
                    + "and CONCAT(first_name,' ',middle_name,' ',last_name) like '%" + parameters[1] + "%' "
                    + "and IFNULL(code,'') like '%" + parameters[2] + "%' "
                    + "and IFNULL(license_plate,'') like '%" + parameters[3] + "%' "
                    + "and date_exit is not null";
        } else if (what.equals("user")) {
            sql = "select count(*) total from user u "
                    + " join company c on u.company_id = c.id "
                    + " where u.user_id like '%" + parameters[1] + "%' "
                    + " and u.full_name like '%" + parameters[2] + "%' "
                    + " and c.company_name like '%" + parameters[3] + "%' "
                    + " and u.user_id <> '" + parameters[0] + "' "
                    + " order by full_name ";
        } else if (what.equals("company")) {
            sql = "select count(*) total from company "
                    + "where IFNULL(company_name, '') like '%" + parameters[0] + "%' "
                    + "and IFNULL(tin_number, '') like '%" + parameters[1] + "%' "
                    + "and IFNULL(phone_no, '') like '%" + parameters[2] + "%' ";
        } else {
            sql = "nothing";
        }

        System.out.println(sql);
        try {
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            ps.close();

            return count;
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ArrayList<Object> getRows(String what, String[] parameters, int page, int limit) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        String sql;
        ResultSet rs;
        int count = 0;

        /* A flag to extract only one record */
        String idWhereClause = "";
        String idWhereClauseParked = "";
        String idWhereClauseExited = "";
        String idWhereClauseUser = "";
        String idWhereClauseCompany = "";

        if (what.equals("parking_lot")) {

            sql = "select * from /****/ (select ROW_NUMBER() OVER (order by PARKING_LOT_NO) AS row_num, id, company_id, PARKING_LOT_NO, description "
                    + "from parking_lot "
                    + "where company_id = " + parameters[0] + " "
                    + "and IFNULL(PARKING_LOT_NO,'') like '%" + parameters[1] + "%' "
                    + "and IFNULL(description,'') like '%" + parameters[2] + "%'  "
                    + "order by PARKING_LOT_NO) x "
                    + "limit " + (page - 1) * limit + ", " + limit;
        } else if (what.equals("customer")) {
            idWhereClause = page < 0 ? "and driver_id = " + parameters[8] + " " : "";/*to Get Only One record*/
            page = page < 0 ? 1 : page;
            sql = "select * from (select ROW_NUMBER() OVER (order by CONCAT(first_name,' ',middle_name,' ',last_name) desc) AS row_num, CONCAT(first_name,' ',middle_name,' ',last_name) fullName, "
                    + "first_name, middle_name, last_name, gender, address, mobile, date_of_birth, job_title, tin, email, code, license_plate, manufacturer, model, year, driver_id from driver d "
                    + "join vehicle v on d.id = v.driver_id "
                    + "where CONCAT(first_name,' ',middle_name,' ',last_name) like '%" + parameters[0] + "%' "
                    + "and IFNULL(mobile,'') like '%" + parameters[1] + "%' "
                    + "and IFNULL(email,'') like '%" + parameters[2] + "%' "
                    + "and IFNULL(code,'') like '%" + parameters[3] + "%' "
                    + "and IFNULL(license_plate,'') like '%" + parameters[4] + "%' "
                    + "and IFNULL(manufacturer,'') like '%" + parameters[5] + "%' "
                    + "and IFNULL(model,'') like '%" + parameters[6] + "%' "
                    + "and IFNULL(year,'') like '%" + parameters[7] + "%' "
                    + idWhereClause
                    + "order by CONCAT(first_name,' ',middle_name,' ',last_name) desc) x "
                    + "limit " + (page - 1) * limit + ", " + limit;
        } else if (what.equals("parked")) {
            idWhereClauseParked = page < 0 ? "and pc.id = " + parameters[5] + " " : "";/*to Get Only One record*/
            page = page < 0 ? 1 : page;
            sql = "select * from (select ROW_NUMBER() OVER (order by CONCAT(first_name,' ',middle_name,' ',last_name) ) AS row_num, CONCAT(first_name,' ',middle_name,' ',last_name) fullName, "
                    + "first_name, middle_name, last_name, gender, address, mobile, date_of_birth, job_title, tin, email, code, license_plate, manufacturer, model, year, driver_id, pc.id, vehicle_id, "
                    + "parking_lot_no, description, date_entry, date_exit, pl.company_id, "
                    + "CASE when TIMESTAMPDIFF(hour, date_entry,date_exit) > 0 then "
                    + "concat(floor(TIMESTAMPDIFF(minute, date_entry,date_exit)/60),'hr ', mod(TIMESTAMPDIFF(minute, date_entry,date_exit),60),'min') "
                    + "else concat(mod(TIMESTAMPDIFF(minute, date_entry,date_exit),60),'min') end AS duration "
                    + "from parked_car pc "
                    + "join vehicle v on pc.vehicle_id = v.id "
                    + "join driver d on v.driver_id = d.id "
                    + "join parking_lot pl on pc.parking_lot_id = pl.id "
                    + "where pc.company_id = " + parameters[0] + " "
                    + "and CONCAT(first_name,' ',middle_name,' ',last_name) like '%" + parameters[1] + "%' "
                    + "and IFNULL(code,'') like '%" + parameters[2] + "%' "
                    + "and IFNULL(license_plate,'') like '%" + parameters[3] + "%' "
                    + "and IFNULL(year,'') like '%" + parameters[4] + "%' "
                    + idWhereClauseParked
                    + "and date_exit is null ) x "
                    + "order by fullName";
        } else if (what.equals("exited")) {
            idWhereClauseExited = page < 0 ? "and pc.id = " + parameters[5] + " " : "";/*to Get Only One record*/
            page = page < 0 ? 1 : page;
            sql = "select * from (select ROW_NUMBER() OVER (order by CONCAT(first_name,' ',middle_name,' ',last_name) ) AS row_num, CONCAT(first_name,' ',middle_name,' ',last_name) fullName, "
                    + "first_name, middle_name, last_name, gender, address, mobile, date_of_birth, job_title, tin, email, code, license_plate, manufacturer, model, year, driver_id, pc.id, vehicle_id, "
                    + "parking_lot_no, description, date_entry, date_exit, pl.company_id, "
                    + "CASE when TIMESTAMPDIFF(hour, date_entry,date_exit) > 0 then "
                    + "concat(floor(TIMESTAMPDIFF(minute, date_entry,date_exit)/60),'hr ', mod(TIMESTAMPDIFF(minute, date_entry,date_exit),60),'min') "
                    + "else concat(mod(TIMESTAMPDIFF(minute, date_entry,date_exit),60),'min') end AS duration "
                    + "from parked_car pc "
                    + "join vehicle v on pc.vehicle_id = v.id "
                    + "join driver d on v.driver_id = d.id "
                    + "join parking_lot pl on pc.parking_lot_id = pl.id "
                    + "where pc.company_id = " + parameters[0] + " "
                    + "and CONCAT(first_name,' ',middle_name,' ',last_name) like '%" + parameters[1] + "%' "
                    + "and IFNULL(code,'') like '%" + parameters[2] + "%' "
                    + "and IFNULL(license_plate,'') like '%" + parameters[3] + "%' "
                    + idWhereClauseExited
                    + "and date_exit is not null ) x "
                    + "order by date_exit ";
        } else if (what.equals("user")) {
            idWhereClauseUser = page < 0 ? "and u.id = " + parameters[4] + " " : "";/*to Get Only One record*/
            page = page < 0 ? 1 : page;
            sql = "select ROW_NUMBER() OVER (order by full_name ) AS row_num, u.user_id username, u.full_name, c.company_name, u.role, u.status, mobile, u.id uid, c.id cid from user u "
                    + " join company c on u.company_id = c.id "
                    + " where u.user_id like '%" + parameters[1] + "%' "
                    + " and u.full_name like '%" + parameters[2] + "%' "
                    + " and c.company_name like '%" + parameters[3] + "%' "
                    + " and u.user_id <> '" + parameters[0] + "' "
                    + idWhereClauseUser
                    + " order by full_name ";
        } else if (what.equals("company")) {
            idWhereClauseCompany = page < 0 ? "and id = " + parameters[3] + " " : "";/*to Get Only One record*/
            page = page < 0 ? 1 : page;
            sql = "select ROW_NUMBER() OVER (order by company_name ) AS row_num, company_name, tin_number, address_1, address_2, fee_per_hr, phone_no, id from company "
                    + "where IFNULL(company_name, '') like '%" + parameters[0] + "%' "
                    + "and IFNULL(tin_number, '') like '%" + parameters[1] + "%' "
                    + "and IFNULL(phone_no, '') like '%" + parameters[2] + "%' "
                    + idWhereClauseCompany
                    + "order by company_name";
        } else if (what.equals("company_all")) {
            sql = "SELECT DISTINCT company_name FROM company ORDER BY company_name";
        } else {
            sql = "nothing";
        }

        System.out.println(sql);

        try {
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            rs = ps.executeQuery();

            if (what.equals("parking_lot")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getInt("row_num"), rs.getString("parking_lot_no"), rs.getString("description")});
                }
                /* Customer's information Section */
            } else if (what.equals("customer") && idWhereClause.equals("")) {
                /*Specific rows for search*/
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getInt("row_num"), rs.getString("fullName"), rs.getString("mobile"), rs.getString("email"), rs.getString("tin"), rs.getString("code"),
                        rs.getString("license_plate"), rs.getString("manufacturer"), rs.getString("model"), rs.getString("year"), rs.getString("driver_id")});
                }
            } else if (what.equals("customer") && !idWhereClause.equals("")) {
                /*Retrive all records */
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getString("first_name"), rs.getString("middle_name"), rs.getString("last_name"), rs.getString("gender"), rs.getString("address"), rs.getString("mobile"),
                        rs.getString("date_of_birth"), rs.getString("job_title"), rs.getString("tin"), rs.getString("email"), rs.getString("code"), rs.getString("license_plate"), rs.getString("manufacturer"),
                        rs.getString("model"), rs.getString("year"), rs.getString("driver_id")});
                }
                /* Currently parked Car's information Section */
            } else if (what.equals("parked") && idWhereClauseParked.equals("")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getInt("row_num"), rs.getString("code"), rs.getString("license_plate"), rs.getString("model"), rs.getString("year"),
                        rs.getString("fullname"), rs.getString("parking_lot_no"), rs.getString("id")});
                }
            } else if (what.equals("parked") && !idWhereClauseParked.equals("")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getString("fullname"), rs.getString("address"), rs.getString("mobile"),
                        rs.getString("code"), rs.getString("license_plate"), rs.getString("manufacturer"), rs.getString("model"), rs.getString("year"),
                        rs.getString("parking_lot_no"), rs.getString("description"), rs.getString("date_entry")});
                }
                /* Exited Car's information Section */
            } else if (what.equals("exited") && idWhereClauseExited.equals("")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getInt("row_num"), rs.getString("code"), rs.getString("license_plate"), rs.getString("parking_lot_no"), rs.getString("fullname"),
                        rs.getString("date_entry"), rs.getString("date_exit"), rs.getString("duration"), rs.getString("id")});
                }
            } else if (what.equals("exited") && !idWhereClauseExited.equals("")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getString("fullname"), rs.getString("address"), rs.getString("mobile"),
                        rs.getString("code"), rs.getString("license_plate"), rs.getString("manufacturer"), rs.getString("model"), rs.getString("year"),
                        rs.getString("parking_lot_no"), rs.getString("description"), rs.getString("date_entry"), rs.getString("date_exit"), rs.getString("duration")});
                }
                /* Users Administration */
            } else if (what.equals("user") && idWhereClauseUser.equals("")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getInt("row_num"), rs.getString("username"), rs.getString("full_name"), rs.getString("company_name"), rs.getString("uid")});
                }
            } else if (what.equals("user") && !idWhereClauseUser.equals("")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getString("username"), rs.getString("full_name"), rs.getString("company_name"),
                        rs.getString("role"), rs.getString("status"), rs.getString("mobile"), rs.getString("uid"), rs.getString("cid")});
                }
                /* Company Administration */
            } else if (what.equals("company") && idWhereClauseCompany.equals("")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getInt("row_num"), rs.getString("company_name"), rs.getString("tin_number"), rs.getString("address_1"),
                        rs.getString("fee_per_hr"), rs.getString("phone_no"), rs.getString("id")});
                }
            } else if (what.equals("company") && !idWhereClauseCompany.equals("")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getString("company_name"), rs.getString("tin_number"), rs.getString("address_1"), rs.getString("address_2"),
                        rs.getString("fee_per_hr"), rs.getString("phone_no")});
                }
                /* List of all companies for the ComboBox Selection */
            } else if (what.equals("company_all")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getString("company_name")});
                }
            } else {

            }

            rs.close();
            ps.close();
            System.out.println(arrayList);
            return arrayList;
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public ArrayList<Object> fetchACar(String what, String[] parameters) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        String sql;
        ResultSet rs;

        if (what.equals("in")) {
            sql = "select CONCAT(first_name,' ',middle_name,' ',last_name) fullName, gender, address, mobile, date_of_birth, job_title, tin, email, code, license_plate, manufacturer, model, year, driver_id, v.id vehicle_id from driver d "
                    + "join vehicle v on d.id = v.driver_id "
                    + "Where code = '" + parameters[0] + "' "
                    + "and license_plate = '" + parameters[1] + "' ";
        } else if (what.equals("out")) {
            sql = "select  CONCAT(first_name,' ',middle_name,' ',last_name) fullName, mobile, code, license_plate, manufacturer, model, year, pc.id, parking_lot_no, description, date_entry "
                    + "from parked_car pc join vehicle v on pc.vehicle_id = v.id "
                    + "join driver d on v.driver_id = d.id "
                    + "join parking_lot pl on pc.parking_lot_id = pl.id "
                    + "Where pl.company_id = " + parameters[0] + " "
                    + "and code = '" + parameters[1] + "' "
                    + "and license_plate = '" + parameters[2] + "' "
                    + "and date_exit is null";
        } else if (what.equals("out_exit")) {
            sql = "select  CONCAT(first_name,' ',middle_name,' ',last_name) fullName, code, license_plate, d.tin customer_tin, date_entry, company_name, c.tin_number company_tin, "
                    + "Address_1, Address_2, fee_per_hr, phone_no, "
                    + "ROUND(TIMESTAMPDIFF(minute, date_entry,now())/60,1) hrs, "
                    + "CASE when TIMESTAMPDIFF(hour, date_entry,now()) > 0 then "
                    + "concat(floor(TIMESTAMPDIFF(minute, date_entry,now())/60),'hr ', mod(TIMESTAMPDIFF(minute, date_entry,now()),60),'min') "
                    + "else concat(mod(TIMESTAMPDIFF(minute, date_entry,now()),60),'min') end duration  "
                    + "from parked_car pc "
                    + "join vehicle v on pc.vehicle_id = v.id "
                    + "join driver d on v.driver_id = d.id "
                    + "join parking_lot pl on pc.parking_lot_id = pl.id "
                    + "join company c on pc.company_id = c.id "
                    + "Where pl.company_id = " + parameters[0] + " and date_exit is null "
                    + "and pc.id = " + parameters[1] + " ";
        } else {
            sql = "nothing";
        }

        System.out.println(sql);
        try {
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            rs = ps.executeQuery();

            if (what.equals("in")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getString("fullName"), rs.getString("mobile"), rs.getString("code"), rs.getString("license_plate"), rs.getString("manufacturer"), rs.getString("model"), rs.getString("year"), rs.getInt("vehicle_id")});
                }
            } else if (what.equals("out")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getString("fullName"), rs.getString("mobile"), rs.getString("code"), rs.getString("license_plate"), rs.getString("manufacturer"),
                        rs.getString("model"), rs.getString("year"), rs.getString("parking_lot_no"), rs.getString("description"), rs.getString("year"), rs.getString("date_entry"),
                        rs.getInt("id")});
                }
            } else if (what.equals("out_exit")) {
                while (rs.next()) {
                    arrayList.add(new Object[]{rs.getString("fullName"), rs.getString("code"), rs.getString("license_plate"), rs.getString("customer_tin"),
                        rs.getString("date_entry"), rs.getString("company_name"), rs.getString("company_tin"), rs.getString("Address_1"), rs.getString("Address_2"),
                        rs.getString("fee_per_hr"), rs.getString("phone_no"), rs.getString("hrs"), rs.getString("duration")});
                }
            } else {

            }

            rs.close();
            ps.close();
            System.out.println(arrayList);
            return arrayList;

        } catch (SQLException ex) {
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int updateARecord(String what, String[] parameters) {
        String parkingSql = "";
        String customerSql = "";
        String exitParkingSql = "";
        String userSql = "";
        String companySql = "";
        int result;

        if (what.equals("parking_lot")) {

            parkingSql = "UPDATE PARKING_LOT "
                    + "SET PARKING_LOT_NO = '" + parameters[2] + "', "
                    + "description    = '" + parameters[3] + "' "
                    + "WHERE company_id = " + parameters[0] + " "
                    + "AND PARKING_LOT_NO = '" + parameters[1] + "'";

        } else if (what.equals("customer")) {

            parkingSql = "update DRIVER set first_name = '" + parameters[1] + "', middle_name = '" + parameters[2] + "', last_name = '" + parameters[3] + "', gender = '" + parameters[4] + "', "
                    + "address = '" + parameters[5] + "', mobile= '" + parameters[6] + "', date_of_birth= '" + parameters[7] + "', job_title= '" + parameters[8] + "', tin= '" + parameters[9] + "', email= '" + parameters[10] + "' "
                    + "where id = " + parameters[0];

            customerSql = "update VEHICLE set code = '" + parameters[11] + "', license_plate = '" + parameters[12] + "', manufacturer = '" + parameters[13] + "', model = '" + parameters[14] + "', year = '" + parameters[15] + "' "
                    + "where driver_id = " + parameters[0];

        } else if (what.equals("exit_parking_lot")) {

            exitParkingSql = "update parked_car set date_exit = now() "
                    + "Where company_id = " + parameters[0] + " and date_exit is null "
                    + "and id = " + parameters[1] + " ";

        } else if (what.equals("user")) {
            String passQuery = parameters[1].equals("0") ? "" : "password = md5('" + parameters[1] + "'), ";
            String status = parameters[4].equals("Enabled") ? "true" : "false";
            userSql = "update user set full_name = '" + parameters[0] + "', " + passQuery + " mobile = '" + parameters[2] + "', role = '" + parameters[3] + "', "
                    + "status = " + status + ", company_id = (select id from company where company_name= '" + parameters[5] + "') "
                    + "where id = " + parameters[6];

        } else if (what.equals("company")) {
            companySql = "update company set company_name = '" + parameters[0] + "', tin_number= '" + parameters[1] + "', Address_1= '" + parameters[2] + "', Address_2= '" + parameters[3] + "', phone_no= '" + parameters[4] + "', fee_per_hr = " + parameters[5] + " "
                    + "where id = " + parameters[6];

        }

        System.out.println(userSql);
        try {

            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps1;
            PreparedStatement ps2;
            PreparedStatement ps3;

            if (what.equals("parking_lot")) {
                ps1 = DatabaseConnection.getInstance().getConnection().prepareStatement(parkingSql);
                result = ps1.executeUpdate();

                ps1.close();
            } else if (what.equals("customer")) {
                ps1 = DatabaseConnection.getInstance().getConnection().prepareStatement(customerSql);
                result = ps1.executeUpdate();

                ps2 = DatabaseConnection.getInstance().getConnection().prepareStatement(parkingSql);
                result = ps2.executeUpdate();

                ps1.close();
                ps2.close();
            } else if (what.equals("exit_parking_lot")) {
                ps3 = DatabaseConnection.getInstance().getConnection().prepareStatement(exitParkingSql);
                result = ps3.executeUpdate();

                ps3.close();
            } else if (what.equals("user")) {
                ps1 = DatabaseConnection.getInstance().getConnection().prepareStatement(userSql);
                result = ps1.executeUpdate();

                ps1.close();
            } else if (what.equals("company")) {
                ps1 = DatabaseConnection.getInstance().getConnection().prepareStatement(companySql);
                result = ps1.executeUpdate();

                ps1.close();
            } else {
                result = 0;
            }

            System.out.println(result);
            return result;

        } catch (SQLException ex) {
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int deleteARecord(String what, String[] parameters) {
        String sql;

        int result = 0;
        PreparedStatement ps;

        if (what.equals("parking_lot")) {

            sql = "DELETE FROM PARKING_LOT "
                    + "WHERE company_id = " + parameters[0] + " "
                    + "AND PARKING_LOT_NO = '" + parameters[1] + "'";

        } else if (what.equals("customer")) {

            sql = "DELETE FROM VEHICLE "
                    + "WHERE driver_id = " + parameters[0] + " ";

        } else if (what.equals("user")) {

            sql = "DELETE FROM USER "
                    + "WHERE ID = " + parameters[0] + " ";

        } else if (what.equals("company")) {

            sql = "DELETE FROM COMPANY "
                    + "WHERE ID = " + parameters[0] + " ";

        } else {
            sql = "";
        }

        System.out.println(sql);
        try {
            DatabaseConnection.getInstance().connectToDatabase();
            if (what.equals("parking_lot")) {
                ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
                result = ps.executeUpdate();
                ps.close();
            } else if (what.equals("customer")) {
                ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
                result = ps.executeUpdate();
                ps.close();
            } else if (what.equals("user")) {
                ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
                result = ps.executeUpdate();
                ps.close();
            } else if (what.equals("company")) {
                ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
                result = ps.executeUpdate();
                ps.close();
            }

            System.out.println(result);
            return result;

        } catch (SQLException ex) {
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ArrayList<Object> login(String username, String password) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        String sql = "select u.*, ud.id user_detail_id, case when password = md5('" + password + "') then 'correct' else 'tempPassword' end correct, company_name "
                + "from user u join user_detail ud on u.id = ud.user_id "
                + "left join company c on u.company_id = c.id "
                + "where u.user_id = '" + username + "' and password=md5('" + password + "') "
                + "or (ud.temp_pass_flag = true and ud.temp_password = md5('" + password + "'))";
        System.out.println(sql);
        ResultSet rs;
        try {
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                arrayList.add(new Object[]{rs.getString("id"), rs.getString("company_id"), rs.getString("user_id"), rs.getString("role"),
                    rs.getString("status"), rs.getString("correct"), rs.getString("company_name")});

                /* Disable temp password (generated with forget pass) b/c user logged in with their original password */
                if (rs.getString("status").equals("1") && rs.getString("correct").equals("correct")) {
                    sql = "update user_detail set temp_pass_flag = false where id = " + rs.getString("user_detail_id");
                    DatabaseConnection.getInstance().getConnection().prepareStatement(sql).executeUpdate();
                    System.out.println(sql);
                }
            } else {
                /* No record found */
            }

            rs.close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(arrayList);
        return arrayList;

    }

    public String forgetPassword(String username) {
        String sql = "select id, full_name, user_id, mobile from user where user_id = '" + username + "' ";
        String id = "";
        String tempPassword = "";
        ResultSet rs;
        ResultSet rs2;
        try {
            DatabaseConnection.getInstance().connectToDatabase();
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println(sql);
            if (rs.next()) {
                id = rs.getString("id");
                tempPassword = generatePassword();
                sql = "update user_detail set temp_password = md5('" + tempPassword + "'), temp_pass_flag = true where user_id = " + id + " ";
                PreparedStatement ps2 = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
                System.out.println(sql);
                if (ps2.executeUpdate() > 0) {
                    ps2.close();
                    createPasswordSMS(rs.getString("full_name"), rs.getString("user_id"), tempPassword, rs.getString("mobile"));
                    rs.close();
                    return "success";
                } else {
                    return "error";
                }
            } else {
                return "no user";

            }

        } catch (SQLException ex) {
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public int mandatoryPasswordChange(String newPassword, String id) {
        String sql = "update user set password = md5('" + newPassword + "') where id =" + id + " ";
        try {
            DatabaseConnection.getInstance().connectToDatabase();
            if (DatabaseConnection.getInstance().getConnection().prepareStatement(sql).executeUpdate() > 0) {
                sql = "update user_detail set temp_pass_flag = false where user_id = " + id;
                System.out.println(sql);
                return DatabaseConnection.getInstance().getConnection().prepareStatement(sql).executeUpdate();
            }
            return 0;

        } catch (SQLException ex) {
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private String generatePassword() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890@#";
        StringBuilder pass = new StringBuilder();
        Random rand = new Random();
        while (pass.length() <= 8) { // length of the random password
            int index = (int) (rand.nextFloat() * characterSet.length());
            pass.append(characterSet.charAt(index));
        }
        return pass.toString();
    }

    private void createPasswordSMS(String fullName, String username, String password, String mobileNo) {
        String fileName = "Password-Reset-SMS\\" + new Date().getTime() + "_" + username + "_" + mobileNo + "_.txt"; // Name of the file to create

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write content to the file
            writer.write("Dear " + fullName + ", You PMS password is " + password);
            writer.newLine();
            writer.write("Please Reset it immediately!");

        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }
    
    public boolean testDbConnection (){
        try {
            if(DatabaseConnection.getInstance().getConnection()== null){
            return false;
        }
        return true;
        } catch (Exception e) {
        }
        return false;
    }
}
