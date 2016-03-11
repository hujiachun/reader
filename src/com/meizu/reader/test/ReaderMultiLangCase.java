/**
 * hujiachun
 */
package com.meizu.reader.test;

import java.util.List;

import android.graphics.RadialGradient;
import com.meizu.automation.By;
import com.meizu.automation.Expectation;
import com.meizu.automation.Steps;
import com.meizu.automation.constants.AutoException;
import com.meizu.automation.service.Element;
import com.meizu.test.util.AppInfo;
import com.meizu.test.util.ShellUtil;

/**
 * @author hujiachun
 */
public class ReaderMultiLangCase extends TestScript{
	
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		System.out.println("-----setUp-----");
		this.pressHome();
		ShellUtil.setUtf7Input();
		this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
		
	}
	
	
	
	@Override
	protected void runTest() throws Throwable {
		// TODO Auto-generated method stub
		
		System.out.println("-----runTest-----");
		try {
			this.watcherS(); 
			this.runWatchers();
			super.runTest();
			ShellUtil.sendResult(this.getName(), true);
			
			System.out.println(true);
			System.out.println(true);
			System.out.println(true);
		} catch (Throwable e) {
			// TODO: handle exception
			ShellUtil.sendResult(this.getName(), false);
			
			System.out.println(false);
			System.out.println(false);
			System.out.println(false);
		
			
			throw e;
		}
	}
		

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		System.out.println("-----tearDown-----");
		this.exitReader();
		ShellUtil.setSystemInput();
	  
	}
	
	@Steps("首页")
	@Expectation("首页")  
	public void test001() throws AutoException{
		ShellUtil.clearCache(AppInfo.PACKAGE_READER);
		this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
		this.click(ReaderUtil.GATEGORY_ICON);
		this.click("com.meizu.media.reader:id/guide_ok_btn");
		this.waitForElement(ReaderUtil.HOME_IMG, 30000);//等待轮播banner加载
	
		this.swipeUp();
		this.swipeUp();
		this.findElement("com.meizu.media.reader:id/tab_container").findElement(By.byIndex(1)).click();
		this.click(ReaderUtil.CONTENT_NAME);
	}
	
	@Steps("订阅栏")
	@Expectation("订阅栏")  
	public void test002() throws AutoException{//订阅栏
		this.waitForElement(ReaderUtil.HOME_IMG, 30000);
		this.clickDownList();
		this.click("com.meizu.media.reader:id/action_edit");
		Element start = this.findElementById(ReaderUtil.GATEGORY_NAME, 0);
		Element end = this.findElementById(ReaderUtil.GATEGORY_NAME, 1);
		this.findElementById(ReaderUtil.RESERVE_DRAG, 0).
		dragTo(this.findElementById(ReaderUtil.RESERVE_DRAG, 1), 80);//移动栏目
		this.click(ReaderUtil.RESERVE_DELETE);
		this.click("com.meizu.media.reader:id/action_edit");
		this.click(ReaderUtil.GATEGORY_NAME);
		this.clickDownList();
		this.click(ReaderUtil.RESERVE_NAME);
		
	}
	
	@Steps("订阅中心")
	@Expectation("订阅中心")  
	public void test003() throws AutoException{//订阅中心
		this.waitForElement(ReaderUtil.HOME_IMG, 30000);
		this.enterReserveCenter();
		this.click(ReaderUtil.RESERVE_RECOMMEND);
		this.click(ReaderUtil.RESERVE_TEXT);
		this.click(ReaderUtil.RESERVE_TITLE);
		this.click(ReaderUtil.CHANNEL_STATE);
		this.click(ReaderUtil.RESERVE_TITLE);
		this.pressBack();
		this.pressBack();
		this.click("com.meizu.media.reader:id/action_search");
		this.click(ReaderUtil.RESERVE_RECOMMEND);
		this.click("com.meizu.media.reader:id/action_cancel");
		this.click("com.meizu.media.reader:id/action_search");
		this.setTextById("com.meizu.media.reader:id/mc_search_edit", "NBA");
		this.sleep(1000);
		this.click("com.meizu.media.reader:id/mc_search_icon_input_clear");
		this.setTextById("com.meizu.media.reader:id/mc_search_edit", "NBA");
		this.sleep(1000);
		this.click(ReaderUtil.RESERVE_TITLE);
		}
	
	@Steps("个人中心")
	@Expectation("个人中心")  
	public void test004() throws AutoException{//个人中心
		this.waitForElement(ReaderUtil.HOME_IMG, 30000);
		this.click(ReaderUtil.READER_CENTER);
		if(isExistById("com.meizu.media.reader:id/iv_list_in")){//login
			this.click("com.meizu.media.reader:id/iv_list_in");
			this.setTextById("com.meizu.account:id/edtAccount", "testtool");
			this.setTextById("com.meizu.account:id/edtPwd", "appadmin111");
			this.click("com.meizu.account:id/btnLogin");
			if(isExistById("com.meizu.account:id/mz_toolbar_nav_button")){
				this.pressBack();
			}	
		}
		
		this.click("com.meizu.media.reader:id/iv_next", 0);//收藏
		if(isExistById("com.meizu.media.reader:id/empty_image")){
			this.click("com.meizu.media.reader:id/empty_image");
		}
		if(isExistById("com.meizu.media.reader:id/title")){
			this.click("com.meizu.media.reader:id/title");
			this.pressBack();
		}
		this.pressBack();
		
		this.click("com.meizu.media.reader:id/iv_next", 1);//消息
		if(isExistById("com.meizu.media.reader:id/empty_image")){
			this.click("com.meizu.media.reader:id/empty_image");
		}
		this.click(ReaderUtil.CENTER_MESSAGE_REPLY);
		this.pressBack();
		this.click(ReaderUtil.COMMENT_FLOOR);
		this.findElementByClassName("android.support.v7.app.e", 1).click();
		if(isExistById("com.meizu.media.reader:id/empty_image")){
			this.click("com.meizu.media.reader:id/empty_image");
		}
		this.click(ReaderUtil.CENTER_MESSAGE_REPLY);
		this.pressBack();
		this.pressBack();
		
		this.click("com.meizu.media.reader:id/tv_text", 2);
		this.click("com.meizu.media.reader:id/tv_text", 2);
	}
	
	@Steps("设置")
	@Expectation("设置")  
	public void test005() throws AutoException{//设置
		this.waitForElement(ReaderUtil.HOME_IMG, 30000);
		this.click(ReaderUtil.READER_CENTER);
		this.click("com.meizu.media.reader:id/iv_next", 2);//消息
		
		for(int i= 0;  i < 4; i++){
			this.click(ReaderUtil.SETTING_SWITCH, i);
			this.sleep(100);
		}
		this.click("com.meizu.media.reader:id/tv_hint");
		this.click("com.meizu.media.reader:id/iv_next");//意见反馈
		this.clickAndBack("com.meizu.media.reader:id/send_mail", 1000);
		this.setTextById("com.meizu.media.reader:id/edit_suggestion", "test");
		this.setTextById("com.meizu.media.reader:id/edit_contact", "test");
		this.click("com.meizu.media.reader:id/submit_button");
		
				
	}
	
	@Steps("文章详情")
	@Expectation("文章详情")  
	public void test006() throws AutoException{//文章详情
		this.waitForElement(ReaderUtil.HOME_IMG, 30000);
		this.enterPaper();			
		this.locationComment();
		this.swipeDown();
		this.sleep(1000);
		
		this.setTextById(ReaderUtil.PAPER_COMMENT_EDIT, "1234");
		this.click(ReaderUtil.PAPER_COMMENT_SUBMIT);//提交评论
		this.sleep(1000);
		this.click(ReaderUtil.PAPER_COLLECT);
		this.click(ReaderUtil.COMMENT_PRAISE);
		this.click(ReaderUtil.COMMENT_FLOOR);
		this.pressBack();
		this.click(ReaderUtil.PAPER_MORE);
		this.click("com.meizu.media.reader:id/tv_font_size_small");
		this.click("com.meizu.media.reader:id/tv_font_size_default");
		this.click("com.meizu.media.reader:id/tv_font_size_big");
		this.click("com.meizu.media.reader:id/sw_nightmode");
		this.click(ReaderUtil.PAPER_MORE);
		this.findElementByClassName("android.widget.TextView").click();
		
		
	}
	
	
	public void test007() throws AutoException{
		this.waitForElement(ReaderUtil.HOME_IMG, 30000);//等待轮播banner加载   
		this.enterHotList();
		
		//定位到有2个以上图片的文章,并点击进入
		List<Element> content_numbers = this.findElementsById(ReaderUtil.CONTENT_NAME);
		int j = 1;
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
			if (this.isExistByClassName(ReaderUtil.PAPER_IMAGE) &&
					this.findElements(By.byClassName(ReaderUtil.PAPER_IMAGE)).size() > 2) {//寻找图片 并且图片个数大于2
				System.out.println("image:" + this.findElements(By.byClassName(ReaderUtil.PAPER_IMAGE)).size());
				int centerY = this.findElement(By.byClassName(ReaderUtil.PAPER_IMAGE)).getBounds().centerY();
				int bottomY = this.getDisplayHeight() / 8 * 7;
				int topY = this.getDisplayHeight() / 8 ;
				if ( !(centerY < bottomY  && centerY > topY)) {//不在在可点击范围内，返回
					System.out.println("不在点击范围内，退出重新下一个点击");
				   this.pressBack();
					continue;
				}
				
				this.clickByElement(By.byClassName(ReaderUtil.PAPER_IMAGE));
				break;
			}
			else {
				this.pressBack();
				j++;
				continue;
			}
		}
		
		
		this.swipeLeftList(50);//左滑
		this.swipeRightList(50);//右滑
		this.click("com.meizu.media.reader:id/image_share");
		this.pressBack();
		
		this.click("com.meizu.media.reader:id/image_save");
		this.click("com.meizu.media.reader:id/mz_toolbar_nav_button");
		this.click("com.meizu.media.reader:id/mz_toolbar_nav_button");
		
		
	}
	
}
