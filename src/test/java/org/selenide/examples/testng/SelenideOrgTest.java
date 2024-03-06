package org.selenide.examples.testng;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.ex.ElementNotFound;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.codeborne.selenide.testng.TextReport;

import java.util.Arrays;
import java.util.List;

@Listeners(TextReport.class)
public class SelenideOrgTest extends BaseTestNGTest {
    @BeforeMethod
    public void setUp() {
        TextReport.onSucceededTest = false;
        TextReport.onFailedTest = true;
        open("https://selenide.org/users.html");
    }

    @Test(enabled = false)
    public void failingMethod() {
        $$("#user-tags .tag").shouldHave(sizeGreaterThan(Integer.MAX_VALUE));
        $("#missing-button").click();
    }

    @Test
    public void successfulMethod() throws Exception {
        var selenideElement = $$("#user-tags .reset-filter").first();
//    if (selenideElement != null) {
//      throw new RuntimeException();
//    }
        selenideElement.shouldHave(text("exception"));
        $$("#user-tags .tag").shouldHave(sizeGreaterThan(5));
    }

    @AfterMethod
    public void afterMethod(ITestResult testResult) {
//        String errorMessageTitle = (testResult.getStatus() == ITestResult.FAILURE && testResult.getThrowable().toString().equals(testResult.getThrowable().getClass().getCanonicalName())) ?
//                "Exception" :
//                "Assertion - possible product defect";
//        ElementShould
//        System.out.println( instanceof Error);
//        InvalidSelectorException
//                ElementShould
//        HttpException
        System.out.println(testResult.getThrowable().getClass().getCanonicalName());
//        String errorMessageTitle = (testResult.getStatus() == ITestResult.FAILURE && (testResult.getThrowable() instanceof ElementShould)) ?
        String errorMessageTitle = (testResult.getStatus() == ITestResult.FAILURE && (testResult.getThrowable() instanceof AssertionError && !testResult.getThrowable().getClass().isAssignableFrom(ElementNotFound.class))) ?
                "Assertion - possible product defect" :
                "Exception";

        System.out.println(errorMessageTitle);
    }

    public String categorizeException(Throwable throwable) {

        if (throwable.getClass().getName().equals("InvalidArgumentException") ||
                throwable.getClass().getName().equals("InvalidCookieDomainException") ||
                throwable.getClass().getName().equals("InvalidElementStateException") ||
                throwable.getClass().getName().equals("ElementNotInteractableException") ||
                throwable.getClass().getName().equals("ElementClickInterceptedException") ||
                throwable.getClass().getName().equals("InvalidSelectorException") ||
                throwable.getClass().getName().equals("JavascriptException") ||
                throwable.getClass().getName().equals("NoAlertPresentException") ||
                throwable.getClass().getName().equals("StaleElementReferenceException") ||
                throwable.getClass().getName().equals("UnableToSetCookieException") ||
                throwable.getClass().getName().equals("UnsupportedCommandException")
        ) {
            return "Product Defect";
        } else if (
                throwable.getClass().getName().equals("NoSuchSessionException") ||
                        throwable.getClass().getName().equals("NotFoundException") ||
                        throwable.getClass().getName().equals("NoSuchContextException") ||
                        throwable.getClass().getName().equals("NoSuchCookieException") ||
                        throwable.getClass().getName().equals("NoSuchElementException") ||
                        throwable.getClass().getName().equals("NoSuchFrameException") ||
                        throwable.getClass().getName().equals("NoSuchShadowRootException") ||
                        throwable.getClass().getName().equals("NoSuchWindowException") ||
                        throwable.getClass().getName().equals("ScriptTimeoutException") ||
                        throwable.getClass().getName().equals("SessionNotCreatedException") ||
                        throwable.getClass().getName().equals("RetrySessionRequestException")
        ) {
            return "Code Defect";
        } else if (
                throwable.getClass().getName().equals("WebDriverException") ||
                        throwable.getClass().getName().equals("DetachedShadowRootException") ||
                        throwable.getClass().getName().equals("HealthCheckFailedException") ||
                        throwable.getClass().getName().equals("InsecureCertificateException") ||
                        throwable.getClass().getName().equals("UnhandledAlertException") ||
                        throwable.getClass().getName().equals("UsernameAndPassword")

        ) {
            return "Environment Defect";
        } else {
            return "Unknown";
        }
    }

    public FailureCause categorizeException2(Throwable throwable) {
        List<String> productDefectExceptions = Arrays.asList(
                "InvalidArgumentException",
                "InvalidCookieDomainException",
                "InvalidElementStateException",
                "ElementNotInteractableException",
                "ElementClickInterceptedException",
                "JavascriptException",
                "NoAlertPresentException",
                "StaleElementReferenceException",
                "UnableToSetCookieException",
                "UnsupportedCommandException"
        );

        List<String> codeDefectExceptions = Arrays.asList(
                "InvalidSelectorException",
                "NoSuchSessionException",
                "NotFoundException",
                "NoSuchContextException",
                "NoSuchCookieException",
                "NoSuchElementException",
                "NoSuchFrameException",
                "NoSuchShadowRootException",
                "NoSuchWindowException",
                "ScriptTimeoutException",
                "SessionNotCreatedException",
                "RetrySessionRequestException"
        );

        List<String> environmentDefectExceptions = Arrays.asList(
                "WebDriverException",
                "DetachedShadowRootException",
                "HealthCheckFailedException",
                "InsecureCertificateException",
                "UnhandledAlertException",
                "UsernameAndPassword"
        );

        String exceptionClassName = throwable.getClass().getName();

        if (productDefectExceptions.contains(exceptionClassName)) {
            return FailureCause.PRODUCT_DEFECT;
        } else if (codeDefectExceptions.contains(exceptionClassName)) {
            return FailureCause.CODE_DEFECT;
        } else if (environmentDefectExceptions.contains(exceptionClassName)) {
            return FailureCause.ENVIRONMENT_DEFECT;
        } else {
            return FailureCause.UNKNOWN;
        }
    }
}
