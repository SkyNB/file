package com.mobanker.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by congyuxiang on 2017/6/14.
 */
public class FirstStep {




    public static String FileName="";

    public static String Path="file"+ File.separator+"step1";

    public static void main(String[] args) throws Exception {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        long times =System.currentTimeMillis();

        Date date=new Date(times);
        String tim=sdf.format(date);

        FileName="step1_input."+tim+".txt";


        Scanner scanner = new Scanner(System.in);
        System.out.println("输入登陆用户名:");
        String userName = scanner.next();

        System.out.println("输入密码:");
        String pwd = scanner.next();


        System.setProperty("webdriver.ie.driver", "F:\\chromedriver.exe");
        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        ieCapabilities.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "about:blank");
        WebDriver driver = new InternetExplorerDriver(ieCapabilities);

        driver.get("http://10.1.2.5/hljloan/loan/");
//        js.123
        WebDriverWait webDriverWait = new WebDriverWait(driver,10);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className("logon_submit")));
        firstStep(driver,userName,pwd);


    }


    private static void firstStep(WebDriver driver,String userName,String pwd) throws Exception {
        WebElement name = driver.findElement(By.name("operid"));
        name.sendKeys(userName);
        WebElement pwdEle = driver.findElement(By.name("password"));
        pwdEle.sendKeys(pwd);

        WebElement login = driver.findElement(By.className("logon_submit"));
        login.click();

        Thread.sleep(1000);
        WebElement keHuGuanLi = findElementWithSleep(driver, ".menubar>table > tbody > tr > td:nth-child(3) > div");
        keHuGuanLi.click();

        //微贷客户信息批量导入
        Thread.sleep(1000);
        WebElement firstAction = driver.findElement(By.cssSelector(".popup-menu tr:nth-child(13) .menu-item-label"));
        firstAction.click();
//        Thread.sleep(2000);


        WebDriverWait waitSelect = new WebDriverWait(driver,10);
        waitSelect.until(ExpectedConditions.presenceOfElementLocated(By.name("credstatecorp")));

        //客户信息批量导入

        //status
        Select statusSelect = new Select(driver.findElement(By.name("credstatecorp")));
        statusSelect.selectByValue("10");

        System.out.println(statusSelect.getFirstSelectedOption().getText());

//        Actions actions = new Actions(driver);
        process(driver);
    }


    private static void process(WebDriver driver) throws Exception {
        try {
            boolean flag = true;
            while (true) {
//            wait(driver);
                List<WebElement> lists = new ArrayList<WebElement>();
                try {
                    lists = driver.findElements(By.cssSelector(".list-body-section tr"));
                } catch (Exception e) {

                    continue;
                }
                int size = lists.size();
                if (size == 0) {
                    flag = true;
                }else{
                    flag = false;
                }
                while (flag) {
                    click(driver);
                    try {
                        lists = driver.findElements(By.cssSelector(".list-body-section tr"));
                    } catch (Exception e) {

                        continue;
                    }
                    if (lists.size() == 0) {
                        continue;
                    } else {
                        flag = false;
                    }
                }
                size = lists.size();

                Random random = new Random();
                int index = random.nextInt(size);

                WebElement element = lists.get(index);
                String userinfo = element.getText();
                element.click();
                WebElement submit = driver.findElements(By.cssSelector(".list-actionbar nobr")).get(0);
                submit.click();

                WebElement testEle = null;
                try {
                    testEle = driver.findElement(By.cssSelector(".msgbox"));
                } catch (Exception e) {

                }
                if (testEle != null) {
                    try {
                        WebElement textEle = testEle.findElement(By.cssSelector(".msgbox-text"));
                        WebElement buttonEle = testEle.findElement(By.cssSelector(".button-right"));
                        System.out.println("异常情况:" + testEle);
                        buttonEle.click();
                        continue;
                    }catch (Exception e){

                    }
                }

//            Thread.sleep(1000);
                WebDriverWait wait = new WebDriverWait(driver, 10);
                wait.until(ExpectedConditions.alertIsPresent());

//            wait(driver);

                Alert alert1 = driver.switchTo().alert();

                try {
                    String text = alert1.getText();

                    if (text.contains("核查成功")) {
                        alert1.dismiss();
                        WebElement alert = driver.findElement(By.cssSelector(".dialog-window"));
//            WebElement alertText = alert.findElement(By.cssSelector(".msgbox-text"));
                        WebElement alertConfirm = alert.findElements(By.tagName("nobr")).get(0);
                        alertConfirm.click();
                        WebDriverWait wait1 = new WebDriverWait(driver, 10);
                        wait1.until(ExpectedConditions.alertIsPresent());

                        Alert finalAlert = driver.switchTo().alert();
                        if (finalAlert.getText().contains("开户成功")) {
                            finalAlert.dismiss();
                        } else {
                            System.out.println("final:" + finalAlert.getText());
                            finalAlert.dismiss();
                            WebElement dialog = driver.findElement(By.cssSelector(".dialog-window"));
//            WebElement alertText = alert.findElement(By.cssSelector(".msgbox-text"));
                            WebElement closeConfirm = dialog.findElements(By.tagName("nobr")).get(1);
                            closeConfirm.click();
                            click(driver);
                            continue;
                        }
                    } else {

                        System.out.println(text); //org.openqa.selenium.NoAlertPresentException
                        alert1.dismiss();
                        WebElement alert = driver.findElement(By.cssSelector(".dialog-window"));
//            WebElement alertText = alert.findElement(By.cssSelector(".msgbox-text"));
                        WebElement alertConfirm = alert.findElements(By.tagName("nobr")).get(1);
                        alertConfirm.click();
                        FileUtil.WriteFile(userinfo,Path,FileName);
                        click(driver);
                        continue;
                    }


                } catch (NoAlertPresentException e) {
                    System.out.println("无alert");
                    click(driver);
                    continue;
                }
            }

        } catch (Exception e) {
            click(driver);
            process(driver);
        }


    }

    private static WebElement findElementWithSleep(WebDriver driver, String css) {
        WebElement result = null;
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            try {
                result = driver.findElement(By.cssSelector(css));
                break;
            } catch (NoSuchElementException e) {
                continue;
            }
        }
        return result;
    }

    private static void wait(WebDriver driver) throws Exception {
        WebElement element;
        while (true) {
            try {
                element = driver.findElement(By.cssSelector(".loading-indicator"));
                Thread.sleep(1000);
            } catch (Exception e) {
                break;
            }
        }
    }

    private static void click(WebDriver driver) throws Exception {
        //查询
        WebElement queryElement = driver.findElements(By.tagName("nobr")).get(0);
        queryElement.click();
        wait(driver);
        System.out.println("jieshu");


    }
}
