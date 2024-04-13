package com.gmail.markushygedombrowski.config;

import com.gmail.markushygedombrowski.items.RareItems;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class VagtVault {
    private String name;
    private Location location;
    private int resetTime;
    private int failresetTime;
    private int standStillTime;
    private List<ItemStack> items;
    private List<ItemStack> heads;
    private List<RareItems> rareItems;
    private List<ItemStack> rareHeads;
    private double headChance;
    private double rareHeadChance;
    private LocalDateTime lastReset = LocalDateTime.now();
    private String boardcastMessage;
    private String playerMessage;
    private boolean isCooldown = false;
    private boolean sendBoardcast;



    public VagtVault(String name, Location location, int resetTime, int failresetTime, int standStillTime, List<ItemStack> items, List<ItemStack> heads, List<RareItems> rareItems, List<ItemStack> rareHeads, double headChance, double rareHeadChance, String boardcastMessage, String playerMessage, boolean sendBoardcast) {
        this.name = name;
        this.location = location;
        this.resetTime = resetTime;
        this.failresetTime = failresetTime;
        this.standStillTime = standStillTime;
        this.items = items;
        this.heads = heads;
        this.rareItems = rareItems;
        this.rareHeads = rareHeads;
        this.headChance = headChance;
        this.rareHeadChance = rareHeadChance;
        this.boardcastMessage = boardcastMessage;
        this.playerMessage = playerMessage;
        this.sendBoardcast = sendBoardcast;
    }
    public VagtVault(String name,Location location,int resetTime,int failresetTime,int standStillTime){
        this.name = name;
        this.location = location;
        this.resetTime = resetTime;
        this.failresetTime = failresetTime;
        this.standStillTime = standStillTime;
        this.boardcastMessage = "§4%player% §7er igang med at §4røve §6%vagtvault_name%";
        this.playerMessage = "§7Du er igang med at §4røve §6%vagtvault_name% du skal stå stille i §4%vagtvault_standstill% §7sekunder";
        this.sendBoardcast = true;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getResetTime() {
        return resetTime;
    }

    public void setResetTime(int resetTime) {
        this.resetTime = resetTime;
    }

    public List<ItemStack> getItems() {
        return items;
    }
    public LocalDateTime getLastReset() {
        return lastReset;
    }
    public void setLastReset(LocalDateTime lastReset) {
        this.lastReset = lastReset;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }
    public void addItem(ItemStack item){
        if(items == null){
            items = new ArrayList<>();
        }
        items.add(item);
    }
    public void addItems(List<ItemStack> items){
        if(this.items == null){
            this.items = new ArrayList<>();
        }
        this.items.addAll(items);
    }

    public List<ItemStack> getHeads() {
        return heads;
    }

    public void setHeads(List<ItemStack> heads) {
        this.heads = heads;
    }
    public void addHead(ItemStack head){
        if(heads == null){
            heads = new ArrayList<>();
        }
        heads.add(head);
    }
    public void addHeads(List<ItemStack> heads){
        if(this.heads == null){
            this.heads = new ArrayList<>();
        }
        this.heads.addAll(heads);
    }

    public List<RareItems> getRareItems() {
        return rareItems;
    }

    public RareItems getRareItem(ItemStack item){
        for(RareItems rareItem : rareItems){
            if(rareItem.getItem().equals(item)){
                return rareItem;
            }
        }
        return null;
    }

    public void addRareItemList(List<RareItems> rareItems){
        if(this.rareItems == null){
            this.rareItems = new ArrayList<>();
        }
        this.rareItems.addAll(rareItems);
    }
    public void addRareItem(RareItems rareItem){
        if(rareItems == null){
            rareItems = new ArrayList<>();
        }
        rareItems.add(rareItem);
    }

    public void setRareItems(List<RareItems> rareItems) {
        this.rareItems = rareItems;
    }
    public void removeRareItem(RareItems rareItem){
        rareItems.remove(rareItem);
    }


    public List<ItemStack> getRareHeads() {
        return rareHeads;
    }

    public void setRareHeads(List<ItemStack> rareHeads) {
        this.rareHeads = rareHeads;
    }
    public void addRareHead(ItemStack rareHead){
        if(rareHeads == null){
            rareHeads = new ArrayList<>();
        }
        rareHeads.add(rareHead);
    }
    public void addRareHeads(List<ItemStack> rareHeads){
        if(this.rareHeads == null){
            this.rareHeads = new ArrayList<>();
        }
        this.rareHeads.addAll(rareHeads);
    }

    public double getHeadChance() {
        return headChance;
    }

    public void setHeadChance(int headChance) {
        this.headChance = headChance;
    }

    public double getRareHeadChance() {
        return rareHeadChance;
    }

    public void setRareHeadChance(int rareHeadChance) {
        this.rareHeadChance = rareHeadChance;
    }

    public int getFailresetTime() {
        return failresetTime;
    }

    public void setFailresetTime(int failresetTime) {
        this.failresetTime = failresetTime;
    }

    public boolean canBeCreated() {
        return name != null && location != null && items != null;
    }

    public int getStandStillTime() {
        return standStillTime;
    }

    public void setStandStillTime(int standStillTime) {
        this.standStillTime = standStillTime;
    }

    public void fixNullLists() {
        if(heads == null) {
            heads = new ArrayList<>();
        }
        if(items == null) {
            items = new ArrayList<>();
        }
        if(rareItems == null) {
            rareItems = new ArrayList<>();
        }
        if(rareHeads == null) {
            rareHeads = new ArrayList<>();
        }

    }

    public String getBoardcastMessage() {
        return boardcastMessage;
    }
    public String getBoardcastMessage(Player player){
        return PlaceholderAPI.setPlaceholders(player,formatMessage(boardcastMessage));
    }

    public void setBoardcastMessage(String boardcastMessage) {
        this.boardcastMessage = boardcastMessage;
    }

    public String getPlayerMessage() {
        return playerMessage;
    }

    public String getPlayerMessage(Player player){

        return PlaceholderAPI.setPlaceholders(player,formatMessage(playerMessage));
    }

    public void setPlayerMessage(String playerMessage) {
        this.playerMessage = playerMessage;
    }

    public boolean isCooldown() {
        return isCooldown;
    }

    public void setCooldown(boolean cooldown) {
        isCooldown = cooldown;
    }

    public boolean isSendBoardcast() {
        return sendBoardcast;
    }

    public void setSendBoardcast(boolean sendBoardcast) {
        this.sendBoardcast = sendBoardcast;
    }

    public String formatMessage(String message){
        if(message.contains("%vagtvault_name%")) {
            message = message.replace("%vagtvault_name%",name);
        }
        if(message.contains("%vagtvault_standstill%")){
            message = message.replace("%vagtvault_standstill%",String.valueOf(standStillTime));
        }
        if(message.contains("%vagtvault_reset%")){
            message = message.replace("%vagtvault_reset%",String.valueOf(resetTime));
        }
        if(message.contains("%vagtvault_failreset%")){
            message = message.replace("%vagtvault_failreset%",String.valueOf(failresetTime));
        }
        if(message.contains("%vagtvault_headchance%")){
            message = message.replace("%vagtvault_headchance%",String.valueOf(headChance));
        }
        if(message.contains("%vagtvault_rareheadchance%")){
            message = message.replace("%vagtvault_rareheadchance%",String.valueOf(rareHeadChance));
        }

        return message;

    }


}
