package com.example.PortalAPIv3.API.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Registered;
import reactor.core.publisher.Mono;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Registered
public class APIService {

    public APIService() {
        Connection con = this.connect();
        try {
            Statement stmtCreateTable1 = con.createStatement();
            stmtCreateTable1.setQueryTimeout(30);
            stmtCreateTable1.executeUpdate(
                    "create table if not exists engineheaters (" +
                            "DEVUI string," +
                            "HEATERISON integer," +
                            "STARTHEATERNOW integer," +
                            "MONTIMEACTIVE integer," +
                            "TUETIMEACTIVE integer," +
                            "WEDTIMEACTIVE integer," +
                            "THUTIMEACTIVE integer," +
                            "FRITIMEACTIVE integer," +
                            "SATTIMEACTIVE integer," +
                            "SUNTIMEACTIVE integer," +
                            "MONENDTIME string," +
                            "TUEENDTIME string," +
                            "WEDENDTIME string," +
                            "THUENDTIME string," +
                            "FRIENDTIME string," +
                            "SATENDTIME string," +
                            "SUNENDTIME string)");
            stmtCreateTable1.close();
            con.close();
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    private Connection connect() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:portalAPI.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    public boolean checkIncomingPayload(JsonNode jsonNode) {
        boolean res = true;
        if (!jsonNode.has("devUi")) return false;
        if (!jsonNode.has("heaterIsOn")) return false;
        if (!jsonNode.has("startHeaterNow")) return false;
        if (!jsonNode.has("monTimeActive")) return false;
        if (!jsonNode.has("tueTimeActive")) return false;
        if (!jsonNode.has("wedTimeActive")) return false;
        if (!jsonNode.has("thuTimeActive")) return false;
        if (!jsonNode.has("friTimeActive")) return false;
        if (!jsonNode.has("satTimeActive")) return false;
        if (!jsonNode.has("sunTimeActive")) return false;
        if (!jsonNode.has("monEndTime")) return false;
        if (!jsonNode.has("tueEndTime")) return false;
        if (!jsonNode.has("wedEndTime")) return false;
        if (!jsonNode.has("thuEndTime")) return false;
        if (!jsonNode.has("friEndTime")) return false;
        if (!jsonNode.has("satEndTime")) return false;
        if (!jsonNode.has("sunEndTime")) return false;
        return res;
    }

    public JsonNode getOneEngineheater(String devUi) {
        JsonNode jsonResult = null;
        String sql = "SELECT * FROM engineheaters WHERE DEVUI = ?";
        try {
            Connection con = this.connect();
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, devUi);
            ResultSet rs = pstmt.executeQuery();
            ObjectMapper mapper = new ObjectMapper();
            while (rs.next()) {
                String tempString = "{\"devUi\":" + rs.getString("DEVUI") + "," +
                        "\"heaterIsOn\": " + (rs.getInt("HEATERISON") == 1) + "," +
                        "\"startHeaterNow\": " + (rs.getInt("STARTHEATERNOW") == 1) + "," +
                        "\"monTimeActive\": " + (rs.getInt("MONTIMEACTIVE") == 1) + "," +
                        "\"tueTimeActive\": " + (rs.getInt("TUETIMEACTIVE") == 1) + "," +
                        "\"wedTimeActive\": " + (rs.getInt("WEDTIMEACTIVE") == 1) + "," +
                        "\"thuTimeActive\": " + (rs.getInt("THUTIMEACTIVE") == 1) + "," +
                        "\"friTimeActive\": " + (rs.getInt("FRITIMEACTIVE") == 1) + "," +
                        "\"satTimeActive\": " + (rs.getInt("SATTIMEACTIVE") == 1) + "," +
                        "\"sunTimeActive\":" + (rs.getInt("SUNTIMEACTIVE") == 1) + "," +
                        "\"monEndTime\": " + rs.getString("MONENDTIME") + "," +
                        "\"tueEndTime\": " + rs.getString("TUEENDTIME") + "," +
                        "\"wedEndTime\": " + rs.getString("WEDENDTIME") + "," +
                        "\"thuEndTime\": " + rs.getString("THUENDTIME") + "," +
                        "\"friEndTime\": " + rs.getString("FRIENDTIME") + "," +
                        "\"satEndTime\": " + rs.getString("SATENDTIME") + "," +
                        "\"sunEndTime\": " + rs.getString("SUNENDTIME") + "}";
                jsonResult = mapper.readTree(tempString);
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    private void insertNewEngineheaterIntoDb(JsonNode jsonNode) throws SQLException {
        String sql = "INSERT INTO engineheaters VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = this.connect();
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, jsonNode.get("devUi").toString());
        pstmt.setInt(2, (jsonNode.get("heaterIsOn").asBoolean()) ? 1 : 0);
        pstmt.setInt(3, (jsonNode.get("startHeaterNow").asBoolean()) ? 1 : 0);
        pstmt.setInt(4, (jsonNode.get("monTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(5, (jsonNode.get("tueTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(6, (jsonNode.get("wedTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(7, (jsonNode.get("thuTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(8, (jsonNode.get("friTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(9, (jsonNode.get("satTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(10, (jsonNode.get("sunTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setString(11, jsonNode.get("monEndTime").toString());
        pstmt.setString(12, jsonNode.get("tueEndTime").toString());
        pstmt.setString(13, jsonNode.get("wedEndTime").toString());
        pstmt.setString(14, jsonNode.get("thuEndTime").toString());
        pstmt.setString(15, jsonNode.get("friEndTime").toString());
        pstmt.setString(16, jsonNode.get("satEndTime").toString());
        pstmt.setString(17, jsonNode.get("sunEndTime").toString());
        con.setAutoCommit(false);
        pstmt.executeUpdate();
        con.commit();
        con.setAutoCommit(true);
        pstmt.close();
        con.close();
    }

    private void updateEngineheaterInDb(JsonNode jsonNode) throws SQLException {
        String sql = "UPDATE engineheaters SET " +
                "HEATERISON=?," +
                "STARTHEATERNOW=?," +
                "MONTIMEACTIVE=?," +
                "TUETIMEACTIVE=?," +
                "WEDTIMEACTIVE=?," +
                "THUTIMEACTIVE=?, " +
                "FRITIMEACTIVE=?, " +
                "SATTIMEACTIVE=?, " +
                "SUNTIMEACTIVE=?, " +
                "MONENDTIME=?, " +
                "TUEENDTIME=?, " +
                "WEDENDTIME=?, " +
                "THUENDTIME=?, " +
                "FRIENDTIME=?, " +
                "SATENDTIME=?, " +
                "SUNENDTIME=? " +
                "WHERE DEVUI=?";
        Connection con = this.connect();
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, (jsonNode.get("heaterIsOn").asBoolean()) ? 1 : 0);
        pstmt.setInt(2, (jsonNode.get("startHeaterNow").asBoolean()) ? 1 : 0);
        pstmt.setInt(3, (jsonNode.get("monTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(4, (jsonNode.get("tueTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(5, (jsonNode.get("wedTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(6, (jsonNode.get("thuTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(7, (jsonNode.get("friTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(8, (jsonNode.get("satTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setInt(9, (jsonNode.get("sunTimeActive").asBoolean()) ? 1 : 0);
        pstmt.setString(10, jsonNode.get("monEndTime").toString());
        pstmt.setString(11, jsonNode.get("tueEndTime").toString());
        pstmt.setString(12, jsonNode.get("wedEndTime").toString());
        pstmt.setString(13, jsonNode.get("thuEndTime").toString());
        pstmt.setString(14, jsonNode.get("friEndTime").toString());
        pstmt.setString(15, jsonNode.get("satEndTime").toString());
        pstmt.setString(16, jsonNode.get("sunEndTime").toString());
        pstmt.setString(17, jsonNode.get("devUi").toString());
        con.setAutoCommit(false);
        pstmt.executeUpdate();
        con.commit();
        con.setAutoCommit(true);
        pstmt.close();
        con.close();
    }

    public Mono<JsonNode> postEngineheater(JsonNode jsonNode) {
        JsonNode checkIfExists = getOneEngineheater(jsonNode.get("devUi").toString());
        try {
            if (checkIfExists == null) {
                insertNewEngineheaterIntoDb(jsonNode);
            } else {
                updateEngineheaterInDb(jsonNode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Mono.just(getOneEngineheater(jsonNode.get("devUi").toString()));
    }

    public List<JsonNode> getAllEngineheaters() {
        List<JsonNode> list = new ArrayList<>();
        String sql = "SELECT * FROM engineheaters";
        try {
            Connection con = this.connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ObjectMapper mapper = new ObjectMapper();
            while (rs.next()) {
                String tempString = "{\"devUi\":" + rs.getString("DEVUI") + "," +
                        "\"heaterIsOn\": " + (rs.getInt("HEATERISON") == 1) + "," +
                        "\"startHeaterNow\": " + (rs.getInt("STARTHEATERNOW") == 1) + "," +
                        "\"monTimeActive\": " + (rs.getInt("MONTIMEACTIVE") == 1) + "," +
                        "\"tueTimeActive\": " + (rs.getInt("TUETIMEACTIVE") == 1) + "," +
                        "\"wedTimeActive\": " + (rs.getInt("WEDTIMEACTIVE") == 1) + "," +
                        "\"thuTimeActive\": " + (rs.getInt("THUTIMEACTIVE") == 1) + "," +
                        "\"friTimeActive\": " + (rs.getInt("FRITIMEACTIVE") == 1) + "," +
                        "\"satTimeActive\": " + (rs.getInt("SATTIMEACTIVE") == 1) + "," +
                        "\"sunTimeActive\":" + (rs.getInt("SUNTIMEACTIVE") == 1) + "," +
                        "\"monEndTime\": " + rs.getString("MONENDTIME") + "," +
                        "\"tueEndTime\": " + rs.getString("TUEENDTIME") + "," +
                        "\"wedEndTime\": " + rs.getString("WEDENDTIME") + "," +
                        "\"thuEndTime\": " + rs.getString("THUENDTIME") + "," +
                        "\"friEndTime\": " + rs.getString("FRIENDTIME") + "," +
                        "\"satEndTime\": " + rs.getString("SATENDTIME") + "," +
                        "\"sunEndTime\": " + rs.getString("SUNENDTIME") + "}";
                JsonNode jsonNode = mapper.readTree(tempString);
                list.add(jsonNode);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return list;
    }


    public int getPingFromWebIot() {
        String ip = "webiot.iioote.io";
        String targetLine = "";
        try {
            String command = "ping " + ip;
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                if (inputLine.contains("Average")) targetLine = inputLine;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) Float.parseFloat(targetLine.substring(targetLine.length() - 4, targetLine.length() - 2));
    }


    public void deleteDataInTableEngineheaters() {
        Connection con = this.connect();
        String sql = "DELETE FROM engineheaters";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
            System.out.println("Deleted all data in table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
