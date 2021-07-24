package Data;

import Data.MainObjectData.AccountData.Account;
import Data.MainObjectData.AccountData.AccountRegistry;
import Data.MainObjectData.AccountData.AccountType;
import Data.MainObjectData.AccountData.TransactionType;
import Data.MainObjectData.Affiliate;
import Data.SecondaryObjectData.Bill;
import Data.SecondaryObjectData.Provider;
import Data.SecondaryObjectData.Service;
import GUI.View.Misc.GUIUtils;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DB {
    public static void connect() {
        //TODO Conectar base de datos aquí
    }

    public static boolean login(String user, char[] password) {
        try {
            //TODO Cambiar por affiliate = null
            Affiliate affiliate = new Affiliate(1, "0704572098",ImageIO.read(DB.class.getClassLoader().getResource("test2.png")), "Max", "Samaniego",
                    "max.samaniego@ucuenca.edu.ec", Calendar.getInstance(), Calendar.getInstance());

            //TODO obtener afiliado y comprobar contraseña en la base de datos aquí

            Affiliate.current = affiliate;

            //TODO Cambiar por: return Affiliate.current != null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Account[] getAccounts(Affiliate affiliate) {
        try {
            ArrayList<Account> accounts = new ArrayList<>();

            //TODO obtener cuentas del afiliado aquí

            //TODO Remover esto:
            accounts.add(new Account(413568069, AccountType.savings, 24976.34));
            accounts.add(new Account(536566748, AccountType.checking, 43168.27));
            accounts.add(new Account(349762851, AccountType.savings, 31748.57));

            return accounts.toArray(new Account[0]);
        } catch (Exception e) {
            errorMsg(e);
            return null;
        }
    }

    public static AccountRegistry[] getRegistries(Account account) {
        return getRegistries(account, null, null, null);
    }

    public static AccountRegistry[] getRegistries(Account account, Date start, Date end, TransactionType type) {
        try {
            ArrayList<AccountRegistry> registries = new ArrayList<>();

            //TODO Obtener registros que se encuentren entre las fechas start y end aquí. Si la fecha es null, ignorarla

            //TODO Remover esto:
            registries.add(new AccountRegistry(TransactionType.payment, "Pago factura electricidad",
                    254976.34, 20.02, 254956.32, Calendar.getInstance()));

            return registries.toArray(new AccountRegistry[0]);
        } catch (Exception e) {
            errorMsg(e);
            return null;
        }
    }

    public static Provider[] getProviders(Service service) {
        ArrayList<Provider> providers = new ArrayList<>();
        //TODO Obtener proveedores de servicios básicos aquí

        //TODO Remover esto:
        switch (service) {
            case power: providers.add(new Provider("CNEL", "FACTURAS_CNEL", service));
                break;

            case water: providers.add(new Provider("Agua :v", "FACTURAS_AGUA", service));
            break;
            //etc, etc...
        }

        return providers.toArray(new Provider[0]);
    }

    public static Bill[] getBills(Provider provider, Service service, String id) {
        ArrayList<Bill> bills = new ArrayList<>();

        //TODO Obtener facturas aquí
        //Saber el servicio hace falta porque hay proveedores que ofrecen más de uno, como TV Cable

        //TODO Remover esto:
        bills.add(new Bill("Factura Julio-2021", 34.25));

        return bills.toArray(new Bill[0]);
    }

    public static boolean pay(Provider provider, Service service, String contractID, Account account) {
        try {
            //TODO pagar planillas aquí

            account.updateRegistries();
            return true;
        } catch (Exception e) {
            errorMsg(e);
        }

        return false;
    }

    public static Affiliate getRecipient(long accountNumber) {
        try {
            Affiliate temp;

            //TODO Obtener beneficiario aquí

            //TODO Remover esto:
            temp = new Affiliate(2, ImageIO.read(DB.class.getClassLoader().getResource("test.jpg")),
                    "Jorge", "Criollo", new Account(547861934, AccountType.savings));

            return temp;
        } catch (Exception e) {
            errorMsg(e);
        }

        return null;
    }

    public static boolean transfer(long from, long to, double amount) {
        try {

            //TODO Hacer transferencia aquí

            return true;
        } catch (Exception e) {
            errorMsg(e);
        }

        return false;
    }

    private static void errorMsg(Exception e) {
        GUIUtils.errorNotification("Algo ha ido mal. Inténtelo de nuevo más tarde.");
        e.printStackTrace();
    }
}
