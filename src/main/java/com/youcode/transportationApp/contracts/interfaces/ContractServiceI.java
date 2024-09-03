package com.youcode.transportationApp.contracts.interfaces;

import com.youcode.transportationApp.enums.ContractStatus;
import java.sql.SQLException;

public interface ContractServiceI {

    public void fetchAllContracts();

    public void addContract() throws SQLException;

    public void updateContract() throws SQLException;

    public boolean handleRenewable();

    public ContractStatus handleContractStatus();

    public ContractStatus handleContractStatusUpdate(ContractStatus currentStatus);
    
    public void deleteContract();
}
