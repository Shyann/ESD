package com.xyz.esd.controllers;


import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import com.xyz.esd.JDBCConnectionHandler;
import com.xyz.esd.beans.Address;
import com.xyz.esd.beans.User;

public class UserManager {

    private JDBCConnectionHandler handler;
    public UserManager(){
         handler = new JDBCConnectionHandler();
    }

    public ArrayList<User> getUsers(){
        String getUsers = "SELECT * FROM users WHERE status <> 'ADMIN'";
        ArrayList<HashMap<String, Object>> resultMap = handler.queryToHashMapList(getUsers);
        /*
        * ArrayList<User> users = resultMap.stream().map(this::constructUser).collect(Collectors.toCollection(ArrayList::new));
         */
        ArrayList<User> users = new ArrayList<>();
        for(HashMap<String, Object> result : resultMap){
            users.add(this.constructUser(result));
        }
        return users;
    }

    private User constructUser(HashMap<String, Object> userHash){
        User tUser = new User();

        tUser.setUsername((String)userHash.get("id"));
        tUser.setStatus(User.State.valueOf((String)userHash.get("status")));

        tUser.setPassword((String)userHash.get("password"));
        
        if (tUser.getStatus() == User.State.ADMIN) {
			return tUser;
		}
        
        // Get name, address, dob, dor, status, balance from Members
        String getInfo = "SELECT * FROM members WHERE id = '" + tUser.getUsername() + "'";

        ArrayList<HashMap<String, Object>>  resultMap = handler.queryToHashMapList(getInfo);

        if(resultMap.size() == 0){
            System.out.println("Unknown error.. could not find entry for user "+tUser.getUsername());
            return null;
        }

        tUser.setName((String)resultMap.get(0).get("name"));
        tUser.setAddress(new Address((String)resultMap.get(0).get("address")));
        tUser.setDob((Date)resultMap.get(0).get("dob")); /* don't think this will work.. */
        tUser.setDor((Date)resultMap.get(0).get("dor"));

        /* Skipping status from members because it's already in users...?? */

        tUser.setBalance((Float)resultMap.get(0).get("balance"));

        return tUser;
    }

    public User getUser(String id){
        // Get username from users
        String getUser = "SELECT * FROM users WHERE id = '" + id + "'";
        ArrayList<HashMap<String, Object>> resultMap = handler.queryToHashMapList(getUser);
        if(resultMap.size() == 0){
            /* Incorrect password or username */
            return null;
        }
        User tUser = this.constructUser(resultMap.get(0));

        return tUser;
    }

    public ArrayList<User> getUsers(User.State state){
        // Get username from users
        String initialStatement = "SELECT * FROM users WHERE status = ?";
        ArrayList<Object> params = new ArrayList<>();
        params.add(state.toString());
        ArrayList<HashMap<String, Object>> resultMap = handler.executeSELECT(initialStatement, params);

        ArrayList<User> users = new ArrayList<>();
        for(HashMap<String, Object> result : resultMap){
            users.add(constructUser(result));
        }

        return users;
    }

    public void updateUser(User tUser){
        User prevUser = this.getUser(tUser.getUsername());
        if(tUser.getBalance() != prevUser.getBalance()){
            System.out.println("updating balance");
            String balance = "UPDATE members SET balance = "+tUser.getBalance()+" WHERE id = '"+tUser.getUsername()+"'";
            handler.executeQuery(balance);
        }
        if(tUser.getStatus() != prevUser.getStatus()){
            String status = "UPDATE members SET status = '"+tUser.getStatus()+"' WHERE id = '"+tUser.getUsername()+"'";
            handler.executeQuery(status);
            status = "UPDATE users SET status = '"+tUser.getStatus()+"' WHERE id = '"+tUser.getUsername()+"'";
            handler.executeQuery(status);
        }
    }

    public void createUser(User tUser){

        String memberQuery = "INSERT INTO Members VALUES(\"" + tUser.getUsername() + "\",\"" + tUser.getName() + "\",\""
                + tUser.getAddress().toString() + "\",\"" + tUser.getDob() + "\",\"" + tUser.getDor() + "\",\"APPLIED\",\"0\")";

        String userQuery = "INSERT INTO Users VALUES(\"" + tUser.getUsername() + "\",\"" + tUser.getPassword() + "\",\"APPLIED\")";
        handler.executeQuery(memberQuery);
        handler.executeQuery(userQuery);

        /*
        6.	When a member is registered or one year passed over, the system automatically charges the member with the membership fee, which can be £10 in this case; this will be added to Balance field of Members table.
        */
        tUser.setBalance(tUser.getBalance() + 10);
        this.updateUser(tUser);
    }

    public boolean isUser(String username){
        String getUser = "SELECT * FROM users WHERE id = '" + username + "'";
        ArrayList<HashMap<String, Object>> resultMap = handler.queryToHashMapList(getUser);
        return resultMap.size() != 0;
    }

    public void approveMember(User user) {
        System.out.println("Approving member");
        user.setStatus(User.State.APPROVED);
        this.updateUser(user);
    }

    public void suspendMember(User user) {
        System.out.println("Suspending member");
        user.setStatus(User.State.SUSPENDED);
        this.updateUser(user);
    }

    public void adminMember(User user){
        System.out.println("Making member an admin");
        user.setStatus(User.State.ADMIN);
        this.updateUser(user);
    }
}
