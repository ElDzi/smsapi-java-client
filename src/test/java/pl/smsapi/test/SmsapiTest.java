package pl.smsapi.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import pl.smsapi.Client;
import pl.smsapi.api.action.BaseAction;
import pl.smsapi.api.response.*;
import pl.smsapi.exception.*;
import pl.smsapi.proxy.Proxy;
import pl.smsapi.proxy.ProxyHttp;

public class SmsapiTest {

	protected String fileToIds = "_ids_test.txt";

	public SmsapiTest() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	protected Client getAuthorizationClient() {

		try {
			Client client = new Client("login");
			client.setPasswordHash("1c29135e8dc2245721632ee1f5adb22e");
			return client;
		} catch (ClientException ex) {
			/**
			 * 101 Niepoprawne lub brak danych autoryzacji. 102 Nieprawidłowy login lub hasło 103 Brak punków dla tego
			 * użytkownika 105 Błędny adres IP 110 Usługa nie jest dostępna na danym koncie 1000 Akcja dostępna tylko
			 * dla użytkownika głównego 1001 Nieprawidłowa akcja
			 */
			System.out.println(ex.getMessage());
		}
		return null;
	}

	protected Response executeAction(BaseAction action) {
		try {

			return (Response)action.execute();

		} catch (SmsapiException ex) {
			System.out.println(ex.getMessage());
		}

		return null;
	}

	protected String[] readIds() {

		BufferedReader br = null;
		StringBuffer result = new StringBuffer();

		try {

			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileToIds));

			while ((sCurrentLine = br.readLine()) != null) {
				result.append(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return result.toString().split(",");
	}

	protected void writeIds(String[] ids) {

		StringBuffer content = new StringBuffer();

		try {

			int i = 1;
			for (String item : ids) {
				content.append(item);
				if (i < ids.length) {
					content.append(",");
				}
				i++;
			}

			File file = new File(fileToIds);

			if (!file.exists()) {
				file.createNewFile();
			}


			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content.toString());
			bw.close();

			System.out.println("Wrote ids");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void renderMessageItem(MessageResponse item) {

		if (item != null) {

			System.out.println("ID: " + item.getId()
					+ " Number: " + item.getNumber()
					+ " Points:" + item.getPoints()
					+ " Status:" + item.getStatus()
					+ " IDx: " + item.getIdx());

		} else {
			System.out.println("Item is null");
		}

	}

    protected Proxy getProxy() {
        return new ProxyHttp("http://api.smsapi.pl/");
    }
}
