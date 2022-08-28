package ir.tamuk.reservation.fragments.ui.home.Model;

import java.util.ArrayList;

public class OptionList {
    public ArrayList<Option> options = new ArrayList();

    public class Option
    {
        private int id;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
