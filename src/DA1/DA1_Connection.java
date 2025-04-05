/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DA1;

import Utils.Dbconnection;
import java.sql.*;

/**
 *
 * @author Dang
 */
public class DA1_Connection {
    public static void main(String[] args) {
        Connection conn = Dbconnection.getConnection();
    }
}
