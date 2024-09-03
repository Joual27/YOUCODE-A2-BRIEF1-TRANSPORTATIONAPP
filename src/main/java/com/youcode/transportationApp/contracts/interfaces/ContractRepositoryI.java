package com.youcode.transportationApp.contracts.interfaces;
import java.util.ArrayList;
import java.sql.SQLException;

import com.youcode.transportationApp.contracts.Contract;;

public interface ContractRepositoryI {
    
    public ArrayList<Contract> getAllContracts();

    public Contract getContractById(String contractId) throws SQLException;

    public void createContract(Contract contract);

    public void editContract(Contract contract) throws SQLException ;

    public void removeContract(String contractId) throws SQLException;
} 
