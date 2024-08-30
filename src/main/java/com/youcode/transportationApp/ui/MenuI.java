package com.youcode.transportationApp.ui;

 /**
  * SubMenuI
  */
public interface MenuI {
  
    public void displayMenu();
    
    public int getMenuChoice();

    public void handleChoice(int choice);

    public void startMenu();

}
