package com.youcode.transportationApp.contracts.interfaces;

import com.youcode.transportationApp.contracts.Contract;
import java.util.List;

public interface ContractServiceI {

    public List<Contract> getAllContracts();

    public Contract addContract(Contract c);

    public Contract updateContract(Contract c);

    public Contract getContractById(String contractId);
    
    public Contract deleteContract(String contractId);
}
