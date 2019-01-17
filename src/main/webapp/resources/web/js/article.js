//一般直接写在一个js文件中
var articleObjext;//传阅对象变量
var commentPage;//评论对象变量
var sendUrl;//声明评论发送url接口变量
var stepStatus;//状态
var createdId;
//JavaScript代码区域
$(document).ready(function(){
    layui.use(['element','form','layer','jquery'], function(){

        var element = layui.element;
        var layer = layui.layer;
        var form = layui.form;
        var $ = layui.jquery;
        var storageData = {};
        var commentPage = null;
        if(getQueryString('userId') && getQueryString('mailStatus') && getQueryString('article')){
            // $.session.set('userId',getQueryString('userId'));
            // $.session.set('mailStatus',getQueryString('mailStatus'));
            // $.session.set('article',getQueryString('article'));
            storageData.userId = getQueryString('userId');
            storageData.mailStatus = getQueryString('mailStatus');

            storageData.article = getQueryString('article');
        }else{
            storageData.userId = $.session.get('userId');
            storageData.mailStatus = $.session.get('mailStatus');
            storageData.article = $.session.get('article');
        }
        $('#content').scroll(function(){
            var scrollTop = $(this).scrollTop()
            if(scrollTop>53){
                $('.oa-receive-right').addClass('active')
            }else{
                $('.oa-receive-right').removeClass('active')
            }
        });
        // 继承
        function extend(Child,Parent){
            var F = function(){};
            F.prototype = Parent.prototype;
            Child.prototype = new F();
            Child.prototype.constructor = Child;
            Child.uber = Parent.prototype;
        };
        // 加载传阅对象
        function ArticleObjext(options){
            this.type = options.type;
            this.url = options.url;
            this.data = options.data;
            this.pageRows;
            this.totalRecord;//总记录数
            this.curPage = 1;//当前页
            this.templateTag = $(options.templateTag);
        };
        extend(ArticleObjext,Pages);
        ArticleObjext.prototype.templateHTML = function(that,data){
            layui.use(['form','table'], function(){
                $('.object-tab .title-left').html('传阅对象（'+this.totalRecord+'个）');
                $('input[name="object-total"]').val(this.totalRecord);
                var form = layui.form;
                this.templateTag.empty();//清空上次内容
                var string = '';
                if(data.length>0){
                    for(var i=0;i<data.length;i++){
                        string += '<tr>';
                        string += '	<td><input value="'+data[i].userId+'" type="checkbox" name="choice" lay-filter="choice" lay-skin="primary"></td>';
                        string += '	<td>'+data[i].lastName+'</td>';
                        if(data[i].confirmRecord == null || data[i].confirmRecord == ''){
                            string += '	<td>暂无记录</td>';
                        }else{
                            var remark = data[i].acRecord===null?data[i].confirmRecord:data[i].acRecord;
                            string += '	<td>'+remark+'</td>';
                        };
                        string += '	<td>'+data[i].sendTime+'</td>';

                        if(data[i].openTime == null || data[i].openTime == ''){
                            string += '	<td>暂无记录</td>';
                        }else{
                            string += '	<td>'+data[i].openTime+'</td>';
                        };
                        //string += '	<td>'+data[i].openTime == null ? '暂无' : data[i].openTime +'</td>';

                        if(data[i].mailStatusss===0){
                            string += '	<td>未读</td>';
                        }else if(data[i].mailStatusss===1){
                            string += '	<td>已读</td>';
                        }else if(data[i].mailStatusss===2){
                            string += '	<td>确认</td>';
                        }
                        string += '	<td style="text-align:right;">';
                        if(data[i].authority === true){
                            if(storageData.mailStatus==='2' || stepStatus===3){

                                string += '		<button type="button" title="删除" class="tab-icon tab-dele layui-disabled" disabled ><img src="/resources/web/images/handle04.png" alt=""></button>';
                            }else{
                                string += '		<button type="button" title="删除" class="tab-icon tab-dele "><img src="/resources/web/images/handle04.png" alt=""></button>';
                            }
                        }
                        string += '	</td>';
                        string += '</tr>';
                    }
                }else{
                    string += '<tr>';
                    string += '    <td colspan="7" style="text-align: center;">';
                    string += '       暂无传阅对象！';
                    string += '    </td>';
                    string += '</tr>';
                }
                var colgroup = '<colgroup>\
                                        <col width="50">\
                                        <col width="100">\
                                        <col>\
                                        <col width="170">\
                                        <col width="170">\
                                        <col width="80">\
                                        <col width="80">\
                                    </colgroup>'
                this.templateTag.append(colgroup+'<tbody>'+string+'</tbody>');
                form.render();
                $('.oa-receive-table .layui-table-header thead .layui-unselect').removeClass('layui-form-checked');
                $('.oa-read-table thead input[name="all-object"]').prop('checked',false);
                objTable();
                $(window).resize(function(){
                    objTable();
                });
                // 取消全选按钮
                form.on('checkbox(choice)', function(data){
                    var allObject = $(data.elem).parents('.layui-table-body').siblings('.layui-table-header').find('input[name="all-object"]');
                    var checkedTag = $(data.elem).parents('.layui-table-body').siblings('.layui-table-header').find('.layui-unselect');
                    checkedTag.removeClass('layui-form-checked');
                    allObject.prop('checked',false);
                });
                // 单个删除
                var totalRecord = data.length;
                $('.object-tab .layui-table .tab-icon').on('click',{totalRecord:totalRecord},function(e){
                    // debugger
                    // if(e.data.totalRecord>1){
                    var arr = [$(this).parent().siblings().children('input[name="choice"]').val()];
                    layer.confirm(
                        '您确定要删除该传阅对象吗？',
                        {
                            btn: ['是','否'],
                            title:'删除提示'
                        }, //按钮
                        function(index, layero){
                            //按钮【按钮一】的回调
                            deleObjext(arr);
                        }
                    );
                    // }else{
                    //     //layer.msg('删除失败，传阅对象不能为空',{time: 2000});
                    //     layer.msg('传阅对象只有一个用户时，不允许删除',{time: 2000});
                    // }
                })
            }.bind(that));
        };
        // 页面进来加载
        function getLoading(isUpdate){
            $.ajax({
                type:'post',
                url:'/web/received/find-mail-particulars.htm',
                dataType:'json',
                data:{
                    userId:storageData.userId,
                    mailId:storageData.article,
                    mailStatus:parseInt(storageData.mailStatus)
                },
                beforeSend:function(){
                    layer_del = layer.msg('数据加载中', {
                        icon: 16,
                        shade: 0.01,
                        area:'40px',
                        time:0
                    });
                },
                success:function(res){
                    layer.close(layer_del);
                    if(res.code === '200'){
                        getArticle(res.data);
                        if(isUpdate === false)return;
                        articleObjext = new ArticleObjext({
                            type:'post',
                            url:'/web/received/find-mail-object.htm',
                            dataType:'json',
                            data:{
                                userId:storageData.userId,
                                mailId:storageData.article,
                                page:$('.layui-table-footer .layui-laypage input').val(),
                                pageRows:$('.layui-table-footer .layui-form-select .layui-input').val()
                            },
                            templateTag:'.object-tab .layui-table-body table'
                        });
                        articleObjext.getData();
                    }else{
                        layer.msg('数据加载出错',{time: 2000,icon: 2});
                    }
                },
                error:function(){
                    layer.close(layer_del);
                    layer.msg('数据加载出错',{time: 2000,icon: 2});
                }
            });
            function getArticle(data){
                var attention;
                createdId = data.userId;
                stepStatus = data.stepStatus;
                var statusName = '';
                switch(storageData.mailStatus){
                    case '3':
                        if(data.ifConfirmss === true){
                            if(data.afreshConfimss === true){
                                $('.btn-all').html('<button type="button" class="layui-btn layui-btn-primary anew-btn" data-confirm="false">重新确认</button><button type="button" class="layui-btn layui-btn-primary goback-btn" onclick="javascript:history.go(-1);">返回</button>');
                            }else{
                                $('.btn-all').html('<button type="button" class="layui-btn layui-btn-primary goback-btn" onclick="window.location.href = document.referrer;">返回</button>');
                            };
                        }else{
                            $('.btn-all').html('<button type="button" class="layui-btn layui-btn-primary affirm-btn" data-confirm="true">确认</button><button type="button" class="layui-btn layui-btn-primary goback-btn" onclick="javascript:history.go(-1);">返回</button>');
                        }
                        $('.layui-body .content-body h1').html('收到的传阅');
                        sendUrl = '/web/received/insert-discuss.htm';
                        attention = data.receiveAttention;
                        break;
                    case '1':
                        $('.btn-all').html('<button type="button" class="layui-btn layui-btn-primary goback-btn" onclick="window.location.href = document.referrer;">返回</button>');
                        $('.layui-body .content-body h1').html('已发传阅');
                        sendUrl = '/web/send/insert-send-discuss.htm';
                        attention = data.attention;
                        break;
                    case '2':
                        $('.btn-all').html('<button type="button" class="layui-btn layui-btn-primary goback-btn" onclick="window.location.href = document.referrer;">返回</button>');
                        $('.layui-body .content-body h1').html('已删除');
                        $('.oa-receive-table .title-right').empty();
                        $('.oa-receive-right .input-addon').attr('disabled','disabled').addClass('layui-disabled');
                        attention = data.attention;
                        break;
                };
                // 判断新增人员按钮是否有权限
                if(!data.ifAdd){
                    $('.oa-receive-table .title-right .annex-add').remove()
                };
                // 判断上传按钮是否有权限
                if(!data.ifUpload){
                    $('.oa-receive-table .title-right .annex-up').remove();
                };
                // 判断是已完成状态
                if(stepStatus == 1){
                    statusName='传阅中';
                }else if(stepStatus == 2){
                    statusName='待办传阅';
                }else if(stepStatus == 3){
                    $('.oa-receive-table .title-right .annex-up').remove();
                    $('.object-tab .title-right').empty();
                    $('.btn-all .anew-btn').remove();
                    statusName='已完成';
                }
                if(storageData.mailStatus == 2){
                    statusName='已删除';
                }
                // 传阅主题
                var liHTML = '<li>'+
                    '	<strong>传阅主题：</strong>'+
                    '	<p>'+data.title+'</p>'+
                    '	<button type="button" id="'+storageData.article+'" class="star '+(attention?'active':'')+' '+(storageData.mailStatus==='2'?'layui-disabled':'')+'" '+(storageData.mailStatus==='2'?'disabled':'')+'></button>'+
                    '</li>'+
                    '<li>'+
                    '	<strong>发 件 人：</strong>'+
                    '	<p>'+data.lastName+'</p>'+
                    '</li> '+
                    '<li>'+
                    '	<strong>接 收 人：</strong>'+
                    '	<p>'+data.allReceiveName+'</p>'+
                    '</li> '+
                    '<li>'+
                    // '<li>'+
                    // '	<strong>传阅规则：</strong>'+
                    // '	<p>'+data.ruleName+'</p>'+
                    // '</li>  '+
                    '<li>'+
                    '	<strong>'+(data.attachmentItemss.length===0?'':'附      件：')+'</strong>'+
                    '	<p>'+(data.attachmentItemss.length === 0?'':data.attachmentItemss.length)+'</p>'+
                    '</li>  '+
                    '<li>'+
                    '	<strong>传阅状态：</strong>'+
                    '	<p>'+statusName+'</p>'+
                    '</li>'+
                    '<li>'+
                    '	<strong>传阅时间：</strong>'+
                    '	<p>'+(storageData.mailStatus==3?data.receiveTime:data.sendTime)+'</p>'+
                    '</li>';
                $('.oa-receive-list').html(liHTML);
                var iframe = $('.oa-import-content')[0];
                var iframeNum = 5;
                var iframeTimer = null;
                iframe.src = 'rich_text.htm?userId='+storageData.userId+'&mailId='+storageData.article+'';
                function reinitIframe(){
                    if(iframeNum == 0){
                        clearInterval(iframeTimer);
                    }
                    try{
                        var bHeight = iframe.contentWindow.document.body.scrollHeight;
                        var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
                        var height = Math.max(bHeight, dHeight);
                        if(height>900){
                            iframe.height = 900
                        }else{
                            iframe.height = height;
                        }
                    }catch (ex){}
                    iframeNum--;
                }
                iframeTimer = window.setInterval(reinitIframe, 200);
                // $('.oa-import-content').html(data.mailContent);//传阅内容
                // 附件
                $('.attachmentItems-tab .table-title .title-left').html('附件（'+data.attachmentItemss.length+'个）')
                var attachmentItemss = data.attachmentItemss.reverse();//附件倒序
                var attachmentTHML = '';
                if(data.attachmentItemss.length>0){
                    for(var j=0;j<attachmentItemss.length;j++){
                        var state;
                        switch(attachmentItemss[j].state){
                            case 0:
                                state = '-';
                                break;
                            case 1:
                                state = '修订';
                                break;
                            case 2:
                                state = '新增';
                                break;
                            default:
                                state = '-';
                                break;
                        };
                        // attachmentTHML += '<tr>'+
                        // '	<td><input type="checkbox" lay-filter="attachment_choice" value="'+attachmentItemss[j].itemId+'" name="attachment_choice" lay-skin="primary"></td>'+
                        // '	<td>'+state+'</td>'+
                        // '	<td>'+attachmentItemss[j].fileName+'</td>'+
                        // '	<td>'+attachmentItemss[j].creator+'</td>'+
                        // '	<td>'+attachmentItemss[j].itemSize+'</td>'+
                        // '	<td>'+attachmentItemss[j].createTime+'</td>'+
                        // '	<td style="text-align:right;">'+
                        // '		<button class="tab-icon tab-preview" data-bulk="'+attachmentItemss[j].bulkId+'" title="预览" ><img src="/resources/web/images/handle01.png" alt=""></button>'+
                        // '		<button class="tab-icon tab-redact '+($.session.get('mailStatus')==='2'?'layui-disabled':(data.ifUpdate===false?'layui-disabled':(stepStatus===3?'layui-disabled':'')))+'" title="编辑" '+($.session.get('mailStatus')==='2'?'disabled':(data.ifUpdate===false?'disabled':(stepStatus===3?'disabled':'')))+'><img src="/resources/web/images/handle02.png" alt=""></button>'+
                        // '		<button class="tab-icon tab-down" title="下载" ><img src="/resources/web/images/handle03.png" alt=""></button>'+
                        // '		<button class="tab-icon tab-dele '+($.session.get('mailStatus')==='2'?'layui-disabled':(stepStatus===3?'layui-disabled':''))+'" '+($.session.get('mailStatus')==='2'?'disabled':(stepStatus===3?'disabled':''))+' title="删除" ><img src="/resources/web/images/handle04.png" alt="" ></button>'+
                        // '	</td>'+
                        // '</tr>';
                        attachmentTHML += '<tr>';
                        attachmentTHML += '	<td><input type="checkbox" lay-filter="attachment_choice" value="'+attachmentItemss[j].itemId+'" name="attachment_choice" lay-skin="primary"></td>';
                        attachmentTHML += '	<td>'+state+'</td>';
                        attachmentTHML += '	<td>'+attachmentItemss[j].fileName+'</td>';
                        attachmentTHML += '	<td>'+attachmentItemss[j].creator+'</td>';
                        attachmentTHML += '	<td>'+attachmentItemss[j].itemSize+'</td>';
                        attachmentTHML += '	<td>'+attachmentItemss[j].createTime+'</td>';
                        attachmentTHML += '	<td style="text-align:right;">';
                        attachmentTHML += '		<!--<button class="tab-icon tab-preview" data-bulk="'+attachmentItemss[j].bulkId+'" title="预览" ><img src="/resources/web/images/handle01.png" alt=""></button>-->';
                        if(storageData.mailStatus==='2'||data.ifUpdate===false ||stepStatus===3){
                            attachmentTHML += '		<button type="button" class="tab-icon tab-redact layui-disabled" disabled title="编辑"><img src="/resources/web/images/handle02.png" alt=""></button>';
                        }else{
                            attachmentTHML += '		<button type="button" class="tab-icon tab-redact" title="编辑"><img src="/resources/web/images/handle02.png" alt=""></button>';
                        }
                        attachmentTHML += '		<button type="button" class="tab-icon tab-down" title="下载"><img src="/resources/web/images/handle03.png" alt=""></button>';
                        if(storageData.mailStatus==='2'||stepStatus===3){
                            attachmentTHML += '		<button type="button" class="tab-icon tab-dele layui-disabled" disabled title="删除"><img src="/resources/web/images/handle04.png" alt=""></button>';
                        }else{
                            attachmentTHML += '		<button type="button" class="tab-icon tab-dele" title="删除"><img src="/resources/web/images/handle04.png" alt=""></button>';
                        }
                        attachmentTHML += '	</td>';
                        attachmentTHML += '</tr>';
                    }
                }else{
                    attachmentTHML += '<tr>'+
                        '    <td colspan="7" style="text-align: center;">'+
                        '       暂无附件！'+
                        '    </td>'+
                        '</tr>';
                }
                $('.attachmentItems-tab .table-box tbody').html(attachmentTHML);
                layui.use(['form'], function(){
                    var form = layui.form;
                    form.render()
                });
            };
        };
        getLoading();
        // 收到传阅的确认与重新确认按钮
        $(document).on('click','.btn-all .affirm-btn,.btn-all .anew-btn',function(e){
            layer.prompt({
                maxlength: 0,
                formType: 2,
                skin: 'layui-dialog',
                title: '确认信息',
                allowBlank:true,
                value:'已阅',
                area: ['500px', '127px'], //自定义文本域宽高
                success: function(layero, index){
                    $(layero).find('.layui-layer-input').attr('placeholder','请输入传阅的确认信息备注')
                }
            }, function(value, index, elem){
                var value = $.trim($(elem).val());
                if(value===''){
                    layer.msg('请输入内容',{time: 2000,icon: 2});
                    return false;
                }
                if(value.length>40){
                    layer.msg('确认内容不能超过40个字',{time: 2000,icon: 2});
                    return false;
                }
                $.ajax({
                    type:'post',
                    url:'/web/received/insert-confirm.htm',
                    dataType:'json',
                    data:{
                        userId:storageData.userId,
                        mailId:storageData.article,
                        remark:$(elem).val(),
                        statusConfirm:$(this).data('confirm')
                    },
                    beforeSend:function(){
                        layer.msg('正在提交中', {
                            icon: 16,
                            shade: 0.01,
                            area:'40px',
                            time:0
                        });
                    },
                    success:function(res){
                        layer.closeAll();
                        if(res.code == 200){
                            getLoading();
                            articleObjext.getData();
                            layer.msg('确认成功',{time: 2000,icon: 1});
                        }else{
                            layer.msg(res.msg,{time: 2000,icon: 2});
                        }

                    },
                    error:function(error){
                        layer.closeAll('dialog')
                        // debugger
                        layer.msg('网络出错',{time: 2000,icon: 2});
                    }
                })
            }.bind(this));
        });
        // 附件单选按钮
        form.on('checkbox(attachment_choice)', function(data){
            $(data.elem).parents('tbody').siblings('thead').find('input[name="all-checked"]').prop('checked',false);
            $(data.elem).parents('tbody').siblings('thead').find('.layui-unselect').removeClass('layui-form-checked');
        });
        // 附件全选按钮
        form.on('checkbox(attachment-checked)', function(data){
            if(data.elem.checked === true){
                $(data.elem).parents('thead').siblings('tbody').find('.layui-unselect').addClass('layui-form-checked');
                $(data.elem).parents('thead').siblings('tbody').find('input[name="attachment_choice"]').prop('checked',true);
            }else{
                $(data.elem).parents('thead').siblings('tbody').find('.layui-unselect').removeClass('layui-form-checked');
                $(data.elem).parents('thead').siblings('tbody').find('input[name="attachment_choice"]').prop('checked',false);
            }
        });
        // 单个下载
        $(document).on('click','.attachmentItems-tab .layui-table .tab-down',function(){
            var itemId = $(this).parent().siblings().children('input[name="attachment_choice"]').val();
            downAttachment({
                url:'/web/received/send-download-file.htm',
                itemArr:itemId
            })
        });
        // 批量下载
        $(document).on('click','.attachmentItems-tab .title-right .annex-down',function(){
            var downArr = [];
            var checked = $('input[name="attachment_choice"]:checked');
            for(var i=0;i<checked.length;i++){
                downArr.push(checked[i].value)
            }
            if(downArr.length<=0){
                layer.msg('请选择附件',{time: 2000});
                return false;
            }
            downAttachment({
                url:'/web/send/send-download-file.htm',
                itemArr:downArr
            })
        })
        // 下载函数
        function downAttachment(options){
            $.ajax({
                url:options.url,
                type:'post',
                data:{
                    itemId:options.itemArr,
                    mailId:storageData.article
                },
                dataType:'json',
                success:function(res){
                    if(res.code === '200'){
                        window.location.href = res.data;
                        // window.location.href = encodeURI(res.data.url+res.data.fileName);
                        // var $eleForm = $("<form method='get'></form>");
                        // $eleForm.attr("action",res.data);
                        // $(document.body).append($eleForm);
                        // //提交表单，实现下载
                        // $eleForm.submit();
                    }else{
                        layer.msg(res.msg,{time: 2000});
                    }
                },
                error:function(){
                    layer.msg('网络异常',{time: 2000});
                }
            })
        };
        // 删除附件
        $(document).on('click','.attachmentItems-tab .layui-table .tab-dele',function(){
            var delId = $(this).parent().siblings().children('input[name="attachment_choice"]').val();
            layer.confirm(
                '您确定要删除该附件吗？',
                {
                    btn: ['是','否'],
                    title:'删除提示'
                }, //按钮
                function(index, layero){
                    //按钮【按钮一】的回调
                    delAttachment({
                        data:{
                            userId:storageData.userId,
                            itemId:delId,
                            mailId:storageData.article
                        },
                        then:function(res){
                            if(res.code === '200'){
                                getLoading();
                                setTimeout(function(){
                                    layer.msg('附件删除成功',{time: 2000,icon: 1});
                                },200)
                            }else if(res.code === '205'){
                                layer.msg('你没有权限删除该附件!',{time: 2000,icon: 2});
                            }else{
                                layer.msg(res.msg,{time: 2000,icon: 2});
                            };
                        },
                        fail:function(){
                            layer.msg('附件删除失败',{time: 2000,icon: 2});
                        }
                    })
                }
            );

        });
        // 附件预览
        $(document).on('click','.attachmentItems-tab .layui-table .tab-preview',function(){
            var itemId = $(this).parent().siblings().children('input[name="attachment_choice"]').val();
            var bulkId = $(this).data('bulk');
            var data = {
                itemId:itemId,
                bulkId:bulkId
            }
            preview(data)
        });
        // 预览函数
        function preview(data){
            $.ajax({
                url:'/web/createmail/preview-file.htm',
                type:'post',
                data:data,
                dataType:'json',
                async:false,
                success:function(res){
                    if(res.code === '200'){
                        //window.location.href = res.data.url;
                        window.open(res.data.url,'_blank');

                    }else if(res.code === '205'){
                        layer.msg(res.msg,{time: 2000});
                    }else{
                        layer.msg(res.msg,{time: 2000});
                    }
                },
                error:function(){
                    layer.msg('网络出错',{time: 2000});
                }
            })
        };
        // 编辑按钮
        $(document).on('click','.attachmentItems-tab .layui-table .tab-redact',function(){
            var itemId = $(this).parent().siblings().children('input[name="attachment_choice"]').val();
            redact(itemId)
        });
        // 编辑函数
        function redact(itemId){
            $.ajax({
                url:'/web/received/edit-file-sdk.htm',
                type:'post',
                data:{
                    itemId:itemId,
                    userId:$.session.get('userId')
                },
                dataType:'json',
                async:false,
                success:function(res){
                    if(res.code === '200'){
                        //                    window.location.href = res.data.editUrl;
                        window.open(res.data.editUrl,'_blank')
                        //                    var link = document.createElement('a');
                        //                    link.target = "_blank";
                        //                    link.href = res.data.editUrl;
                        //                     document.body.appendChild(link);
                        //                    link.click();
                    }else{
                        layer.msg(res.msg,{time: 2000});
                    }
                },
                error:function(){
                    layer.msg('网络出错',{time: 2000});
                }
            })
        }

        // 每页显示几条
        form.on('select(item-num)', function(data){
            articleObjext = new ArticleObjext({
                type:'post',
                url:'/web/received/find-mail-object.htm',
                dataType:'json',
                data:{
                    userId:storageData.userId,
                    mailId:storageData.article,
                    page:1 ,
                    pageRows:data.value
                },
                templateTag:'.object-tab .layui-table-body table'
            });
            articleObjext.getData();
        });

        // 批量删除
        $('.object-tab .title-right .annex-dele').on('click',function(e){
            var choice = $(this).parents('.object-tab').find('.layui-table-body tbody tr').children().children('input[name="choice"]:checked');
            var choiceArr = [];
            var newChoiceArr = [];
            var totalRecord = parseInt($('input[name="object-total"]').val());
            for(var i=0;i<choice.length;i++){
                choiceArr.push(choice[i].value)
            };
            if(choiceArr.length<=0){
                layer.msg('请选择传阅对象',{time: 2000});
                return false;
            }else if(totalRecord == choiceArr.length){
                newChoiceArr = choiceArr;
                // if(newChoiceArr.length<=0){
                //     layer.msg('传阅对象只有一个用户时，不允许删除',{time: 2000});
                //     return false;
                // };
                layer.confirm(
                    '您确定要删除所选中的传阅对象吗？',
                    {
                        btn: ['是','否'],
                        title:'删除提示'
                    }, //按钮
                    function(index, layero){
                        //按钮【按钮一】的回调
                        deleObjext(newChoiceArr);
                    }
                );
            }else{
                layer.confirm(
                    '您确定要删除所选中的传阅对象吗？',
                    {
                        btn: ['是','否'],
                        title:'删除提示'
                    }, //按钮
                    function(index, layero){
                        //按钮【按钮一】的回调
                        deleObjext(choiceArr);
                    }
                );
            };
        });

        // 传阅对象删除函数
        function deleObjext(arrId){
            var layer_msg;
            $.ajax({
                tyep:'post',
                url:'/web/received/batch-delete-received.htm',
                dataType:'json',
                data:{
                    userId:storageData.userId,
                    mailId:storageData.article,
                    receiveUserId:arrId
                },
                beforeSend:function(xhr){
                    layer_msg = layer.msg('正在删除中', {
                        icon: 16,
                        shade: 0.01,
                        area:'40px',
                        time:0
                    });
                },
                success:function(res){
                    layer.close(layer_msg);
                    if(res.code === '200'){
                        setTimeout(function(){
                            layer.msg('联系人删除成功',{time: 2000,icon: 1});
                        },200)
                        getLoading(false)
                        $('input[name="all-object"]').prop('checked',false);
                        var objCurrPage = $('.object-tab .layui-laypage input').val();
                        articleObjext.getData(parseInt(objCurrPage));
                    }else if(res.code === '205'){
                        layer.msg('删除传阅对象失败!,你没有权限删除',{time: 2000});
                    }else if(res.code === '501'){
                        layer.msg('传阅对象只有一个用户时，不允许删除',{time: 2000});
                    }else{
                        layer.msg(res.msg,{time: 2000});
                    }
                },
                error:function(){
                    layer.close(layer_msg);
                    layer.msg('网络出错',{time: 2000});
                }
            })
        }
        // 全选按钮
        form.on('checkbox(all-object)', function(data){
            if(data.elem.checked === true){
                $(data.elem).parents('.layui-table-header').siblings('.layui-table-body').find('.layui-unselect').addClass('layui-form-checked');
                $(data.elem).parents('.layui-table-header').siblings('.layui-table-body').find('input[name="choice"]').prop('checked',true);
            }else{
                $(data.elem).parents('.layui-table-header').siblings('.layui-table-body').find('.layui-unselect').removeClass('layui-form-checked');
                $(data.elem).parents('.layui-table-header').siblings('.layui-table-body').find('input[name="choice"]').prop('checked',false);
            }
        });

        // 评论加载
        commentPage = new CommentPage({
            type:'post',
            url:'/web/send/grid-discuss.htm',
            dataType:'json',
            data:{
                mailId:storageData.article,
                pageRows:15,
                page:1
            }
        });
        commentPage.getPage();
        commentPage.clickPage();

        // 评论发送
        function commentSend(_this){
            var discussContent = $(_this).siblings('input[name=discussContent]');
            if(discussContent.val()===''){
                layer.msg('发送内容不能为空',{time: 2000});
                return false;
            };
            var layer_msg;
            $.ajax({
                type:'post',
                url:sendUrl,
                dataType:'json',
                data:{
                    userId:storageData.userId,
                    mailId:storageData.article,
                    discussContent:discussContent.val()
                },
                beforeSend:function(xhr){
                    layer_msg = layer.msg('正在发送中', {
                        icon: 16,
                        shade: 0.01,
                        area:'40px',
                        time:0
                    });
                },
                success:function(res){
                    layer.close(layer_msg);
                    if(res.code === '200'){
                        layer.msg('发送成功',{time: 2000});
                        $('.comment-box .interacts-list').html('');
                        $('input[name="discussContent"]').val('');
                        commentPage;
                        var commentPage = new CommentPage({
                            type:'post',
                            url:'/web/send/grid-discuss.htm',
                            dataType:'json',
                            data:{
                                mailId:storageData.article,
                                pageRows:15,
                                page:1
                            }
                        });
                        commentPage.getPage();
                    }else{
                        layer.msg('发送失败',{time: 2000});
                    }
                },
                error:function(){
                    layer.msg('网络出错',{time: 2000});
                }
            })
        }
        $('.oa-receive-right .oa-form-input input[name="discussContent"]').on('keyup',function(e){
            if(e.keyCode === 13){
                commentSend($('.oa-receive-right .input-addon'))
            }
        })
        $('.oa-receive-right .input-addon').on('click',function(){
            commentSend(this)
        });

        function upFile(){
            var $list = $('#thelist');
            var uploader = WebUploader.create({
                method: "POST",
                // 选完文件后，是否自动上传。
                auto: true,
                // swf文件路径
                swf: '/resources/web/webupload/Uploader.swf',

                // 文件接收服务端。
                server: '/web/received/insert-upload-sdk.htm',
                // server: 'http://resource.duapp.com/resource_not_found.html',
                formData:{
                    userId:storageData.userId,//用户id
                    mailId:storageData.article//传阅的ID
                    // fullname:'武当派',//分部
                    // loginid:'一代宗师',//用户登录名
                    // lastname:'张三丰'//当前用户姓名
                },
                // 选择文件的按钮。可选。
                // 内部根据当前运行是创建，可能是input元素，也可能是flash.
                // pick: '#filePicker',
                pick: {
                    id: '#annex-up',
                    multiple:true //是否开起同时选择多个文件能力。
                },
                //sendAsBinary:true,
                duplicate :true, // [默认值：undefined] 去重， 根据文件名字、文件大小和最后修改时间来生成hash Key.
                compress:false,//不压缩
                // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
                resize: false,
                // chunked: true,//分开上传
                threads :1//上传并发数。允许同时最大上传进程数
                // chunkSize:10,//大于多好B分开上传
            });
            uploader.on( 'beforeFileQueued', function( file ) {
                var reg = /^(dwt|dwg|dws|dxf)$/i;
                if(reg.test(file.ext)){
                    //        		var layer = layui.layer;
                    layer.msg('目前不支持<span style="color:#008cd6;">'+file.ext+'</span>格式',{time: 2000});
                    return false;
                }
            })
            // 当有文件被添加进队列的时候
            uploader.on( 'fileQueued', function( file,data ) {
                // layui.use('layer', function(){
                //     var layer = layui.layer;
                layer.msg('正在上传中', {
                    icon: 16,
                    shade: 0.01,
                    area:'40px',
                    time:0
                });
                // })

            });
            // 当文件上传成功时触发。
            uploader.on( 'uploadSuccess', function( file,data ) {
                if(data.code === '200'){
                    getLoading();
                    setTimeout(function(){
                        layer.msg('上传成功',{time: 2000});
                    },100)
                }else if(data.code === '205'){
                    setTimeout(function(){
                        layer.msg(data.msg,{time: 2000});
                    },100)

                }else{
                    setTimeout(function(){
                        layer.msg(data.msg,{time: 2000});
                    },100)
                }

            });
            // 上传出错
            uploader.on( 'uploadError', function( file ) {
                layer.msg('上传失败',{time: 2000});
            });
            // 不管成功或者失败，文件上传完成时触发
            uploader.on( 'uploadComplete', function(file) {
                layer.closeAll('dialog'); //关闭信息框
            });
        }
        if(!WebUploader.Uploader.support()){
            $('.oa-receive-table .title-right .annex-up').on('click',function(){
                layer.msg('上传附件功能 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器',{time: 2000});
            })
        }else{
            upFile();
        }

        // 新增联系人
        $('.oa-receive-table .title-right .annex-add').on('click',function(){
            layer.open({
                type: 1,
                title: '请选择',
                area: ['80%','90%'],
                skin: 'dialog-contacts',
                content: $('.contacts-content'),//弹窗内容
                success:function(layero, index){
                    $('.contacts-box-r .contacts-table tbody').html('');
                    $('.contacts-box-l .contacts-table-outer').empty();
                    $.loadContactsData();
                    allLeftArrow();
                    allrightArrow();
                },
                allowBlank:true,
                btnAlign: 'c',//按钮对其方式
                btn: ['确认(0人)', '清除', '取消'],
                yes: function(index, layero){
                    var allCheckedArr = $('.contacts-box-r .contacts-table-outer table tr').find('input[name="contacts-choice"]');
                    var userTotal = [];
                    var subcompanyIds = [];
                    var departmentIds = [];
                    var receiveUserId = [];

                    var layer_msg;
                    for(var i=0;i<allCheckedArr.length;i++){
                        if($(allCheckedArr[i]).data('type') === 'userMssage'){
                            receiveUserId.push($(allCheckedArr[i]).data('receiveuserid'))
                        }else if($(allCheckedArr[i]).data('type') === 'subcompany'){
                            subcompanyIds.push($(allCheckedArr[i]).data('supsubcomid'))
                        }else if($(allCheckedArr[i]).data('type') === 'department'){
                            departmentIds.push($(allCheckedArr[i]).data('departmentid'))
                        }else if($(allCheckedArr[i]).data('type') === 'usertotal'){
                            userTotal.push($(allCheckedArr[i]).data('usertotal'))
                        }
                    }
                    if(receiveUserId.length == 0 && subcompanyIds.length == 0 && departmentIds.length == 0 && userTotal.length == 0){
                        layer.msg('请添加联系人',{time: 2000});
                        return false;
                    };
                    $.ajax({
                        type:'post',
                        url:'/web/received/insert-mail-object.htm',
                        data:{
                            userId:storageData.userId,//用户id
                            mailId:storageData.article,//传阅的ID
                            userTotal : userTotal.length,
                            subcompanyIds : subcompanyIds,
                            departmentIds : departmentIds,
                            receiveUserIds:receiveUserId

                        },
                        beforeSend:function(xhr){
                            layer_msg = layer.msg('正在请求数据中', {
                                icon: 16,
                                shade: 0.01,
                                area:'40px',
                                time:0
                            });
                        },
                        dataType:'json',
                        success:function(res){
                            layer.close(layer_msg);
                            if(res.code === '200'){
                                layer.close(index);
                                layer.msg('添加成功',{time: 2000});
                                getLoading()
                                // articleObjext = new ArticleObjext();
                                // articleObjext.getData(1);
                            }else{
                                layer.msg(res.msg,{time: 2000,icon: 2});
                            }
                        },
                        error:function(){
                            layer.close(layer_msg);
                            layer.msg('网络出错',{time: 2000,icon: 2});
                        }
                    })

                    //按钮【按钮一】的回调
                },
                btn2: function(index, layero){
                    console.log(123)
                    var allCheckedArr = $('.contacts-box-r .contacts-table-outer table tr').find('input[name="contacts-choice"]');
                    var isTree = $('.contacts-table-outer .contact-tree').length;
                    if(isTree>0){
                        addLeftTree(allCheckedArr);
                        treeUniq('right');
                    }else{
                        addRightTr(allCheckedArr,'.contacts-box-l .contacts-table-outer table tbody','left');
                        repetition('right');
                    }
                    allrightArrow();
                    allLeftArrow();
                    trItemSize();
                    return false;
                }
            });

        });

    });

})
// 判断传阅对象的对齐
function objTable(){
    var objScroll = hasScrolled($('.oa-receive-table .layui-table-body')[0], "vertical");
    if(objScroll){
        $('.oa-receive-table .layui-table-header').css({'padding-right':'28px'});
    }else{
        $('.oa-receive-table .layui-table-header').css({'padding-right':'10px'});
    };
    var THeaderH = $('.layui-table-header').height();
    $('.obj-table-header-height').css({'padding-top':THeaderH});
}
// 判断是否有滚动条
function hasScrolled(el, direction) {
    if(direction === "vertical") {
        return el.scrollHeight > el.clientHeight;
    }else if(direction === "horizontal") {
        return el.scrollWidth > el.clientWidth;
    }
}
// 评论
function CommentPage(options){
    this.type = options.type;
    this.url = options.url;
    this.data = options.data;
    this.curPage = 1;//当前页
    this.lastPage;
}
CommentPage.prototype = {
    aa:1,
    getPage:function(page){
        this.data.page = page === undefined?this.curPage:page
        $.ajax({
            type:this.type,
            url:this.url,
            dataType:'json',
            data:this.data,
            beforeSend:function(xhr){
                $('.interacts-record .more').html('正在加载中...');
            },
            success:function(res){
                if(res.code === '200'){
                    var moreH = $('.interacts-record .more').height();
                    var beforeUlh = $('.interacts-list').height();
                    var commentH = $('.comment-box').height();
                    $('.interacts-record .more').html('加载更多');
                    // 判断是否显示加载更多按钮
                    $('.interacts-bd').html('传阅讨论互动（'+res.data.totalRecord+'）');
                    if(res.data.totalRecord<15){
                        $('.interacts-record .more').remove();
                    };
                    for(var k=0;k<res.data.list.length;k++){
                        var liHTML = '';
                        liHTML += '<li>';
                        liHTML += '	<div class="user layui-clear">';
                        liHTML += '		<h4>'+res.data.list[k].lastName+'</h4>';
                        liHTML += '		<span>'+res.data.list[k].createTime+'</span>';
                        liHTML += '	</div>';
                        liHTML += '	<p>'+res.data.list[k].discussContent+'</p>';
                        liHTML += '</li>';
                        $('.comment-box .interacts-list').append(liHTML);
                    }
                    // 判断滚动条的位置
                    // var afterUlH = $('.interacts-list').height();
                    // if(res.data.firstPage === true){
                    //     $('.comment-box').scrollTop((moreH+afterUlH)-commentH);
                    // }else{
                    //     $('.comment-box').scrollTop(afterUlH-beforeUlh+moreH);
                    // }
                    this.lastPage = res.data.lastPage;
                    this.curPage = res.data.currentPage+1;
                    this.clickPage();
                }else{
                    $('.interacts-record .more').html('加载失败');
                }
            }.bind(this)
        });
    },
    clickPage:function(){
        // 点击加载更多
        $('.interacts-record .more').off('click').on('click',function(){
            if(this.lastPage){
                $('.interacts-record .more').html('已是最后一页')
                return false;
            };
            this.getPage(this.curPage);
        }.bind(this))
    }
};
