package com.liule.jdbc;

import java.sql.*;
import java.util.Set;

public class jdbcHomeWork {
    private final static String USER = "root";
    private final static String PASSWORD = "276668";
    private final static String URL = "jdbc:mysql://localhost:3306/jdbc?serverTimezone=GMT&characterEncoding=UTF-8";
    private final static String SQLC = "insert into teacher(id,name,course) values(?,?,?)";
    private final static String SQLU = "update teacher set name=? where id=?";
    private final static String SQLR = "select id,name from teacher";
    private final static String SQLD = "delete from teacher where id=?";
    public static void C(){
        //增加练习
        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);){
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(SQLC)){
                ps.setInt(1,1);
                ps.setString(2,"刘乐");
                ps.setString(3,"JAVAWEB");
                ps.executeUpdate();

                conn.commit();
            }catch (SQLException e){
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void R(){
        //查询练习
        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);)
        {
            try(PreparedStatement ps = conn.prepareStatement(SQLR)){
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        System.out.println(rs.getInt(1));
                        System.out.println(rs.getString(2));
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void D(){
        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(SQLD);){
                ps.setInt(1,1);
                ps.executeUpdate();
                conn.commit();
            }catch (SQLException e){
                conn.rollback();
            }finally {
                conn.setAutoCommit(true);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void U(){
        //更改练习
        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(SQLU)){
                ps.setInt(2,1);
                ps.setString(1,"刘乐刘乐");
                ps.executeUpdate();
                conn.commit();
            }catch (SQLException e){
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void Cbat(){
        //增加练习
        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);){
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(SQLC)){
                for(int i=1;i<=500;i++) {

                    ps.setInt(1, i);
                    ps.setString(2, "刘乐"+i);
                    ps.setString(3, "JAVAWEB"+i);
                    ps.addBatch();
                    if(i%100==0){
                        ps.executeBatch();
                        ps.clearBatch();
                    }
                }
                conn.commit();
            }catch (SQLException e){
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void Rbat(){
        //查询练习
        try(Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);)
        {
            try(PreparedStatement ps = conn.prepareStatement(SQLR,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)){
                try(ResultSet rs = ps.executeQuery()){
                    rs.absolute(-2);
                    System.out.println(rs.getInt(1));
                    System.out.println(rs.getString(2));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Rbat();

    }
}
