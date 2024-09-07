package com.youcode.transportationApp.contracts.interfaces;
import java.util.ArrayList;

import com.youcode.transportationApp.contracts.Contract;;

public interface ContractRepositoryI {
    
    public ArrayList<Contract> getAllContracts();

    public Contract getContractById(String contractId);

    public void createContract(Contract contract);

    public void editContract(Contract contract) ;

    public void removeContract(String contractId);
} 
