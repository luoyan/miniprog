package com.luoyan.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExampleForFireFox  {
    public static void main(String[] args) {
    	// 如果你的 FireFox 没有安装在默认目录，那么必须在程序中设置
//    	System.setProperty("webdriver.firefox.bin", "D:\\Program Files\\Mozilla Firefox\\firefox.exe");
    	// 创建一个 FireFox 的浏览器实例
    	System.out.println("start to new firefox");
        WebDriver driver = new FirefoxDriver();
    	System.out.println("end to new firefox");

        // 让浏览器访问 Baidu
    	System.out.println("start to access baidu");
        driver.get("http://www.baidu.com");
    	System.out.println("end to access baidu");
        // 用下面代码也可以实现
        // driver.navigate().to("http://www.baidu.com");

        // 获取 网页的 title
        System.out.println("1 Page title is: " + driver.getTitle());

        // 通过 id 找到 input 的 DOM
        WebElement element = driver.findElement(By.id("kw"));

        // 输入关键字
        element.sendKeys("zTree");

        // 提交 input 所在的  form
        element.submit();
        
        // 通过判断 title 内容等待搜索页面加载完毕，间隔10秒
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().endsWith("ztree");
            }
        });

        // 显示搜索结果页面的 title
        System.out.println("2 Page title is: " + driver.getTitle());
        
        //关闭浏览器
        driver.quit();
    }
}
