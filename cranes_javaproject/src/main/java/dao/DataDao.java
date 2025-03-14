package dao;

import db.MyConnection;
import model.Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

public class DataDao {
    public static List<Data>getAllFiles(String email) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from data where email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        List<Data> files = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String path = rs.getString(3);
            files.add(new Data(id, name, path));
        }
        return files;
    }
    public static int hideFile(Data file)throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("insert into data(name, path, email, bin_data) values(?,?,?,?)");
        ps.setString(1,file.getFilename());
        ps.setString(2, file.getPath());
        ps.setString(3, file.getEmail());
        File f = new File(file.getPath());
        FileReader fr = new FileReader(f);
        ps.setCharacterStream(4,fr,f.length());
        int ans = ps.executeUpdate();
        fr.close();
        f.delete();
        return ans;
    }
    public static void unhide(int id) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();

        // Corrected SQL query
        PreparedStatement ps = connection.prepareStatement("SELECT path, bin_data FROM data WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        // Check if data exists
        if (!rs.next()) {
            System.out.println("No file found with ID: " + id);
            return;
        }

        String path = rs.getString("path");
        Clob c = rs.getClob("bin_data");
        Reader r = c.getCharacterStream();
        FileWriter fw = new FileWriter(path);

        int i;
        while ((i = r.read()) != -1) {
            fw.write((char) i);
        }

        fw.close();
        r.close();

        // Correct way to delete the file from DB
        ps = connection.prepareStatement("DELETE FROM data WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();

        System.out.println("Successfully Unhidden");

        // Close resources
        rs.close();
        ps.close();
        connection.close();
    }

}
