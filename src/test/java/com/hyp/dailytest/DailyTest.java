package com.hyp.dailytest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyp.pojo.datatransferobject.NewDTO;
import com.hyp.pojo.datatransferobject.WeatherDTO;
import com.hyp.utils.HttpClientUtil;
import com.hyp.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/11/30 14:53
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DailyTest {


    @Test
    public void testIpUtils() {
        String talkText = "nihao";
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String talkWord = httpClientUtil.getParameter("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + talkText, null, null, 10000, 10000, 10000);
        //log.info("聊天内容：" + talkText + "返回内容：" + talkWord);
        System.out.println("聊天内容：" + talkText + "返回内容：" + talkWord);

    }


    @Test
    public void testNew() {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String s = httpClientUtil.getParameter("http://v.juhe.cn/toutiao/index?type=top&key=320fe5990868976ec7a68fa3627c7fe2", null, null, 2000, 2000, 2000);
        JSONObject parse = JSONObject.parseObject(s);
        JSONObject result = parse.getJSONObject("result");
        String data = result.getString("data");
        List<NewDTO> NewDTOs = JsonUtils.jsonToList(data, NewDTO.class);
        System.out.println(NewDTOs);

    }

    @Test
    public void testDailyWord() {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String dailyWordReturn = httpClientUtil.getParameter("http://api.hanximeng.com/hitokoto/api.php", null, null, 2000, 2000, 2000);
        System.out.println(dailyWordReturn);
    }

    @Test
    public void showOneArticle() {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
       /* String parameter = httpClientUtil.getParameter("http://t.weather.sojson.com/api/weather/city/101020800", null, null, 2000, 2000, 2000);
        System.out.println(parameter);*/

        String s = "{\"message\":\"success感谢又拍云(upyun.com)提供CDN赞助\"," +
                "\"status\":200,\"date\":\"20191130\",\"time\":\"2019-11-30 14:50:13\"," +
                "\"cityInfo\":{\"city\":\"青浦区\",\"citykey\":\"101020800\",\"parent\":\"上海市\",\"updateTime\":\"14:00\"}," +
                "\"data\":{\"shidu\":\"90%\",\"pm25\":21.0,\"pm10\":39.0,\"quality\":\"优\",\"wendu\":\"11\",\"ganmao\":\"各类人群可自由活动\",\"forecast\":[{\"date\":\"30\",\"high\":\"高温 13℃\",\"low\":\"低温 9℃\",\"ymd\":\"2019-11-30\",\"week\":\"星期六\",\"sunrise\":\"06:34\",\"sunset\":\"16:53\",\"aqi\":47,\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"小雨\",\"notice\":\"雨虽小，注意保暖别感冒\"},{\"date\":\"01\",\"high\":\"高温 13℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-01\",\"week\":\"星期日\",\"sunrise\":\"06:35\",\"sunset\":\"16:53\",\"aqi\":122,\"fx\":\"西北风\",\"fl\":\"3-4级\",\"type\":\"小雨\",\"notice\":\"雨虽小，注意保暖别感冒\"},{\"date\":\"02\",\"high\":\"高温 11℃\",\"low\":\"低温 0℃\",\"ymd\":\"2019-12-02\",\"week\":\"星期一\",\"sunrise\":\"06:36\",\"sunset\":\"16:53\",\"aqi\":57,\"fx\":\"北风\",\"fl\":\"3-4级\",\"type\":\"阴\",\"notice\":\"不要被阴云遮挡住好心情\"},{\"date\":\"03\",\"high\":\"高温 11℃\",\"low\":\"低温 -1℃\",\"ymd\":\"2019-12-03\",\"week\":\"星期二\",\"sunrise\":\"06:36\",\"sunset\":\"16:53\",\"aqi\":56,\"fx\":\"北风\",\"fl\":\"3-4级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"04\",\"high\":\"高温 12℃\",\"low\":\"低温 2℃\",\"ymd\":\"2019-12-04\",\"week\":\"星期三\",\"sunrise\":\"06:37\",\"sunset\":\"16:53\",\"aqi\":67,\"fx\":\"北风\",\"fl\":\"4-5级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"05\",\"high\":\"高温 13℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-05\",\"week\":\"星期四\",\"sunrise\":\"06:38\",\"sunset\":\"16:53\",\"aqi\":48,\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"06\",\"high\":\"高温 9℃\",\"low\":\"低温 3℃\",\"ymd\":\"2019-12-06\",\"week\":\"星期五\",\"sunrise\":\"06:39\",\"sunset\":\"16:53\",\"fx\":\"北风\",\"fl\":\"3-4级\",\"type\":\"小雨\",\"notice\":\"雨虽小，注意保暖别感冒\"},{\"date\":\"07\",\"high\":\"高温 9℃\",\"low\":\"低温 2℃\",\"ymd\":\"2019-12-07\",\"week\":\"星期六\",\"sunrise\":\"06:40\",\"sunset\":\"16:53\",\"fx\":\"北风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"08\",\"high\":\"高温 10℃\",\"low\":\"低温 4℃\",\"ymd\":\"2019-12-08\",\"week\":\"星期日\",\"sunrise\":\"06:40\",\"sunset\":\"16:53\",\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"09\",\"high\":\"高温 13℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-09\",\"week\":\"星期一\",\"sunrise\":\"06:41\",\"sunset\":\"16:53\",\"fx\":\"东风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"10\",\"high\":\"高温 14℃\",\"low\":\"低温 8℃\",\"ymd\":\"2019-12-10\",\"week\":\"星期二\",\"sunrise\":\"06:42\",\"sunset\":\"16:53\",\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"11\",\"high\":\"高温 14℃\",\"low\":\"低温 6℃\",\"ymd\":\"2019-12-11\",\"week\":\"星期三\",\"sunrise\":\"06:43\",\"sunset\":\"16:53\",\"fx\":\"西北风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"12\",\"high\":\"高温 10℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-12\",\"week\":\"星期四\",\"sunrise\":\"06:43\",\"sunset\":\"16:54\",\"fx\":\"东北风\",\"fl\":\"3-4级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"13\",\"high\":\"高温 10℃\",\"low\":\"低温 6℃\",\"ymd\":\"2019-12-13\",\"week\":\"星期五\",\"sunrise\":\"06:44\",\"sunset\":\"16:54\",\"fx\":\"东风\",\"fl\":\"3-4级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"14\",\"high\":\"高温 12℃\",\"low\":\"低温 7℃\",\"ymd\":\"2019-12-14\",\"week\":\"星期六\",\"sunrise\":\"06:45\",\"sunset\":\"16:54\",\"fx\":\"东风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"}],\"yesterday\":{\"date\":\"29\",\"high\":\"高温 12℃\",\"low\":\"低温 9℃\",\"ymd\":\"2019-11-29\",\"week\":\"星期五\",\"sunrise\":\"06:33\",\"sunset\":\"16:53\",\"aqi\":32,\"fx\":\"东北风\",\"fl\":\"3-4级\",\"type\":\"阴\",\"notice\":\"不要被阴云遮挡住好心情\"}}}";

        /*s = "{\n" +
                "\t\"message\": \"success感谢又拍云(upyun.com)提供CDN赞助\",\n" +
                "\t\"status\": 200,\n" +
                "\t\"date\": \"20191130\",\n" +
                "\t\"time\": \"2019-11-30 14:50:13\",\n" +
                "\t\"cityInfo\": {\n" +
                "\t\t\"city\": \"青浦区\",\n" +
                "\t\t\"citykey\": \"101020800\",\n" +
                "\t\t\"parent\": \"上海市\",\n" +
                "\t\t\"updateTime\": \"14:00\"\n" +
                "\t},\n" +
                "\t\"data\": {\n" +
                "\t\t\"shidu\": \"90%\",\n" +
                "\t\t\"pm25\": 21.0,\n" +
                "\t\t\"pm10\": 39.0,\n" +
                "\t\t\"quality\": \"优\",\n" +
                "\t\t\"wendu\": \"11\",\n" +
                "\t\t\"ganmao\": \"各类人群可自由活动\",\n" +
                "\t\t\"forecast\": [{\n" +
                "\t\t\t\"date\": \"30\",\n" +
                "\t\t\t\"high\": \"高温 13℃\",\n" +
                "\t\t\t\"low\": \"低温 9℃\",\n" +
                "\t\t\t\"ymd\": \"2019-11-30\",\n" +
                "\t\t\t\"week\": \"星期六\",\n" +
                "\t\t\t\"sunrise\": \"06:34\",\n" +
                "\t\t\t\"sunset\": \"16:53\",\n" +
                "\t\t\t\"aqi\": 47,\n" +
                "\t\t\t\"fx\": \"东北风\",\n" +
                "\t\t\t\"fl\": \"<3级\",\n" +
                "\t\t\t\"type\": \"小雨\",\n" +
                "\t\t\t\"notice\": \"雨虽小，注意保暖别感冒\"\n" +
                "\t\t}]\n" +
                "\t}\n" +
                "}";*/
        //System.out.println(s);

        JSONObject parse = JSONObject.parseObject(s);
        String status = parse.getString("status");
        //System.out.println(status);
        String data = parse.getString("date");
        //System.out.println(data);
        JSONObject cityInfo = parse.getJSONObject("cityInfo");
        //System.out.println(cityInfo);
        JSONObject data1 = parse.getJSONObject("data");
        //System.out.println(data1);
        JSONArray forecast = data1.getJSONArray("forecast");
        List<WeatherDTO> weatherDTOS1 = JsonUtils.jsonToList(forecast.toString(), WeatherDTO.class);
        //System.out.println(weatherDTOS1);
        // System.out.println(forecast);
        List<WeatherDTO> weatherDTOS = new ArrayList<>();
        for (Object o : forecast) {
            //T t = JsonUtils.jsonToPojo( o,new WeatherDTO());
            WeatherDTO weatherDTO = JsonUtils.jsonToPojo(o.toString(), WeatherDTO.class);
            weatherDTO.setHigh(weatherDTO.getHigh().substring(3));
            weatherDTO.setLow(weatherDTO.getLow().substring(3));
            weatherDTOS.add(weatherDTO);
            //System.out.println(weatherDTO);
        }
        JSONObject yesterday = data1.getJSONObject("yesterday");
        WeatherDTO yesterdayWeatherDTO = JsonUtils.jsonToPojo(yesterday.toString(), WeatherDTO.class);
        System.out.println("昨天天气：" + yesterdayWeatherDTO);
        System.out.println(weatherDTOS.toString());


        // ymd='2019-11-30', high='高温 13℃', sunrise='06:34', fx='东北风', week='星期六', low='低温 9℃', fl='<3级',
        // sunset='16:53', type='小雨', notice='雨虽小，注意保暖别感冒'

    }
}
