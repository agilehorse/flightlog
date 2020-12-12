package eu.profinit.education.flightlog.selenium.driver;

import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import static eu.profinit.education.flightlog.selenium.driver.DriverType.CHROME;

public class DriverFactory {

    private DriverType selectedDriverType;
    private Properties properties;

    private final boolean useRemoteWebDriver = Boolean.getBoolean("remoteDriver");

    public DriverFactory() throws IOException {
        DriverType driverType = CHROME;

        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + ".properties";

        properties = new Properties();
        try {
            properties.load(new FileInputStream(appConfigPath));
        }
        catch (FileNotFoundException e) {
            System.out.println(appConfigPath + " not found, using defaults.");
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("default.properties");
            properties.load(inputStream);
        }

        Properties systemProperties = System.getProperties();

        // override with system properties
        properties.putAll(systemProperties);

        String browser = properties.getProperty("browser", driverType.toString()).toUpperCase();

        try {
            driverType = DriverType.valueOf(browser);
        } catch (IllegalArgumentException ignored) {
            System.err.println("Unknown driver specified, defaulting to '" + driverType + "'...");
        } catch (NullPointerException ignored) {
            System.err.println("No driver specified, defaulting to '" + driverType + "'...");
        }
        selectedDriverType = driverType;
    }

    public RemoteWebDriver getDriver() throws MalformedURLException {
        return instantiateWebDriver(selectedDriverType);
    }

    public RemoteWebDriver getDriver(String path) throws MalformedURLException {
        return instantiateWebDriver(selectedDriverType, path);
    }


    private RemoteWebDriver instantiateWebDriver(DriverType driverType) throws MalformedURLException {
        return instantiateWebDriver(driverType, null);
    }

    private RemoteWebDriver instantiateWebDriver(DriverType driverType, String path) throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        String downloadFilepath = "";
        if (path != null) {
            downloadFilepath = path;
        }
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);

        desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

        if (useRemoteWebDriver) {

            System.out.println("Connecting to Selenium Grid: " + useRemoteWebDriver);
            URL seleniumGridURL = new URL(properties.getProperty("gridURL"));
            String desiredBrowserVersion = properties.getProperty("desiredBrowserVersion");
            String desiredPlatform = properties.getProperty("desiredPlatform");

            if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
                desiredCapabilities.setPlatform(Platform.valueOf(desiredPlatform.toUpperCase()));
            }

            if (null != desiredBrowserVersion && !desiredBrowserVersion.isEmpty()) {
                desiredCapabilities.setVersion(desiredBrowserVersion);
            }

            desiredCapabilities.setBrowserName(selectedDriverType.toString());
            return new RemoteWebDriver(seleniumGridURL, desiredCapabilities);
        } else {
            String headlessPropertyValue = properties.getOrDefault("headless", "false").toString();
            boolean headless = headlessPropertyValue.equalsIgnoreCase("true");
            desiredCapabilities.setCapability("headless", headless);
            System.out.println("Selected Browser: " + selectedDriverType + ", headless mode? " + headless);
            System.out.println("Selected Path: " + downloadFilepath);
            return driverType.getWebDriverObject(desiredCapabilities, downloadFilepath);
        }
    }
}
