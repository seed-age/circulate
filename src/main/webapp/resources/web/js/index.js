
layui.use(['element','layer'], function () {
    $(function(){
        var element = layui.element;
        var layer = layui.layer;
        readySloa();//加载传阅数据
        // 帮助
        var tipArr = [
            '说明：统计收到传阅的未读传阅和待办传阅数量，点击查看跳转到收到传阅列表',
            '说明：统计已发传阅的传阅中传阅和已完成传阅数量，点击查看跳转到已发传阅列表',
            '说明：统计已删除传阅数量，点击查看跳转到已删除列表',
            '说明：统计传阅中的发送传阅和收到传阅数量，点击查看跳转到已发传阅或收到传阅列表',
            '说明：统计传阅完成的发送传阅和收到传阅数量，点击查看跳转到已发传阅或收到传阅列表',
            '说明：统计待发传阅数量，点击查看跳转到待发传阅列表'
        ];
        $(document).on('mouseenter','.sub-title .tips',function(){
            layer.tips(tipArr[$(this).parents('.sloa-box').index()], this, {
                tips: [3, '#008cd6'],
                guide:2,
                time: 0
            });
        });
        $(document).on('mouseleave','.sub-title .tips',function(){
            layer.closeAll('tips'); //关闭所有的tips层
        });
    })
});


// 说明：存放自己收到的传阅，对收到的传阅，你能发表意见或平平并进行确认，也可对收到的传阅新增传阅人员

// 传阅一览
function readySloa(){
    $.ajax({
        type:'post',
        url:'/web/homepage/cont-state-number.htm',
        dataType:'json',
        data:{
            userId:$.session.get('userId'),
        },
        success:function(reg){
            if(reg.code === '200'){
                loadSloa(reg.data);
            }
        },
        error:function(msg){
            console.log(msg.status);
        }
    })
    function loadSloa(data){
        var solaData = [
            {
                title:"收到传阅",
                count:Number(data.receiveUnreadCount)+Number(data.receiveReadCount),
                list:[
                    {
                        title:'待办传阅',
                        value:data.receiveAwaitCount, //待办
                        url:'receive.htm?state=2'
                    },
                    {
                        title:'未读传阅',
                        value:data.receiveUnreadCount, //未读
                        url:'receive.htm?state=5'
                    },
                    {
                        title:'已读传阅',
                        value:data.receiveReadCount, //已读
                        url:'receive.htm?state=6'
                    },
                    {
                        title:'传阅中',
                        value:data.receiveHalfwayCount, //传阅中
                        url:'receive.htm?state=1'
                    },
                    {
                        title:'已完成',
                        value:data.receiveCompleteCount, //已完成
                        url:'receive.htm?state=3'
                    }

                ]
            },
            {
                title:"发送传阅",
                count:Number(data.sendWaitCount)+Number(data.sendCount)+Number(data.deleteCount),
                list:[
                    {
                        title:'待发传阅',
                        value:data.sendWaitCount, //待发传阅
                        url:'await.htm'
                    },
                    {
                        title:'已发送',
                        value:data.sendCount, //已发送
                        url:'send.htm'
                    },
                    {
                        title:'传阅中',
                        value:data.sendHalfwayCount, //传阅中
                        url:'send.htm?state=1'
                    },
                    {
                        title:'已完成',
                        value:data.sendCompleteCount, //已完成
                        url:'send.htm?state=3'
                    },
                    {
                        title:'已删除',
                        value:data.deleteCount, //已删除
                        url:'delete.htm'
                    }
                ]
            },
            // {
            //     title:"已删除",
            //     list:[
            //         {
            //             title:'已删除',
            //             value:data.deleteCount, //已删除
            //             url:'delete.htm'
            //         }
            //     ]
            // },
            // {
            //     title:"传阅中",
            //     list:[
            //         {
            //             title:'发送传阅',
            //             value:data.sendInCount, //发送传阅
            //             url:'send.htm?state=1'
            //         },
            //         {
            //             title:'收到传阅',
            //             value:data.receiveInCount, //收到传阅
            //             url:'receive.htm?state=1'
            //         }
            //     ]
            // },
            // {
            //     title:"传阅完成",
            //     list:[
            //         {
            //             title:'发送传阅',
            //             value:data.completeCount, //发送传阅
            //             url:'send.htm?state=3'
            //         },
            //         {
            //             title:'收到传阅',
            //             value:data.ceceiveCompleteCount, //收到传阅
            //             url:'receive.htm?state=3'
            //         }
            //     ]
            // },
            // {
            //     title:"待发传阅",
            //     list:[
            //         {
            //             title:'待发传阅',
            //             value:data.waitSendCount, //待发传阅
            //             url:'await.htm'
            //         }
            //     ]
            // }
        ];
        for(var i=0;i<solaData.length;i++){
            var html ='';
            // debugger
            html += '<div class="layui-col-sm12 sloa-box">';
            html += '    <div class="sub-title">';
            html += '        <h2>'+solaData[i].title+'（'+solaData[i].count+'）</h2>';
            html += '        <div class="tips">';
            html += '           <img src="/resources/web/images/question-icon.png"/>';
            html += '        </div>';
            html += '    </div>';
            html += '    <div class="content-box">';
            html += '        <div class="layui-row">';

            for(var j=0;j<solaData[i].list.length;j++){
                html += '            <div class="layui-col-sm3 content-item">';
                html += '                <p class="txt">'+solaData[i].list[j].title+'</p>';
                html += '                <h3>'+solaData[i].list[j].value+'</h3>';
                html += '                <a class="watch" href="'+solaData[i].list[j].url+'">查看</a>';
                html += '            </div>';
            }
            // if(solaData[i].list.length > 1){
            //     html += '            <div class="layui-col-sm3 content-item">';
            //     html += '                <p class="txt">'+solaData[i].list[0].title+'</p>';
            //     html += '                <h3>'+solaData[i].list[0].value+'</h3>';
            //     html += '                <a class="watch" href="'+solaData[i].list[0].url+'">查看</a>';
            //     html += '            </div>';
            //     html += '            <div class="layui-col-sm3 content-item">';
            //     html += '                <p class="txt">'+solaData[i].list[1].title+'</p>';
            //     html += '                <h3>'+solaData[i].list[1].value+'</h3>';
            //     html += '                <a class="watch" href="'+solaData[i].list[1].url+'">查看</a>';
            //     html += '            </div>';
            // }else{
            //     html += '            <div class="layui-col-sm12 content-item">';
            //     html += '                <p class="txt">'+solaData[i].list[0].title+'</p>';
            //     html += '                <h3>'+solaData[i].list[0].value+'</h3>';
            //     html += '                <a class="watch" href="'+solaData[i].list[0].url+'">查看</a>';
            //     html += '            </div>';
            // }
            html += '        </div>';
            html += '    </div>';
            html += '</div>';
            $('.sloa-list').append(html);

        }

    }
}
