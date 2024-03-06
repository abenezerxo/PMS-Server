package main;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ClientRequests extends Remote {

    public String registerClient() throws RemoteException;

    public String[] getDashboardInfo(int companyID) throws RemoteException, SQLException;

    public String[] getDashboardAdmin(int companyID) throws RemoteException;

    public String greeting(int hour) throws RemoteException;

    public int registerParkingLot(String companyID, String parkingLotNO, String descrption) throws RemoteException;

    public int registerClient(String firstName, String middleName, String lastName, String gender, String address, String mobile, String dateOfBirth, String jobTitle, String tin, String email,
            String code, String licensePlate, String manufacturer, String model, String year) throws RemoteException;

    public int getTotalCount(String what, String[] parameters) throws RemoteException;

    public ArrayList<Object> getRows(String what, String[] parameters, int page, int limit) throws RemoteException;

    public int deleteARecord(String what, String[] parameters) throws RemoteException;

    public int updateARecord(String what, String[] parameters) throws RemoteException;

    public ArrayList<Object> fetchACar(String what, String[] parameters) throws RemoteException;

    public int parkACar(String companyID, String vehicleId, String parkingLotNO, String descrption) throws RemoteException;

    public ArrayList<Object> login(String username, String password) throws RemoteException;

    public String forgetPassword(String username) throws RemoteException;

    public int mandatoryPasswordChange(String newPassword, String id) throws RemoteException;

    public int registerUser(String username, String fullname, String password, String mobile, String role, String status, String company) throws RemoteException;

    public int registerCompany(String companyName, String tinNo, String address1, String address2, String phoneNo, String feePerHr) throws RemoteException;

}
