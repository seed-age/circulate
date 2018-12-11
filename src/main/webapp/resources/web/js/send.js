//JavaScript代码区域
layui.use(['element','laypage','table','form','layer','jquery','laydate'], function(){
    $(function(){
        var element = layui.element;
        var table = layui.table;
        var laypage = layui.laypage;
        var form = layui.form;
        var $= layui.jquery;
        var laydate = layui.laydate;
        // 删除
        $('.oa-read-delete').on('click',function(){
            // var choice = $('.oa-read-table tbody input[name="choice"]:checked');
            var choice = $('.oa-read-table tbody input[name="choice"]:not([disabled]):checked');
            var choiceArr = [];
            var layer_del;
            for(var i=0;i<choice.length;i++){
                choiceArr.push(choice[i].value);
            };
            // 判断是否有选择删除的，如果没有不提交数据
            if(choiceArr.length>0){
                layer.confirm(
                    '删除后将存入到已删除传阅，您确定要删除吗？',
                    {
                        btn: ['是','否'],
                        title:'删除提示'
                    }, //按钮
                    function(index, layero){
                        //按钮【按钮一】的回调
                        $.ajax({
                            type:'post',
                            url:'/web/send/delete-mail.htm',
                            dataType:'json',
                            data:{
                                userId:$.session.get('userId'),
                                mailId:choiceArr
                            },
                            beforeSend:function(xhr){
                                layer_del = layer.msg('正在删除中', {
                                    icon: 16,
                                    shade: 0.01,
                                    area:'40px',
                                    time:0
                                });
                            },
                            success:function(res){
                                layer.close(layer_del)
                                var page = $('input[name="currPage"]').val();
                                var currPage = page === ''?1:page;
                                if(res.success === false){
                                    layer.msg(res.msg,{time: 2000,icon: 2});
                                };
                                if(res.code === '200'){
                                    layer.msg('删除成功',{time: 2000,icon: 1});
                                    pages.getData(parseInt(currPage));
                                };
                            },
                            error:function(){
                                layer.close(layer_del);
                                layer.msg('网络出错',{time: 2000,icon: 2});
                            }
                        })
                    },
                    function(index){
                        //按钮【按钮二】的回调
                    }
                );

            }else{
                layer.msg('请选择要删除的传阅',{time: 2000});
                return false;
            }

        });
        var urlState;
        // 判断是否从传阅一览进去来的
        switch(getQueryString('state') ){
            case '1':
                urlState = 1;
                $('select[name="mailStatus"]').val(1);
                break;
            case '3':
                urlState = 3;
                $('select[name="mailStatus"]').val(3);
            default:
                urlState = $('select[name="mailStatus"]').val();
        };
        var itemNum = $('select[name="item-num"]').val();
        var startTime = '';
        var endTime = '';
        // 页面打开执行分页数据加载
        var pages = new Pages({
            type:'post',
            url:'/web/homepage/grid-send-in-list.htm',
            data:{
                userId:$.session.get('userId'),
                page:1,
                pageRows:itemNum,
                mailStatus:urlState,
                startTime : startTime,
                endTime :endTime,
                likeName:$('#search').val()
            },
            templateTag:'.oa-read-table .layui-table tbody'
        });
        pages.getData();
        // 流程状态数据加载
        form.on('select(mailStatus)', function(data){
            urlState = data.value;
            var pages = new Pages({
                type:'post',
                url:'/web/homepage/grid-send-in-list.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    pageRows:itemNum,
                    mailStatus:data.value,
                    startTime : startTime,
                    endTime :endTime,
                    likeName:$('#search').val()
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            pages.getData();
        });
        // 每页显示几条
        form.on('select(item-num)', function(data){
            itemNum = $('select[name="item-num"]').val();
            var pages = new Pages({
                type:'post',
                url:'/web/homepage/grid-send-in-list.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    pageRows:itemNum,
                    mailStatus:urlState,
                    startTime : startTime,
                    endTime :endTime,
                    likeName:$('#search').val()
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            pages.getData();
        });
        // 查询数据加载
        $('#search_btn').on('click',function(){
            // urlState = $('select[name="mailStatus"]').val(5);
            var pages = new Pages({
                type:'post',
                url:'/web/homepage/grid-send-in-list.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    pageRows:itemNum,
                    mailStatus:urlState,
                    startTime : startTime,
                    endTime :endTime,
                    likeName:$('#search').val()
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            pages.getData();

        });
        laydate.render({
            elem: '#calendar', //指定元素
            type:'date',
            range:'到',
            theme:'#008cd6',
            done: function(value, date, endDate){
                if(date.year === undefined || endDate.year === undefined){
                    startTime = '';
                    endTime = '';
                }else{
                    startTime = date.year+'-'+date.month+'-'+date.date;
                    endTime = endDate.year+'-'+endDate.month+'-'+endDate.date;
                }
                var pages = new Pages({
                    type:'post',
                    url:'/web/homepage/grid-send-in-list.htm',
                    data:{
                        userId:$.session.get('userId'),
                        page:1,
                        pageRows:itemNum,
                        startTime : startTime,
                        endTime :endTime,
                        mailStatus:urlState,
                        likeName:$('#search').val()
                    },
                    templateTag:'.oa-read-table .layui-table tbody'
                });
                pages.getData();
            }

        });
        $(document).on('click','.oa-read-table .layui-table tbody tr td',function(){
            var value = $(this).parent('tr').children().children('input[name=choice]').val();
            if(value === undefined || $(this).index() === 0 || $(this).index() === $(this).parent('tr').children().length-1){
                return  false;
            };
            $.session.set('article',value);
            $.session.set('mailStatus',1);
            window.location.href = 'article.htm';
        });
        $.session.set('attentionStatus',1);
    })
});

