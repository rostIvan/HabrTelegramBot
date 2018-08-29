package app.telegram.bot.unit.data.api.fb

import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.firefox.FirefoxBinary
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile


@RunWith(JUnit4::class)
@Ignore
class FacebookParseTest {

    @Test fun test() {
        val before = System.currentTimeMillis()

        val webDriver = getWebDriver()
        val baseUrl = "https://www.facebook.com/"
        webDriver.loadPage(baseUrl) {
            waitForLoginPage(webDriver)
            clickSignIn(webDriver)
            webDriver.get(getEventsLink())

            waitForEvents(webDriver)
            scrollToEndAndWait(webDriver)
            val titles = webDriver.findElementsByClassName("_7ty")
            titles.forEach { println(it.text) }
            printLine()
            println("${titles.size} --> time spent: ${System.currentTimeMillis() - before}")

            assertThat(titles).isNotEmpty
        }
    }

    private fun waitForEvents(webDriver: FirefoxDriver) {
        WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated(By.className("_7ty")))
    }

    private fun scrollToEndAndWait(webDriver: FirefoxDriver) {
        (1..100).forEach {
                webDriver.executeScript("window.scrollTo(0, document.body.scrollHeight)")
            Thread.sleep(20)
        }
    }

    private fun clickSignIn(webDriver: FirefoxDriver) {
        val login = webDriver.findElementById("email")
        val pass = webDriver.findElementById("pass")
        val signIn = webDriver.findElementById("loginbutton")

        login.sendKeys("+380971844197")
        pass.sendKeys("pass")
        signIn.click()
    }

    private fun waitForLoginPage(webDriver: FirefoxDriver) {
        WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("email")))
        WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("pass")))
    }

    private fun getWebDriver() : FirefoxDriver {
        val firefoxBinary = FirefoxBinary()
        val capabilities = FirefoxOptions()
        val profile = FirefoxProfile()
        firefoxBinary.addCommandLineOptions("--headless")
        profile.setPreference("dom.webnotifications.enabled", false)
        capabilities.setCapability(FirefoxDriver.PROFILE, profile)
        capabilities.setCapability(FirefoxDriver.BINARY, firefoxBinary)
        System.setProperty("webdriver.gecko.driver", "/home/rost/Selenium/geckodriver")
        return FirefoxDriver(capabilities)
    }

    private fun WebDriver.loadPage(baseUrl: String, workFunc: () -> Unit) {
        try {
            this.get(baseUrl)
            workFunc.invoke()
        } finally { this.quit() }
    }

    private fun getEventsLink() =
            "https://www.facebook.com/events/discovery/?suggestion_token=%7B%22city%22%3A%22108466925844385%22%7D&acontext=%7B%22ref%22%3A2%2C%22ref_dashboard_filter%22%3A%22upcoming%22%2C%22source%22%3A2%2C%22source_dashboard_filter%22%3A%22discovery%22%2C%22action_history%22%3A%22[%7B%5C%22surface%5C%22%3A%5C%22dashboard%5C%22%2C%5C%22mechanism%5C%22%3A%5C%22main_list%5C%22%2C%5C%22extra_data%5C%22%3A%7B%5C%22dashboard_filter%5C%22%3A%5C%22upcoming%5C%22%7D%7D%2C%7B%5C%22surface%5C%22%3A%5C%22discover_filter_list%5C%22%2C%5C%22mechanism%5C%22%3A%5C%22surface%5C%22%2C%5C%22extra_data%5C%22%3A%7B%5C%22dashboard_filter%5C%22%3A%5C%22discovery%5C%22%7D%7D]%22%2C%22has_source%22%3Atrue%7D"

    private fun printLine() {
        println()
        repeat(100) { print("-") }
        println()
    }
}