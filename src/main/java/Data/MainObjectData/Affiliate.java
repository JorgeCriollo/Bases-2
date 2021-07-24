package Data.MainObjectData;

import Data.DB;
import Data.MainObjectData.AccountData.Account;
import Data.Misc.SerializableImage;

import java.awt.*;
import java.util.Calendar;

public class Affiliate {
    public static transient Affiliate current = null;

    private final int db_id;
    private final String id;
    private final SerializableImage picture;
    private final String name;
    private final String lastName;
    private final String email;
    private final Calendar birthday;
    private final Calendar memberSince;
    private Account[] accounts;

    public Affiliate(int db_id, String id, Image picture, String name, String lastName, String email, Calendar birthday,
                     Calendar memberSince) {
        this.db_id = db_id;
        this.id = id;
        this.picture = new SerializableImage(picture);
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.memberSince = memberSince;
        accounts = null;
    }

    public Affiliate(int db_id, Image picture, String name, String lastName, Account account) {
        this(db_id, "", picture, name, lastName, "", null, null);
        this.accounts = new Account[]{account};
    }

    public int getDB_ID() {
        return db_id;
    }

    public String getId() {
        return id;
    }

    public Image getPicture() {
        return picture.getImage();
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public Calendar getMemberSince() {
        return memberSince;
    }

    public Account[] getAccounts() {
        if (accounts == null)
            updateAccounts();

        return accounts;
    }

    public void updateAccounts() {
        accounts = DB.getAccounts(this);
    }
}
