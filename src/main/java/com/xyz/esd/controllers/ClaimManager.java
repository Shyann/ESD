package com.xyz.esd.controllers;

import com.xyz.esd.JDBCConnectionHandler;
import com.xyz.esd.beans.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ClaimManager {

    private JDBCConnectionHandler handler;

    public ClaimManager() {
        handler = new JDBCConnectionHandler();
    }

    public ArrayList<Claim> getClaims() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");        
        String today = format1.format(cal.getTime());
        cal.add(Calendar.YEAR, -1);
        String lastYear = format1.format(cal.getTime());

        String query = "SELECT * FROM claims WHERE date BETWEEN \'" + lastYear + "\' and \'" + today + "\';";
        ArrayList<HashMap<String, Object>> resultMap = handler.queryToHashMapList(query);
        ArrayList<Claim> claims = new ArrayList<>();
        for (HashMap<String, Object> row : resultMap) {
            Claim tClaim = new Claim();
            tClaim.setId((Integer) row.get("id"));
            tClaim.setMemberID((String) row.get("mem_id"));
            tClaim.setDate((Date) row.get("date"));
            tClaim.setRationale((String) row.get("rationale"));
            tClaim.setStatus(Claim.State.valueOf((String) row.get("status")));
            tClaim.setAmount((Float) row.get("amount"));
            /* count how many claims for username */
            int num_claims = 0;
            System.out.println("Looping through exisitng claims");
            for(Claim claim : claims){
                // TODO make sure we only fetch 1 year worth of claims
                if(tClaim.getMemberID().equals(claim.getMemberID())){
                    num_claims++;
                    System.out.println("found a claim");
                }
            }
            if(num_claims >= 2){
                System.out.println("Setting claim validity to false");
                tClaim.setValidity(false); /* make this claim not valid */
            }
            claims.add(tClaim);
        }
        return claims;
    }

    public ArrayList<Claim> getClaims(Claim.State state) {
        String query = "SELECT * FROM claims WHERE status = \"" + state + "\"";
        ArrayList<HashMap<String, Object>> resultMap = handler.queryToHashMapList(query);
        ArrayList<Claim> claims = new ArrayList<>();
        for (HashMap<String, Object> row : resultMap) {
            Claim tClaim = new Claim();
            tClaim.setMemberID((String) row.get("mem_id"));
            tClaim.setId((Integer) row.get("id"));
            tClaim.setDate((Date) row.get("date"));
            tClaim.setRationale((String) row.get("rationale"));
            tClaim.setStatus(Claim.State.valueOf((String) row.get("status")));
            tClaim.setAmount((Float) row.get("amount"));
            claims.add(tClaim);
        }
        return claims;
    }

    public ArrayList<Claim> getClaims(String username) {
        String query = "SELECT * FROM claims WHERE mem_id = \"" + username + "\"";
        ArrayList<HashMap<String, Object>> resultMap = handler.queryToHashMapList(query);
        ArrayList<Claim> claims = new ArrayList<>();
        for (HashMap<String, Object> row : resultMap) {
            Claim tClaim = new Claim();
            tClaim.setId((Integer) row.get("id"));
            tClaim.setMemberID((String) row.get("mem_id"));
            tClaim.setDate((Date) row.get("date"));
            tClaim.setRationale((String) row.get("rationale"));
            tClaim.setStatus(Claim.State.valueOf((String) row.get("status")));
            tClaim.setAmount((Float) row.get("amount"));
            claims.add(tClaim);
        }
        return claims;
    }

    public float sumClaims() {
        ArrayList<Claim> claims = getClaims(Claim.State.APPROVED);
        float sum = 0;
        return claims.stream().map((c) -> c.getAmount()).reduce(sum, (accumulator, _item) -> accumulator + _item);
    }

    public void addClaim(User tUser, Claim tClaim) {
        String claimQuery = "INSERT INTO Claims (mem_id, date, rationale, status, amount) VALUES('" + tUser.getUsername() + "','" + tClaim.getDate() + "','" + tClaim.getRationale() + "','" + tClaim.getStatus() + "'," + tClaim.getAmount() + ")";
        handler.executeQuery(claimQuery);
    }

    public void approveClaim(Claim claim) {
        claim.setStatus(Claim.State.APPROVED);
        String balance = "UPDATE claims SET status = '" + claim.getStatus() + "' WHERE id = " + claim.getId() + "";
        handler.executeQuery(balance);
    }

    public void rejectClaim(Claim claim) {
        claim.setStatus(Claim.State.REJECTED);
        String balance = "UPDATE claims SET status = '" + claim.getStatus() + "' WHERE id = " + claim.getId() + "";
        handler.executeQuery(balance);
    }

    public Claim getClaim(int id) {
        String query = "SELECT * FROM claims WHERE id = " + id;
        ArrayList<HashMap<String, Object>> resultMap = handler.queryToHashMapList(query);
        Claim tClaim = new Claim();
        HashMap<String, Object> row = resultMap.get(0);
        tClaim.setMemberID((String) row.get("mem_id"));
        tClaim.setId((Integer) row.get("id"));
        tClaim.setDate((Date) row.get("date"));
        tClaim.setRationale((String) row.get("rationale"));
        tClaim.setStatus(Claim.State.valueOf((String) row.get("status")));
        tClaim.setAmount((Float) row.get("amount"));
        return tClaim;
    }
}
