package com.example.haozai.jen.Utils;

/**
 * Created by haozai on 2018-06-14.
 */

import com.example.haozai.jen.Car.ShoppingCartBean;
import com.example.haozai.jen.Tool.IpAddress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebServicePost {
    private static String IP = IpAddress.IP;
    //private static String IP = "10.23.32.100:8080";

    // 通过 POST 方式获取HTTP服务器数据
    public static String executeHttpPost(String username, String password) {

        try {
            String path = "http://" + IP + "/haozai/LogLet";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);

            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String UpdateAddress(String ming,String pnumber,String area,String detail,String sid) {

        try {
            String path = "http://" + IP + "/haozai/UpdateAddress";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("ming", ming);
            params.put("pnumber",pnumber);
            params.put("area",area);
            params.put("detail",detail);
            params.put("sid",sid);

            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String AddAddress(String ming,String pnumber,String area,String detail,String uname,String keynum) {

        try {
            String path = "http://" + IP + "/haozai/AddAddress";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("ming", ming);
            params.put("pnumber",pnumber);
            params.put("area",area);
            params.put("detail",detail);
            params.put("uname",uname);
            params.put("keynum",keynum);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String CheckPswd(String username, String paywd) {

        try {
            String path = "http://" + IP + "/haozai/CheckPswd";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("paywd",paywd);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String BuyPay(String username, String paywd,String price) {

        try {
            String path = "http://" + IP + "/haozai/BuyPay";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("pswd",paywd);
            params.put("price",price);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String SendDaiFu( String username, String bookimg, String bookname, String author ,String press, String price,String mount,
                                    String zonge, String yunfei, String ordering) {

        try {
            String path = "http://" + IP + "/haozai/DaiFu";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("bookimg",bookimg);
            params.put("bookname",bookname);
            params.put("bookauthor",author);
            params.put("bookpress",press);
            params.put("bookprice",price);
            params.put("bookmount",mount);
            params.put("bookzonge",zonge);
            params.put("yunfei",yunfei);
            params.put("order",ordering);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String SendSuccess( String username, String bookimg, String bookname, String author ,String press, String price,String mount,
                                    String zonge, String yunfei, String ordering) {

        try {
            String path = "http://" + IP + "/haozai/SendSuccess";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("bookimg",bookimg);
            params.put("bookname",bookname);
            params.put("bookauthor",author);
            params.put("bookpress",press);
            params.put("bookprice",price);
            params.put("bookmount",mount);
            params.put("bookzonge",zonge);
            params.put("yunfei",yunfei);
            params.put("order",ordering);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String Orderlist(String username,String ordering) {

        try {
            String path = "http://" + IP + "/haozai/Orderlist";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("ordering", ordering);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String AllOrderlist(String username) {

        try {
            String path = "http://" + IP + "/haozai/AllOrderlist";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String checkall(String username,String ordering) {

        try {
            String path = "http://" + IP + "/haozai/CheckAll";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("ordering", ordering);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String CancelOrder(String sid) {

        try {
            String path = "http://" + IP + "/haozai/CancelOrder";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("sid", sid);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String ChangeOrder(String sid,String ordering) {

        try {
            String path = "http://" + IP + "/haozai/ChangeOrder";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("sid", sid);
            params.put("ordering", ordering);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String AddCar(String booknum, String name, String author, String press, String price, String id) {
        try {
            String path = "http://" + IP + "/haozai/AddCar";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("booknum", booknum);
            params.put("name",name);
            params.put("author",author);
            params.put("price",price);
            params.put("press",press);
            params.put("id",id);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String BuyPayChangeOrder(String username, String paywd,String price,String sid,String ordering) {

        try {
            String path = "http://" + IP + "/haozai/BuyPay";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("pswd",paywd);
            params.put("price",price);
            params.put("sid",sid);
            params.put("ordering",sid);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String ShopCar(String userid) {
        try {
            String path = "http://" + IP + "/haozai/ShopCar";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", userid);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String checkcar(String id) {
        try {
            String path = "http://" + IP + "/haozai/CheckCar";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", id);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String Recommond() {
        try {
            String path = "http://" + IP + "/haozai/Recommend";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    public static String pswdexist(String id) {
        try {
            String path = "http://" + IP + "/haozai/PswdExist";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", id);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String resetPhone(String id,String newphone) {
        try {
            String path = "http://" + IP + "/haozai/setPhone";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", id);
            params.put("newphone", newphone);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String resetPswd(String id,String newpswd) {
        try {
            String path = "http://" + IP + "/haozai/setPswd";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", id);
            params.put("newpswd", newpswd);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String checkYe(String readstring) {
        try {
            String path = "http://" + IP + "/haozai/checkYe";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", readstring);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getphone(String id) {
        try {
            String path = "http://" + IP + "/haozai/getPhoneNum";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", id);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String infor(String id) {
        try {
            String path = "http://" + IP + "/haozai/Information";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", id);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String Ordering(String userid, List<ShoppingCartBean> list,String ordering) {
        try {
            String path = "http://" + IP + "/haozai/CarOrder";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", userid);
            params.put("ordering", ordering);
            params.put("num", list.size()+"");
            String num ;
            for (int i=0;i<=list.size();i++ ){
                try {
                     num = String.valueOf(i);
                    params.put(num, list.get(i).toString());
                }catch (Exception e){
                }
            }
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String deleCar(String userid, List<ShoppingCartBean> list) {
        try {
            String path = "http://" + IP + "/haozai/deleCar";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", userid);
            params.put("num", list.size()+"");
            String num ;
            for (int i=0;i<=list.size();i++ ){
                try {
                    num = String.valueOf(i);
                    params.put(num, list.get(i).getImageUrl());
                }catch (Exception e){
                }
            }
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String deleoneCar(String userid, String url) {
        try {
            String path = "http://" + IP + "/haozai/deleoneCar";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", userid);
            params.put("url", url);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String recharge(String username, String jine) {
        try {
            String path = "http://" + IP + "/haozai/recharge";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("money",jine);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String changeCar(String userid, String url, String change) {
        try {
            String path = "http://" + IP + "/haozai/booksCount";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", userid);
            params.put("url", url);
            params.put("change",change);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String checkname(String name) {
        try {
            String path = "http://" + IP + "/haozai/checkusername";
            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", name);
            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // 处理发送数据请求
    private static String sendPOSTRequest(String path, Map<String, String> params, String encoding) throws Exception {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, encoding);

        HttpPost post = new HttpPost(path);
        post.setEntity(entity);
        DefaultHttpClient client = new DefaultHttpClient();
        // 请求超时
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        // 读取超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        HttpResponse response = client.execute(post);

        // 判断是否成功收取信息
        if (response.getStatusLine().getStatusCode() == 200) {
            return getInfo(response);
        }
        // 未成功收取信息，返回空指针
        return null;
    }

    // 收取数据
    private static String getInfo(HttpResponse response) throws Exception {

        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        // 将输入流转化为byte型
        byte[] data = WebService.read(is);
        // 转化为字符串
        return new String(data, "UTF-8");
    }

}
