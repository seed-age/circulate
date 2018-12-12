//JavaScript代码区域
layui.use(['element','form','table','jquery','laydate'], function(){
    $(function(){
        var element = layui.element;
        var table = layui.table;
        var form = layui.form;
        var $ = layui.jquery;
        var laydate = layui.laydate;
        var itemNum = $('select[name="item-num"]').val();
        var startTime = '';
        var endTime = '';
        function AwaitPages(){ }
        AwaitPages.prototype = new Pages({
            type:'post',
            url:'/web/waitsend/find-like-wait-send.htm',
            data:{
                userId:$.session.get('userId'),
                page:1,
                startTime : startTime,
                endTime :endTime,
                likeName:$('#search').val(),
                pageRows:itemNum
            },
            templateTag:'.oa-read-table .layui-table tbody'
        });
        var awaitPages = new AwaitPages();
        awaitPages.getData();
        Pages.prototype.templateHTML = function(that,data){
            layui.use(['form','table'], function(){
                var form = layui.form;
                this.templateTag.html('');//清空上次内容
                var string = '';
                if(data.length>0){
                    for(var i=0;i<data.length;i++){
                        string += '<tr>';
                        string += '    <td><input value="'+data[i].mailId+'" type="checkbox" name="choice" lay-filter="choice" lay-skin="primary"></td>';
                        string += '    <td>';
                        // string += '        <img src="/resources/web/images/main-icon9-cur.png" style="margin:0 3px" alt="">';
                        if(data[i].hasAttachment){
                            string += '        <img src="/resources/web/images/accessory-icon9.png" style="margin:0 3px;" alt="">';
                        }
                        string += '    </td>';
                        string += '    <td>'+data[i].title+'</td>';
                        string += '    <td>'+data[i].lastName+'</td>';
                        string += '    <td>'+data[i].allReceiveName+'</td>';
                        string += '    <td>'+data[i].createTime+'</td>';
                        // string += '    <td style="text-align:right;"><span class="star '+(data[i].attention?'active':'')+'"></span></td>';
                        string += '</tr>';
                    }
                }else{
                    string += '<tr>';
                    string += '    <td colspan="6" style="text-align: center;">';
                    string += '       没有结果！';
                    string += '    </td>';
                    string += '</tr>';
                }
                this.templateTag.append(string);
                form.render();
            }.bind(that));
        };
        // 每页显示几条
        form.on('select(item-num)', function(data){
            itemNum = $('select[name="item-num"]').val();
            AwaitPages.prototype = new Pages({
                type:'post',
                url:'/web/waitsend/find-like-wait-send.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    startTime : startTime,
                    endTime :endTime,
                    likeName:$('#search').val(),
                    pageRows:itemNum
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            awaitPages = new AwaitPages();
            awaitPages.getData();
        });

        // 查询数据加载
        $('#search_btn').on('click',function(){
            AwaitPages.prototype = new Pages({
                type:'post',
                url:'/web/waitsend/find-like-wait-send.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    startTime : startTime,
                    endTime :endTime,
                    likeName:$('#search').val(),
                    pageRows:itemNum
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            awaitPages = new AwaitPages();
            awaitPages.getData();
        });
        // 查询数据加载
        $('#search').on('keydown',function(e){
            if(e.keyCode === 13){
                AwaitPages.prototype = new Pages({
                    type:'post',
                    url:'/web/waitsend/find-like-wait-send.htm',
                    data:{
                        userId:$.session.get('userId'),
                        page:1,
                        startTime : startTime,
                        endTime :endTime,
                        likeName:$('#search').val(),
                        pageRows:itemNum
                    },
                    templateTag:'.oa-read-table .layui-table tbody'
                });
                awaitPages = new AwaitPages();
                awaitPages.getData();
            }
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
                };
                AwaitPages.prototype = new Pages({
                    type:'post',
                    url:'/web/waitsend/find-like-wait-send.htm',
                    data:{
                        userId:$.session.get('userId'),
                        page:1,
                        startTime : startTime,
                        endTime :endTime,
                        likeName:$('#search').val(),
                        pageRows:itemNum
                    },
                    templateTag:'.oa-read-table .layui-table tbody'
                });
                awaitPages = new AwaitPages();
                awaitPages.getData();
            }

        });
        // 删除
        $('.oa-read-delete').on('click',function(){
            var choice = $('.oa-read-table tbody input[name="choice"]:checked');
            var choiceArr = [];
            var layer_del;
            for(var i=0;i<choice.length;i++){
                choiceArr.push(choice[i].value);
            };
            // 判断是否有选择删除的，如果没有不提交数据
            if(choiceArr.length>0){
                layer.confirm(
                    '删除后将无法恢复，您确定要删除吗？',
                    {
                        btn: ['是','否'],
                        title:'删除提示'
                    }, //按钮
                    function(index, layero){
                        //按钮【按钮一】的回调
                        $.ajax({
                            type:'post',
                            url:'/web/waitsend/delete-wait-send.htm',
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
                                if(res.code === '200'){
                                    awaitPages.getData(parseInt(currPage));
                                    setTimeout(function(){
                                        layer.msg('删除成功',{time: 1000,icon: 1});
                                    },200)

                                };
                            },
                            error:function(xhr){
                                layer.close(layer_del)
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
        $(document).on('click','.oa-read-table .layui-table tbody tr td',function(){
            var value = $(this).parent('tr').children().children('input[name=choice]').val();
            if(value === undefined || $(this).index() === 0){
                return  false;
            };
            window.location.href = 'new.htm?listId='+value+'';
        });
    });
});
