package com.mobanker.selenium.tianya;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by lixiaofeng on 2017-8-2.
 * 天涯评论
 */
public class TianyaComment {

    /**
     * 调用天涯评论的方法
     * @param args
     */
    public static void main(String[] args) {
        //控制台扫描
        //Scanner scanner = new Scanner(System.in);
        System.out.println("输入登录用户名");
        //读取一个字符串：用户
        String usersName = "lxftuiguang";//scanner.next();

        System.out.println("输入用户密码");
        //读取一个字符串：密码
        String pwd = "lixiaofeng520";//scanner.next();

        //谷歌浏览器驱动加载
        System.setProperty("webdriver.chrome.driver", "F:\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //把浏览器设置到最大状态
        //driver.manage().window().setSize(new Dimension(600, 400));
        driver.get("http://focus.tianya.cn/");
        //设置等待时间为10秒
        WebDriverWait webDriverWait = new WebDriverWait(driver,5);
        //等元素加载出来，再找登录按钮的元素
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("js_login")));
        //点击登录按钮
        WebElement webElement = driver.findElement(By.id("js_login"));
        webElement.click();

        //用户登录
        comment(driver, usersName, pwd);
    }

    /**
     * 天涯论坛评论
     * @param driver 驱动
     * @param userName 用户名
     * @param pwd 密码
     */
    public static void comment(WebDriver driver, String userName, String pwd){
        //设置等待时间为10秒
        WebDriverWait webDriverWait = new WebDriverWait(driver,3);

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("vwriter")));
        //输入用户名
        WebElement name = driver.findElement(By.name("vwriter"));
        name.sendKeys(userName);

        //输入密码
        //webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("vpassword")));
        WebElement password = driver.findElement(By.name("vpassword"));
        password.sendKeys(pwd);

        //点击登录，实现登录
        WebElement login = driver.findElement(By.className("loginWin-submit-btn"));
        login.click();

        //进入到评论链接页面
        driver.get("http://bbs.tianya.cn/post-170-850843-1.shtml");

        //查询元素，填充评论内容
        WebElement textArea = driver.findElement(By.id("textAreaContainer"));
        textArea.sendKeys("写的真的很好，能联系你吗？请加我微信：hfs542277568");

        //提交评论
        WebElement comment = driver.findElement(By.className("common-submitBtn"));
        comment.click();

    }



}
