var loaduUrl = window.location.search.substr(1);//链接
var urlReg = /listId/;//正则
var editor;
var fileBulkId = null;
 //JavaScript代码区域
 $(document).ready(function(){
    layui.use(['element','form','layer'], function(){
        var element = layui.element;
        var form = layui.form;
        var layer = layui.layer;
        var ie9 = "Microsoft Internet Explorer" && navigator.appVersion.match(/9./i)=="9.";
        if(!WebUploader.Uploader.support()){
            $('.uploader-accessory #filePicker').on('click',function(){
                layer.msg('添加附件功能 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器',{time: 2000});
            })
        }else{
            uploaderFile();
        };
        // 公司分部
        $('.rightArea .org-branch-one').one('click',function(){
            // $.getContact(this,'new')
            $.getContact(this,{},'new')
//            console.log('1234')

        });
        $('.rightArea .org-branch-one').trigger('click').addClass('cur')
        $('.rightArea').off('click','.org-branch-two').on('click','.org-branch-two',function(){
            if($(this).siblings('.tree-box')[0])return;
            var options = {
                supsubcomid:$(this).data('supsubcomid'),
            }
            $.getContact(this,options,'new')
        });
        $('.rightArea').off('click','.org-branch-three').on('click','.org-branch-three',function(){
            if($(this).siblings('.tree-box')[0])return;
            var options = {
                departmentid: $(this).data('departmentid'),//公司分部ID
            }
            $.getContact(this,options,'new');
        })
        $('.rightArea').off('click','.org-branch-personnel').on('click','.org-branch-personnel',function(){
            addReceive(this)
        })
        // 模糊搜索
        $('#recipients').manifest({
            formatDisplay: function (data, $item, $mpItem) {
                return data.name;
            },
            formatValue: function (data, $value, $item, $mpItem) {
                return data;
            },
            marcoPolo: {
                url: '/web/createmail/find-like-hrm.htm',
                formatData: function (data) {
                    return data.data;
                },
                formatItem: function (data, $item) {
                    return data;
                }
            },
            required: true
        });

        // 更多发送选项
        $('.form-checked .more').on('click',function(){
            $('.form-checked  .form-checked-pact')
            if($(this).hasClass('cur')){
                $(this).removeClass('cur');
                $('.form-checked-pact').stop().slideUp();
            }else{
                $(this).addClass('cur');
                $('.form-checked-pact').stop().slideDown();
            }
        });
        // //实例化富文本编辑器
        if(navigator.appName == "Microsoft Internet Explorer"&&parseInt(navigator.appVersion.split(";")[1].replace(/[ ]/g, "").replace("MSIE",""))<9){
            layer.msg('您的浏览器版本过低，不支持富文本功能，请升级您的浏览器',{time: 2000});
        }
        editor = new Simditor({
			textarea: $('#editor'),
			toolbar: ['title', 'bold', 'italic', 'underline', 'strikethrough','|', 'ol', 'ul',  '|', 'image', 'hr', '|', 'indent', 'outdent'],
			pasteImage: true,//支持通过从剪贴板粘贴图像来上传
			locale:'en-US',//语言
			upload :{
				url : '/web/createmail/insert-upload-image.htm', //文件上传的接口地址
				params: null, //键值对,指定文件上传接口的额外参数,上传的时候随文件一起提交
				fileKey: 'fileData', //服务器端获取文件数据的参数名
				connectionCount: 3,
				leaveConfirm: '正在上传文件'
			}
		});
        awaitLoad();
        // 返回按钮
        $('.goback-btn').on('click',function(){
            var editorContent = UE.getEditor('editor').getContent();
            var send = $('.mf_container .mf_list').children();
            var theme = $('.layui-form-item #theme').val();
            if(editorContent != '' && send.length>0 && theme != ''){
                if(urlReg.test(loaduUrl)){
                    javascript:history.go(-1);
                }else{
                    layer.confirm(
                        '你是否放弃这次操作！',
                        {
                            btn: ['是','否'],
                            title:'离开提示'
                        }, //按钮
                        function(index, layero){
                            if(urlReg.test(loaduUrl)){
                                javascript:history.go(-1);
                            }else{
                                window.location.href = 'index.htm';
                            }
                            //按钮【按钮一】的回调
                        },
                        function(index){
                            //按钮【按钮二】的回调
                        }
                    );
                }
            }else{
                if(urlReg.test(loaduUrl)){
                    javascript:history.go(-1);
                }else{
                    window.location.href = 'index.htm';
                }
            }
        });
        // 新增联系人
        $('.table-input .add-search').on('click',function(){
            layer.open({
                type: 1,
                title: '请选择',
                area: ['80%','90%'],
                skin: 'dialog-contacts',
                content: $('.contacts-content'),//弹窗内容
                success:function(layero, index){
                    $('.contacts-box-r .contacts-table tbody').empty();
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
                    var beLi = $('.table-input .mf_container .mf_list li').children('input');
                    var receiveTag = $('.table-input .mf_container .mf_list');
                    // var numArr = [];
                    var newArr = [];
                    var userTotalArr =  [];
                    var subcompanyArr = [];
                    var departmentArr = [];
                    var receiveArr = [];
                    for(var i=0;i<beLi.length;i++){
                        receiveArr.push($(beLi[i]).data('userid'));
                    };
                    for(var j=0;j<allCheckedArr.length;j++){
                        if(receiveArr.indexOf($(allCheckedArr[j]).data('receiveuserid')) == -1){
                            newArr.push(allCheckedArr[j])
                        }
                    }
                    var liHTML = '';
                    for(var k=0;k<newArr.length;k++){
                        liHTML += '<li class="mf_item" role="option" aria-selected="false">';
                        liHTML +=      $(newArr[k]).data('receivelastname');
                        if($(newArr[k]).data('type') === 'usertotal'){
                            liHTML +=      $(newArr[k]).data('count');
                            liHTML += '    <input type="hidden" class="mf_value" data-type="'+$(newArr[k]).data('type')+'" data-usertotal="'+$(newArr[k]).data('usertotal')+'">';
                        }else if($(newArr[k]).data('type') === 'department'){
                            liHTML +=      $(newArr[k]).data('count');
                            liHTML += '    <input type="hidden" class="mf_value" data-type="'+$(newArr[k]).data('type')+'" data-departmentid="'+$(newArr[k]).data('departmentid')+'">';
                        }else if($(newArr[k]).data('type') === 'subcompany'){
                            liHTML +=      $(newArr[k]).data('count');
                            liHTML += '    <input type="hidden" class="mf_value" data-type="'+$(newArr[k]).data('type')+'" data-supsubcomid="'+$(newArr[k]).data('supsubcomid')+'">';
                        }else{
                            liHTML += '    <input type="hidden" class="mf_value" data-type="'+$(newArr[k]).data('type')+'" data-userid="'+$(newArr[k]).data('receiveuserid')+'">';
                        }
                        liHTML += '    <a href="#" class="mf_remove layui-icon" title="删除">&#x1006;</a>';
                        liHTML += '</li>';
                    };
                    receiveTag.append(liHTML);
                    layer.close(index);
                    //按钮【按钮一】的回调
                },
                btn2: function(index, layero){
                    var allCheckedArr = $('.contacts-box-r .contacts-table-outer table tr').find('input[name="contacts-choice"]');
                    var isTree = $('.contacts-table-outer .contact-tree').length;
                    if(isTree>0){
                        addLeftTree(allCheckedArr);
                        treeUniq('right');
                    }else{
                        addRightTr(allCheckedArr,'.contacts-box-l .contacts-table-outer table tbody');
                        repetition('right');
                    }
                    allrightArrow();
                    allLeftArrow();
                    trItemSize();
                    return false;
                },
                btn3: function(index, layero){
                    //按钮【按钮三】的回调

                    //return false 开启该代码可禁止点击该按钮关闭
                }
            });
        });

    });
})

// 点击联系人添加到发件人
function addReceive(_this){
    var $self = _this;
    var receiveTag = $('.table-input .mf_container .mf_list');
    var dataInput = receiveTag.find('input');
    var li = '';
    li += '<li class="mf_item" role="option" aria-selected="false">'+$($self).data('lastname')+'';
    li += '    <a href="#" class="mf_remove layui-icon" title="删除">&#x1006;</a>';
    li += '    <input type="hidden" class="mf_value" data-userid="'+$($self).data('userid')+'">';
    li += '</li>';
    // 判断是否重复
    for(var i=0;i<dataInput.length;i++){
        if(Number($($self).data('userid')) === $(dataInput[i]).data('userid')){
          return false;
        }
    }
    receiveTag.append(li);
};
//附件上传
function uploaderFile(){
    var $list = $('#thelist');
    var uploader = WebUploader.create({
        method: "POST",
        // 选完文件后，是否自动上传。
        auto: true,
        // swf文件路径
        swf: '/resources/web/webupload/Uploader.swf',

        // 文件接收服务端。
        server: '/web/createmail/insert-upload-sdk.htm',
        // server: 'http://resource.duapp.com/resource_not_found.html',
        formData:{
            userId:$.session.get('userId'),//用户id
            bulkId:null
            // fullname:'武当派',//分部
            // loginid:'一代宗师',//用户登录名
            // lastname:'张三丰'//当前用户姓名

        },
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        // pick: '#filePicker',
        pick: {
            id: '#filePicker',
            multiple:true //是否开起同时选择多个文件能力。
        },
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
            var layer = layui.layer;
            layer.msg('目前不支持<span style="color:#008cd6;">'+file.ext+'</span>格式',{time: 2000});
            return false;
        }
    })

    // 当有文件被添加进队列的时候
    uploader.on( 'fileQueued', function( file ) {
        var taskLi = $('#thelist').find('.txt');
        for(var i=0;i<taskLi.length;i++){
            if(taskLi[i].innerHTML === file.name){
                layui.use('layer', function(){
                    var layer = layui.layer;
                    layer.msg('<span style="color:#008cd6;">'+file.name+'</span>文件已存在，请重名后再上传',{time: 2000});
                });
                this.removeFile(file);
                return false;
            }
        };
        var fileSize = '';
        if(file.size>1024*1024){
            fileSize = (file.size/1024/1024).toFixed(2) + 'M';
        }else if(file.size>1024*1024*1024){
            fileSize = (file.size/1024/1024/1024).toFixed(2) + 'G';
        }else if(file.size>1024*1024*1024*1024){
            fileSize = (file.size/1024/1024/1024/1024).toFixed(2) + 'T';
        }else{
            fileSize = (file.size/1024).toFixed(2) + 'k';
        };
        $li = '<li id="'+file.id+'">'+
                '<input type="hidden" name="upload_file"></input>'+
                '<p class="txt">'+file.name+'</p>'+
                '<i>（'+fileSize+'）</i>'+
                '<span  class="state"></span>'+
            '</li>';
        $list.append($li);
        $list.scrollTop($list.height());
        $('.layui-all-btn .start-btn,.layui-all-btn .save-btn').attr('disabled',true).addClass('layui-disabled');
    });
    // 上传过程中触发，携带上传进度
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var id = file.id;
        var $li = $( '#'+file.id),
            $percent = $li.find('.progress .progress-bar');
            $iTag = $li.find('.progress .progress-percent');
        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<div class="progress">'+
                    '<div class="progress-border">'+
                        '<span class="progress-bar"></span>'+
                    '</div>'+
                    '<i class="progress-percent"></i>'+
                    '<span class="cancel">取消</span>'+
                '</div>').appendTo($li).find('.progress-bar');
            $iTag = $li.find('.progress .progress-percent');
        }
        var params = file;
        $percent.css( 'width', percentage * 100 + '%' );
        $iTag.html(parseFloat((percentage.toFixed(4)*100).toFixed(2)) + '%');
        $('.uploader-accessory .cancel').on('click',{file:params},function(event){
            uploader.removeFile(file,true);
            uploader.cancelFile( file );
        })

    });
    //取消上传
    uploader.on( 'fileDequeued', function( file ) {
        $('#'+file.id).slideUp(300,function(){
            $(this).remove();
        });
        $('.layui-all-btn .start-btn,.layui-all-btn .save-btn').attr('disabled',false).removeClass('layui-disabled');
    });
    // 当文件上传成功时触发。
    uploader.on( 'uploadSuccess', function( file,data ) {
        uploader.options.formData.bulkId = data.data[0].bulkId;
        $( '#'+file.id ).find('.state').empty()
        $( '#'+file.id ).append($('<span class="dele">删除</span>'));
        $( '#'+file.id ).find('input[name=upload_file]').attr({
            'data-bulkId':data.data[0].bulkId,
            'data-itemId':data.data[0].itemId
        });
    });
    // 删除按钮
    $(document).on('click','.uploader-list li .dele',function(){
        var delId = $(this).siblings('input[name="upload_file"]').data('itemid');
        var self = $(this);
        layer.confirm(
            '您确定要该附件删除吗？',
            {
                btn: ['是','否'],
                title:'删除提示'
            }, //按钮
            function(index, layero){
                //按钮【按钮一】的回调
                // if(delId){
                    delAttachment({
                        data:{
                            userId:$.session.get('userId'),
                            itemId:delId,
                            mailId:getQueryString('listId')
                        },
                        then:function(res){
                            if(res.code === '200'){
                                layer.msg('删除成功',{time: 2000,icon: 1});
                                self.parent().remove();
                                awaitLoad();

                            }else{
                                layer.msg('删除失败',{time: 2000,icon: 2});
                            }
                            layer.close(index);
                        },
                        fail:function(){
                            layer.close(index);
                            layer.msg('网络出错',{time: 2000,icon: 2});
                        }
                    });
                // }else{
                //     self.parent().remove();
                // }
                // layer.close(index);
            },
            function(index){
                //按钮【按钮二】的回调
            }
        );

    });
    // 重新上传
    function retry(file) {
        uploader.retry(file);
    }
    // 上传出错
    uploader.on( 'uploadError', function( file ) {
        layer.msg('上传失败',{time: 2000,icon: 2});
        var $li = $('#'+file.id);
        $li.append($('<span class="reset">重新上传</span>'));
        $( '#'+file.id ).find('.state').html('上传失败');
        $li.children('.reset').on('click',function(){
            $(this).remove();
            retry(file);
        });
    });

    // 不管成功或者失败，文件上传完成时触发
    uploader.on( 'uploadComplete', function(file) {
        $( '#'+file.id ).find('.progress').fadeOut(300,function(){
            $(this).remove();
        });
        $('.layui-all-btn .start-btn,.layui-all-btn .save-btn').attr('disabled',false).removeClass('layui-disabled');
    });
};
// 是否从待发进来
function awaitLoad(){
    if(urlReg.test(loaduUrl)){
        var mailId = getQueryString('listId');
        $.ajax({
            type:'post',
            url:'/web/received/find-mail-particulars.htm',
            data:{
                userId:$.session.get('userId'),
                mailId:mailId,
                mailStatus:0
            },
            dataType:'json',
            beforeSend:function(xhr){
                layui.use('layer', function(){
                    var layer = layui.layer;
                    layer_msg = layer.msg('正在加载数据中', {
                        icon: 16,
                        shade: 0.01,
                        area:'40px',
                        time:0
                    });
                });
            },
            success:function(res){
                layui.use('layer', function(){
                    var layer = layui.layer;
                    layer.closeAll('dialog');
                });

                if(res.code === '200'){
                    loadHTML(res.data);
                }else{
                    layui.use('layer', function(){
                        var layer = layui.layer;
                        layer.msg('数据加载失败',{time: 2000,icon: 2});
                    });
                }
            },
            error:function(){
                layui.use('layer', function(){
                    layer.closeAll('dialog');
                    layer.msg('网络出错',{time: 2000,icon: 2});
                });
            }
        })
    }else{
        loadHTML()
    }
};
function loadHTML(data){
    if(data !== undefined){
        $('.layui-body .content-body h1').html('待发传阅');
        $('#theme').val(data.title);
        // 发件人
        var sendLi = '';
        for(var i=0;i<data.receivess.length;i++){
            sendLi  += '<li class="mf_item" role="option" aria-selected="false">'+data.receivess[i].lastName+'';
            sendLi  += '    <a href="#" class="mf_remove layui-icon" title="删除">&#x1006;</a>';
            sendLi += '    <input type="hidden" class="mf_value" data-userid="'+data.receivess[i].userId+'">';
            sendLi += '</li>';
        };
        $('.table-input .mf_container .mf_list').html(sendLi);
        // 附件
        var fileHTML = '';
        for(var k=0;k<data.attachmentItemss.length;k++){
            fileHTML += '<li>'+
            '    <input type="hidden" name="upload_file" data-bulkid="'+data.attachmentItemss[k].bulkId+'" data-itemid="'+data.attachmentItemss[k].itemId+'">'+
            '    <p class="txt">'+data.attachmentItemss[k].fileName+'</p>'+
            '    <i>（'+data.attachmentItemss[k].itemSize+'）</i>'+
            // '    <span class="state">上传附件成功..</span>'+
            '    <span class="dele">删除</span>'+
            '</li>';
        };
        $('#thelist').html(fileHTML);
        //判断ueditor 编辑器是否创建成功
        editor.setValue(data.mailContent);
        // UE.delEditor('editor');
        // if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/9./i)=="9." || navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i)=="8."){
        //     UE.getEditor('editor').setContent(data.mailContent);
        // }else{
        //     UE.getEditor('editor').addListener("ready", function () {
        //         // editor准备好之后才可以使用
        //         UE.getEditor('editor').setContent(data.mailContent);
        //     });
        // }

        layui.use(['form'], function(){
	        $('#ifImportant').prop('checked',data.ifImportant);//重要传阅
	        $('#ifUpdate').prop('checked',data.ifUpdate), ///允许修订附件
	        $('#ifUpload').prop('checked',data.ifUpload),//允许上传附件
	        $('#ifRemind').prop('checked',data.ifRemind),//确认时提醒
	        $('#ifAdd').prop('checked',data.ifAdd),//允许新添加人员
	        $('#ifRead').prop('checked',data.ifRead),//开封已阅确认
	        $('#ifNotify').prop('checked',data.ifNotify),//短信提醒
	        $('#ifRemindAll').prop('checked',data.ifRemindAll),//确认时提醒所有传阅对象
	        $('#ifSecrecy').prop('checked',data.ifSecrecy),//传阅密送(不用实现)
	        $('#ifSequence').prop('checked',data.ifSequence)//有序确认
            var form = layui.form;
            form.render();
        });
    }
};

// 提交数据
function sendData(flg){
    var params = {
        transmission:flg,//如果是true 代表是发送新建传阅 如果是 false 表示保存新建传阅 不是发送
        mailId:0,
        userId:$.session.get('userId'),//发件人id
        // workCode:'876154646',//发件人工作编号
        // lastName:'张三丰',//发件人名称
        // loginId:'一代宗师',//发件人登录名
        // subcompanyName:'武当山',//发件人公司
        // departmentName:'技术部',//发件人部门
        title:'',//主题
        userTotal:0,//组织架构
        subcompanyIds:[],//分部Ids
        departmentIds:[],//部们Ids
        receiveUserId:[],//收件人id

        // receiveWorkCode:[],//收件人工作编号
        // receiveLastName:[],//收件人名称
        // receiveLoginId:[],//收件人登录名
        // receiveSubcompanyName:[],//公司
        // receiveDepartmentName:[],//部门
        bulkId:[],//附件上传批次ID
        itemId:[],// 传阅主题
        mailContent:'',// 邮件内容
        ifImportant:$('#ifImportant').is(':checked'),  //重要传阅
        ifUpdate:$('#ifUpdate').is(':checked'), ///允许修订附件
        ifUpload:$('#ifUpload').is(':checked'),//允许上传附件
        ifRemind:$('#ifRemind').is(':checked'),//确认时提醒
        ifAdd:$('#ifAdd').is(':checked'),//允许新添加人员
        ifRead:$('#ifRead').is(':checked'),//开封已阅确认
        ifNotify:$('#ifNotify').is(':checked'),//短信提醒
        ifRemindAll:$('#ifRemindAll').is(':checked'),//确认时提醒所有传阅对象
        ifSecrecy:$('#ifSecrecy').is(':checked'),//传阅密送(不用实现)
        ifSequence:$('#ifSequence').is(':checked')//有序确认
    };
    if(urlReg.test(loaduUrl)){
        params.mailId = parseInt(getQueryString('listId'));
    };
    var mf_value = $(' .mf_container .mf_value');
    if(flg){
        // 发送人判断
        if(!$('.mf_container .mf_list').children().length>0){
            layui.use('layer', function(){
                var layer = layui.layer;
                layer.msg('请填写接收人',{time: 2000});
            });
            return false;
        };
    }
    // 获取发送人信息
    if(mf_value){
        for(var i=0;i<mf_value.length;i++){
            if($(mf_value[i]).data('type') === 'usertotal'){
                params.userTotal = $(mf_value[i]).data('usertotal');
            }else if($(mf_value[i]).data('type') === 'subcompany'){
                params.subcompanyIds.push($(mf_value[i]).data('supsubcomid'))
            }else if($(mf_value[i]).data('type') === 'department'){
                params.departmentIds.push($(mf_value[i]).data('departmentid'))
            }else{
                params.receiveUserId.push($(mf_value[i]).data('userid'));
            }
        };
    }
    // 判断主题是否为空
    if($('.layui-form-item #theme').val()===''){
        layui.use('layer', function(){
            var layer = layui.layer;
            layer.msg('请填写主题',{time: 2000});
        });
        return false;
    }else{
        var regHtml=/<[^<>]+>/g;
        params.title = $('.layui-form-item #theme').val().replace(regHtml,'');
    }
    var file = $('.uploader-accessory input[name="upload_file"]');
    for(var j=0;j<file.length;j++){
        params.bulkId.push($(file[j]).data('bulkid'));
        params.itemId.push($(file[j]).data('itemid'));
    }
    // 传阅内容
    params.mailContent = editor.getValue();
    if(params.mailContent === ''){
        layui.use('layer', function(){
            var layer = layui.layer;
            layer.msg('请填写内容',{time: 2000});
        });
        return false;
    }
    var layer_msg;
    $.ajax({
        type:'post',
        url:'/web/createmail/insert-mail.htm',
        data:params,
        dataType:'json',
        beforeSend:function(xhr){
            layer_msg = layer.msg('正在提交中', {
                icon: 16,
                shade: 0.01,
                area:'40px',
                time:0
            });
        },
        success:function(res){
            layer.close(layer_msg);
            //开始传阅
            if(res.code === '200'){
                window.location.href = "send.htm";
            }else if(res.code === '201'){// 保存
                window.location.href = "await.htm";
                // layui.use('layer', function(){
                //     var layer = layui.layer;
                //     layer.msg('保存成功',{time: 2000,icon: 1});
                // });
            }else if(res.code === '205'){// 超过150个联系人时,给出提示
            	layui.use('layer', function(){
                    var layer = layui.layer;
                    layer.msg(res.msg,{time: 2000,icon: 2});
            	});
            }else{
                layui.use('layer', function(){// 其他状态
                    var layer = layui.layer;
                    layer.msg('提交失败',{time: 2000,icon: 2});
                });
            }
		},
        error:function(){
            layer.close(layer_msg);
            layer.msg('网络出错',{time: 2000,icon: 2});
        }

    })
};
function alreadyDialog(){
    return false;
}