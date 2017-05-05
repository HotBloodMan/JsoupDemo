package com.ljt.jsoupdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView tvShow;
    private String title=null;

    Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x1){
                String title = (String) msg.obj;
                tvShow.setText("标题是："+ title);
            }
        }
    };
    private Document doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvShow = (TextView) findViewById(R.id.tv_show);
//        initData();
//        initData2();
        initData3();
    }

    private void initData3() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String content=null;
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://www.cnblogs.com/");
                try {
                    HttpResponse resp = client.execute(get);
                    HttpEntity entity = resp.getEntity();
                    content = EntityUtils.toString(entity, "utf-8");

                } catch (IOException e) {
                    e.printStackTrace();
                }

                doc = Jsoup.parse(content);

//                Log.d("TAG","aaa---->>>>doc "+ doc.toString());
                Elements elements = doc.getElementsByTag("title");
                Element element = elements.get(0);
                //返回元素的文本
                // <title>博客园 - 开发者的网上家园</title>
                title = element.text();
                Log.d("TAG","aaa---->>>网页标题是 title= "+ title.toString());//aaa---->>>网页标题是 title= 博客园 - 开发者的网上家园

/*
* e = <a href="/aggsite/mycommented" title="我评论过的博文">我评</a>
*  aaa--->>> e = 我评
* */
                //抓特定的标题 如<h3><a class="titlelnk" href="http://www.cnblogs.com/CloudMan6/p/6806193.html" target="_blank">镜像的分层结构 - 每天5分钟玩转容器技术（11）</a></h3>
                Elements select = doc.getElementsByTag("h3").select("a[href]");
                for(Element e:select){
                    Log.d("TAG","aaa--->>> e = "+e.toString());
                    Log.d("TAG","aaa---------------------------------------->>>>>>>>>>>> ");
                    Log.d("TAG","aaa--->>> e = "+e.text().toString());

                }
                System.out.println("cccc====>>>>----------------------------");
//                Elements h3 = doc.select("#post_item .post_item.body h3 a");
//                Log.d("TAG","aaa--->>> h3 = "+h3.get(0).toString());
//                Log.d("TAG","aaa--->>> h3 1 = "+h3.get(1).toString());
//                Log.d("TAG","aaa--->>> h3 2= "+h3.get(2).toString());
//                 Log.d("TAG","aaa---------------------------------------->>>>>>>>>>>> ");

                //拿图片,也可以拿特定的图片
                Elements imgElements=doc.select("img[src$=.png]"); // 查找扩展名为.png的图片DOM节点
                for(Element e:imgElements){
                    System.out.println(e.toString());
                }
                 /*
               * <img width="48" height="48" class="pfs" src="//pic.cnblogs.com/face/440369/20150215133556.png" alt="">
               * cccc====>>>>//pic.cnblogs.com/face/440369/20150215133556.png
               * cccc====>>>>222
               * */
                for(Element e:imgElements){
//                    System.out.println(e.toString());
//                    System.out.println("cccc====>>>>"+e.attr("src").toString());
                    //能选出特定尺寸的图片
                    System.out.println("cccc====>>>>333 "+e.select("[width=48]").select("[height=48]").attr("src").toString());
//                    System.out.println("cccc====>>>>222"+e.getElementsByTag("src").text());
                }
                System.out.println("-----------------------------------------------------------------");
                //抓文本
                /*
                *  cccc====>>>>333---->>p <p class="post_item_summary"> <a href="http://www.cnblogs.com/guanmanman/"
                 *  target="_blank"><img width="48" height="48" class="pfs"
                 *  src="//pic.cnblogs.com/face/955290/20160512181121.png"
                 *  alt=""></a>
                  *  JNI：Java Native Interface 也就是java本地接口，它是一个协议，这个协议用来沟通java代码和本地代码(c/c++)。通过这个协议，Java类的某些方法可以使用原生实现，同时让它们可以像普通的Java方法一样被调用和使用，而原生方法也可以使用Java对象，调用和使用Java... ... </p>
                *
                *
                *
                * */
//                String p = doc.getElementsByTag("p").select("[class=post_item_summary]").toString();
//                System.out.println("cccc====>>>>333---->>p "+p.toString());
                //只能抓到第一个文本
//                String p1 = doc.getElementsByTag("p").select("[class=post_item_summary]").text();
//                System.out.println("cccc====>>>>333---->>p1 "+p1.toString());
               //这个能抓一个集合的文本。
                Elements p = doc.getElementsByTag("p").select("[class=post_item_summary]");
                for(Element s:p){
                    System.out.println("cccc====>>>>333---->> sss "+s.text().toString());
                }


            }

        }.start();

    }

    //參考網站：http://www.jianshu.com/p/7d658636764a  2：http://www.open-open.com/jsoup/
    private void initData2() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect("http://www.jcodecraeer.com/plus/list.php?tid=1&&TotalResult=1415&PageNo=1").get();
                    Log.d("TAG","aaa--->>>doc:"+doc.toString());
                    Elements links = doc.select("div.archive-detail");
                    Log.d("TAG","aaa--->>>links:"+links.size());
                    for(Element e:links) {
                        Log.d("TAG", "aaa--->>>詳情鏈接:" +e.select("a").attr("href"));
                        Log.d("TAG", "aaa--->>>地址:" + e.select("a").attr("title"));
                        Log.d("TAG", "aaa--->>>地址:" + e.select("img").attr("src"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String content=null;
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://www.cnblogs.com/");
                try {
                    HttpResponse resp = client.execute(get);
                    HttpEntity entity = resp.getEntity();
                    content = EntityUtils.toString(entity, "utf-8");

                } catch (IOException e) {
                    e.printStackTrace();
                }

                doc = Jsoup.parse(content);

                Log.d("TAG","aaa---->>>>doc "+ doc.toString());
                Elements elements = doc.getElementsByTag("title");
                Element element = elements.get(0);
                //返回元素的文本
                // <title>博客园 - 开发者的网上家园</title>
                title = element.text();
                Log.d("TAG","aaa---->>>网页标题是 title= "+ title.toString());//aaa---->>>网页标题是 title= 博客园 - 开发者的网上家园

                //<div id="site_nav_top">代码改变世界</div>
                //aaa--->>> navTop= 代码改变世界
                Element element2 = doc.getElementById("site_nav_top");
                String navTop = element2.text();
                Log.d("TAG","aaa--->>> navTop= "+navTop);

                /*
                * <ul class="post_nav_block">
                        <li><a href="/"  class="current_nav" >首页</a></li>
                        <li><a href="/pick/"   title="编辑精选博文">精华</a></li>
                        <li><a href="/candidate/"   title="候选区的博文">候选</a></li>
                        <li><a href="/news/"   title="新闻频道最新新闻">新闻</a></li>
                        <li><a href="/following"   title="我关注博客的最新博文">关注</a></li>
                        <li><a href="/aggsite/mycommented"   title="我评论过的博文">我评</a></li>
                        <li><a href="/aggsite/mydigged"   title="我推荐过的博文">我赞</a></li>
                </ul>

                *
                *
                *
                * aaa--->>> element1= <ul class="post_nav_block">
                                        <li><a href="/" class="current_nav">首页</a></li>
                                        <li><a href="/pick/" title="编辑精选博文">精华</a></li>
                                        <li><a href="/candidate/" title="候选区的博文">候选</a></li>
                                        <li><a href="/news/" title="新闻频道最新新闻">新闻</a></li>
                                        <li><a href="/following" title="我关注博客的最新博文">关注</a></li>
                                        <li><a href="/aggsite/mycommented" title="我评论过的博文">我评</a></li>
                                        <li><a href="/aggsite/mydigged" title="我推荐过的博文">我赞</a></li>
                                       </ul>
                *
                * */
                Elements urls = doc.getElementsByClass("post_nav_block");
                for(int i=0;i<urls.size();i++){
                    Element element1 = urls.get(i);
                    Log.d("TAG","aaa--->>> element1= "+element1);
                    Elements titles = element1.getElementsByAttribute("href");
                    Log.d("TAG","aaa--->>> titles= "+titles.get(0));
                    Elements titlesyy = element1.getElementsByAttribute("title");
                    Log.d("TAG","aaa--->>> titlesyy= "+titlesyy.get(0));
//                    Elements hrefs = element1.getElementsByTag("href");
//                    Log.d("TAG","aaa--->>> hrefs= "+hrefs.get(0));
                    for(int j=0;j<titlesyy.size();j++){
                        Element titleTT = titlesyy.get(j);
                        //titleTT= <a href="/pick/" title="编辑精选博文">精华</a>
                        Log.d("TAG","aaa--->>> titleTT= "+titleTT);
                        //a = 编辑精选博文
                        String a = titlesyy.get(j).select("a").attr("title");
                        Log.d("TAG","aaa--->>> a = "+a);
                        //text = 精华
                        String text = titlesyy.get(j).text();
                        Log.d("TAG","aaa--->>> text = "+text.toString());

                    }

                }
                Message message = new Message();
                message.obj=title;
                message.what=0x1;
                h.sendMessage(message);
            }

        }.start();
    }
}
