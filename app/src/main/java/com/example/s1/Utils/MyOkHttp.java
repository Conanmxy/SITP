package com.example.s1.Utils;

import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maxiuyu on 2018/3/28.
 */
public class MyOkHttp {

    public static ArrayList<ArrayList<String> >  login4m3(String userName,String passsword)
    {
        ArrayList<ArrayList<String> >table=new ArrayList<>();
        try
        {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            });

            OkHttpClient client=builder.build();


            String url1="http://4m3.tongji.edu.cn/eams/samlCheck";
            Request request1=new Request.Builder()
                    .url(url1)
                    .build();
            Response response1=client.newCall(request1).execute();
            String ht1=response1.body().string();
            // System.out.println(ht1);

            String url2=ht1.substring(ht1.indexOf("https"),ht1.indexOf("\"></head>"));
            //得到两个重要参数SAMLRquest,RelayState;
            String SAMLRequest=url2.substring(url2.indexOf("SAMLR"),url2.indexOf('&'));
            System.out.println("SAMLRquest:"+SAMLRequest);
            String RelayState=url2.substring(url2.indexOf("RelayState"),url2.indexOf("&SigAlg"));
            System.out.println("RelayState:"+RelayState);
            Request request2=new Request.Builder()
                    .url(url2)
                    .build();
            Response response2=client.newCall(request2).execute();
            String ht2=response2.body().string();
            //System.out.println(ht2);


            String url3="https://ids.tongji.edu.cn:8443/nidp/saml2/sso?id=4996&sid=0&option=credential&sid=0";
            RequestBody requestBody3=new FormBody.Builder()
                    .add("?","")
                    .build();
            Request request3=new Request.Builder()
                    .url(url3)
                    .post(requestBody3)
                    .build();
            Response response3=client.newCall(request3).execute();
            String ht3=response3.body().string();
            // System.out.println(ht3);

            String url4="https://ids.tongji.edu.cn:8443/nidp/saml2/sso?sid=0&sid=0";
            RequestBody requestBody4=new FormBody.Builder()
                    .add("Ecom_User_ID",userName)
                    //.add("Ecom_Password","010641")
                    .add("Ecom_Password",passsword)
                    .add("option","credential")
                    .add("submit","登录")
                    .build();
            Request request4=new Request.Builder()
                    .url(url4)
                    .post(requestBody4)
                    .build();
            Response response4=client.newCall(request4).execute();
            String ht4=response4.body().string();
            //System.out.println(ht4);



            String url5="https://ids.tongji.edu.cn:8443/nidp/saml2/sso?sid=0";
            Request request5=new Request.Builder()
                    .url(url5)
                    .build();
            Response response5=client.newCall(request5).execute();
            String ht5=response5.body().string();
            // System.out.println(ht5);

            String dest = "";
            if (ht5!=null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(ht5);
                dest = m.replaceAll("");
            }
            System.out.println(dest);
            String SAMLResponse=dest.substring(dest.indexOf("value")+7,dest.lastIndexOf("\"/><input"));
            System.out.println(SAMLResponse);
            String RelayState2=dest.substring(dest.lastIndexOf("value")+7,dest.indexOf("\"/></form>"));
            System.out.println(RelayState2);



            String url6="http://4m3.tongji.edu.cn/eams/saml/SAMLAssertionConsumer";
            RequestBody requestBody6=new FormBody.Builder()
                    .add("RelayState",RelayState2)
                    .add("SAMLResponse",SAMLResponse)
                    .build();
            Request request6=new Request.Builder()
                    .url(url6)
                    .post(requestBody6)
                    .build();
            Response response6=client.newCall(request6).execute();
            String ht6=response6.body().string();
            // System.out.println("ht6"+ht6);


            String url7="http://4m3.tongji.edu.cn/eams/samlCheck";
            Request request7=new Request.Builder()
                    .url(url7)
                    .build();
//            Response response7=client.newCall(request7).execute();
//            String ht7=response7.body().string();
//            //System.out.println("ht7"+ht7);
//
//
//            String url8="http://4m3.tongji.edu.cn/eams/home.action";
//            Request request8=new Request.Builder()
//                    .url(url8)
//                    .build();
//            Response response8=client.newCall(request8).execute();
//            String ht8=response8.body().string();
            //System.out.println("ht8"+ht8);



            //爬取主页
            String url9="http://4m3.tongji.edu.cn/eams/home!submenus.action?menu.id=";
            RequestBody requestBody9=new FormBody.Builder()
                    .build();
            Request request9=new Request.Builder()
                    .url(url9)
                    .post(requestBody9)
                    .build();
            Response response9=client.newCall(request9).execute();
            String ht9=response9.body().string();
            System.out.println("ht9"+ht9);



            //获取ids
            String urlIds="http://4m3.tongji.edu.cn/eams/courseTableForStd.action?_=1522307078031";
            Request requestIds=new Request.Builder()
                    .url(urlIds)
                    .build();
            Response responseIds=client.newCall(requestIds).execute();
            String htIds=responseIds.body().string();
            System.out.println("htIds"+htIds);

            String Ids=htIds.substring(htIds.indexOf("8832"),htIds.indexOf("8832")+9);
            System.out.println(Ids);

            //爬取课程表
            String urlSchedule="http://4m3.tongji.edu.cn/eams/courseTableForStd!courseTable.action";
            //  "http://4m3.tongji.edu.cn/eams/courseTableForStd!courseTable.action"
            RequestBody requestBody10=new FormBody.Builder()
                    .add("ignoreHead","1")
                    .add("startWeek","1")
                    .add("semester.id","105")
                    .add("ids",Ids)
                    .add("setting.kind","std")
                    .build();
            Request request10=new Request.Builder()
                    .url(urlSchedule)
                    .post(requestBody10)
                    .build();
            Response response10=client.newCall(request10).execute();
            String htSc=response10.body().string();
            System.out.println("htSc"+htSc);





            String input=htSc;
            Document doc= Jsoup.parse(input);
            Elements tbodys=doc.select("div");
            String tbody0=tbodys.get(0).html();
            //System.out.println(tbody0);

            Document doc2= Jsoup.parse(tbody0);
            Elements tds=doc2.select("td");
            //System.out.println(tds.size());

            int i=0;
            while(i<tds.size())
            {
                ArrayList<String>t=new ArrayList<>();
                for(int j=0;j<11;j++)
                {
                    t.add(tds.get(i).text());
                    System.out.print(tds.get(i).text());
                    i++;
                }
                System.out.println();
                table.add(t);
            }


        }catch (IOException e)
        {
            e.printStackTrace();
        }

        return table;

    }

//    public static void main(String []args)
//    {
//        loginXuanke();
//        //parseScore("dd");
//    }
    public static ArrayList<ArrayList<String>> loginXuanke(String userName,String password)
    {
        try
        {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            });

            OkHttpClient client=builder.build();


            String url1="http://xuanke.tongji.edu.cn:443/oiosaml/saml/login";
            Request request1=new Request.Builder()
                    .url(url1)
                    .build();
            Response response1=client.newCall(request1).execute();
            String ht1=response1.body().string();
            //System.out.println(ht1);

            String url2=ht1.substring(ht1.indexOf("https"),ht1.indexOf("\"></head>"));
            //得到两个重要参数SAMLRquest,RelayState;
            String SAMLRequest=url2.substring(url2.indexOf("SAMLR"),url2.indexOf('&'));
            System.out.println("SAMLRquest:"+SAMLRequest);
            Request request2=new Request.Builder()
                    .url(url2)
                    .build();
            Response response2=client.newCall(request2).execute();
            String ht2=response2.body().string();
            //System.out.println(ht2);


            String url3="https://ids.tongji.edu.cn:8443/nidp/saml2/sso?id=4996&sid=0&option=credential&sid=0";
            RequestBody requestBody3=new FormBody.Builder()
                    .add("?","")
                    .build();
            Request request3=new Request.Builder()
                    .url(url3)
                    .post(requestBody3)
                    .build();
            Response response3=client.newCall(request3).execute();
            String ht3=response3.body().string();
            //System.out.println(ht3);

            String url4="https://ids.tongji.edu.cn:8443/nidp/saml2/sso?sid=0&sid=0";
            RequestBody requestBody4=new FormBody.Builder()
                    .add("Ecom_User_ID",userName)
                    .add("Ecom_Password",password)
                    .add("option","credential")
                    .add("submit","登录")
                    .build();
            Request request4=new Request.Builder()
                    .url(url4)
                    .post(requestBody4)
                    .build();
            Response response4=client.newCall(request4).execute();
            String ht4=response4.body().string();
            System.out.println("ht4"+ht4+"cc");
            if(ht4.contains("如有问题请拨打电话"))
            {
                return null;
            }


            String url5="https://ids.tongji.edu.cn:8443/nidp/saml2/sso?sid=0";
            Request request5=new Request.Builder()
                    .url(url5)
                    .build();
            Response response5=client.newCall(request5).execute();
            String ht5=response5.body().string();
            System.out.println(ht5);

            String dest = "";
            if (ht5!=null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(ht5);
                dest = m.replaceAll("");
            }
            //System.out.println(dest);
            String SAMLResponse=dest.substring(dest.indexOf("value")+7,dest.lastIndexOf("\"/></form"));


            String url6="http://xuanke.tongji.edu.cn:443/oiosaml/saml/SAMLAssertionConsumer";
            RequestBody requestBody6=new FormBody.Builder()
                    // .add("RelayState",RelayState2)
                    .add("SAMLResponse",SAMLResponse)
                    .build();
            Request request6=new Request.Builder()
                    .url(url6)
                    .post(requestBody6)
                    .build();
            Response response6=client.newCall(request6).execute();
            String ht6=response6.body().string();
            //System.out.println("ht6"+ht6);


            String url7="http://xuanke.tongji.edu.cn/tj_login/redirect.jsp?link=/tj_xuankexjgl/score" +
                    "/query/student/cjcx.jsp?qxid=20051013779916$mkid=20051013779901&qxid=20051013779916&HELP_URL=null&MYXSJL=null";
            Request request7=new Request.Builder()
                    .url(url7)
                    .build();
            Response response7=client.newCall(request7).execute();
            String ht7=response7.body().string();
            //System.out.println("ht7"+ht7);

           return parseScore(ht7);



        }catch (IOException e)
        {
            e.printStackTrace();
        }

         return null;

    }

    public static ArrayList<ArrayList<String>> parseScore(String input)
    {
        //需要返回的数据
        ArrayList<ArrayList<String> >allScore=new ArrayList<>();
        String studentInfo;
        String scoreTotal;


        Document doc= Jsoup.parse(input);
        Elements tdScores=doc.select("td");
        studentInfo=tdScores.get(1).text();
        scoreTotal=tdScores.get(2).text();
        ArrayList<String>t=new ArrayList<>();
        t.add(studentInfo);
        allScore.add(t);
        t=new ArrayList<>();
        t.add(scoreTotal);
        allScore.add(t);
        //System.out.println(studentInfo);
        int i=3;
        while (i<tdScores.size())
        {
            String temp=tdScores.get(i).text();
            if(temp.contains("学年"))
            {
                ArrayList<String>ta=new ArrayList<>();

                ta.add(temp);
                allScore.add(ta);
                i++;
            }
            else if(temp.contains("本学期"))
            {
                ArrayList<String>ta=new ArrayList<>();
                ta.add(temp);
                allScore.add(ta);
                i++;
            }
            else
            {
                ArrayList<String>tb=new ArrayList<>();
                for(int j=0;j<9;j++)
                {
                    String tempb=tdScores.get(i).text();
                    tb.add(tempb);
                    i++;
                }
                allScore.add(tb);
            }

        }


//        for(int k=0;k<allScore.size();k++)
//        {
//            for(int k1=0;k1<allScore.get(k).size();k1++)
//            {
//                //System.out.print(allScore.get(k).get(k1)+"  ");
//            }
//           //System.out.println();
//        }

        return allScore;
    }

    public static ArrayList<String> loginLib(String userName,String password,String bookName)
    {
        ArrayList<String> booksList=new ArrayList<>();
        try
        {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            });
            OkHttpClient client=builder
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10,TimeUnit.SECONDS)
                    .writeTimeout(10,TimeUnit.SECONDS)
                    .build();
//密码登录教务处
//
//            String url1="http://lib.tongji.edu.cn/sfrz/index.aspx?sp=" +
//                    "http://webpac.lib.tongji.edu.cn/reader/self_login.php?" +
//                    "returnUrl=&a=a&goto=webpac&idp=a&type=huiwen";
//            Request request1=new Request.Builder()
//                    .url(url1)
//                    .build();
//            Response response1=client.newCall(request1).execute();
//            String ht1=response1.body().string();
//             System.out.println("ht1"+ht1);
//
//
//            String url2=ht1.substring(ht1.indexOf("https"),ht1.indexOf("\"></head>"));
//            //得到两个重要参数SAMLRquest,RelayState;
//            String SAMLRequest=url2.substring(url2.indexOf("SAMLR"),url2.indexOf('&'));
//            System.out.println("SAMLRquest:"+SAMLRequest);
//
//            Request request2=new Request.Builder()
//                    .url(url2)
//                    .build();
//            Response response2=client.newCall(request2).execute();
//            String ht2=response2.body().string();
//            System.out.println(ht2);
//
//
//
//            String url3="https://ids.tongji.edu.cn:8443/nidp/saml2/sso?id=4996&sid=0&option=credential&sid=0";
//            RequestBody requestBody3=new FormBody.Builder()
//                    .add("?","")
//                    .build();
//            Request request3=new Request.Builder()
//                    .url(url3)
//                    .post(requestBody3)
//                    .build();
//            Response response3=client.newCall(request3).execute();
//            String ht3=response3.body().string();
//             System.out.println(ht3);
//
//
//            String url4="https://ids.tongji.edu.cn:8443/nidp/saml2/sso?sid=0&sid=0";
//            RequestBody requestBody4=new FormBody.Builder()
//                    .add("Ecom_User_ID",userName)
//                    //.add("Ecom_Password","010641")
//                    .add("Ecom_Password",password)
//                    .add("option","credential")
//                    .add("submit","登录")
//                    .build();
//            Request request4=new Request.Builder()
//                    .url(url4)
//                    .post(requestBody4)
//                    .build();
//            Response response4=client.newCall(request4).execute();
//            String ht4=response4.body().string();
//            System.out.println("ht4"+ht4);
//
//            String url5="https://ids.tongji.edu.cn:8443/nidp/saml2/sso?sid=0";
//            Request request5=new Request.Builder()
//                    .url(url5)
//                    .build();
//            Response response5=client.newCall(request5).execute();
//            String ht5=response5.body().string();
//             System.out.println("ht5"+ht5);
//
//            String dest = "";
//            if (ht5!=null) {
//                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//                Matcher m = p.matcher(ht5);
//                dest = m.replaceAll("");
//            }
//            System.out.println(dest);
//            String SAMLResponse=dest.substring(dest.indexOf("value")+7,dest.lastIndexOf("\"/></form"));
//            System.out.println("samlresponse:"+SAMLResponse);
//
//
//            String url6="http://lib.tongji.edu.cn/sfrz/SAML2/servlet/TestSaml";
//            RequestBody requestBody6=new FormBody.Builder()
//                    .add("SAMLResponse",SAMLResponse)
//                    .build();
//            Request request6=new Request.Builder()
//                    .url(url6)
//                    .post(requestBody6)
//                    .build();
//            Response response6=client.newCall(request6).execute();
//            String ht6=response6.body().string();
//             System.out.println("ht6"+ht6);
//
//
//             //拿到tokenId;
//            String tokenId="";
//            tokenId=ht6.substring(ht6.indexOf("value")+7,ht6.indexOf("\"/>"));
//            System.out.println(tokenId);
//
////            String url9="http://lib.tongji.edu.cn/sfrz/return.aspx";
////            RequestBody requestBody9=new FormBody.Builder()
////                    .add("tokenID",tokenId)
////                    .build();
////            Request request9=new Request.Builder()
////                    .url(url9)
////                    .post(requestBody9)
////                    .build();
////            Response response9=client.newCall(request9).execute();
////            String ht9=response9.body().string();
////            System.out.println("ht9"+ht9);
//
//
//
//            String url7="http://webpac.lib.tongji.edu.cn/reader/self_login.php?returnUrl=&goto=webpac&artifact=" +
//                    userName + "&idp=a";
//
//            Request request7=new Request.Builder()
//                    .url(url7)
//                    .build();
//            Response response7=client.newCall(request7).execute();
//            String ht7=response7.body().toString();
//            System.out.println("ht7"+ht7);
//
//            String url8="http://webpac.lib.tongji.edu.cn/reader/redr_info.php";
//            Request request8=new Request.Builder()
//                    .url(url8)
//                    .build();
//            Response response8=client.newCall(request8).execute();
//            String ht8=response8.body().string();
//            System.out.println("ht8"+ht8);



            //查询图书
            String queryBookName=bookName;
            String urlBooks="http://webpac.lib.tongji.edu.cn/opac/openlink.php?" +
                    "strSearchType=title" +
                    "&match_flag=forward" +
                    "&historyCount=1" +
                    "&strText=" +
                    URLEncoder.encode(queryBookName) +
                    "&doctype=ALL" +
                    "&with_ebook=on" +
                    "&displaypg=20" +
                    "&showmode=list" +
                    "&sort=CATA_DATE" +
                    "&orderby=desc" +
                    "&location=ALL";
            Request requestBooks=new Request.Builder()
                    .url(urlBooks)
                    .build();
            Response responseBooks=client.newCall(requestBooks).execute();
            String htBooks=responseBooks.body().string();
            // System.out.println("htBooks"+htBooks);


            //获得书本信息
            booksList=new ArrayList<>();
            booksList= parseBooks(htBooks);



        }catch(IOException e)
        {
            e.printStackTrace();

        }
        return booksList;

    }

    public static ArrayList<ArrayList<String> > queryAbook(String aBookNo)
    {
        ArrayList<String>aBookList=new ArrayList<>();
        ArrayList<ArrayList<String>>aBookaa=new ArrayList<>();
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            });
            OkHttpClient client = builder
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            String aBookUrl = "http://webpac.lib.tongji.edu.cn/opac/item.php?marc_no="
                    + aBookNo;
            Request requestABook = new Request.Builder()
                    .url(aBookUrl)
                    .build();
            Response responseABook = client.newCall(requestABook).execute();
            String htABook = responseABook.body().string();
            //System.out.println("htBooks"+htABook);

            //解析出每一本书的位置
            Document doc = Jsoup.parse(htABook);
            Elements elements = doc.select("tr.whitetext");
            aBookList=new ArrayList<>();

            for(int i=0;i<elements.size();i++)
            {
                aBookList.add(elements.get(i).text());
                System.out.println(elements.get(i).html());
                String str=elements.get(i).html();
                str="<table>"+str+"</table>";
                Document doc2=Jsoup.parse(str);
                Elements elements2=doc2.select("td");
                ArrayList<String>ta=new ArrayList<>();
                for(int j=0;j<elements2.size();j++)
                {
                    ta.add(elements2.get(j).text());
                    System.out.println(elements2.get(j).text());
                }
                aBookaa.add(ta);
            }


            String isbn="";
            String aBookIntro="";
            if(htABook!=null)
            {
                Pattern p = Pattern.compile("isbn=(.*?)\"");
                Matcher m = p.matcher(htABook);
                while(m.find())
                {
                    isbn=m.group(1);
                }

                //获取简介
                //去除空格换行
                String dest = "";
                Pattern p0= Pattern.compile("\\s*|\t|\r|\n");
                Matcher m0= p0.matcher(htABook);
                dest = m0.replaceAll("");

                Pattern p1 = Pattern.compile("提要文摘附注:</dt>(.*?)</dd>");
                Matcher m1= p1.matcher(dest);
                while(m1.find())
                {
                    aBookIntro=m1.group(1);
                }
                if(!aBookIntro.equals(""))
                {
                    aBookIntro=decode(aBookIntro.substring(4));
                }
                System.out.println("abbbb"+aBookIntro);

            }
            String aBookImageUrl="http://webpac.lib.tongji.edu.cn/opac/ajax_douban.php?isbn="+isbn;
            System.out.println(aBookImageUrl);
            Request requestImage = new Request.Builder()
                    .url(aBookImageUrl)
                    .build();
            Response responseImage = client.newCall(requestImage).execute();
            String htImage = responseImage.body().string();
            System.out.println("htBooks"+htImage);

            String getImageUrl="";
            if(htImage.contains("http")) {
                getImageUrl= htImage.substring(htImage.indexOf("http"), htImage.indexOf("summary") - 3);
            }
            else
            {
                getImageUrl="http://webpac.lib.tongji.edu.cn/tpl/images/nobook.jpg";
            }



            aBookList.add(getImageUrl);

            //倒数第二个存储简介
            ArrayList<String>a2=new ArrayList<>();
            a2.add(aBookIntro);
            aBookaa.add(a2);
            //aBookaa最后一个存放该书的连接
            ArrayList<String>a1=new ArrayList<>();
            a1.add(getImageUrl);
            aBookaa.add(a1);



        }catch (IOException e){
            e.printStackTrace();
        }
        return aBookaa;

    }

    public static ArrayList<String> parseBooks(String input)
    {
        String dest =input;

        Document doc=Jsoup.parse(dest);
        //System.out.print("dest"+dest);
        Elements elements=doc.select("li.book_list_info");


        ArrayList<String> booksList=new ArrayList<>();
        for(int i=0;i<elements.size();i++)
        {
            String tempStr=elements.get(i).html();
            System.out.println(tempStr);


            booksList.add(tempStr);
            System.out.println(booksList.get(i));//这里返回的是源html代码，具体数据解析在适配器实现

        }

        return booksList;

    }

    public static String decode(String str) {
        StringBuffer sb = new StringBuffer();
        int i1=0;
        int i2=0;


        while(i2<str.length()) {
            i1 = str.indexOf("&#",i2);
            if (i1 == -1 ) {
                sb.append(str.substring(i2));
                break ;
            }
            sb.append(str.substring(i2, i1));
            i2 = str.indexOf(";", i1);
            if (i2 == -1 ) {
                sb.append(str.substring(i1));
                break ;
            }


            String tok = str.substring(i1+2, i2);
            try {
                int radix = 10 ;
                if (tok.charAt(0) == 'x' || tok.charAt(0) == 'X') {
                    radix = 16 ;
                    tok = tok.substring(1);
                }
                sb.append((char) Integer.parseInt(tok, radix));
            } catch (NumberFormatException exp) {
                //sb.append(unknownCh);
            }
            i2++ ;
        }
        return sb.toString();
    }


}
