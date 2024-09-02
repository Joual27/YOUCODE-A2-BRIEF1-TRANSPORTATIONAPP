package com.youcode.transportationApp.partners.interfaces;

import com.youcode.transportationApp.enums.PartnershipStatus;
import com.youcode.transportationApp.enums.TransportationType;
import java.sql.SQLException;

public interface PartnerServiceI {

    public void fetchAllPartners();
    public void addPartner();
    public TransportationType handleTransportationType();
    public PartnershipStatus handlePartnershipStatus();
    public void updatePartner() throws SQLException;
    public TransportationType handleTransportationTypeUpdate(TransportationType currentType);
    public PartnershipStatus handlePartnershipStatusUpdate(PartnershipStatus currPartnershipStatus);

    public void deletePartner();


} 
