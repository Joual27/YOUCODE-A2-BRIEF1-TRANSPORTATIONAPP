package com.youcode.transportationApp.contracts;

import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Timestamp;



import com.youcode.transportationApp.contracts.interfaces.ContractRepositoryI;
import com.youcode.transportationApp.database.DbConnection;
import com.youcode.transportationApp.enums.ContractStatus;
import com.youcode.transportationApp.partners.Partner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ContractRepository implements ContractRepositoryI{
    

    private Connection cnx;

    public ContractRepository(){
        try {
            cnx = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Contract> getAllContracts(){
        ArrayList<Contract> contracts = new ArrayList<Contract>();
        String query = "SELECT * FROM contracts WHERE deleted_at IS NULL";
        try(PreparedStatement stmt = cnx.prepareStatement(query)){  
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Contract c = new Contract();
                c.setContractId(rs.getString("contractid"));
                Timestamp ts = rs.getTimestamp("startingDate");
                if (ts != null) {
                    LocalDate startingDate = ts.toLocalDateTime().toLocalDate();
                    c.setStartingDate(startingDate);
                }
                Timestamp ts2 = rs.getTimestamp("endDate");
                if (ts2 != null) {
                    LocalDate endingDate = ts2.toLocalDateTime().toLocalDate();
                    c.setEndDate(endingDate);
                }
                c.setSpecialRate(rs.getDouble("specialRate"));
                c.setAgreementConditions(rs.getString("agreementConditions"));
                c.setRenewable(rs.getBoolean("renewable"));
                c.setContractStatus(ContractStatus.valueOf(rs.getString("contractStatus")));
                Partner p = new Partner();
                p.setPartnerId(rs.getString("partnerId"));
                c.setPartner(p);

                contracts.add(c);
            }
        }
        catch(SQLException e){
           e.printStackTrace();
        }
        return contracts;
    }


    public void createContract(Contract contract){
        String query = "INSERT INTO contracts VALUES(?,?,?,?,?,?,?,?)";

        LocalDate startingDate = contract.getStartingDate();
        LocalDateTime startingDateTime = startingDate.atStartOfDay();
        Timestamp startingTimestamp = Timestamp.valueOf(startingDateTime);

        LocalDate endingDate = contract.getStartingDate();
        LocalDateTime endingDateTime = endingDate.atStartOfDay();
        Timestamp endingTimestamp = Timestamp.valueOf(endingDateTime);

        try(PreparedStatement stmt = cnx.prepareStatement(query)){
            stmt.setString(1, contract.getContractId());
            stmt.setTimestamp(2, startingTimestamp);
            stmt.setTimestamp(3, endingTimestamp);
            stmt.setDouble(4, contract.getSpecialRate());
            stmt.setString(5, contract.getAgreementConditions());
            stmt.setBoolean(6, contract.isRenewable());
            stmt.setObject(7, contract.getContractStatus().name(), java.sql.Types.OTHER);
            stmt.setString(8, contract.getPartner().getPartnerId());
            stmt.executeUpdate();
        }
        catch(SQLException e){
          e.getStackTrace();
        }
    }

    
    @Override
    public void editContract(Contract contract) throws SQLException {
        String query = "UPDATE contracts SET startingDate = ?, endDate = ?, specialRate = ?, agreementConditions = ?, renewable = ?, contractStatus = ? WHERE contractId = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(contract.getStartingDate()));
            stmt.setDate(2, java.sql.Date.valueOf(contract. getEndDate()));
            stmt.setDouble(3, contract.getSpecialRate());
            stmt.setString(4, contract.getAgreementConditions());
            stmt.setBoolean(5, contract.isRenewable());
            stmt.setObject(6, contract.getContractStatus().name(), java.sql.Types.OTHER);
            stmt.setString(7, contract.getContractId());
            stmt.executeUpdate();
        }
    }

    @Override
    public Contract getContractById(String contractId) {
        String query = "SELECT * FROM contracts WHERE contractId = ? AND deleted_at IS NULL";

        try{
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, contractId);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                Partner partner = new Partner();
                partner.setPartnerId(res.getString("partnerId"));
                return new Contract(res.getString("contractId"), 
                                    res.getDate("startingDate").toLocalDate(), 
                                    res.getDate("endDate").toLocalDate(), 
                                    res.getDouble("specialRate"), 
                                    res.getString("agreementConditions"), 
                                    res.getBoolean("renewable"), 
                                    ContractStatus.valueOf(res.getString("contractStatus")), 
                                    partner);
            } else {
                System.out.println("No contract found with that ID.");
                return null;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        
    }


    @Override
    public void removeContract(String contractId) throws SQLException{
        String query = "UPDATE contracts SET deleted_at = NOW() WHERE contractId = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, contractId);
            stmt.executeUpdate();
        }
    }

    

}
