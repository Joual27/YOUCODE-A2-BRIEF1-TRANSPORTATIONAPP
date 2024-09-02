package com.youcode.transportationApp.partners.interfaces;

import java.util.ArrayList;
import java.sql.SQLException;
import com.youcode.transportationApp.partners.Partner;

public interface PartnerRepositoryI {

    public ArrayList<Partner> getAllPartners();

    public Partner getPartnerById(String partnerId) throws SQLException;    

    public void createPartner(Partner partner) throws SQLException;

    public void editPartner(Partner partner) throws SQLException;

    public void removePartner(String partnerId);
}