package eu.profinit.education.flightlog.selenium.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;

public enum DriverType implements DriverSetup {
    FIREFOX {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities, String path) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.merge(capabilities);
            if (capabilities.getCapability("headless").equals(true)) {
                options.setHeadless(true);
            }

            return new FirefoxDriver(options);
        }
    },
    CHROME {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities, String path) {
            ChromeOptions options = new ChromeOptions();
            String downloadFilepath = "";
            if (path != null) {
                downloadFilepath = path;
            }
            WebDriverManager.chromedriver().setup();

            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("download.default_directory", downloadFilepath);
            chromePrefs.put("profile.password_manager_enabled", false);
            options.merge(capabilities);
            if (capabilities.getCapability("headless").equals(true)) {
                options.setHeadless(true);
            }

            options.addArguments(new String[]{"--no-default-browser-check"});
            options.setExperimentalOption("prefs", chromePrefs);
            return new ChromeDriver(options);
        }
    },
    IE {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities, String path) {
            WebDriverManager.iedriver().setup();
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.merge(capabilities);
            options.setCapability("ensureCleanSession", true);
            options.setCapability("enablePersistentHover", true);
            options.setCapability("requireWindowFocus", true);
            return new InternetExplorerDriver(options);
        }
    },
    EDGE {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities, String path) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.merge(capabilities);
            return new EdgeDriver(options);
        }
    },
    SAFARI {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities, String path) {
            SafariOptions options = new SafariOptions();
            options.merge(capabilities);
            return new SafariDriver(options);
        }
    },
    OPERA {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities, String path) {
            OperaOptions options = new OperaOptions();
            options.merge(capabilities);
            return new OperaDriver(options);
        }
    };

    private DriverType() {
    }
}
