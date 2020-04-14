package vk.com.korne3v.AsyncMenu.api;

public class MenuAPI {

    /**
    * Initialization of the plugin, for convenient work with the main class
    **/
    public AsyncMenu getPlugin();
    
    /**
    *
    * You can create a separate menu in your plugin,
    * it will not depend on the configuration of the AsyncMenu plugin
    *
    * @param title - This is inventory title name.
    * @param rows - Is the number of inventory sections, the maximum value of 6 is the minimum 1
    *
    **/
    public Menu create(final String title, int rows);

}
