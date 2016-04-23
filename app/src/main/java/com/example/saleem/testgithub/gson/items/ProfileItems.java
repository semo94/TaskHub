package com.example.saleem.testgithub.gson.items;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 4/21/2016.
 */
public class ProfileItems {
    private String Name;
    private String Email;
    private String Image;
    private Boolean IsSucceeded;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }
    public Boolean getIsSucceeded() {
        return IsSucceeded;
    }

    public void setIsSucceeded(Boolean IsSucceeded) {
        this.IsSucceeded = IsSucceeded;
    }

}
