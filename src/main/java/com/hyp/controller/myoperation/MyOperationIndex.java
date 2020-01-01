package com.hyp.controller.myoperation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyp.pojo.datatransferobject.NewDTO;
import com.hyp.pojo.datatransferobject.WeatherDTO;
import com.hyp.utils.HttpClientUtil;
import com.hyp.utils.IpUtils;
import com.hyp.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/7/20 15:32
 * @Description: TODO 这里是用户前台页面
 */

@Controller
@RequestMapping(value = "hyp")
@Slf4j
public class MyOperationIndex {

    /**
     * 访问主页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "myoperation/index";
    }

    /**
     * 返回天气的信息
     *
     * @return
     */
    @RequestMapping(value = "/getTalking/{talkText}", produces = {"text/html;charset=UTF-8;", "application/json;charset=UTF-8;"})
    @ResponseBody
    public String getTalking(@PathVariable String talkText) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String talkWord = httpClientUtil.getParameter("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + talkText, null, null, 10000, 10000, 10000);
        log.info("聊天内容：" + talkText + "返回内容：" + talkWord);
        return talkWord;
    }


    /**
     * 返回每日一言
     *
     * @return
     */
    @RequestMapping(value = "/getDailyWord", produces = {"text/html;charset=UTF-8;", "application/json;charset=UTF-8;"})
    @ResponseBody
    public String getDailyWord() {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String dailyWordReturn = httpClientUtil.getParameter("http://api.hanximeng.com/hitokoto/api.php", null, null, 2000, 2000, 2000);
        log.info("每日一言：" + dailyWordReturn);
        return dailyWordReturn;
       /* try {
            return new String(dailyWordReturn.getBytes("utf-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "好喜欢何亚培呀";*/
    }


    /**
     * 返回新闻
     *
     * @return
     */
    @RequestMapping("/getNews")
    @ResponseBody
    public List<NewDTO> getNews() {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String newsReturn = httpClientUtil.getParameter("http://v.juhe.cn/toutiao/index?type=top&key=320fe5990868976ec7a68fa3627c7fe2", null, null, 2000, 2000, 2000);
        JSONObject parse = JSONObject.parseObject(newsReturn);
        JSONObject result = parse.getJSONObject("result");
        String data = result.getString("data");
        List<NewDTO> NewDTOs = JsonUtils.jsonToList(data, NewDTO.class);
        return NewDTOs;
    }


    /**
     * 返回天气的信息
     *
     * @return
     */
    @RequestMapping("/getWeather")
    @ResponseBody
    public List<WeatherDTO> getWeather(HttpServletRequest request) {

        HttpClientUtil httpClientUtil = new HttpClientUtil();

        String ipFromRequest = IpUtils.getIpFromRequest(request);
        log.info("请求地址：" + ipFromRequest);
        //读取json
        //a6189d40a6e51b60ef1e55f7593fe962


        String weatherReturn = httpClientUtil.getParameter("http://t.weather.sojson.com/api/weather/city/101020800", null, null, 2000, 2000, 2000);
       /* String weatherReturn = "{\"message\":\"success感谢又拍云(upyun.com)提供CDN赞助\"," +
                "\"status\":200,\"date\":\"20191130\",\"time\":\"2019-11-30 14:50:13\"," +
                "\"cityInfo\":{\"city\":\"青浦区\",\"citykey\":\"101020800\",\"parent\":\"上海市\",\"updateTime\":\"14:00\"}," +
                "\"data\":{\"shidu\":\"90%\",\"pm25\":21.0,\"pm10\":39.0,\"quality\":\"优\",\"wendu\":" +
                "\"11\",\"ganmao\":\"各类人群可自由活动\",\"forecast\":[{\"date\":\"30\"," +
                "\"high\":\"高温 13℃\",\"low\":\"低温 9℃\",\"ymd\":\"2019-11-30\",\"week\":\"星期六\",\"sunrise\":\"06:34\",\"sunset\":\"16:53\",\"aqi\":47,\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"小雨\",\"notice\":\"雨虽小，注意保暖别感冒\"},{\"date\":\"01\",\"high\":\"高温 13℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-01\",\"week\":\"星期日\",\"sunrise\":\"06:35\",\"sunset\":\"16:53\",\"aqi\":122,\"fx\":\"西北风\",\"fl\":\"3-4级\",\"type\":\"小雨\",\"notice\":\"雨虽小，注意保暖别感冒\"},{\"date\":\"02\",\"high\":\"高温 11℃\",\"low\":\"低温 0℃\",\"ymd\":\"2019-12-02\",\"week\":\"星期一\",\"sunrise\":\"06:36\",\"sunset\":\"16:53\",\"aqi\":57,\"fx\":\"北风\",\"fl\":\"3-4级\",\"type\":\"阴\",\"notice\":\"不要被阴云遮挡住好心情\"},{\"date\":\"03\",\"high\":\"高温 11℃\",\"low\":\"低温 -1℃\",\"ymd\":\"2019-12-03\",\"week\":\"星期二\",\"sunrise\":\"06:36\",\"sunset\":\"16:53\",\"aqi\":56,\"fx\":\"北风\",\"fl\":\"3-4级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"04\",\"high\":\"高温 12℃\",\"low\":\"低温 2℃\",\"ymd\":\"2019-12-04\",\"week\":\"星期三\",\"sunrise\":\"06:37\",\"sunset\":\"16:53\",\"aqi\":67,\"fx\":\"北风\",\"fl\":\"4-5级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"05\",\"high\":\"高温 13℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-05\",\"week\":\"星期四\",\"sunrise\":\"06:38\",\"sunset\":\"16:53\",\"aqi\":48,\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"06\",\"high\":\"高温 9℃\",\"low\":\"低温 3℃\",\"ymd\":\"2019-12-06\",\"week\":\"星期五\",\"sunrise\":\"06:39\",\"sunset\":\"16:53\",\"fx\":\"北风\",\"fl\":\"3-4级\",\"type\":\"小雨\",\"notice\":\"雨虽小，注意保暖别感冒\"},{\"date\":\"07\",\"high\":\"高温 9℃\",\"low\":\"低温 2℃\",\"ymd\":\"2019-12-07\",\"week\":\"星期六\",\"sunrise\":\"06:40\",\"sunset\":\"16:53\",\"fx\":\"北风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"08\",\"high\":\"高温 10℃\",\"low\":\"低温 4℃\",\"ymd\":\"2019-12-08\",\"week\":\"星期日\",\"sunrise\":\"06:40\",\"sunset\":\"16:53\",\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"09\",\"high\":\"高温 13℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-09\",\"week\":\"星期一\",\"sunrise\":\"06:41\",\"sunset\":\"16:53\",\"fx\":\"东风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"10\",\"high\":\"高温 14℃\",\"low\":\"低温 8℃\",\"ymd\":\"2019-12-10\",\"week\":\"星期二\",\"sunrise\":\"06:42\",\"sunset\":\"16:53\",\"fx\":\"东北风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"11\",\"high\":\"高温 14℃\",\"low\":\"低温 6℃\",\"ymd\":\"2019-12-11\",\"week\":\"星期三\",\"sunrise\":\"06:43\",\"sunset\":\"16:53\",\"fx\":\"西北风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"},{\"date\":\"12\",\"high\":\"高温 10℃\",\"low\":\"低温 5℃\",\"ymd\":\"2019-12-12\",\"week\":\"星期四\",\"sunrise\":\"06:43\",\"sunset\":\"16:54\",\"fx\":\"东北风\",\"fl\":\"3-4级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"13\",\"high\":\"高温 10℃\",\"low\":\"低温 6℃\",\"ymd\":\"2019-12-13\",\"week\":\"星期五\",\"sunrise\":\"06:44\",\"sunset\":\"16:54\",\"fx\":\"东风\",\"fl\":\"3-4级\",\"type\":\"晴\",\"notice\":\"愿你拥有比阳光明媚的心情\"},{\"date\":\"14\",\"high\":\"高温 12℃\",\"low\":\"低温 7℃\",\"ymd\":\"2019-12-14\",\"week\":\"星期六\",\"sunrise\":\"06:45\",\"sunset\":\"16:54\",\"fx\":\"东风\",\"fl\":\"<3级\",\"type\":\"多云\",\"notice\":\"阴晴之间，谨防紫外线侵扰\"}],\"yesterday\":{\"date\":\"29\",\"high\":\"高温 12℃\",\"low\":\"低温 9℃\",\"ymd\":\"2019-11-29\",\"week\":\"星期五\",\"sunrise\":\"06:33\",\"sunset\":\"16:53\",\"aqi\":32,\"fx\":\"东北风\",\"fl\":\"3-4级\",\"type\":\"阴\",\"notice\":\"不要被阴云遮挡住好心情\"}}}";
*/
        JSONObject parse = JSONObject.parseObject(weatherReturn);
        JSONObject data = parse.getJSONObject("data");
        JSONArray forecast = data.getJSONArray("forecast");
        List<WeatherDTO> weatherDTOS = new ArrayList<>();
        for (Object o : forecast) {
            WeatherDTO weatherDTO = JsonUtils.jsonToPojo(o.toString(), WeatherDTO.class);
            weatherDTO.setHigh(weatherDTO.getHigh().substring(3, weatherDTO.getHigh().indexOf("℃")));
            weatherDTO.setLow(weatherDTO.getLow().substring(3, weatherDTO.getLow().indexOf("℃")));
            weatherDTOS.add(weatherDTO);
        }
        return weatherDTOS;
    }


}
