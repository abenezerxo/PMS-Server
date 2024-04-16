package main;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.ibatis.jdbc.ScriptRunner;

public class DatabaseConfig extends javax.swing.JFrame {

    public static String addressFromFile;
    public static String usernameFromFile;
    public static String passwordFromFile;
    public static String portFromFile;

    private static Connection connection;

    public static boolean isConnectionSuccess;

    public DatabaseConfig() {
        initComponents();
        initMoving(this);
        getDatabaseCred();

        addressFromFile = txtIpAddress.getText();
        usernameFromFile = txtUser.getText();
        passwordFromFile = txtPassword.getText();
        portFromFile = txtPort.getText();

        if (initialDatabaseCon()){
            if(connectToPMSDB()){
                isConnectionSuccess = true;
            } else{
                createPMSData();
            }
             isConnectionSuccess = true;
        } else {
            isConnectionSuccess = false;
        }
        
    }

    public static boolean initialDatabaseCon() {
        try {
            java.sql.DriverManager.getConnection("jdbc:mysql://" + addressFromFile + ":" + portFromFile, usernameFromFile, passwordFromFile);
            System.out.println("Connection sucess");
            return true;
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    public static boolean connectToPMSDB() {
        try {
            java.sql.DriverManager.getConnection("jdbc:mysql://" + addressFromFile + ":" + portFromFile + "/PMS", usernameFromFile, passwordFromFile);
            System.out.println("Connection to pms db sucess");
            return true;
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    public static void createPMSData() {
        try {
            connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + addressFromFile + ":" + portFromFile, usernameFromFile, passwordFromFile);
            System.out.println("Connection sucess");
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader("PMS-script\\query.sql"));
            sr.runScript(reader);
            System.out.println("Sucessuly executeed");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void getDatabaseCred() {
        String fnAddress = "db-cred\\address.txt"; // Read Address
        String fnUsername = "db-cred\\username.txt";
        String fnPassword = "db-cred\\password.txt";
        String fnPort = "db-cred\\port.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fnAddress))) {
            while ((addressFromFile = reader.readLine()) != null) {
                System.out.println(addressFromFile);
                txtIpAddress.setText(addressFromFile);
            }
        } catch (IOException e) {
            System.err.println("Error reading from the file: " + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fnUsername))) {
            while ((usernameFromFile = reader.readLine()) != null) {
                System.out.println(usernameFromFile);
                txtUser.setText(usernameFromFile);
            }
        } catch (IOException e) {
            System.err.println("Error reading from the file: " + e.getMessage());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fnPassword))) {
            while ((passwordFromFile = reader.readLine()) != null) {
                System.out.println(passwordFromFile);
                txtPassword.setText(passwordFromFile);

            }
        } catch (IOException e) {
            System.err.println("Error reading from the file: " + e.getMessage());
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(fnPort))) {
            while ((portFromFile = reader.readLine()) != null) {
                System.out.println(portFromFile);
                txtPort.setText(portFromFile);
            }
        } catch (IOException e) {
            System.err.println("Error reading from the file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pannelLogin = new javax.swing.JPanel();
        lbDriver = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        buttonPanel1 = new javax.swing.JPanel();
        btnEdit = new javax.swing.JButton();
        btnDefault = new javax.swing.JButton();
        lbHeader = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIpAddress = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pannelLogin.setBackground(new java.awt.Color(177, 224, 224));

        lbDriver.setBackground(new java.awt.Color(204, 204, 204));
        lbDriver.setFont(new java.awt.Font("Century Gothic", 0, 40)); // NOI18N
        lbDriver.setForeground(new java.awt.Color(221, 92, 92));
        lbDriver.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDriver.setText(" M y S Q L  D a t a b a s e   E r r o r  ! ! !");

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 30, 5));

        jPanel5.setOpaque(false);

        buttonPanel1.setBackground(new java.awt.Color(221, 240, 255));
        buttonPanel1.setLayout(new java.awt.GridLayout(1, 0));

        btnEdit.setBackground(new java.awt.Color(255, 249, 224));
        btnEdit.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        btnEdit.setText(" S A V E ");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        buttonPanel1.add(btnEdit);

        btnDefault.setBackground(new java.awt.Color(255, 249, 224));
        btnDefault.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        btnDefault.setText(" D E F A U L T ");
        btnDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefaultActionPerformed(evt);
            }
        });
        buttonPanel1.add(btnDefault);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(293, 293, 293)
                    .addComponent(buttonPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(293, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(buttonPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        lbHeader.setFont(new java.awt.Font("Century Gothic", 0, 40)); // NOI18N
        lbHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbHeader.setText("P a r k i n g  M a n a g e m e n t   S y s t e m ");

        jSeparator1.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator1.setToolTipText("");

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridLayout(4, 5, 1, 5));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("S e r v e r   A d d r e s s  ");
        jPanel2.add(jLabel1);

        txtIpAddress.setColumns(20);
        txtIpAddress.setFont(new java.awt.Font("Century Gothic", 3, 24)); // NOI18N
        txtIpAddress.setText("localhost");
        txtIpAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIpAddressActionPerformed(evt);
            }
        });
        jPanel2.add(txtIpAddress);

        jLabel2.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("u s e r  ");
        jPanel2.add(jLabel2);

        txtUser.setColumns(20);
        txtUser.setFont(new java.awt.Font("Century Gothic", 3, 24)); // NOI18N
        txtUser.setText("root");
        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });
        jPanel2.add(txtUser);

        jLabel3.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("p a s s w o r d  ");
        jPanel2.add(jLabel3);

        txtPassword.setColumns(20);
        txtPassword.setFont(new java.awt.Font("Century Gothic", 3, 24)); // NOI18N
        txtPassword.setText("root@123@pms");
        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });
        jPanel2.add(txtPassword);

        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("p o r t  ");
        jPanel2.add(jLabel4);

        txtPort.setColumns(20);
        txtPort.setFont(new java.awt.Font("Century Gothic", 3, 24)); // NOI18N
        txtPort.setText("3306");
        txtPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPortActionPerformed(evt);
            }
        });
        jPanel2.add(txtPort);

        javax.swing.GroupLayout pannelLoginLayout = new javax.swing.GroupLayout(pannelLogin);
        pannelLogin.setLayout(pannelLoginLayout);
        pannelLoginLayout.setHorizontalGroup(
            pannelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1189, Short.MAX_VALUE)
            .addGroup(pannelLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pannelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pannelLoginLayout.createSequentialGroup()
                        .addGroup(pannelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbDriver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(pannelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pannelLoginLayout.createSequentialGroup()
                    .addGap(46, 46, 46)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(47, 47, 47)))
        );
        pannelLoginLayout.setVerticalGroup(
            pannelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pannelLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbDriver, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(pannelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pannelLoginLayout.createSequentialGroup()
                    .addGap(200, 200, 200)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(123, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pannelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(pannelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtIpAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIpAddressActionPerformed

    private void btnDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDefaultActionPerformed
        if (btnDefault.getText().equals(" D E F A U L T ")) {

            if (JOptionPane.showConfirmDialog(null, "Are you sure you to use the Default Database Cred.?", "Default Database Credential", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                // Revert back to localhost
                txtIpAddress.setText("localhost");
                txtUser.setText("root");
                txtPassword.setText("root@1234@pms");
                txtPort.setText("3306");

                changeDatabaseConfig();
            }

        } else {
            // when cancel is clicked
            changeBtnToEdit();
        }
    }//GEN-LAST:event_btnDefaultActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (btnEdit.getText().equals(" E D I T ")) {
            changeBtnToSave();
        } else {
            changeDatabaseConfig();
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserActionPerformed

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void txtPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPortActionPerformed

    private int x;
    private int y;

    public void initMoving(JFrame fram) {
        pannelLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

        });
        pannelLogin.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                fram.setLocation(e.getXOnScreen() - x, e.getYOnScreen() - y);
            }

        });
    }

    private void changeBtnToEdit() {
        btnEdit.setText(" E D I T ");
        btnDefault.setText(" D E F A U L T ");

        txtIpAddress.setEditable(false);
        txtUser.setEditable(false);
        txtPort.setEditable(false);
        txtPassword.setEditable(false);
    }

    private void changeBtnToSave() {
        btnEdit.setText(" S A V E ");
        btnDefault.setText(" C A N C E L ");

        txtIpAddress.setEditable(true);
        txtUser.setEditable(true);
        txtPort.setEditable(true);
        txtPassword.setEditable(true);
    }

    private void changeDatabaseConfig() {

        /* Before chaning Setting check the connection with the new address First */
        try {
            java.sql.DriverManager.getConnection("jdbc:mysql://" + txtIpAddress.getText() + ":" + txtPort.getText() + "/" + "pms", txtUser.getText(), txtPassword.getText());

            /*Update the db cred files */

            String fnAddress = "db-cred\\address.txt"; // Read Address
            String fnUsername = "db-cred\\username.txt";
            String fnPassword = "db-cred\\password.txt";
            String fnPort = "db-cred\\port.txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fnAddress))) {
                // Write content to the file
                writer.write(txtIpAddress.getText());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Something is not right!! Please try again", "!!! ERROR !!!", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error writing to the file: " + e.getMessage());
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fnUsername))) {
                // Write content to the file
                writer.write(txtUser.getText());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Something is not right!! Please try again", "!!! ERROR !!!", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error writing to the file: " + e.getMessage());
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fnPassword))) {
                // Write content to the file
                writer.write(txtPassword.getText());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Something is not right!! Please try again", "!!! ERROR !!!", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error writing to the file: " + e.getMessage());
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fnPort))) {
                // Write content to the file
                writer.write(txtPort.getText());
                JOptionPane.showMessageDialog(null, "Database Setting has been updated", "Database Setting Update", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                changeBtnToEdit();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Something is not right!! Please try again", "!!! ERROR !!!", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error writing to the file: " + e.getMessage());
            }
            
            

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unalbe to connect to Database, Please insert a valid Information or Check MySql Database is running!!!", "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDefault;
    private javax.swing.JButton btnEdit;
    private javax.swing.JPanel buttonPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbDriver;
    private javax.swing.JLabel lbHeader;
    private javax.swing.JPanel pannelLogin;
    public static javax.swing.JTextField txtIpAddress;
    public static javax.swing.JTextField txtPassword;
    public static javax.swing.JTextField txtPort;
    public static javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
