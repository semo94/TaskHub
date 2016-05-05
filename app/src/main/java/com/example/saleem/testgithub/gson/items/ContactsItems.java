package com.example.saleem.testgithub.gson.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 4/21/2016.
 */
public class ContactsItems {
    private List<MyContactsList> MyContactsList = new ArrayList<MyContactsList>();
    private boolean IsSucceeded;

    public List<MyContactsList> getMyContactsList() {
        return MyContactsList;
    }

    public void setMyContactsList(List<MyContactsList> MyContactsList) {
        this.MyContactsList = MyContactsList;
    }

    public Boolean getIsSucceeded() {
        return IsSucceeded;
    }

    public void setIsSucceeded(Boolean IsSucceeded) {
        this.IsSucceeded = IsSucceeded;
    }


    public class MyContactsList {

        private String Id;
        private String Name;
        private String Image;
        private boolean isSelected;

        public boolean getisSelected() {
            return isSelected;
        }

        public void setisSelected(boolean selected) {
            isSelected = selected;
        }


        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }

    }
}
