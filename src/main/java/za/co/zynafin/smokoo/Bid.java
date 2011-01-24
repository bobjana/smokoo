package za.co.zynafin.smokoo;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findBidsByAmmount" })
public class Bid {

    private Date date;

    private String user;

    private double ammount;

    private String type;
        
    @ManyToOne(targetEntity=Auction.class )
    private Auction auction;
    
    public static TypedQuery<Bid> findBidsByAuctionAndUserAndDate(Auction auction, String user, Date date) {
        EntityManager em = Bid.entityManager();
        TypedQuery<Bid> q = em.createQuery("SELECT Bid FROM Bid AS bid WHERE bid.auction = :auction and bid.user = :user and bid.date = :date", Bid.class);
        q.setParameter("auction", auction);
        q.setParameter("user", user);
        q.setParameter("date", date);
        return q;
    }
}
