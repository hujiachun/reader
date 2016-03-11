/**
 * hujiachun
 */
package com.meizu.reader.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;












































import org.apache.http.client.RedirectException;

import android.R.integer;
import android.graphics.Rect;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.meizu.automation.AutoWatcher;
import com.meizu.automation.By;
import com.meizu.automation.DefaultElement;
import com.meizu.automation.constants.AutoException;
import com.meizu.automation.service.Element;
import com.meizu.sift.ElementWatcher;
import com.meizu.test.common.AutoAllInOneTestCase;
import com.meizu.test.util.AppInfo;
import com.meizu.test.util.ShellUtil;

/**
 * @author hujiachun
 */
public class TestScript extends AutoAllInOneTestCase{

    /**
     * 设置悬浮球可见状态
     * @param status true:可见, false:隐藏
     */
    public void setFloatVisibility(boolean status){
    	
        String command = "am broadcast -a " + (status? "sk.action.FLOAT_ON":"sk.action.FLOAT_OFF");
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
   public String getAppVersions(String packageName) throws IOException{
	   BufferedReader reader;
	   Process p;
	   String line = null, versionName = null; 
		p = Runtime.getRuntime().exec("dumpsys package " + packageName);
		reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		while (((line = reader.readLine()) != null)){
			if(line.contains("versionName")){
				versionName = line;	
			}
		}
		p.destroy();
		reader.close();
	   return versionName;
   }

	/**
	 * 判断yunos和flyme
	 * @return
	 * @throws IOException
     */
	public boolean isYunos() throws IOException{
		BufferedReader reader;
		Process p;
		String line = null ;
		boolean yunos = false;
		p = Runtime.getRuntime().exec("getprop ro.yunos.build.version");
		reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		while (((line = reader.readLine()) != null)){
			if(line != ""){
				yunos = true;
			}
		}
		p.destroy();
		reader.close();
		return yunos;
	}

   
   public String getDevice() throws IOException{
	   BufferedReader reader;
	   Process p;
	   String line = null, versionName = null; 
		p = Runtime.getRuntime().exec("getprop ro.product.device ");
		reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		while (((line = reader.readLine()) != null)){
				versionName = line;
		}
		
		p.destroy();
		reader.close();
	   return versionName;
   }
	
	
	   /**
	    * 绑定wifi
	    * @param ssid
	    * @param pwd
	    * @date 2015年8月31日下午2:28:16
	    * @author hujiachun
	    */
	   public void sendWIFI(String ssid, String pwd){
			String command = "am broadcast -a sk.action.WIFILOCK_REBIND --es SSID " + ssid + " --es PWD " + pwd;
			
			try {
				Process p = Runtime.getRuntime().exec(command);
				p.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	   
	
	   
	   /**
	    * 获取像素点
	    * @param id
	    * @throws AutoException
	    * @date 2016年1月4日下午4:05:23
	    * @author hujiachun
	    */
	   public String getRectGRB(String id) throws AutoException{
			Rect rect = this.findElement(id).getBounds();
			rect.bottom = rect.top + 3;
			rect.top = rect.top + 2;
			rect.right = rect.right - 2;
			rect.left = rect.right - 3;
			int color = ElementWatcher.startWatch(new Rect(rect)).averageColor();
			System.out.println("color:" + Integer.toHexString(color));
			return Integer.toHexString(color);
	   }
	   
	   
	   /**
	    * 设置夜间模式
	    * 
	    * @date 2016年1月4日下午4:08:38
	    * @author hujiachun
	    * @throws AutoException
	    */
	   public void openNightMode(Boolean bool) throws AutoException{
		   this.click(ReaderUtil.PAPER_MORE);
		   if(bool){
			   Boolean isChecked = this.findElement("com.meizu.media.reader:id/sw_nightmode").isChecked();
			   if(!isChecked){
				   click("com.meizu.media.reader:id/sw_nightmode");
			   }
			   else {
				this.pressBack();
			}
		   }
		   else {
			   Boolean isChecked = this.findElement("com.meizu.media.reader:id/sw_nightmode").isChecked();
			if(isChecked){
				click("com.meizu.media.reader:id/sw_nightmode");
			}
			 else {
					this.pressBack();
				}
		}
		   
	   }
	
	
	/**
	 * 退出阅读
	 * 
	 * @date 2015年8月13日上午10:18:00
	 * @author hujiachun
	 */
	public void exitReader(){
		int i = 0;
		while ((!isExistById(ReaderUtil.LAUNCHER, 1000)) && i < 10 ) {
			System.out.println(i);
			this.pressBack();
			i++;
		}
	}
	

	/**
	 * 重写startApp,加入权限申请
	 */
	@Override
	public void startApp(String packageName, String launchActivity){
		super.startApp(packageName, launchActivity);
	    Element accessibility = new DefaultElement(By.byText("权限申请").id("android:id/alertTitle"));
	    Element permit = new DefaultElement(By.byText("始终允许").className("android.widget.CheckBox"));

	                if (permit.exists() && accessibility.exists()) {
	                    try {
	                        new DefaultElement(By.byId("android:id/button1")).click();
	                    } catch (AutoException e) {
	                        e.printStackTrace();
	                    }
	                   
	                
	              
	            }
              
	       
	}
	
	
	/**
	 * 返回页面是否更改
	 * @param before_title
	 * @return
	 * @throws AutoException
	 * @date 2015年8月22日下午2:59:10
	 * @author hujiachun
	 */
	public boolean isChangeTitle(String before_title) throws AutoException{
		
		return isExistByText(before_title);
	}


    /**
	 * 点击下拉列表
	 * @throws AutoException
     */
	public void clickDownList() throws AutoException{
		this.findElement(ReaderUtil.GATEGORY_DOWN).click();
		this.sleep(1000);
	}
	
   
	/**
	 * 点击Add按钮
	 * @date 2015年8月12日下午5:59:30
	 * @author hujiachun
	 * @throws AutoException 
	 */
	public void clickAdd() throws AutoException{
		this.findElement(ReaderUtil.RESERVE_ADD).click();
		this.sleep(1000);
	}
	
	
	/**
	 * 进入编辑模式
	 * 
	 * @date 2015年8月13日下午2:35:10
	 * @author hujiachun
	 * @throws AutoException 
	 */
	public void enterEdit() throws AutoException{
		this.clickDownList();
		this.clickByText("编辑");
		
	}
	
	
	/**
	 * 首页进入搜索订阅
	 * 
	 * @date 2015年8月12日下午6:45:56
	 * @author hujiachun
	 * @throws AutoException 
	 */
	public void enterReserveSearch() throws AutoException{
		this.clickDownList();
		this.clickAdd();
		this.click(ReaderUtil.RESERVE_SEARCH);
		
	}
	
	    
	 /**
     * 首页进入订阅中心
     * 
     * @date 2015年8月12日下午9:33:27
     * @author hujiachun
	 * @throws AutoException 
     */
    public void enterReserveCenter() throws AutoException{
    	this.clickDownList();
		this.clickAdd();
		this.waitForElement(ReaderUtil.RESERVE_TITLE, 60000);
    }
    
	 
	/**
	 * 查找多个文本    
	 * @param texts
	 * @date 2015年8月11日下午5:44:11
	 * @author hujiachun
	 */
	public void assertMoreText(String... texts) {
		for (String text : texts) {
			boolean istext = this.findElement(By.byTextContains(text)).exists();

			assertTrue(text + "not find", istext);
			this.sleep(1000);
			}

		}
		
	
	/**
	 * 首页定位分类
	 * @param index
	 * @date 2015年8月12日下午3:38:49
	 * @author hujiachun
	 * @throws AutoException 
	 */
	public void clickTop(int index) throws AutoException{
		Element top = this.findElement(By.byClassName(ReaderUtil.GATEGORY_TAB_TEXT).instance(index));
		top.click();
	}
	
	
	/**
	 * 首页分类是否选中
	 * @param index
	 * @date 2015年8月12日下午3:38:49
	 * @author hujiachun
	 * @throws AutoException 
	 */
	public boolean isTopChecked(int index) throws AutoException{
		Element top = this.findElement(By.byClassName(ReaderUtil.GATEGORY_TAB_TEXT).instance(index));
		return top.isChecked();
	
	}
	
	
	/**
	 * 返回首页top文本
	 * @param index
	 * @date 2015年8月12日下午3:38:49
	 * @author hujiachun
	 * @throws AutoException 
	 */
	public String getTopText(int index) throws AutoException{
		Element top = this.findElement(By.byClassName(ReaderUtil.GATEGORY_TAB_TEXT).instance(index));
		return top.getText();
	
	}
	
	
	/**
	 * 订阅栏定位分类
	 * @param index
	 * @date 2015年8月12日下午3:38:49
	 * @author hujiachun
	 * @throws AutoException 
	 */
	public void clickReserveTop(int index) throws AutoException{
		Element top = this.findElement(By.byId(ReaderUtil.RESERVE_TEXT).instance(index));
		top.click();
	}
	
	
	/**
	 * 订阅栏分类是否选中
	 * @param index
	 * @date 2015年8月12日下午3:38:49
	 * @author hujiachun
	 * @throws AutoException 
	 */
	public boolean isReserveSelected(int index) throws AutoException{
		Element top = this.findElement(By.byId(ReaderUtil.RESERVE_TEXT).instance(index));
		return top.isSelected();
	
	}
	
	

	/**
	 * 返回订阅栏分类文本
	 * @param index
	 * @date 2015年8月12日下午3:38:49
	 * @author hujiachun
	 * @throws AutoException 
	 */
	public String getReserveText(int index) throws AutoException{
		Element top = this.findElement(By.byId(ReaderUtil.RESERVE_TEXT).instance(index));
		return top.getText();
	
	}
	
	
	/**
	 * 左滑动列表
	 * @param steps
	 * @date 2015年8月12日下午3:52:12
	 * @author hujiachun
	 */
	public void swipeLeftList(int steps) {
	    this.swipe(this.getDisplayWidth() / 10 * 9, this.getDisplayHeight() / 2,
	    		this.getDisplayWidth() / 10, this.getDisplayHeight() / 2, steps);
	    this.sleep(1000);
	    }

	
	/**
	 * 右滑动列表
	 * @param steps
	 * @date 2015年8月12日下午3:51:50
	 * @author hujiachun
	 */
    public void swipeRightList(int steps) {
    	this.swipe(this.getDisplayWidth() / 10, this.getDisplayHeight() / 2,
    			this.getDisplayWidth() / 10 * 9, this.getDisplayHeight() / 2, steps);
    	this.sleep(1000);
	    }
    
    
    /**
     * 订阅列表取消订阅
     * @param name
     * @date 2015年8月12日下午8:57:01
     * @author hujiachun
     * @throws AutoException 
     */
    public void cancelReserve(String name) throws AutoException{
    	this.findElement(ReaderUtil.RESERVE_LIST).toVerticalList().searchByText(name, 60000);
    	this.clickByText("编辑");
    	this.sleep(2000);
    	this.findElement(By.byText(name)).click();
    	this.clickByText("完成");
    }
 
    
    
    
    /**
     * 返回订阅栏列表
     * @date 2015年8月12日下午9:30:05
     * @author hujiachun
     */
    public void backReserveList(){
    	int i = 0;
    	while (!this.isExistById(ReaderUtil.RESERVE_ADD) && i < 5) {
			this.pressBack();
			this.sleep(500);
			i++;
		}
    }
    
    
    /**
     * 返回订阅中心顶部
     * 
     * @date 2015年8月13日上午9:22:29
     * @author hujiachun
     */
   public void backReserveListTop(){
	   int i = 0;
	   while ((!isExistByText("精选")) && i < 10) {
			this.swipeDown(10);
			i++;
			
		}
	   this.sleep(2000);
   }
    
   
   /**
    * 订阅频道
    * @return
    * @throws AutoException
    * @date 2015年10月9日下午2:44:36
    * @author hujiachun
    */
   public String reserveChannel() throws AutoException{
	  
	   int number = 0; 
		int size = this.findElements(ReaderUtil.RESERVE_ICON).size();
		for (int i = 0; i < size; i++) {
			if (isReserve(i)) {//如果已订阅 查找下一个
				System.out.println("continue");
				continue;
			}
			
		    this.findElement(By.byId(ReaderUtil.RESERVE_TITLE).instance(i)).click();//未订阅时点击进入，退出循环
			number = i;
			System.out.println("break");
			break;
		}
		this.waitForText("订阅", 60000);//订阅
		this.clickByText("订阅");
		this.pressBack();
		System.out.println(number);
		assertTrue("未订阅成功", isReserve(number));//判断是否订阅成功
		String titleName = this.findElement(By.byId(ReaderUtil.RESERVE_TITLE).instance(number)).getText();//获取订阅频道
		return titleName;
   }
   
   /**
    * 返回频道是否订阅
    * @param index
    * @return
    * @throws AutoException
    * @date 2015年8月13日上午11:09:49
    * @author hujiachun
    */
   public boolean isReserve(int index) throws AutoException{
	   Element isRecommend = this.findElement(By.byId(ReaderUtil.RESERVE_RECOMMEND).instance(index));
	   if (isRecommend.getText().equals("已订阅")) {
		return true;
	}
	   else {
		   return false;
	}
   }
   
   
   /**
    * 首页进入文章详情
    * 
    * @date 2015年8月13日下午6:09:43
    * @author hujiachun
    * @throws AutoException 
    */
   public void enterPaper() throws AutoException{
	   this.findElement(ReaderUtil.HOME_IMAGE).click();
	   this.isExistById(ReaderUtil.CONTENT_NAME, 60000);
	   Element paper = this.findElement(ReaderUtil.CONTENT_NAME);
	   paper.click();
	   this.isExistById(ReaderUtil.PAPER_COMMENT_EDIT, 60000);
   }
   
   
   /**
    * 首页进入到热议列表
    * 
    * @date 2015年8月15日上午10:15:13
    * @author hujiachun
    * @throws AutoException 
    */
   public void enterHotList() throws AutoException {
	   this.findElement(ReaderUtil.HOME_IMAGE).click();
	   this.isExistById(ReaderUtil.CONTENT_NAME, 60000);

   }
   
   
   /**
    * 首页进入到专题列表
    * 
    * @date 2015年8月15日上午10:15:13
    * @author hujiachun
    * @throws AutoException 
    */
   public void enterSpecialList() throws AutoException {
	   this.findElementById(ReaderUtil.HOME_IMAGE, 1).click();
	   this.isExistById(ReaderUtil.CONTENT_NAME, 60000);

   }
   
   
   /**
    * 验证是否已经登陆账户
    * @return
    * @date 2015年8月13日下午9:37:43
    * @author hujiachun
    */
   public boolean isLoginAccount(){
	   // 打开账户中心
       ShellUtil.exitApp(AppInfo.PACKAGE_ACCOUNT);
       ShellUtil.startApp(AppInfo.PACKAGE_ACCOUNT, AppInfo.ACTIVITY_ACCOUNT);
       if (this.findElement(By.byText("允许")).exists()) {
			try {
				this.findElement(By.byText("允许")).click();
			} catch (AutoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
   
       // 如果当前已经有其他账户，则先退出其他账户
       if (!this.isExistByText("登录/注册")) {
    	   return true;
       }
	return false;
   }


	/**
	 * 拍照
	 * @throws AutoException
     */
	public void takePhoto() throws AutoException {
		this.click(ReaderUtil.PHOTO_STEP1);
		this.click(ReaderUtil.PHOTO_STEP2);
		this.click(ReaderUtil.PHOTO_STEP3);
	}
   
   
   /**
    * 刷新资讯页面
    * @param app
    * @throws AutoException
    * @date 2015年10月27日上午9:13:46
    * @author hujiachun
 * @throws IOException 
    */
   public void refreshApp(String app) throws AutoException, IOException{
	   int sum = 0;
	   System.out.println(getDevice());
	   if(getDevice().contains("m1note") || getDevice().contains("mx4")){
		   
	   }
	   else{
		   this.pressHome();
		   this.sleep(500);
		   this.pressHome();
		   this.sleep(200);
		   while ((!this.findElementByText(app).exists()) && sum < 10) {
				swipeLeftOther();//左滑屏幕
				sum++;
			}
			this.clickByText(app);
			this.sleep(500);
	   }
	  
   }
  
   
   /**
	 * 
	 *左滑动屏幕 
	 */
	public void swipeLeftOther(){
		int dh = this.getDisplayHeight();
		int dw = this.getDisplayWidth();
		this.swipe(dw * 9 / 10, dh / 2, dw / 10, dh / 2, 100);
	}
   
   
   /**
    * 文章界面定位至评论区域
    * @throws AutoException
    * @date 2015年8月13日下午10:07:25
    * @author hujiachun
    */
   public void  locationComment() throws AutoException {
	   this.click(ReaderUtil.PAPER_COMMENT);//点击评论数
	   this.sleep(1000);
	   this.click(ReaderUtil.PAPER_COMMENT);//再次点击评论数
	   this.sleep(2000);
	  
   }
   
    
   /**
	 * 验证toast
	 *
	 * @param text
	 *            期望值
	 * @throws java.io.IOException
	 */
	public boolean logParsing(String tag, String text) {
		Process p = null;
		BufferedReader reader = null;
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String testLog = "logcat -v time";
		try {
			p = Runtime.getRuntime().exec(testLog);
			reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			stringBuilder.setLength(0);
			Date wifiTime = new Date(System.currentTimeMillis() + 10000);
			
			while (((line = reader.readLine()) != null)
					&& wifiTime.after(new Date(System.currentTimeMillis()))) {
				stringBuilder.append(line);
				if ( line.contains(text)) {
					System.out.println(line);
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (p != null) {
			p.destroy();
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
	/**
	 * 复制文本至便签，并获取文本
	 * @return
	 * @throws AutoException
	 * @date 2015年8月14日下午8:45:34
	 * @author hujiachun
	 */
	public String copyTextToNotePaper() throws AutoException{
		this.startApp(AppInfo.PACKAGE_NOTEPAPER, AppInfo.ACTIVITY_NOTEPAPER);
		this.isExistById(ReaderUtil.NOTEPAPER_ADD, 20000);
		this.click(ReaderUtil.NOTEPAPER_ADD);//新建便签
		this.findElement(ReaderUtil.NOTEPAPER_EDIT).longClick(3000);
		this.sleep(2000);
		//获取坐标
		Rect title = this.findElement(ReaderUtil.NOTEPAPER_TITLE).getBounds();
		int title_bottom = title.bottom;
		int title_top = title.top;
		int title_right = title.right;
		int copy_value = title_bottom - title_top;
		int copy_X = title_right / 5;
		int copy_Y = copy_value + title_bottom;
		this.click(copy_X, copy_Y);
		
		this.sleep(2000);
		String noteText = this.findElement(By.byId(ReaderUtil.NOTEPAPER_TEXT).
				className(ReaderUtil.NOTEPAPER_EDIT_CLASS)).getText();//获取到编辑框文本
		
		this.sleep(1000);
		this.exitApp(AppInfo.PACKAGE_NOTEPAPER);
		return noteText;
		
	}
	
	
	/**
	 * 在文件中点击某个文件夹名进入
	 * @param fileString
	 * @throws AutoException
	 * @date 2015年8月10日下午3:11:31
	 * @author hujiachun
	 */
	public void clickTextForFile(String fileString) throws AutoException{
		this.findElement(By.byId(ReaderUtil.FILE_LIST)).toVerticalList().scrollTextInto(fileString);
		this.sleep(1000);
			
		int fileString_top = findElementByText(fileString).getBounds().top;
		int fileText_bottom = findElement(By.byId(ReaderUtil.FILE_TEXT)).getBounds().bottom;
		
		int fileString_bottom = findElementByText(fileString).getBounds().bottom;
			
		if (fileString_top < fileText_bottom) {//需要点击的文件顶端 < 存储盘文本下方
				this.swipeDownLittle();	
			}
			
		if (fileString_bottom == this.getDisplayHeight()) {//需要点击的文件低部 隐藏
			    this.swipeUpLittle();	
			}
			
			
		this.clickByText(fileString);
		this.sleep(1000);
	}
	
 
	/**
	 * 删除文件
	 * @param fileString
	 * @throws IOException
	 * @throws AutoException
	 * @date 2015年8月15日下午2:33:40
	 * @author hujiachun
	 */
	public void deleteFile(String fileString) throws IOException, AutoException{
	    this.exitApp(AppInfo.PACKAGE_FILEMANAGER);
	    this.startApp(AppInfo.PACKAGE_FILEMANAGER, ReaderUtil.FILE_ACTIVITY);
		this.findElement(By.byId(ReaderUtil.FILE_LIST)).toVerticalList().scrollTextInto(fileString);
		this.sleep(1000);
			
		this.clickTextForFile(fileString);
		
		if (isExistByText("Photo")) {
			this.clickTextForFile("Photo");
			if(findElementByText("空文件夹").exists()){
				this.exitApp(AppInfo.PACKAGE_FILEMANAGER);

				}
			
			else{
				List<Element> item = this.findElementsById(ReaderUtil.FILE_ITEM);
				if (item.size() == 1) {//只要一个文件
					item.get(0).longClick(2000);
					this.clickByTexts("删除");
					this.findElement(By.byTextContains("删除")).click();
					this.sleep(1000);
					this.exitApp(AppInfo.PACKAGE_FILEMANAGER);
				}
				else {//多个文件
					item.get(0).longClick(2000);
					this.clickByText("全选");
					this.clickByText("删除");
					this.click(ReaderUtil.ANDROID_TEXT1);//点击删除
					this.sleep(1000);
					this.exitApp(AppInfo.PACKAGE_FILEMANAGER);
				}
				
				}
					
		}
		
		else {
			this.exitApp(AppInfo.PACKAGE_FILEMANAGER);
		}
			
	    }
	
	
	/**
	 * 确定图片只有一张
	 * @return
	 * @throws AutoException
	 * @date 2015年8月15日下午3:05:16
	 * @author hujiachun
	 */
	public boolean isExistPicture() throws AutoException{
		this.exitApp(AppInfo.PACKAGE_FILEMANAGER);
	    this.startApp(AppInfo.PACKAGE_FILEMANAGER, ReaderUtil.FILE_ACTIVITY);
	    boolean result = false;
		this.findElement(By.byId(ReaderUtil.FILE_LIST)).toVerticalList().scrollTextInto("Download");
		this.sleep(1000);
			
		this.clickTextForFile("Download");
		this.clickTextForFile("Photo");
		List<Element> item = this.findElementsById(ReaderUtil.FILE_ITEM);
		int number = item.size();
		if (number == 1) {
			result = true;
		}
		
		this.exitApp(AppInfo.PACKAGE_FILEMANAGER);
		return result;
		
	}
	
	
    /**
     * 
     * 向上滑动小部分
     */
    public void swipeUpLittle() {
		this.swipe(this.getDisplayWidth() / 2, this.getDisplayHeight() / 2,
				this.getDisplayWidth() / 2, this.getDisplayHeight() / 3, 100);
		this.sleep(200);
	}
    
    /**
     * 
     * 向下滑动小部分
     */
    public void swipeDownLittle() {
		this.swipe(this.getDisplayWidth() / 2, this.getDisplayHeight() / 3,
				this.getDisplayWidth() / 2, this.getDisplayHeight() / 2, 100);
		this.sleep(200);
	}
	
	
    /**
	 * 连续点击text
	 * @param texts
	 * @throws AutoException
	 * @date 2015年7月30日下午2:15:36
	 * @author hujiachun
	 */
	public void clickByTexts(String... texts) throws AutoException {
		for (String text : texts) {
			this.findElement(By.byText(text)).click();
			this.sleep(500);
		}
		
	}
    
  
	/**
	 * 定位到有图片的文章,并点击进入
	 * @throws AutoException
	 * @date 2015年8月15日下午4:10:05
	 * @author hujiachun
	 */
    public void clickPicture(String vs) throws AutoException{
    	int j = 1, picture;
    	List<Element> content_numbers = this.findElementsById(ReaderUtil.CONTENT_NAME);
		for (int i = 0; i < content_numbers.size(); i++) {
			if (j % 5 == 0){
				this.swipeUp();
				i = 0;
			}
			System.out.println(i);
			if (content_numbers.get(i).getBounds().top < this.findElement(ReaderUtil.PAPER_PICTURE_BACK).getBounds().bottom) {
				this.swipeDownLittle();
			}
			if (content_numbers.get(i).getBounds().bottom == this.getDisplayHeight()) {
				this.swipeUpLittle();
			}
			content_numbers.get(i).click();
			if(vs.equals("2.2")){
				picture = 1;	
			}
			else{
				picture = 2;	
			}
			
			if(isExistByDescContains(ReaderUtil.SPECIAL_DESC)){
				picture = picture +1;
				
			}
			if(isExistByDescContains(ReaderUtil.CHANNEL_DESC)){
				picture = picture +1;	
			}
			
		if (this.isExistByClassName(ReaderUtil.PAPER_IMAGE) && this.findElements(By.byClassName(ReaderUtil.PAPER_IMAGE)).size() >= picture + 1 ) {//寻找图片
			int centerY = this.findElement(By.byClassName(ReaderUtil.PAPER_IMAGE)).getBounds().centerY();
			int bottomY = this.getDisplayHeight() / 8 * 7;
			int topY = this.getDisplayHeight() / 8 ;
			if ( !(centerY < bottomY  && centerY > topY)) {//不在在可点击范围内，返回
				System.out.println("不在点击范围内，退出重新下一个点击");
			    this.pressBack();
				continue;
			}
			
			if(vs.equals("2.2")){
				this.clickByElement(By.byClassName(ReaderUtil.PAPER_IMAGE));	
			}
			else{
				this.clickByElement(By.byClassName(ReaderUtil.PAPER_IMAGE).instance(1));	
			}
			break;
		}
		else {
			this.pressBack();
			continue;
		 }
	     	}    			
    	
    }
    
    
    /**
     * 首页进入个人中心并进行点击
     * @throws AutoException
     * @param text
     * @date 2015年8月15日下午4:43:52
     * @author hujiachun
     */
    public void enterPersonCenterAndClick(String text) throws AutoException{
    	this.click(ReaderUtil.CENTER_ACTION);
    	this.clickByText(text);
    	
    }
    
    
    /**
     * 从收藏列表返回至热议列表
     * @throws AutoException
     * @date 2015年8月15日下午5:18:38
     * @author hujiachun
     */
    public void backHotListFromCollect() throws AutoException{
    	this.pressBack();
		this.sleep(500);
		this.pressBack();
		this.enterHotList();
    }
	
    
    /**
     * 从热议文章返回至收藏列表
     * @throws AutoException
     * @date 2015年8月15日下午5:20:55
     * @author hujiachun
     */
    public void backCollectFromPaper() throws AutoException{
    	this.sleep(1000);
		this.pressBack();
		this.sleep(500);
		this.pressBack();
		this.enterPersonCenterAndClick("收藏");
    }
    
    
    /**
     * 文章详情点击更多
     * @param text
     * @throws AutoException
     * @date 2015年8月15日下午5:55:53
     * @author hujiachun
     */
    public void clickMore(String text) throws AutoException{
    	this.click(ReaderUtil.PAPER_MORE);
    	if (!text.equals("")) {
			this.clickByText(text);
		}
    	
    }
    
    
    /**
     * 个人中心模块存在
     * @param instance
     * @param module
     * @return
     * @throws AutoException
     * @date 2015年8月17日下午2:10:15
     * @author hujiachun
     */
    public boolean isCenterModule(int instance, String module) throws AutoException{
    	boolean isexists = this.findElement(By.byId(ReaderUtil.CENTER_ICON).instance(instance)).toRight(By.byText(module)).exists();
		return isexists;
    	
    }
    
    
    /**
     * 删除收藏
     * @throws AutoException
     * @date 2015年8月17日下午3:01:30
     * @author hujiachun
     */
    public void deleteCollect() throws AutoException{
    	//删除收藏
        if (isExistById(ReaderUtil.RESERVE_TITLE)) {
        	List<Element> item = this.findElementsById(ReaderUtil.RESERVE_TITLE);
    		
    		if (item.size() == 1) {//只有一个收藏
    			item.get(0).longClick(2000);
    			this.click(ReaderUtil.CENTER_DELETE);
    			this.click(ReaderUtil.ANDROID_TEXT1);
    			this.sleep(1000);
    			
    		}
    		else {//多个收藏
    			item.get(0).longClick(2000);
    			this.clickByText("全选");
    			this.click(ReaderUtil.CENTER_DELETE);
    			this.click(ReaderUtil.ANDROID_TEXT1);
    			this.sleep(1000);
    		
    		}
		}
       
    }
    
    
    /**
     * 收藏
     * @throws AutoException
     * @date 2015年8月17日下午4:19:05
     * @author hujiachun
     */
    public void addCollect() throws AutoException {
    	
		this.isExistById(ReaderUtil.PAPER_COLLECT, 60000);
		if (isExistById(ReaderUtil.PAPER_COLLECT)) {
			this.click(ReaderUtil.PAPER_COLLECT);//点击收藏
		}
	}
    
    
    /**
	 * 离开通知栏
	 * 
	 * @date 2015年7月31日上午9:22:39
	 * @author hujiachun
	 */
	public void leaveNotify(){
	    int deviceHeight = this.getDisplayHeight();
	    int deviceWidth = this.getDisplayWidth();
	 
	    this.drag(deviceHeight, deviceWidth / 2, deviceHeight / 2, deviceWidth / 2, 100);
	}
    
	
	/**
	 * 添加插件
	 * @param last_page 最后一页桌面
	 * @param widget 插件名称
	 * @param page 桌面的页数
	 * @throws AutoException
	 * @date 2015年8月18日下午2:12:16
	 * @author hujiachun
	 */
	public void addLauncherWidget(Element last_page, String widget, int page) throws AutoException{
		int centerY = last_page.getBounds().centerY();
		int differenceX = last_page.getBounds().right - last_page.getBounds().left;
		this.longClick(differenceX, centerY);//长按空白处
		this.clickByText("添加工具");
		
		
		while (this.findElement(ReaderUtil.LAUNCHER_WIDGET_PAGE).getBounds().right > this.getDisplayWidth() / 2) {//从头开始滑动
			this.swipe(this.getDisplayWidth() / 10, this.getDisplayHeight() / 10 * 9,
	    			this.getDisplayWidth() / 10 * 9, this.getDisplayHeight() / 10 * 9, 50);
			
		}
		
		
		this.findElement(ReaderUtil.LAUNCHER_WIDGET_LIST).toHorizontalList().searchBy(By.byId
				(ReaderUtil.LAUNCHER_WIDGET_NAME).text(widget), 5000);	
		
		this.findElement(By.byId(ReaderUtil.LAUNCHER_WIDGET_NAME).text(widget)).dragTo(this.getDisplayWidth(), 0, 100);//拖拽移动插件
		this.sleep(2000);
		this.pressHome();
		Element last_page_after = this.findElement(By.byId(ReaderUtil.LAUNCHER_PAGE)).findElement(By.byIndex(0).index(page));
		last_page_after.click();//点击桌面	
	}
    
    
	/**
	 * 滑动插件
	 * @throws AutoException
	 * @date 2015年8月20日下午8:51:04
	 * @author hujiachun
	 */
	public void swipeADT() throws AutoException{
		int centerX = this.findElement(ReaderUtil.LAUNCHER_READER_LIST).getBounds().centerX();
		int top = this.findElement(ReaderUtil.LAUNCHER_READER_LIST).getBounds().top;
		int bottom = this.findElement(ReaderUtil.LAUNCHER_READER_LIST).getBounds().bottom;
		int ADT_height = bottom - top;
		swipe(centerX, ADT_height / 100 * 98, centerX, ADT_height / 100 , 50);
		this.sleep(500);
	}
	
    /**
	 * 监听器
	 * 
	 * @date 2015年7月30日下午2:45:46
	 * @author hujiachun
	 */
	 public void watcherS(){
		 this.registerWatcher("日历", new AutoWatcher() {
				
				@Override
				public boolean checkForCondition() {
					// TODO Auto-generated method stub
					if(findElement(By.byId("android:id/button2").text("稍后提醒")).exists()){
						try {
							findElement(By.byText("确定")).click();
						} catch (AutoException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					return false;
				}
			});
		 
		 this.registerWatcher("允许", new AutoWatcher() {

				@Override
				public boolean checkForCondition() {
					if (this.findElement(By.byText("允许")).exists()) {
						try {
							this.findElement(By.byText("允许")).click();
						} catch (AutoException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return false;
				}
			});

	    	 this.registerWatcher("停止运行", new AutoWatcher() {
	 			
	 			@Override
	 			public boolean checkForCondition() {
	 				if (findElement(By.byTextContains("停止运行")).exists()) {
	 					try {
	 						
	 				    	Process p = Runtime.getRuntime().exec("am broadcast -a sk.action.SCREENSHOT --es NAME "+ "stop" + new Date().getTime());
	 						p.waitFor();
	 						findElement(By.byText("确定")).click();
	 					} catch (Exception e) {
	 						e.printStackTrace();
	 					}
	 				}
	 				return false;
	 			}
	 		});
	    	
	    	 this.registerWatcher("无响应", new AutoWatcher() {
	  			
	  			@Override
	  			public boolean checkForCondition() {
	  				if (findElement(By.byTextContains("无响应")).exists()) {
	  					try {
	  						
	  				    	Process p = Runtime.getRuntime().exec("am broadcast -a sk.action.SCREENSHOT --es NAME "+ "ANR" + new Date().getTime());
	  						p.waitFor();
	  						findElement(By.byText("确定")).click();
	  					} catch (Exception e) {
	  						e.printStackTrace();
	  					}
	  				}
	  				return false;
	  			}
	  		});
	    }
	 
	 
	 
	 
	 
		//阿里云统计
		 
		    /**
			 * 判断是不是MA01的userdebug版本 //
			 * data/data/com.meizu.scriptkeeper/files/uitest/tools/aoctrace.sh
			 * 
			 * @throws IOException
			 */
			public boolean checkUserDebug(String model, String version) throws IOException {
				Process process = Runtime.getRuntime().exec("getprop ro.build.description");
				InputStreamReader ir = new InputStreamReader(process.getInputStream());
				BufferedReader input = new BufferedReader(ir);
				String result = input.readLine();
				if (result.contains(model) && result.contains(version)) {
					return true;
				}
				return false;

			}
			

			/**
			 * 函数调用统计
			 * 
			 * @throws IOException
			 */
			public void yunosTest(String cmd) throws IOException {
				String commandstr = "system/bin/sh /data/aoctrace.sh " + cmd;
				exec(commandstr);
				/*
				String[] commandstr = { "system/bin/sh /data/aoctrace.sh " + cmd };
				Process process = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(process.getOutputStream());
				for (String command : commandstr) {
					System.out.println(command);
					os.write(command.getBytes());
					os.writeBytes("\n");
					os.flush();
				}
				os.writeBytes("exit\n");
				os.flush();
				try {
					process.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				os.close();
				process.destroy();
				*/
			}


			
			
			//多语言
			
			
			/**
		     * 点击控件并返回
		     * @param id
		     * @param wait
		     * @date 2015年9月7日下午3:07:50
		     * @author hujiachun
		     * @throws AutoException 
		     */
		    public void clickAndBack(String id, int wait) throws AutoException{
		    	this.findElement(id).click();
				this.sleep(wait);
				this.pressBack();
		    }
		    
		    
		    /**
		     * 点击控件
		     * @param id
		     * @date 2015年9月7日下午3:07:50
		     * @author hujiachun
		     * @throws AutoException 
		     */
		    public void clickAndWait(String id) throws AutoException{
		    	this.findElement(id).click();
				this.sleep(500);

		    }
	 
		    
		  //获取国家
		    public String getCountry(){
		    	
				return ShellUtil.getProperty("persist.sys.country");
				}
		    
		    //获取语言
		    public String getLanguage(){
		    	
				return ShellUtil.getProperty("persist.sys.language");
				}
	 
}
