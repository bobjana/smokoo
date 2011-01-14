// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package za.co.zynafin.smokoo;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import za.co.zynafin.smokoo.Bid;

privileged aspect Bid_Roo_Finder {
    
    public static TypedQuery<Bid> Bid.findBidsByAmmount(double ammount) {
        EntityManager em = Bid.entityManager();
        TypedQuery<Bid> q = em.createQuery("SELECT Bid FROM Bid AS bid WHERE bid.ammount = :ammount", Bid.class);
        q.setParameter("ammount", ammount);
        return q;
    }
    
}