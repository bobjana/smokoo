// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package za.co.zynafin.smokoo;

import java.lang.String;
import java.util.Date;

privileged aspect Auction_Roo_JavaBean {
    
    public String Auction.getName() {
        return this.name;
    }
    
    public void Auction.setName(String name) {
        this.name = name;
    }
    
    public String Auction.getAuctionTitle() {
        return this.auctionTitle;
    }
    
    public void Auction.setAuctionTitle(String auctionTitle) {
        this.auctionTitle = auctionTitle;
    }
    
    public long Auction.getAuctionId() {
        return this.auctionId;
    }
    
    public void Auction.setAuctionId(long auctionId) {
        this.auctionId = auctionId;
    }
    
    public Date Auction.getDate() {
        return this.date;
    }
    
    public void Auction.setDate(Date date) {
        this.date = date;
    }
    
    public boolean Auction.isClosed() {
        return this.closed;
    }
    
    public void Auction.setClosed(boolean closed) {
        this.closed = closed;
    }
    
    public double Auction.getRetailPrice() {
        return this.retailPrice;
    }
    
    public void Auction.setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }
    
    public boolean Auction.isFastAndFurious() {
        return this.fastAndFurious;
    }
    
    public void Auction.setFastAndFurious(boolean fastAndFurious) {
        this.fastAndFurious = fastAndFurious;
    }
    
    public double Auction.getBidIncrementAmount() {
        return this.bidIncrementAmount;
    }
    
    public void Auction.setBidIncrementAmount(double bidIncrementAmount) {
        this.bidIncrementAmount = bidIncrementAmount;
    }
    
    public boolean Auction.isBiddingAutomated() {
        return this.biddingAutomated;
    }
    
    public void Auction.setBiddingAutomated(boolean biddingAutomated) {
        this.biddingAutomated = biddingAutomated;
    }
    
    public int Auction.getMaxNumberOfBids() {
        return this.maxNumberOfBids;
    }
    
    public void Auction.setMaxNumberOfBids(int maxNumberOfBids) {
        this.maxNumberOfBids = maxNumberOfBids;
    }
    
}
