package com.meizu.reader.test;

import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;
import com.meizu.automation.By;
import com.meizu.automation.Expectation;
import com.meizu.automation.Priority;
import com.meizu.automation.Steps;
import com.meizu.automation.constants.AutoException;
import com.meizu.automation.service.Element;
import com.meizu.test.util.AppInfo;
import com.meizu.test.util.ShellUtil;
import com.meizu.uibridge.NotRootException;
import jp.jun_nama.test.utf7ime.helper.Utf7ImeHelper;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by hujiachun on 16/3/7.
 */
public class ToolSanityCase extends TestScript{
    private final static int TIME = 30000;
    public static String VERSIONS;
    public static boolean ISYUNOS;
    @Override
    protected void initial() {
        // TODO Auto-generated method stub
        try {
            VERSIONS = getAppVersions(AppInfo.PACKAGE_READER).split("versionName=")[1];
            System.out.println("version : " + VERSIONS);
            System.out.println("isYunos : " + isYunos());
            ISYUNOS = isYunos();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.initial();
    }
    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        System.out.println("-----setUp-----");

        this.pressHome();
        this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
        ShellUtil.sendBroadcast(" st.START.PF ", new Object[]{"--es NAME " + getName()});
    }


    /* (non-Javadoc)
     * @see junit.framework.TestCase#runTest()
     */
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



    /* (non-Javadoc)
     * @see com.android.uiautomator.testrunner.UiAutomatorTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
        System.out.println("-----tearDown-----");
//		this.exitApp(AppInfo.PACKAGE_READER);
        this.exitReader();
//		this.exec("ime set com.meizu.flyme.input/com.meizu.input.MzInputService");
        ShellUtil.setSystemInput();
        ShellUtil.sendBroadcast("st.STOP.PF", new Object[]{"--es NAME " + getName()});
    }



    @Priority(Priority.P2)
    @Steps("1.查看推荐栏目列表文章标题下的标签" +
            "2.metal手机查看文章列表" +
            "3.点击推荐栏目文章列表的删除按钮")
    @Expectation("1.文章标题下依次显示：来源和来自网易推荐的标签（如大家都在看）"+
            "2、显示网易推荐的文章" +
            "3.文章在列表中消失,toast提示“不再推荐相似文章”")
    public void test003Recommend() throws AutoException, RemoteException, NotRootException {
//		ShellUtil.clearCache(AppInfo.PACKAGE_READER);
//		this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
//		this.clickByText("确定");
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.clickByText("推荐");

        assertTrue("2", isExistById(ReaderUtil.CONTENT_NAME, TIME));
        String name = this.getTextById(ReaderUtil.CONTENT_NAME);
        this.click(ReaderUtil.CONTENT_DELETE);
        assertTrue("3", !this.getTextById(ReaderUtil.CONTENT_NAME).equals(name));



        /**
         * 	HashSet<String> paper_title = new HashSet<String>();
         for (int j = 0; j < 5; j++) {
         for (int i = 0; i < 5; i++) {
         paper_title.add(this.findElementById(ReaderUtil.CONTENT_NAME, i).getText());
         if(i == 4){
         this.findElementById(ReaderUtil.CONTENT_NAME, i).dragTo(
         this.findElementById(ReaderUtil.CONTENT_NAME, i - 4), 100);
         this.sleep(2000);
         }
         }
         }
         int before = paper_title.size();
         System.out.println("before=" + before);
         this.swipeDown(50);
         for (int j = 0; j < 8; j++) {
         for (int i = 0; i < 5; i++) {
         paper_title.add(this.findElementById(ReaderUtil.CONTENT_NAME, i).getText());
         if(i == 4){
         this.findElementById(ReaderUtil.CONTENT_NAME, i).dragTo(
         this.findElementById(ReaderUtil.CONTENT_NAME, i - 4), 100);
         this.sleep(2000);
         }
         }
         }
         int after = paper_title.size();
         System.out.println("after=" + after);
         */
    }




    @Priority(Priority.P2)
    @Steps("进入首页，检查默认热门页面显示")
    @Expectation("1.首页默认显示热门页面，由上到下依次显示轮播banner、广告位和要闻推荐列表")
    public void test004EnterReaderGUI() throws AutoException{

        if(this.findElementByText("选择你喜欢的分类").exists()){
            this.clickByText("确定");
        }
        Element img = this.findElement(ReaderUtil.HOME_IMG);//轮播banner
        List<Element> image = this.findElements(ReaderUtil.HOME_IMAGE);//广告位
        assertTrue("NOT轮播图", img.waitForExists(20000));
        this.sleep(2000);
        System.out.println(image.size());
        assertTrue("广告位错误", image.size() == 2);
        this.sleep(2000);
        //要闻推荐列表
        List<Element> content_image = this.findElements(ReaderUtil.CONTENT_IMAGE);
        List<Element> content_name = this.findElements(ReaderUtil.CONTENT_NAME);
        List<Element> content_site = this.findElements(ReaderUtil.CONTENT_SITE);
        List<Element> content_time = this.findElements(ReaderUtil.CONTENT_TIME);
        List<Element> content_comment = this.findElements(ReaderUtil.CONTENT_COMMENT);
        this.sleep(2000);
        System.out.println(content_site.size());
        assertTrue("要闻推荐列表图片显示错误", content_image.size() > 1);
        assertTrue("要闻推荐列表标题显示错误", content_name.size() > 1);
        assertTrue("要闻推荐列表来源显示错误", content_site.size() > 1);
        if(VERSIONS.equals("2.2")){
            assertTrue("要闻推荐列表时间显示错误", content_time.size() > 1);
        }
        else{
            assertTrue("要闻推荐列表评论显示错误", content_comment.size() > 1);
        }


    }

    @Priority(Priority.P2)
    @Steps("在首页不进行任何操作，检查Banner自动滚动播放")
    @Expectation("banner每隔5S自动由前往后轮播")
    public void test005AutoChangeBanner() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载

        String title_before = this.getTextById(ReaderUtil.BANNER_TITLE);//获取banner标题
        System.out.println("title_before" + title_before);

        this.sleep(7000);
        String title_after = this.getTextById(ReaderUtil.BANNER_TITLE);//5.5s后获取banner标题
        System.out.println("title_after" + title_after);
        assertTrue("标题相同", !title_before.equals(title_after));

    }


    @Priority(Priority.P1)
    @Steps("1点击文章类型类型banner ")
    @Expectation("正确进入相应页面")
    public void test006PaperBanner() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载

        List<Element> paper = this.findElements(ReaderUtil.BANNER_TITLE);//由于会加载出三个文章类型banner，人眼看到的是第二个

        String paperName = paper.get(1).getText();
        System.out.println(paperName);
        paper.get(1).click();//点击轮播
        assertTrue("未进入轮播", this.isExistById(ReaderUtil.PAPER_TITLE, 50000));

    }

    @Priority(Priority.P1)
    @Steps("2.点击热议类型banner")
    @Expectation("正确进入相应页面")
    public void test006HotBanner() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.click(ReaderUtil.HOME_IMAGE, 0);//点击热议
        assertTrue("未进入热议", this.isExistByText("热议", 20000));
    }


    @Priority(Priority.P2)
    @Steps("3.点击专题类型banner")
    @Expectation("正确进入相应页面")
    public void test006SpecialBanner() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.click(ReaderUtil.HOME_IMAGE, 1);//点击热议
        assertTrue("未进入专题", this.isExistByText("专题", 20000));
    }

    @Priority(Priority.P2)
    @Steps("在首页banner区域左右依次滑动，检查Banner手动滚动切换")
    @Expectation("banner滚动切换正常，每页显示正确")
    public void test007ManualChangeBanner() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载

        String title_before = this.getTextById(ReaderUtil.BANNER_TITLE);
        System.out.println("title_before: " + title_before);
        int left = this.findElementById(ReaderUtil.HOME_IMG, 1).getBounds().left;
        int right = this.findElementById(ReaderUtil.HOME_IMG, 1).getBounds().right;
        int centerY = this.findElementById(ReaderUtil.HOME_IMG, 1).getBounds().centerY();
        int centerX = this.findElementById(ReaderUtil.HOME_IMG, 1).getBounds().centerX();
        this.swipe(centerX, centerY, left, centerY, 20);

        String title_after = this.getTextById(ReaderUtil.BANNER_TITLE);
        System.out.println("title_after: " + title_after);


        assertTrue("标题相同", !title_before.equals(title_after));

    }


    @Priority(Priority.P2)
    @Steps("首页栏目切换1.文章列表处左右滑动切换2.栏目标题栏处点击切换")
    @Expectation("1.可成功切换至相邻栏目2.可成功切换至所点栏目")
    public void test008SwipeLeft_Right() throws AutoException, RemoteException, IOException {
        boolean isMA01 = this.checkUserDebug("m1metal", "test-keys");

        if(isMA01){

        }else{

            this.pub_logout("appadmin111");
            ShellUtil.clearCache(AppInfo.PACKAGE_READER);
            this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
            this.clickByText("确定");
            this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载

            //左右滑动
            assertTrue(this.getTopText(0)+"未选中", this.isTopChecked(0));

            String before_title = this.getTextById(ReaderUtil.CONTENT_NAME);
            this.swipeLeftList(100);
            assertTrue(this.getTopText(1)+"未选中", this.isTopChecked(1) && !isChangeTitle(before_title));
            this.swipeRightList(100);
            assertTrue(this.getTopText(0)+"未选中", this.isTopChecked(0) && isChangeTitle(before_title));

            //点击
            assertTrue(this.getTopText(0)+"未选中", this.isTopChecked(0));
            this.clickTop(1);
            assertTrue(this.getTopText(1)+"未选中", this.isTopChecked(1) && !isChangeTitle(before_title));
            this.clickTop(0);
            assertTrue(this.getTopText(0)+"未选中", this.isTopChecked(0) && isChangeTitle(before_title));
        }
    }

    @Priority(Priority.P1)
    @Steps("点击栏目文章列表下的文章")
    @Expectation("进入相应文章详情页面")
    public void test009ClickPaper() throws AutoException{
        if(this.findElementByText("选择你喜欢的分类").exists()){
            this.clickByText("确定");
        }
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        Element paper = this.findElement(ReaderUtil.CONTENT_NAME);
        String paperName = paper.getText();//获取第一个文章名字
        System.out.println(paperName);
        assertTrue("文章不能点击", paper.click());

        //判断当前界面是否出现paperName
        assertTrue("未正确进入文章", isExistByDescContains(paperName, 60000));

    }

    //---------------------------订阅管理----------------------------------------------------------------------

    @Priority(Priority.P1)
    @Steps("点击标题栏三角下标1.首页2.我的订阅")
    @Expectation("1.打开我的订阅2.收起我的订阅")
    public void test010ClickPaper() throws AutoException {
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.clickDownList();
        assertTrue("未打开订阅", isExistById(ReaderUtil.RESERVE_LIST));
        this.clickDownList();
        assertTrue("未关闭订阅", !isExistById(ReaderUtil.RESERVE_LIST));

    }

    @Priority(Priority.P2)
    @Steps("在我的订阅页面，点击"+"号按钮")
    @Expectation("进入订阅中心分类页面")
    public void test011ClickAdd() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.clickDownList();
        this.clickAdd();
        assertTrue("未进入订阅中心分类", isExistById(ReaderUtil.RESERVE_SEARCH, TIME) && isExistByText("订阅", TIME));

    }

    @Priority(Priority.P1)
    @Steps("在订阅中心页面，点击顶栏搜索按钮")
    @Expectation("进入频道搜索页面，弹出键盘光标定位搜素框")
    public void test012ClickSearch() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        ShellUtil.setSystemInput();//设置系统输入法
        this.enterReserveSearch();
        assertTrue("未弹出输入框", this.isExistById(ReaderUtil.RESERVE_SEARCH_EDIT, 10000));

        List<Element> reserveIcon_after = this.findElements(ReaderUtil.RESERVE_ICON);

//		assertTrue("未弹出输入法", reserveIcon.size() < 5 );
        this.pressBack();
        List<Element> reserveIcon_before = this.findElements(ReaderUtil.RESERVE_ICON);
        assertTrue("未弹出输入法", reserveIcon_before.size() > reserveIcon_after.size());

    }


    @Priority(Priority.P2)
    @Steps("搜索结果列表1.输入框无关键词2.输入框有关键词")
    @Expectation("1.显示后台提供的热门搜索频道，并正确显示订阅状态2.可成功搜索出结果，并正确显示订阅状态")
    public void test013InputSearch() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterReserveSearch();
        assertTrue("未显示热门标题", isExistByText("热门频道", 100000));

        List<Element> recommendList = this.findElements(By.byId(ReaderUtil.RESERVE_RECOMMEND));
        System.out.println(recommendList.size());
        assertTrue("热门频道未显示", recommendList.size() > 0);//显示热门频道推荐
        boolean isExistCancel = false;
        for (int j = 0; j < recommendList.size(); j++) {
            if (recommendList.get(j).getText().equals("已订阅")) {
//				continue;
                isExistCancel = true;
                break;
            }
            else {
                recommendList.get(j).click();
                isExistCancel = recommendList.get(j).getText().equals("已订阅");
                break;
            }

        }
        this.sleep(1500);
        assertTrue("未正确显示订阅状态", isExistCancel);//正确显示订阅状态

        this.setTextById(ReaderUtil.RESERVE_SEARCH_EDIT, Utf7ImeHelper.e("NBA"));
        this.sleep(2000);
        assertTrue("未显示搜索结果", this.getTextById(ReaderUtil.RESERVE_TITLE).equals("NBA"));
        assertTrue("未正确显示订阅状态", this.findElement(By.byId(ReaderUtil.RESERVE_TITLE)).toRight(By.byText("订阅")).exists());
    }


    @Priority(Priority.P1)
    @Steps("在源搜索页面，搜索一个频道1.订阅该频道2.取消订阅该频道")
    @Expectation("1.成功订阅，我的订阅页面相应栏目下增加相应频道卡片2.成功取消订阅，相应频道卡片从我的订阅页面自动删除")
    public void test014InputSearch() throws AutoException, IOException{
        boolean isMA01 = this.checkUserDebug("m1metal", "test-keys");

        if(isMA01){

        }else{
            this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
            this.enterReserveSearch();
            this.setTextById(ReaderUtil.RESERVE_SEARCH_EDIT, Utf7ImeHelper.e("虎扑NBA"));
            this.sleep(5000);
            String recommended = this.getTextById(ReaderUtil.RESERVE_RECOMMEND);
            if (recommended.equals("已订阅")) {
                this.click(ReaderUtil.RESERVE_RECOMMEND);
                this.sleep(1000);
            }
            this.findElement(By.byText("虎扑NBA")).toRight(By.byText("订阅")).click();
            this.backReserveList();
//		this.sleep(2000);
//		this.exitReader();
//		this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
//		this.clickDownList();
            boolean result1 = this.findElement(ReaderUtil.RESERVE_LIST).toVerticalList().searchByText("虎扑NBA", 60000);
            assertTrue("未找到订阅", result1);

            this.pressBack();//搜索取消订阅
            this.enterReserveSearch();
            this.setTextById(ReaderUtil.RESERVE_SEARCH_EDIT, Utf7ImeHelper.e("虎扑NBA"));
            this.sleep(5000);
            this.click(ReaderUtil.RESERVE_RECOMMEND);
            this.backReserveList();
            this.backReserveListTop();
            boolean result2 = !this.findElement(ReaderUtil.RESERVE_LIST).toVerticalList().searchByText("虎扑NBA", 60000);
            assertTrue("未删除订阅", result2);

        }

    }


    @Priority(Priority.P1)
    @Steps("在订阅中心页面，进入各栏目分类下1.频道列表处滑动切换2.标题栏处点击切换")
    @Expectation("1.可成功进入，显示相邻分类的所有频道列表2.可成功进入，显示对应分类的所有频道列表")
    public void test015ReserveCenter() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterReserveCenter();
        String before_title = this.getTextById(ReaderUtil.RESERVE_TITLE);
        //左右滑动
        assertTrue(this.getReserveText(0)+"未选中", this.isReserveSelected(0) && isChangeTitle(before_title));
        this.swipeLeftList(100);
        assertTrue(this.getReserveText(1)+"未选中", this.isReserveSelected(1) && !isChangeTitle(before_title));
        this.swipeRightList(100);
        assertTrue(this.getReserveText(0)+"未选中", this.isReserveSelected(0) && isChangeTitle(before_title));

        //点击
        assertTrue(this.getReserveText(0)+"未选中", this.isReserveSelected(0) && isChangeTitle(before_title));
        this.clickReserveTop(1);
        assertTrue(this.getReserveText(1)+"未选中", this.isReserveSelected(1) && !isChangeTitle(before_title));
        this.clickReserveTop(0);
        assertTrue(this.getReserveText(0)+"未选中", this.isReserveSelected(0) && isChangeTitle(before_title));

    }


    @Priority(Priority.P1)
    @Steps("在栏目分类下订阅多个频道")
    @Expectation("可成功订阅，我的订阅页面增加相应频道卡片")
    public void test016MoreReserve() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterReserveCenter();

        //订阅第一个
        Element reserve1 = this.findElement(By.byId(ReaderUtil.RESERVE_RECOMMEND).text("订阅"));
        String reserveTitle1 = reserve1.toLeft(By.byId(ReaderUtil.RESERVE_TITLE)).getText();
        reserve1.click();
        System.out.println(reserveTitle1);

        //订阅第二个
        this.swipeLeftList(100);
        Element reserve2 = this.findElement(By.byId(ReaderUtil.RESERVE_RECOMMEND).text("订阅"));
        String reserveTitle2 = reserve1.toLeft(By.byId(ReaderUtil.RESERVE_TITLE)).getText();
        reserve2.click();
        System.out.println(reserveTitle2);

        //订阅第三个
        this.swipeLeftList(100);
        Element reserve3 = this.findElement(By.byId(ReaderUtil.RESERVE_RECOMMEND).text("订阅"));
        String reserveTitle3 = reserve1.toLeft(By.byId(ReaderUtil.RESERVE_TITLE)).getText();
        reserve3.click();
        System.out.println(reserveTitle3);
        this.backReserveList();

        boolean reault1 = this.findElement(ReaderUtil.RESERVE_LIST).toVerticalList().searchByText(reserveTitle1, 60000);

        this.backReserveListTop();
        boolean reault2= this.findElement(ReaderUtil.RESERVE_LIST).toVerticalList().searchByText(reserveTitle2, 60000);

        this.backReserveListTop();
        boolean reault3 = this.findElement(ReaderUtil.RESERVE_LIST).toVerticalList().searchByText(reserveTitle3, 60000);

        System.out.println("'reault1:" + reault1 +"' " + "'reault2:" + reault2 +"' " + "'reault3:" + reault3 +"' ");
        assertTrue("'reault1:" + reault1 +"' " + "'reault2:" + reault2 +"' " + "'reault3:" + reault3 +"' "
                , reault1 && reault2 && reault3);

    }


    @Priority(Priority.P2)
    @Steps("在频道文章列表内订阅多个频道")
    @Expectation("可成功订阅，栏目分类下订阅状态正确更新，我的订阅页面增加相应源卡片")
    public void test017EnterReserve() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterReserveCenter();

        String titleName1 = this.reserveChannel();
        String titleName2 = this.reserveChannel();
        this.pressBack();
        this.backReserveListTop();
        boolean reault1 = this.findElement(ReaderUtil.RESERVE_LIST).toVerticalList().searchByText(titleName1, 60000);
        this.backReserveListTop();
        boolean reault2 = this.findElement(ReaderUtil.RESERVE_LIST).toVerticalList().searchByText(titleName2, 60000);
        assertTrue("reault1订阅中心未找到", reault1);
        assertTrue("reault2订阅中心未找到", reault2);

    }


    @Priority(Priority.P1)
    @Steps("在我的订阅页面，点击任意栏目卡片")
    @Expectation("进入首页对应栏目下的文章列表")
    public void test018ClickTop() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.clickDownList();
        Element gategory = this.findElement(ReaderUtil.GATEGORY_NAME);
        String gategoryNme = gategory.getText();
        System.out.println(gategoryNme);
        gategory.click();

        boolean isChecked = this.findElementByText(gategoryNme).isChecked();
        assertTrue("首页未选中", isChecked);
    }


    @Priority(Priority.P1)
    @Steps("在我的订阅页面，点击任意频道卡片")
    @Expectation("进入频道文章详情页面")
    public void test019ClickChanel() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.clickDownList();
        Element gategory = this.findElement(ReaderUtil.RESERVE_NAME);
        String gategoryNme = gategory.getText();
        System.out.println(gategoryNme);
        gategory.click();
        this.waitForElement(ReaderUtil.RESERVE_STARS, TIME);
        assertTrue("未正确进入文章详情", this.findElement(By.byId(ReaderUtil.RESERVE_STARS))
                .toUp(By.byText(gategoryNme)).exists());

    }


//	@Steps("进入编辑模式1.点击顶栏编辑按钮2.长按栏目/频道")
//	@Expectation("成功进入编辑模式，除精选栏目外，其余栏目以及栏目下的频道，均在左上角显示出移动/删除按钮")
//	public void test020ClickEdit() throws AutoException{
//		this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
//
//		String number = this.findElement(ReaderUtil.RESERVE_NUMBER).getText().substring(1, 2);
//		System.out.println(number);
//
//
//		//待实现
//	}


    @Priority(Priority.P2)
    @Steps("在编辑模式下移动栏目卡片")
    @Expectation("栏目卡片成功移动，移动完成后首页栏目顺序重新排列")
    public void test021EditDrag() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterEdit();
        Element start = this.findElementById(ReaderUtil.GATEGORY_NAME, 0);
        Element end = this.findElementById(ReaderUtil.GATEGORY_NAME, 1);
        //移动前两位置的栏目
        String drag_start = start.getText();
        String drag_end = end.getText();
        System.out.println(drag_start + "=>" + drag_end);
        //移动操作
        this.findElementById(ReaderUtil.RESERVE_DRAG, 0).
                dragTo(this.findElementById(ReaderUtil.RESERVE_DRAG, 1), 80);
        //移动后
        this.sleep(2000);
        String after_start = start.getText();
        String after_end = end.getText();
        System.out.println(after_start + "--" + after_end);
        assertTrue("1", after_start.equals(drag_end));
        assertTrue("2", after_end.equals(drag_start));

    }


    @Priority(Priority.P2)
    @Steps("在编辑模式下删除栏目下的频道卡片1.删除一个卡片2.删除全部卡片")
    @Expectation("1.可成功删除该频道卡片2.可成功删除所有频道卡片和该栏目")
    public void test022EditDrag() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterEdit();
        Element start = this.findElementById(ReaderUtil.GATEGORY_NAME, 0);//第一个栏目
        String startText = start.getText();
        System.out.println(startText);
        int count = this.findElement(By.byId(ReaderUtil.RESERVE_VIEW).instance(0)).getChildCount();

        //栏目下不足两个频道
        if (count < 2) {
            this.backReserveList();
            this.clickByElement(By.byId(ReaderUtil.RESERVE_ADD));

            //寻找订阅中心第一栏的栏目

            int i = 0, j= 0;
            while (!isExistByText(startText) && i < 10) {
                this.swipeLeftList(50);
                i++;
                System.out.println(i +"-");
            }
            Element text = this.findElement(By.byText(startText));
            String text_name = text.getText();
            while ( !text.isSelected() && j< 10) {//是否被选中
                this.swipeLeftList(50);
                j++;
                System.out.println(j +"*");
            }
            this.waitForText("订阅", 60000);
            //订阅
            for (int k = 0; k < 2; k++) {
                this.clickByText("订阅");
                this.sleep(500);
            }
            this.backReserveList();
            this.clickByText("编辑");
        }

        Element firstChannel = this.findElementById(ReaderUtil.RESERVE_NAME, 0);//第一个频道
        String before_text = firstChannel.getText();
        firstChannel.click();
        assertTrue("频道未被删除", !firstChannel.getText().equals(before_text));

        //删除所有
        int count_after = this.findElement(By.byId(ReaderUtil.RESERVE_VIEW).instance(0)).getChildCount();
        System.out.println("child=" + count_after);
        for (int i = 0; i < count_after; i++) {
            this.findElementById(ReaderUtil.RESERVE_NAME).click();
            this.sleep(500);
        }

        assertTrue("栏目没有消失", !isExistByText(startText));
    }


    @Priority(Priority.P1)
    @Steps("进入频道文章列表，点击阅读文章")
    @Expectation("可进入相应的频道文章详情")
    public void test023EnterChannelAndPaper() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterReserveCenter();
        this.click(ReaderUtil.RESERVE_TITLE, 0);//点击频道
        this.waitForElement(ReaderUtil.CHANNEL_STATE, 60000);
        String clickText = this.findElementById(ReaderUtil.RESERVE_TITLE, 0).getText();
        this.click(ReaderUtil.CHANNEL_IMG, 0);//点击文章
        assertTrue("未正确进入文章详情", isExistByDescContains(clickText, 60000));

        //轻松一刻文不对题

    }






    //---------------------------文章详情----------------------------------------------------------------------

    @Priority(Priority.P2)
    @Steps("在文章详情页面滑动1.正文区域上下滑动2.评论区域上下滑动")
    @Expectation("评论输入框保持显示")
    public void test024EnterPaper() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        boolean swipePaper = false;
        for (int i = 0; i < 5; i++) {
            this.swipeUp();

        }

        assertTrue("文章滑动评论框消失",  isExistById(ReaderUtil.PAPER_COMMENT_EDIT));
        this.locationComment();
        if (isExistByText("最新评论")) {
            swipePaper = true;
        }
        System.out.println(swipePaper);
        this.swipeUp();
        this.sleep(1000);
        this.swipeUp();
        assertTrue("评论滑动评论框消失", swipePaper  && isExistById(ReaderUtil.PAPER_COMMENT_EDIT));

    }


    /**
     * 不登录账户区域
     */

    @Priority(Priority.P2)
    @Steps("评论输入框默认文本1.账户未登录")
    @Expectation("1.登录添加评论")
    public void test025CommentGUI() throws AutoException, RemoteException{
        //不登陆账户
        this.pub_logout("appadmin111");
        this.exitReader();//回到桌面
        this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        assertTrue("未登录账户显示异常", isExistByText("登录添加评论", 60000));


    }


    @Priority(Priority.P2)
    @Steps("发表评论1.未登录账户时点击评论输入框进行发表")
    @Expectation("1.转跳到Flyme账户登录页面")
    public void test026SubmitComment() throws AutoException, RemoteException{
        //不登陆账户
        this.pub_logout("appadmin111");
        this.exitReader();//回到桌面
        this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();

        this.click(ReaderUtil.PAPER_COMMENT_EDIT);
//		this.setTextById(ReaderUtil.PAPER_COMMENT_EDIT, "感人");

        assertTrue("未跳转账户登陆页面", isExistById(ReaderUtil.ACCOUNT_EDIT, 10000));

    }


    @Priority(Priority.P2)
    @Steps("回复评论1.未登录账户时点击评论进行回复")
    @Expectation("1.转跳到Flyme账户登录页面")
    public void test027ReplyComment() throws AutoException, RemoteException, IOException{
        //不登陆账户
        this.pub_logout("appadmin111");
        this.exitReader();//回到桌面
        this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.locationComment();
        this.refreshApp("资讯");
        assertTrue("未跳转评论", isExistByText("最新评论", 10000));
        this.click(ReaderUtil.COMMENT_FLOOR, 0);//点击一楼
//		this.findElement(By.byId("com.meizu.media.reader:id/ArticleCommentListview").index(1)).click();
        assertTrue("未跳转账户登陆页面", isExistById(ReaderUtil.ACCOUNT_EDIT, 10000));


    }


    @Priority(Priority.P2)
    @Steps("举报评论1.未登录账户时点击举报")
    @Expectation("1.弹出对话框提示登录flyme账号")
    public void test028ReportPraise() throws AutoException, RemoteException, IOException{
        this.pub_logout("appadmin111");
        this.exitReader();//回到桌面
        this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.locationComment();
        this.refreshApp("资讯");
        assertTrue("未跳转评论", isExistByText("最新评论", 10000));
        this.findElementById(ReaderUtil.COMMENT_FLOOR, 0).longClick(2000);
        this.clickByText("举报");
        this.findElementById(ReaderUtil.COMMENT_REPORE, 0).click();
        this.clickByText("提交");
        assertTrue("未跳转账户登陆页面", isExistByText("举报评论需要登录账号", 10000));
    }


    @Priority(Priority.P1)
    @Steps("账户栏点击登陆1.未登录账户时点击")
    @Expectation("1.转跳到Flyme账户登录页面")
    public void test029Account() throws AutoException, RemoteException{
        this.pub_logout("appadmin111");
        this.exitReader();//回到桌面
        this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.click(ReaderUtil.CENTER_ACTION);//点击个人中心
        assertTrue("未显示未登录", isExistByText("未登录", 10000));
        this.click(ReaderUtil.CENTER_ICON_HEAD);
        assertTrue("未跳转账户登陆页面", isExistById(ReaderUtil.ACCOUNT_EDIT, 10000));
    }


    @Priority(Priority.P2)
    @Steps("其他账户登录入口1.消息页面回复我的tab点击登录2.消息页面我的评论tab点击登录")
    @Expectation("转跳到Flyme账户登录页面")
    public void test030ReplyMessage() throws AutoException, RemoteException{
        this.pub_logout("appadmin111");
        this.exitReader();//回到桌面
        this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPersonCenterAndClick("消息");
        this.clickByTexts("回复我的", "请点击登录");
        assertTrue("未跳转账户登陆页面", isExistById(ReaderUtil.ACCOUNT_EDIT, 10000));

        this.exitReader();//回到桌面
        this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPersonCenterAndClick("消息");
        this.clickByTexts("我的评论", "请点击登录");
        assertTrue("未跳转账户登陆页面", isExistById(ReaderUtil.ACCOUNT_EDIT, 10000));
    }


    @Priority(Priority.P2)
    @Steps("列表边界值滑动检查2.在列表最后一篇文章左滑翻页")
    @Expectation("不可翻页，页面无翻动效果")
    public void test031SwipeLeftRightPaper() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterHotList();
        for (int i = 0; i < 2; i++) {//滑到底部
            this.swipeUp();
            this.sleep(500);
        }
        List<Element> titles = this.findElements(ReaderUtil.CONTENT_NAME);
        String title = this.findElementById(ReaderUtil.CONTENT_NAME, titles.size() - 1).getText();
        this.click(ReaderUtil.CONTENT_NAME, titles.size() - 1 );//点击最后一篇文章
        assertTrue("未正确进入文章详情", isExistByDescContains(title, TIME));
        this.sleep(2000);
        this.swipeLeftList(50);
        assertTrue("尾页不可翻页失效1", isExistByDescContains(title, 60000));
        this.swipeRightList(50);
        assertTrue("尾页不可翻页失效2", !isExistByDescContains(title, 10000));

    }


    @Priority(Priority.P2)
    @Steps("列表边界值滑动检查1.在列表第一篇文章右滑翻页")
    @Expectation("不可翻页，页面无翻动效果")
    public void test032SwipeLeftRightPaper() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterReserveCenter();
        this.click(ReaderUtil.RESERVE_TITLE, 0);//点击频道

        String title = this.findElementById(ReaderUtil.RESERVE_TITLE, 0).getText();

        System.out.println(title);
        this.click(ReaderUtil.RESERVE_TITLE, 0);//点击文章

        assertTrue("未正确进入文章详情", isExistByDescContains(title, TIME));
        this.sleep(2000);
        this.swipeRightList(50);
        assertTrue("首篇不可翻页失效1", isExistByDescContains(title, TIME));
        this.swipeLeftList(50);
        assertTrue("首篇不可翻页失效2", !isExistByDescContains(title, 10000));

    }

    //登录账户区域




    @Priority(Priority.P2)
    @Steps("1.点击消息2.左右滑动切换tab2.点击切换tab")
    @Expectation("1.进入消息页面，并默认定位至回复我的tab2.正常切换tab")
    public void test033ChangeMessageTab() throws AutoException, RemoteException {
        //登陆账户
        if (!isLoginAccount()) {
            this.pub_login("testtool", "appadmin111");
        }
        this.exitReader();//回到桌面
        this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPersonCenterAndClick("消息");
        if (isExistByText("请点击登录")) {
            this.click("com.meizu.media.reader:id/empty_image");
        }
        assertTrue("未定位至回复我的tab", isSelected(By.byText("回复我的")));
        assertTrue("未定位至回复我的tab", !isSelected(By.byText("我的评论")));

        this.clickByText("我的评论");
        assertTrue("未定位至我的评论tab", !isSelected(By.byText("回复我的")));
        assertTrue("未定位至我的评论tab", isSelected(By.byText("我的评论")));
    }


    @Priority(Priority.P1)
    @Steps("1.在我的评论页面点击评论的文章标题")
    @Expectation("1.进入相应文章详情页面")
    public void test034ClickEnterCommentTab() throws AutoException, RemoteException {
        if (!isLoginAccount()) {
            this.pub_login("testtool", "appadmin111");
        }
        this.exitApp(AppInfo.PACKAGE_ACCOUNT);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPersonCenterAndClick("消息");
        if (isExistByText("请点击登录")) {
            this.click("com.meizu.media.reader:id/empty_image");
        }
        this.clickByText("我的评论");
//		this.isExistById(ReaderUtil.CENTER_MESSAGE_REPLY, 60000);
//		String paper_title = this.getTextById(ReaderUtil.CENTER_MESSAGE_REPLY).split("：")[1];
//		System.out.println(paper_title);

        this.click(ReaderUtil.CENTER_MESSAGE_REPLY);//点击文章标题
        assertTrue("未成功跳转原文", isExistById(ReaderUtil.PAPER_COMMENT, TIME));

        boolean bool = !this.getTextById(ReaderUtil.PAPER_COMMENT).equals("");
        assertTrue("评论异常", bool);


    }

    @Priority(Priority.P2)
    @Steps("1.在回复我的tab点击一条回复我的评论2.在回复我的tab回复一条回复我的评论")
    @Expectation("1.可弹出键盘与评论框2.可成功回复该条评论")
    public void test035ClickMessageTab() throws AutoException, RemoteException {
        if (!isLoginAccount()) {
            this.pub_login("testtool", "appadmin111");
        }
        this.exitApp(AppInfo.PACKAGE_ACCOUNT);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPersonCenterAndClick("消息");
        if (isExistByText("请点击登录")) {
            this.click("com.meizu.media.reader:id/empty_image");
        }
        this.isExistById(ReaderUtil.CENTER_MESSAGE_REPLY, 60000);
        this.click(ReaderUtil.COMMENT_FLOOR);
        assertTrue("未弹出评论框", isExistById(ReaderUtil.PAPER_COMMENT_EDIT));
        this.sleep(2000);
        Random random = new Random();
        int tail = random.nextInt();
        String Strtail = Integer.toString(tail).substring(2, 7);//获取随机数字
        System.out.println(Strtail);
        this.exec("input text " + Strtail);//输入评论
        this.sleep(2000);
        this.click(ReaderUtil.PAPER_COMMENT_SUBMIT);//提交评论
        this.sleep(2000);
        this.pressBack();
        this.sleep(10000);
        this.clickByTexts("消息", "我的评论");
        this.swipeDown(50);
        this.sleep(1000);
        this.swipeDown(50);
        assertTrue("未成功回复评论", isExistByText(Strtail));

    }


    @Priority(Priority.P1)
    @Steps("1.在回复我的tab点击收到评论的文章标题")
    @Expectation("1.进入相应文章详情页面")
    public void test036ClickEnterMessageTab() throws AutoException, RemoteException {
        if (!isLoginAccount()) {
            this.pub_login("testtool", "appadmin111");
        }
        this.exitApp(AppInfo.PACKAGE_ACCOUNT);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPersonCenterAndClick("消息");
        if (isExistByText("请点击登录")) {
            this.click("com.meizu.media.reader:id/empty_image");
        }
        this.isExistById(ReaderUtil.CENTER_MESSAGE_REPLY, 60000);
//		String paper_title = this.getTextById(ReaderUtil.CENTER_MESSAGE_REPLY).split("：")[1];
//		System.out.println(paper_title);
        this.click(ReaderUtil.CENTER_MESSAGE_REPLY);//点击文章标题
        this.isExistById(ReaderUtil.PAPER_COMMENT_EDIT, 60000);
        assertTrue("未成功跳转原文", isExistById(ReaderUtil.PAPER_COMMENT, TIME));

        boolean bool = !this.getTextById(ReaderUtil.PAPER_COMMENT).equals("");
        assertTrue("评论异常", bool);
    }





    @Priority(Priority.P1)
    @Steps("账户栏点击登陆1.登录系统账户时点击")
    @Expectation("2.账户自动登录应用")
    public void test038LoginAccount() throws AutoException, RemoteException{
        //登陆账户
        if (!isLoginAccount()) {
            this.pub_login("testtool", "appadmin111");
        }
        this.exitApp(AppInfo.PACKAGE_ACCOUNT);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.click(ReaderUtil.CENTER_ACTION);//点击个人中心
        this.click(ReaderUtil.CENTER_ICON_HEAD);
        assertTrue("未显示登录状态", isExistByText("54952766", 10000));
    }


    @Priority(Priority.P2)
    @Steps("举报评论1.登录账户时点击举报")
    @Expectation("成功提交举报内容，返回文章详情页面")
    public void test039LoginReportPraise() throws AutoException, RemoteException, NotRootException, IOException{
        //登陆账户
        if (!isLoginAccount()) {
            this.pub_login("testtool", "appadmin111");
        }
        this.exitApp(AppInfo.PACKAGE_ACCOUNT);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.locationComment();
        this.refreshApp("资讯");
        this.findElementById(ReaderUtil.COMMENT_FLOOR, 0).longClick(2000);
        this.clickByText("举报");
        this.findElementById(ReaderUtil.COMMENT_REPORE, 4).click();//点击其他
        this.setTextById(ReaderUtil.COMMENT_REPORE_DEC, Utf7ImeHelper.e("TEST"));//输入评论
        this.sleep(1000);
        this.clickByText("提交");//提交
//	    assertTrue("未弹出提示", BridgeUtil.waitForNotificationText("举报成功", 10000));
        assertTrue("未返回文章详情页面", isExistById(ReaderUtil.COMMENT_FLOOR, 20000));
    }



    @Priority(Priority.P2)
    @Steps("评论输入框默认文本账户已登录")
    @Expectation("添加评论")
    public void test040LoginCommentGUI() throws AutoException, RemoteException{
        //登陆账户
        if (!isLoginAccount()) {
            this.pub_login("testtool", "appadmin111");
        }
        this.exitApp(AppInfo.PACKAGE_ACCOUNT);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        assertTrue("未登录账户显示异常", isExistByText("添加评论", 60000));
    }


    @Priority(Priority.P2)
    @Steps("发表评论2.登陆账户时点击评论输入框进行发表3.在文章详情页面回复自己的评论")
    @Expectation("2.弹出键盘，可发表评论成功，评论数加1 3.提示无法回复自己")
    public void test041LoginSubmitComment() throws AutoException, RemoteException, NotRootException, IOException{
        //登陆账户
        if (!isLoginAccount()) {
            this.pub_login("testtool", "appadmin111");
        }
        this.exitApp(AppInfo.PACKAGE_ACCOUNT);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();

        //评论前
        int amount_before = Integer.parseInt(this.getTextById(ReaderUtil.PAPER_COMMENT));
        this.locationComment();
        this.refreshApp("资讯");
        this.click(ReaderUtil.PAPER_COMMENT_EDIT);
        this.setTextById(ReaderUtil.PAPER_COMMENT_EDIT, Utf7ImeHelper.e("1321"));
        this.click(ReaderUtil.PAPER_COMMENT_SUBMIT);
        boolean Commented = this.findElement(ReaderUtil.PAPER_COMMENT_LIST).toVerticalList().searchByText("1321", 60000);
        assertTrue("未找到评论内容", Commented);
        int amount_after = Integer.parseInt(this.getTextById(ReaderUtil.PAPER_COMMENT));

        //评论后
        assertTrue("评论数未加1", (amount_after - amount_before) == 1);

        //回复自己
        this.clickByText("1321");
//		assertTrue("未提示不能回复自己", BridgeUtil.waitForNotificationText("不能回复自己", 10000));
    }


    @Priority(Priority.P2)
    @Steps("回复评论2.登陆账户时点击评论进行回复")
    @Expectation("2.弹出键盘，可回复评论成功，评论数加1")
    public void test042LoginReplyComment() throws AutoException, RemoteException, IOException{
        //登陆账户
        if (!isLoginAccount()) {
            this.pub_login("testtool", "appadmin111");
        }
        this.exitApp(AppInfo.PACKAGE_ACCOUNT);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.locationComment();
        this.refreshApp("资讯");
        //评论前
        int amount_before = Integer.parseInt(this.getTextById(ReaderUtil.PAPER_COMMENT));
        String username = this.getTextById(ReaderUtil.COMMENT_USERNAME); //获取将要回复的用户名称
        this.click(ReaderUtil.COMMENT_FLOOR, 0);//点击一楼
        this.findElement(ReaderUtil.PAPER_COMMENT_EDIT).click();
//	    this.findElement(ReaderUtil.PAPER_COMMENT_EDIT).setText(Utf7ImeHelper.e("Touched Lives!"));
        this.exec("input text 123456");
        this.sleep(2000);
        this.click(ReaderUtil.PAPER_COMMENT_SUBMIT);
        boolean Commented = this.findElement(ReaderUtil.PAPER_COMMENT_LIST).toVerticalList().searchByText("123456", 60000);

        assertTrue("未找到评论内容", Commented);
        assertTrue("未在指定楼层看到评论", this.findElement(By.byText("123456")).toUp(By.byText(username)).exists());
        int amount_after = Integer.parseInt(this.getTextById(ReaderUtil.PAPER_COMMENT));

        //评论后
        assertTrue("评论数未加1", (amount_after - amount_before) == 1);


    }


    @Priority(Priority.P1)
    @Steps("在文章详情页面左右滑动1.向左滑动2.向右滑动")
    @Expectation("1.可成功翻页到下一篇文章2.可成功翻页到上一篇文章")
    public void test043SwipeLeftRightPaper() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterReserveCenter();
        this.click(ReaderUtil.RESERVE_TITLE, 0);//点击频道

        String text1 = this.findElementById(ReaderUtil.RESERVE_TITLE, 0).getText();
        String text2 = this.findElementById(ReaderUtil.RESERVE_TITLE, 1).getText();
        System.out.println(text1);
        System.out.println(text2);
        this.click(ReaderUtil.RESERVE_TITLE, 0);//点击文章

        assertTrue("未正确进入文章详情", isExistByDescContains(text1, 60000));
        //左滑动
        this.swipeLeftList(50);
        assertTrue("左滑动失效", isExistByDescContains(text2, 60000));

        //右滑动
        this.swipeRightList(50);
        assertTrue("右滑动失效", isExistByDescContains(text1, 60000));

        //热议进入查找不到webview  只能从订阅中心-栏目-文章 进入
    }


    @Priority(Priority.P1)
    @Steps("在文章详情页面点击文章顶部评论数")
    @Expectation("可定位至文章评论区域")
    public void test044LocationComment() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.click(ReaderUtil.PAPER_COMMENT);//点击评论数
        assertTrue("未定位至评论区域", isExistByText("最新评论", 10000));
        int y = this.findElementByText("最新评论").getBounds().centerY();
        System.out.println(y);
        assertTrue("未出现在屏幕内", y < this.getDisplayHeight());


    }


    @Priority(Priority.P1)
    @Steps("1.点击未赞的评论条目右侧的点赞按钮2.点击已赞的评论条目右侧的点赞按钮")
    @Expectation("1.点赞图标高亮，点赞数加1 2.点赞无法取消，图标保持高亮，点赞数不变")
    public void test045ClickPraise() throws AutoException, IOException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.locationComment();
        this.refreshApp("资讯");
        //点赞之前
        String text = this.getTextById(ReaderUtil.COMMENT_NUMBER, 0);
        if (text.equals("赞")) {//首赞
            //点赞之后
            this.click(ReaderUtil.COMMENT_PRAISE, 0);
            assertTrue("未点赞成功", this.getTextById(ReaderUtil.COMMENT_NUMBER, 0).equals("1"));

            //第二次点赞
            this.click(ReaderUtil.COMMENT_PRAISE, 0);
            assertTrue("第二次点赞", this.getTextById(ReaderUtil.COMMENT_NUMBER, 0).equals("1"));
        }
        else {//有赞了

            int before_praise = Integer.parseInt(this.getTextById(ReaderUtil.COMMENT_NUMBER, 0));
            System.out.println("before_praise:" + before_praise);
            this.click(ReaderUtil.COMMENT_PRAISE, 0);
            //点赞之后
            int after_praise = Integer.parseInt(this.getTextById(ReaderUtil.COMMENT_NUMBER, 0));
            System.out.println("after_praise:" + after_praise);
            assertEquals(before_praise + 1, after_praise);


            //第二次点赞
            this.click(ReaderUtil.COMMENT_PRAISE, 0);
            int secondPraise = Integer.parseInt(this.getTextById(ReaderUtil.COMMENT_NUMBER, 0));
            System.out.println("secondPraise" + secondPraise);
            assertEquals(after_praise, secondPraise);
        }


    }


    @Priority(Priority.P1)
    @Steps("在文章评论区域，长按一条评论，点击复制")
    @Expectation("可成功复制该条评论")
    public void test046CopyPraise() throws AutoException, IOException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.locationComment();
        this.refreshApp("资讯");
        String copyText = this.getTextById(ReaderUtil.COMMENT_FLOOR);
        System.out.println(copyText);
        this.findElementById(ReaderUtil.COMMENT_FLOOR).longClick(2000);
        this.clickByText("复制");

//		this.startApp(AppInfo.PACKAGE_NOTEPAPER, AppInfo.ACTIVITY_NOTEPAPER);
//		this.isExistById(ReaderUtil.NOTEPAPER_ADD, 20000);
//		this.click(ReaderUtil.NOTEPAPER_ADD);//新建便签
//		this.findElement(ReaderUtil.NOTEPAPER_EDIT).longClick(3000);
//		this.sleep(2000);
//		//获取坐标
//		Rect title = this.findElement(ReaderUtil.NOTEPAPER_TITLE).getBounds();
//		int title_bottom = title.bottom;
//		int title_top = title.top;
//		int title_right = title.right;
//		int copy_value = title_bottom - title_top;
//		int copy_X = title_right / 5;
//		int copy_Y = copy_value + title_bottom;
//		this.click(copy_X, copy_Y);
//
//		this.sleep(2000);
//		String noteText = this.findElement(By.byId(ReaderUtil.NOTEPAPER_TEXT).
//				className(ReaderUtil.NOTEPAPER_EDIT_CLASS)).getText();//获取到编辑框文本

        String noteText = this.copyTextToNotePaper();//获取复制的文本

        System.out.println(noteText);
        assertEquals(copyText, noteText);

    }


    @Priority(Priority.P1)
    @Steps("长按一条评论，点击举报")
    @Expectation("进入评论举报页面")
    public void test047ClickReportCommnet() throws AutoException, IOException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.locationComment();
        this.refreshApp("资讯");
        this.findElementById(ReaderUtil.COMMENT_FLOOR, 0).longClick(2000);
        this.clickByText("举报");
        assertTrue("未进入举报页面", isExistById(ReaderUtil.COMMENT_REPORE, 20000));
    }


    @Priority(Priority.P1)
    @Steps("相关内容跳转1.点击相关频道2.点击相关话题")
    @Expectation("1.进入相应频道文章列表2.进入相应话题")
    public void test048ContentGoto() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterSpecialList();
        this.swipeUpLittle();

        //定位相关内容存在 专题 和 频道
        for (int i = 0; i < 6; i++) {
            this.findElementById(ReaderUtil.CONTENT_NAME, i).click();
            if (isExistByDescContains(ReaderUtil.SPECIAL_DESC) && isExistByDescContains(ReaderUtil.CHANNEL_DESC)) {
                break;
            }
            this.pressBack();

        }

//		this.locationComment();
        int center_top = this.findElement(By.byDescriptionContains(ReaderUtil.SPECIAL_DESC)).getBounds().top;
//		int center_bottom = this.findElement(By.byId(ReaderUtil.TOP_BAR)).getBounds().bottom;
//
//		if (center_top < center_bottom) {
//			this.swipeDown();
//			this.sleep(2000);
//		}

        //定位
        while (this.findElement(By.byDescriptionContains(ReaderUtil.SPECIAL_DESC)).getBounds().centerY() > this.getDisplayHeight() / 2) {
            this.swipeUp(10);
            this.sleep(100);
            if (this.findElement(By.byDescriptionContains(ReaderUtil.SPECIAL_DESC)).getBounds().centerY() <  this.getDisplayHeight() / 10 * 9
                    ) {
                break;
            }

        }
        if (this.findElement(By.byDescriptionContains(ReaderUtil.SPECIAL_DESC)).getBounds().centerY() <
                this.findElement(By.byId(ReaderUtil.PAPER_PICTURE_BACK)).getBounds().top) {
            this.swipeDown();
        }
        this.sleep(2000);


        this.findElement(By.byDescriptionContains(ReaderUtil.CHANNEL_DESC)).click();//点击频道

        assertTrue("未跳转至该频道", this.isExistById(ReaderUtil.CHANNEL_STATE, 60000));
        this.pressBack();
        this.findElement(By.byDescriptionContains(ReaderUtil.SPECIAL_DESC)).click();//点击专题
        assertTrue("未跳转至专题", this.isExistByText("专题", 60000));
    }


    @Priority(Priority.P1)
    @Steps("长按选中文章内的文字1.点击复制2.点击搜索")
    @Expectation("1.可成功复制文字到剪贴板2.可成功转跳至浏览器，使用默认搜索引擎搜索选中文字")
    public void test049CopyText() throws AutoException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterHotList();

        //排除标题带有"," ，否则定位不到
        List<Element> content_numbers = this.findElementsById(ReaderUtil.CONTENT_NAME);
        for (int i = 0; i < content_numbers.size(); i++) {
            if (content_numbers.get(i).getText().contains("，")) {
                continue;
            }
            content_numbers.get(i).click();
            break;
        }

        Element sign = this.findElementByDescriptionContains("，");
        assertTrue("未找到符号", sign.waitForExists(5000));

        int i = 0;
        while (sign.getBounds().centerY() > this.getDisplayHeight() / 5 * 4  && i < 30) {
            this.swipeUp();
            this.sleep(500);
        }


        Rect collect = this.findElement(ReaderUtil.PAPER_MORE).getBounds();
        int collectList = collect.bottom - collect.top;
        int signX = sign.getBounds().centerX();
        int signY = sign.getBounds().centerY();
        sign.longClick();

        //找到复制 的相对坐标
        int copyX = signX - collectList / 3 * 2;
        int copyY = signY - collectList ;
        this.sleep(1000);

        this.click(copyX, copyY);//点击复制
        this.sleep(1000);
        String copyText =copyTextToNotePaper();//获取复制的文本
        System.out.println(copyText);
        this.sleep(3000);
        assertTrue("当前页面未找到复制的文本", isExistByDescContains(copyText, 20000));

        sign.longClick();
        //找到搜索的相对坐标
        int searchX = signX + (collectList / 3 * 2);
        int searchY = signY - collectList;
        this.click(searchX, searchY);//点击搜索
        assertTrue("未跳转至浏览器", isExistById(ReaderUtil.BROWSER_URL, 20000));
        assertTrue("当前页面未找到搜索的文本", isExistByDescContains(copyText, 20000));

    }


    @Priority(Priority.P2)
    @Steps("点击文章详情页面静态图片,左右滑动切换图片")
    @Expectation("可通过内置图片浏览器打开相应图片,可成功切换上一张/下一张图片")
    public void test050OpenPictureAndSwipe() throws AutoException{
        int picture = 4;
        if(VERSIONS.equals("2.2")){
            picture = 3;
        }

        if(isExistByDescContains(ReaderUtil.SPECIAL_DESC)){
            picture = picture +1;

        }
        if(isExistByDescContains(ReaderUtil.CHANNEL_DESC)){
            picture = picture +1;
        }

        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterHotList();

        Set<String> titles = new HashSet<String>();
        //定位到有2个以上图片的文章,并点击进入
        List<Element> content_numbers = this.findElementsById(ReaderUtil.CONTENT_NAME);

        int j = 1;
//		int size = content_numbers.size();
        for (int i = 0; i < 10; i++) {
            if(j > 5){
                break;
            }
//			if (j % 6 == 0){
//				this.findElementById(ReaderUtil.CONTENT_NAME, 4).dragTo(findElementById(ReaderUtil.CONTENT_NAME, 0), 100);
//				this.sleep(1000);
//				i = 2;
//			}

//			String title_text = content_numbers.get(i).getText();
//			if (content_numbers.get(i).getBounds().top < this.findElement(ReaderUtil.PAPER_PICTURE_BACK).getBounds().bottom) {
//				this.swipeDownLittle();
//				this.sleep(200);
//			}
//			if (content_numbers.get(i).getBounds().bottom == this.getDisplayHeight()) {
//				this.swipeUpLittle();
//				this.sleep(200);
//			}

//			if(titles.size() == 0){
//				content_numbers.get(i).click();
//			}else {
//				for(int n = 0; n < titles.size(); n++){
//					if(titles.contains(title_text)){
//						i = i + 1;
//					}
//
//				}
//				System.out.println(i);
//				content_numbers.get(i).click();
//			}

            if(i == 5){
                this.findElementById(ReaderUtil.CONTENT_NAME, 4).dragTo(findElementById(ReaderUtil.CONTENT_NAME, 0), 100);
                this.sleep(1000);
                j++;
                i = 2;
            }
            content_numbers.get(i).click();

            if (this.isExistByClassName(ReaderUtil.PAPER_IMAGE) &&
                    this.findElements(By.byClassName(ReaderUtil.PAPER_IMAGE)).size() >= picture + 1) {//寻找图片 并且图片个数大于2
                System.out.println("image:" + this.findElements(By.byClassName(ReaderUtil.PAPER_IMAGE)).size());
                int centerY = this.findElement(By.byClassName(ReaderUtil.PAPER_IMAGE)).getBounds().centerY();
                int bottomY = this.getDisplayHeight() / 8 * 7;
                int topY = this.getDisplayHeight() / 8 ;
                if ( !(centerY < bottomY  && centerY > topY)) {//不在在可点击范围内，返回
                    System.out.println("不在点击范围内，退出重新下一个点击");
                    this.pressBack();
                    continue;
                }
                if(VERSIONS.equals("2.2")){
                    this.clickByElement(By.byClassName(ReaderUtil.PAPER_IMAGE));
                }
                else{
                    this.clickByElement(By.byClassName(ReaderUtil.PAPER_IMAGE).instance(1));
                }

                break;
            }
            else {
//				titles.add(title_text);
                this.pressBack();
                continue;
            }
        }
        assertTrue("未打开图片", isExistById(ReaderUtil.PAPER_PICTURE_SAVE, 20000));

        //验证图片左右滑动
        String number_before = this.getTextById(ReaderUtil.PAPER_PICTURE_NUMBER);//滑动之前
        System.out.println("number_before:" + number_before);
        this.swipeLeftList(50);//左滑
        String number_left = this.getTextById(ReaderUtil.PAPER_PICTURE_NUMBER);//左滑动之后
        System.out.println("number_left:" + number_left);
        assertTrue("左滑不成功", number_left.contains("2/"));
        this.swipeRightList(50);//右滑
        String number_right = this.getTextById(ReaderUtil.PAPER_PICTURE_NUMBER);//右滑动之后
        System.out.println("number_right:" + number_right);
        assertTrue("右滑不成功", number_right.contains("1/"));
    }


    @Priority(Priority.P1)
    @Steps("点击底栏保存按钮")
    @Expectation("可成功保存当前图片至系统路径sdcard\\download\\photo")
    public void test051SavePicture() throws AutoException, IOException{
        this.deleteFile("Download");//清除已有图片
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterHotList();


        //定位到有图片的文章,并点击进入

        this.clickPicture(VERSIONS);
//		List<Element> content_numbers = this.findElementsById(ReaderUtil.CONTENT_NAME);
//		for (int i = 0; i < content_numbers.size(); i++) {
//			content_numbers.get(i).click();
//		if (this.isExistByClassName(ReaderUtil.PAPER_IMAGE) ) {//寻找图片
//			int centerY = this.findElement(By.byClassName(ReaderUtil.PAPER_IMAGE)).getBounds().centerY();
//			int bottomY = this.getDisplayHeight() / 8 * 7;
//			int topY = this.getDisplayHeight() / 8 ;
//			if ( !(centerY < bottomY  && centerY > topY)) {//不在在可点击范围内，返回
//				System.out.println("不在点击范围内，退出重新下一个点击");
//			    this.pressBack();
//				continue;
//			}
//
//			this.clickByElement(By.byClassName(ReaderUtil.PAPER_IMAGE));
//			break;
//		}
//		else {
//			this.pressBack();
//			continue;
//		 }
//	     	}

        this.click(ReaderUtil.PAPER_PICTURE_SAVE);
        assertTrue("没有成功保存", this.isExistPicture());

    }


    @Priority(Priority.P2)
    @Steps("点击图片以及图片以外区域")
    @Expectation("关闭图片浏览器，返回文章详情页面")
    public void test052ClosePicture() throws AutoException, IOException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterHotList();
        this.clickPicture(VERSIONS);
        assertTrue("未打开图片", isExistById(ReaderUtil.PAPER_PICTURE_SAVE, 20000));
        this.click(ReaderUtil.PAPER_PICTURE_NUMBER);
        assertTrue("未关闭图片", isExistById(ReaderUtil.PAPER_COLLECT, 20000));

    }


    @Priority(Priority.P2)
    @Steps("点击顶栏返回按钮")
    @Expectation("关闭图片浏览器，返回文章详情页面")
    public void test053BackPicture() throws AutoException, IOException{
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterHotList();
        this.clickPicture(VERSIONS);
        assertTrue("未打开图片", isExistById(ReaderUtil.PAPER_PICTURE_SAVE, 20000));
        this.click(ReaderUtil.PAPER_PICTURE_BACK);
        assertTrue("未关闭图片", isExistById(ReaderUtil.PAPER_COLLECT, 20000));
    }


    @Priority(Priority.P1)
    @Steps("收藏文章1.在文章详情页面点击低栏收藏按钮2.进入已经收藏文章，再次点击低栏收藏按钮")
    @Expectation("1.成功收藏该篇文章2.可成功取消收藏该篇文章")
    public void test054CollectAndRemove() throws AutoException {
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPersonCenterAndClick("收藏");
        this.deleteCollect();
        this.backHotListFromCollect();

        //验证收藏
        String collect_name = this.getTextById(ReaderUtil.CONTENT_NAME);//获得收藏的文章名
        this.click(ReaderUtil.CONTENT_NAME);
        this.isExistById(ReaderUtil.PAPER_COLLECT, TIME);
        this.click(ReaderUtil.PAPER_COLLECT);//点击收藏
        this.backCollectFromPaper();
        String collectAfter_name = this.getTextById(ReaderUtil.RESERVE_TITLE);
        assertTrue("未收藏成功", collectAfter_name.equals(collect_name));

        //验证取消收藏
        this.backHotListFromCollect();
        this.click(ReaderUtil.CONTENT_NAME);
        this.isExistById(ReaderUtil.PAPER_REMOVE, TIME);
        if(isExistById(ReaderUtil.PAPER_COLLECT)){
            this.click(ReaderUtil.PAPER_COLLECT);//点击取消收藏
        }
        else {
            this.click(ReaderUtil.PAPER_REMOVE);//点击取消收藏
        }


        this.backCollectFromPaper();
        assertTrue("取消收藏失败", !isExistById(ReaderUtil.RESERVE_TITLE, 5000));

    }


    @Priority(Priority.P1)
    @Steps("点击文章详情页面右下角更多按钮")
    @Expectation("可成功弹出文章详情页面样式菜单")
    public void test055ClickMore() throws AutoException {
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.clickMore("");
        assertTrue("没有分享", isExistByText("分享"));
        assertTrue("没有夜间模式", isExistByText("夜间模式"));
        assertTrue("没有字号选项", isExistByText("字号"));
    }


    @Priority(Priority.P2)
    @Steps("点击样式菜单中分享按钮1.通过邮件分享")
    @Expectation("可成功分享，分享内容包括文章标题与文章链接")
    public void test056EmailShare() throws AutoException {
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.clickMore("分享");
        this.isExistByText("分享", TIME);
        this.clickByText("邮件");
        assertTrue("未跳转邮件", isExistById(ReaderUtil.SHARE_EMAIL, TIME));
    }


    @Priority(Priority.P2)
    @Steps("点击样式菜单中分享按钮2.通过信息分享")
    @Expectation("可成功分享，分享内容包括文章标题与文章链接")
    public void test057MessageShare() throws AutoException {
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.clickMore("分享");
        this.isExistByText("分享", TIME);
        this.clickByText("新信息");
        assertTrue("未跳转新信息", isExistById(ReaderUtil.SHARE_MMS, TIME));
    }


    @Priority(Priority.P2)
    @Steps("点击样式菜单中分享按钮3.通过蓝牙分享")
    @Expectation("可成功分享，分享内容包括文章标题与文章链接")
    public void test058BluetoothShare() throws AutoException {
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.clickMore("分享");
        this.isExistByText("分享", TIME);
        this.clickByText("蓝牙");
        assertTrue("未跳转蓝牙", isExistById(ReaderUtil.SHARE_BLUETOOTH, TIME));
    }


    @Priority(Priority.P2)
    @Steps("点击样式菜单中分享按钮4.通过便签分享")
    @Expectation("可成功分享，分享内容包括文章标题与文章链接")
    public void test059NotepaperShare() throws AutoException {
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.clickMore("分享");
        this.isExistByText("分享", TIME);
        this.clickByText("便签");
        assertTrue("未跳转便签", isExistById(ReaderUtil.SHARE_NOTEPAPER, TIME));
    }


    @Priority(Priority.P2)
    @Steps("点击样式菜单中分享按钮5.通过内置微博分享")
    @Expectation("可成功分享，分享内容包括文章标题与文章链接")
    public void test060WeiboShare() throws AutoException {
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.clickMore("分享");
        this.isExistByText("分享", TIME);
        if (!isExistByText("微博")) {
            this.clickByText("更多");
            this.findElement(By.byId(ReaderUtil.MORE_LIST)).toVerticalList().searchByText("微博", 30000);
        }
        this.clickByText("微博");
        if (isExistById(ReaderUtil.SHARE_WEIBO, 25000)) {
            this.clickByText("确定");
        }
        boolean result = isExistById(ReaderUtil.WEIBO_LOGIN, TIME) || isExistByText("转发到微博");
        this.exitApp(AppInfo.PACKAGE_WEIBO);
        assertTrue("未跳转微博", result );
    }


    @Priority(Priority.P2)
    @Steps("点击样式菜单中分享按钮6.通过Flyme信息分享")
    @Expectation("可成功分享，分享内容包括文章标题与文章链接")
    public void test061FlymeMssShare() throws AutoException {
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPaper();
        this.clickMore("分享");
        this.isExistByText("分享", TIME);
        this.clickByText("网络信息");
        assertTrue("未跳转网络信息", isExistById(ReaderUtil.SHARE_FLRME_MSS, TIME));
    }


    //---------------------------个人中心----------------------------------------------------------------------


    @Priority(Priority.P1)
    @Steps("在首页点击顶栏个人中心按钮")
    @Expectation("进入个人中心页面")
    public void test062EnterPersonCenter() throws AutoException {
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.click(ReaderUtil.CENTER_ACTION);
        assertTrue("账户不存在", isExistById(ReaderUtil.CENTER_ICON_HEAD));
        assertTrue("收藏不存在", isCenterModule(0, "收藏"));
        assertTrue("消息不存在", isCenterModule(1, "消息"));
        assertTrue("离线下载不存在", isCenterModule(2, "离线下载"));
        assertTrue("设置不存在", isCenterModule(3, "设置"));
    }


    @Priority(Priority.P1)
    @Steps("1.点击收藏2.在收藏夹页面点击已收藏文章3.在收藏夹页面点击进入已收藏文章，取消收藏")
    @Expectation("1.进入收藏夹页面2.进入相应文章详情页面3.返回收藏夹页面后，自动清除相应文章")
    public void test063ClickCollect() throws AutoException, RemoteException {
        //登陆账户
        if (!isLoginAccount()) {
            this.pub_login("testtool", "appadmin111");
        }
        this.exitApp(AppInfo.PACKAGE_ACCOUNT);
        this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
        this.enterPersonCenterAndClick("收藏");
        if (isExistByText("请点击登录")) {
            this.pressBack();
            this.sleep(3000);
            this.clickByText("消息");
        }
        assertTrue("未跳转收藏", isExistByText("暂无收藏") || isExistById(ReaderUtil.RESERVE_TITLE));//验证进入收藏夹页面

        if (isExistByText("暂无收藏")) {//无收藏时收藏新的文章
            this.exitReader();
            this.startApp(AppInfo.PACKAGE_READER, AppInfo.ACTIVITY_READER);
            this.waitForElement(ReaderUtil.HOME_IMG, TIME);//等待轮播banner加载
            this.enterPaper();
            this.click(ReaderUtil.PAPER_COLLECT);//点击收藏
            this.pressBack();
            this.sleep(500);
            this.pressBack();
            this.enterPersonCenterAndClick("收藏");
        }

        String collect_name = this.getTextById(ReaderUtil.RESERVE_TITLE);//获取文章名称
        this.click(ReaderUtil.RESERVE_TITLE);
        assertTrue("未正确跳转文章", isExistByDescContains(collect_name, TIME));//验证在收藏夹页面点击已收藏文章


        if(isExistById(ReaderUtil.PAPER_COLLECT)){
            this.click(ReaderUtil.PAPER_COLLECT);//点击取消收藏
        }
        else {
            this.click(ReaderUtil.PAPER_REMOVE);
        }

        this.pressBack();
        if (isExistByText("暂无收藏")) {
            assertTrue(true);
        }
        else {
            boolean isexists = this.searchText(this.findElement(ReaderUtil.ANDROID_LIST),
                    collect_name, 30000, true);//验证.在收藏夹页面点击进入已收藏文章，取消收藏
            assertTrue("未成功取消", !isexists);
        }
    }
}
