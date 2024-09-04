package com.youcode.transportationApp.contracts;

import com.youcode.transportationApp.partners.Partner;

public class ValidContractDTO {
    private Contract contract;
    private Partner partner;
    

    
    public ValidContractDTO(Contract contract, Partner partner) {
        this.contract = contract;
        this.partner = partner;
    }


    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

}
