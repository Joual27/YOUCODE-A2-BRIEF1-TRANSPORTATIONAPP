package com.youcode.transportationApp.contracts;

import com.youcode.transportationApp.contracts.interfaces.ContractRepositoryI;
import com.youcode.transportationApp.contracts.interfaces.ContractServiceI;


import java.util.List;




public class ContractService implements ContractServiceI{


    private final ContractRepositoryI contractRepository;


    public ContractService(ContractRepositoryI contractRepository){
        this.contractRepository = contractRepository;
    }

     @Override
     public List<Contract> getAllContracts(){
        return contractRepository.getAllContracts();
     }

    public Contract addContract(Contract c){
        contractRepository.createContract(c);
        return c;
    }


    @Override 
    public Contract getContractById(String contractId){
        return contractRepository.getContractById(contractId);
    }


    @Override
    public Contract updateContract(Contract c) {    
        contractRepository.editContract(c);
        return c;
    }

   

    @Override
    public Contract deleteContract(String contractId) {
        Contract existingContract = contractRepository.getContractById(contractId);
        contractRepository.removeContract(contractId);
        return existingContract;
    }
}
