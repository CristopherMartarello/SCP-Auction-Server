
package com.mycompany.auctionserver;

/**
 *
 * @author crisr
 */
public class Item {
    
    private String name;
    private double price;
    private String description;
    private double currentBid = 0;
    private int currentTime = 20;
    private String currentWinner;
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public Item(String name, double price, String description, String imagePath) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentWinner() {
        return currentWinner;
    }

    public void setCurrentWinner(String currentWinner) {
        this.currentWinner = currentWinner;
    }

    @Override
    public String toString() {
        return "Item{" + "name=" + name + ", price=" + price + ", description=" + description + ", currentBid=" + currentBid + ", currentTime=" + currentTime + ", currentWinner=" + currentWinner + '}';
    }
    
    
}
