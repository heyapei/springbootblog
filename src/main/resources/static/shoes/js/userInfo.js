function showTrendMap(courseCode) {
    toastr.success("正在打开课程" + courseCode + "的趋势图");
    $('#myModal').modal("show");
    $('#myModalTitle').html(courseCode + "趋势图");
    $('#myModal').on('shown.bs.modal', centerModals);

    $.ajax({
        url: "/c/myoperation/courseStatistics/getCourseTrendMap.jsp",
        type: "POST",
        data: {
            courseCode: courseCode,
        },
        dataType: "json",
        //async : true,// 必须为true
        cache: false,
        success: function (data) {
            var myChart = echarts.init(document.getElementById('echartsContainer'));
            myChart.showLoading();
            var option = {
                title: {
                    text: '内容不作为具体依据',
                    textStyle: {//主标题文本样式{"fontSize": 18,"fontWeight": "bolder","color": "#333"}
                        fontFamily: 'Arial, Verdana, sans...',
                        fontSize: 8,
                        fontStyle: 'italic',
                        fontWeight: 'normal',
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    /* axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }*/
                },
                legend: {
                    data: ['选课总数', '评论总数']
                },
                toolbox: {
                    show: true,
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: true,
                        data: data.dateTime
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                grid: {
                    left: '3%',
                    right: '4%',
                    containLabel: true
                },
                dataZoom: [{
                    type: 'inside',
                    start: 0,
                    end: 100,
                }, {
                    start: 0,
                    end: 100,
                    handleSize: '80%',
                    handleStyle: {
                        color: '#fff',
                        shadowBlur: 3,
                        shadowColor: 'rgba(0, 0, 0, 0.6)',
                        shadowOffsetX: 2,
                        shadowOffsetY: 2
                    }
                }],
                series: [
                    {
                        name: '选课总数',
                        type: 'bar',
                        stack: '总量',
                        barMaxWidth: 30,
                        //itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: data.selectCount,
                        smooth: true,
                        label: {
                            normal: {
                                show: true,
                                position: 'inside',
                                textStyle: {
                                    color: 'black'
                                }
                            }
                        }
                    },
                    {
                        name: '评论总数',
                        type: 'bar',
                        stack: '总量',
                        smooth: true,
                        //itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: data.evaluate
                    }
                ]
            };
            myChart.setOption(option);
            myChart.hideLoading();
        },
        error: function (data, type, err) {  // 以下依次是返回过来的数据，错误类型，错误码
            console.log("ajax错误类型：" + type);
            console.log(err);
        }
    });


}


/*模态框居中*/
function centerModals() {
    $('#myModal').each(function (i) {
        var $clone = $(this).clone().css('display', 'block').appendTo('body');
        var top = Math.round(($clone.height() - $clone.find('.modal-content').height()) / 2);
        top = top > 0 ? top : 0;
        $clone.remove();
        $(this).find('.modal-content').css("margin-top", top);
    });
};


function cp(ipage) {
    $("#currentPage").val(ipage);
    $("#courseStatisticsForm").append("<input type='hidden' name='currentPage' value='" + ipage + "' />");
    $("#courseStatisticsForm").submit();
}


function goPage(maxPageNum) {
    var jumpPage = document.getElementById("jumpPage").value;
    var totalPage = maxPageNum;
    if (isNaN(jumpPage)) {
        toastr.error("请输入数字!");
        return;
    } else if (jumpPage.length == 0) {
        toastr.error("请输入页码!");
    } else if (jumpPage <= 0 || Number(jumpPage) > Number(totalPage)) {
        toastr.error("非法的页码【" + jumpPage + "】!");
        document.getElementById("jumpPage").value = "";
        return;
    } else {
        var flag = $("input[name='pageNumber']");
        flag.remove();
        $("#courseStatisticsForm").append("<input type='hidden' name='currentPage' value='" + jumpPage + "' />");
        $("#courseStatisticsForm").submit();
    }
}


//设置日期时间控件
function myDateTimePicker() {
    $('#datetimepicker1').datetimepicker({
        language: 'zh-CN',//显示中文
        format: 'yyyy-mm-dd',//显示格式
        minView: "month",//设置只显示到月份
        initialDate: new Date(),
        autoclose: true,//选中自动关闭
        todayBtn: true,//显示今日按钮
        locale: moment.locale('zh-cn')
    });
    $('#datetimepicker2').datetimepicker({
        language: 'zh-CN',//显示中文
        format: 'yyyy-mm-dd',//显示格式
        minView: "month",//设置只显示到月份
        initialDate: new Date(),
        autoclose: true,//选中自动关闭
        todayBtn: true,//显示今日按钮
        locale: moment.locale('zh-cn')
    });
    /*//默认获取当前日期
    var today = new Date();
    var nowDate = (today.getFullYear()) + "-" + (today.getMonth() + 1) + "-" + today.getDate();
    //对日期格式进行处理
    var date = new Date(nowDate);
    var mon = date.getMonth() + 1;
    var day = date.getDate();
    var myDate = date.getFullYear() + "-" + (mon < 10 ? "0" + mon : mon) + "-" + (day < 10 ? "0" + day : day);
    document.getElementById("nowDate1").value = myDate;
    myDate = date.getFullYear() + 1 + "-" + (mon < 10 ? "0" + mon : mon) + "-" + (day < 10 ? "0" + day : day);
    document.getElementById("nowDate2").value = myDate;*/
}


$(function () {
    /* var date = new Date;
     var year = date.getFullYear();
     $("#yearBeforeLast").val(year - 2 + "选课");
     $("#yearLast").text(year - 1 + "选课");
     $("#yearNow").html(year + "选课");*/


    /*初始化加载日期控件*/
    myDateTimePicker();

    $('#courseStatisticsTable').bootstrapTable('destroy').bootstrapTable({
        //导出功能设置（关键代码）
        exportDataType: 'all',//'basic':当前页的数据, 'all':全部的数据, 'selected':选中的数据
        showExport: true,  //是否显示导出按钮
        buttonsAlign: "right",  //按钮位置
        exportTypes: ['excel']  //导出文件类型，[ 'csv', 'txt', 'sql', 'doc', 'excel', 'xlsx', 'pdf']

    });


})