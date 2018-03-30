package com.example.s1.Utils;

import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maxiuyu on 2018/3/28.
 */
public class MyOkHttp {
    public static void login4m3()
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
                    .add("Ecom_User_ID","1552239")
                    //.add("Ecom_Password","010641")
                    .add("Ecom_Password","louishw")
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

            ArrayList<ArrayList<String> >table=new ArrayList<>();
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
//            String RelayState=url2.substring(url2.indexOf("RelayState"),url2.indexOf("&SigAlg"));
//            System.out.println("RelayState:"+RelayState);
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
            // System.out.println(ht4);



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
}
