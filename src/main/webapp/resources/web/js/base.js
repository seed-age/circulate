$(function(){

    function loadAjax(){
        //if(typeof $.session.get('userId') !== 'undefined')return;
        $.ajax({
            type:'post',
            url:'/web/createmail/login-user.htm',
            dataType:'json',
            async:false,
            success:function(res){
                if(res.code == '200'){
                    $.session.set('userId',res.data.userId);
                    $('#mainTopBlockTr .layui-layout-right .login>a').append(res.data.loginId);
                }else{
                    alert(res.msg)
                }
            },
            error:function(){
                alert('网络错误')
            }
        })
    }
    loadAjax();
});
layui.use(['element','laypage','table','form','layer','jquery'], function(){

    var element = layui.element;
    var table = layui.table;
    var laypage = layui.laypage;
    var form = layui.form;
    var $= layui.jquery;

    // 全选按钮
    form.on('checkbox(all-checked)', function(data){
        if(data.elem.checked === true){
            $(data.elem).parents('thead').siblings('tbody').find('.layui-unselect').addClass('layui-form-checked');
            $(data.elem).parents('thead').siblings('tbody').find('input[name="choice"]').prop('checked',true);
            $('.layui-btn-action').removeClass('layui-btn-disabled').addClass('layui-btn-normal').attr("disabled",false)
        }else{
            $(data.elem).parents('thead').siblings('tbody').find('.layui-unselect').removeClass('layui-form-checked');
            $(data.elem).parents('thead').siblings('tbody').find('input[name="choice"]').prop('checked',false);
            $('.layui-btn-action').removeClass('layui-btn-normal').addClass('layui-btn-disabled').attr("disabled",true)
        }
    });
    // 单选按钮阻止冒泡
    form.on('checkbox(choice)', function(data){
        $('.oa-read-table thead .layui-unselect').removeClass('layui-form-checked');
        $('.oa-read-table thead input[name="all-checked"]').prop('checked',false);
        if($(this).parents('tbody').find('input[name="choice"]:checked').length>0){
            $('.layui-btn-action').removeClass('layui-btn-disabled').addClass('layui-btn-normal').attr("disabled",false)
        }else{
            $('.layui-btn-action').removeClass('layui-btn-normal').addClass('layui-btn-disabled').attr("disabled",true)
        }
        var oEvent = event || ev;
        oEvent.stopPropagation();

    });
    // 关注
    $(document).off('.oa-receive-list li .star,.oa-read-table .layui-table .star').on('click','.oa-receive-list li .star,.oa-read-table .layui-table .star',function(ev){
        var oEvent = ev || event;
        oEvent.stopPropagation();
        var flg;
        var id = $(this).parent().siblings().children('input[name="choice"]').val() ||$(this)[0].id;
        if($(this).hasClass('active')){
            flg = false;
            $(this).removeClass('active');
        }else{
            flg = true;
            $(this).addClass('active');
        };
        $.ajax({
            type:'post',
            url:'/web/send/insert-attention.htm',
            dataType:'json',
            data:{
                userId:$.session.get('userId'),
                mailId:id,
                attention:flg,
                status:$.session.get('attentionStatus')
            },
            beforeSend:function(xhr){
                layer_del = layer.msg('正在提交数据中', {
                    icon: 16,
                    shade: 0.01,
                    area:'40px',
                    time:0
                });
            },
            success:function(res){
                layer.close(layer_del);
                var page = $('input[name="currPage"]').val();
                var currPage = page === ''?1:page;
                if(res.code === '200'){
                    setTimeout(function(){
                        layer.msg(res.msg,{time: 2000});
                    },200)
                };
            },
            error:function(){
                layer.close(layer_del);
                layer.msg('网络出错',{time: 2000,icon: 2});
            }
        });
    });
    // 联系人展开与显示
    $(document).on('click','.contact-tree .tree-branch',function(){
        if($(this).children('img').length>0){
            if($(this).hasClass('cur')){
                $(this).removeClass('cur');
                $(this).siblings('.tree-box').stop().slideUp();
            }else{

                $(this).addClass('cur');
                $(this).siblings('.tree-box').stop().slideDown();
            };
        }
    })
    // 退出登录
    $('.topcolor .layui-nav .layui-nav-item.quit').on('click',function(){
        cookie.delAll();
        setTimeout(function(){
            window.location.href = 'http://sso-test.seedland.cc/pactera_sso_localmodel_exit.axd';
        },200)
    });
    $('.contacts-content').off('click','.org-branch-one').on('click','.org-branch-one',function(){
        if($(this).siblings('.tree-box')[0])return;
        if($(this).hasClass('layui-disabled'))return;
        $.getContact(this,{openType:true},'article')
    });
    $('.contacts-content').off('click','.org-branch-two').on('click','.org-branch-two',function(){
        if($(this).siblings('.tree-box')[0])return;
        var options = {
            supsubcomid:$(this).data('supsubcomid'),
            openType:true
        }
        var flag = true;
        var departmentArr = $('.dialog-contacts .contacts-box-r .contacts-table').find('[data-supsubcomid]');
        for(var i=0;i<departmentArr.length;i++){
            var dom = $(departmentArr[i]);
            if(dom.data('supsubcomid') === $(this).data('supsubcomid') && dom.data('type') === $(this).data('type')){
                flag = false;
                return;
            }
        }
        if(flag){
            $.getContact(this,options,'article')
        }
    })
    $('.contacts-content').off('click','.org-branch-three').on('click','.org-branch-three',function(){
        if($(this).siblings('.tree-box')[0])return;
        var options = {
            departmentid:$(this).data('departmentid'),
            openType:true
        };
        var flag = true;
        var departmentArr = $('.dialog-contacts .contacts-box-r .contacts-table').find('[data-departmentid]');
        for(var i=0;i<departmentArr.length;i++){
            var dom = $(departmentArr[i]);
            if(dom.data('departmentid') === $(this).data('departmentid') && dom.data('type') === $(this).data('type')){
                flag = false;
                return;
            }
        }
        if(flag){
            $.getContact(this,options,'article')
        }
    })
    $('.contacts-content').off('click','.org-branch-personnel').on('click','.org-branch-personnel',function(){
        addLight(this);
    })

});
(function($) {
    $.extend({
        myTime: {
            UnixDate: function(unixTime, isFull, timeZone) {
                if (typeof (timeZone) == 'number')
                {
                    unixTime = parseInt(unixTime) + parseInt(timeZone) * 60 * 60;
                }
                var time = new Date(unixTime);
                var ymdhis = "";
//                ymdhis += time.getUTCFullYear() + "年";
                ymdhis += (time.getUTCMonth()+1) + "月";
                ymdhis += time.getUTCDate() + '日';
                if (isFull === true)
                {
                    ymdhis += " " + time.getUTCHours() + ":";
                    ymdhis += time.getUTCMinutes() + ":";
                    ymdhis += time.getUTCSeconds();
                }
                return ymdhis;
            }
        }
    });
})(jQuery);
// 获取当前时间
function CurentTime(){
    var now = new Date();
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
    var clock = year + "-";
    if(month < 10)
        clock += "0";
    clock += month + "-";
    if(day < 10)
        clock += "0";
    clock += day + " ";
    if(hh < 10)
        clock += "0";
    clock += hh + ":";
    if (mm < 10) clock += '0';
    clock += mm;
    return(clock);
};
// 兼容ie的bind(this)方法
if (!Function.prototype.bind) {
    Function.prototype.bind = function (oThis) {
        if (typeof this !== "function") {
            throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
        }
        var aArgs = Array.prototype.slice.call(arguments, 1),
            fToBind = this,
            fNOP = function () { },
            fBound = function () {
                return fToBind.apply(this instanceof fNOP && oThis
                    ? this
                    : oThis,
                    aArgs.concat(Array.prototype.slice.call(arguments)));
            };
        fNOP.prototype = this.prototype;
        fBound.prototype = new fNOP();
        return fBound;
    };
}
function Pages(options){
    this.type = options.type;
    this.url = options.url;
    this.data = options.data;
    this.pageRows;
    this.totalRecord;//总记录数
    this.curPage = 1;//当前页
    this.templateTag = $(options.templateTag);
}
Pages.prototype = {
    getData:function(page){
        this.data.page = page === undefined?this.curPage:page;
        var layer_del;
        $.ajax({
            type:this.type,
            url:this.url,
            dataType:'json',
            data:this.data,
            beforeSend:function(xhr){
                layui.use('layer', function(){
                    layer_del = layui.layer;
                    layer.msg('数据加载中', {
                        icon: 16,
                        area:'40px',
                        time:0
                    });
                });
                // 跳转分页取消全选
                $('.oa-read-table thead .layui-unselect').removeClass('layui-form-checked');
                $('.oa-read-table thead input[name="all-checked"]').prop('checked',false);
            },
            success:function(res){
                layui.use('layer', function(){
                    var layer = layui.layer;
                    // layer.closeAll()
                    layer.close(layer_del.index);
                });
                if(res.code === '200'){
                    this.totalRecord = res.data.totalRecord;
                    this.pageRows = res.data.pageSize;
                    this.templateHTML(this,(res.data.list||res.data));
                }
            }.bind(this),
            complete:function(){
                layui.use(['laypage','layer','form'], function(){
                    var laypage = layui.laypage;
                    var form = layui.form;
                    var that = this;
                    laypage.render({
                        elem: 'paging',
                        count: this.totalRecord,
                        curr:this.curPage,
                        limit:this.pageRows,//每页几条数据
                        layout: ['count', 'prev', 'page', 'next', 'skip'],
                        jump: function(obj,first){
                            if(!first){
                                this.curPage = obj.curr;
                                this.getData(this.curPage);
                                $('input[name="currPage"]').val(obj.curr);
                            }

                        }.bind(this)
                    });
                }.bind(this));
            }.bind(this),
            error:function(msg){
                layui.use('layer', function(){
                    var layer = layui.layer;
                    // layer.closeAll('dialog');
                    layer.close(layer_del.index);
                    layer.msg('网络出错',{time: 2000,icon: 2});
                });
            }
        })
    },
    templateHTML:function(that,data){
        layui.use(['form','table'], function(){
            var form = layui.form;
            this.templateTag.html('');//清空上次内容
            var string = '';
            var status = $.session.get('attentionStatus');
            if(data.length>0){
                for(var i=0;i<data.length;i++){
                    string += '<tr>';
                    if(data[i].stepStatus == 3 && status == 1){
                        string += '    <td><input disabled value="'+data[i].mailId+'" type="checkbox" name="choice" lay-filter="choice" lay-skin="primary"></td>';
                    }else{
                        string += '    <td><input value="'+data[i].mailId+'" type="checkbox" name="choice" lay-filter="choice" lay-skin="primary"></td>';
                    }
                    string += '    <td>';
                    // 判断是收到传阅或者其他传阅的
                    if(data[0].hasOwnProperty('receiveAttention')){
                        string += '        <img src="/resources/web/images/main-icon9'+(data[i].receiveMailState===5?'':'-cur')+'.png" style="margin:0 3px" alt="">';
                    }else{
                        string += '        <img src="/resources/web/images/main-icon9-cur.png" style="margin:0 3px" alt="">';
                    };
                    if(data[i].hasAttachment){
                        string += '        <img src="/resources/web/images/accessory-icon9.png" style="margin:0 3px;" alt="">';
                    };
                    string += '    </td>';
                    string += '    <td>'+data[i].title+'</td>';
                    string += '    <td>'+data[i].lastName+'</td>';
                    string += '    <td>'+data[i].allReceiveName+'</td>';
                    if(status == 3 || status == 1){
                        string += '    <td>';
                        if(data[i].stepStatus == 1){
                            string +='     传阅中';
                        }else if(data[i].stepStatus == 2){
                            string += '    待办传阅'
                        }else if(data[i].stepStatus == 3){
                            string += '    已完成'
                        }else{
                            string += '    '
                        }
                        string += '    </td>';
                    }
                    if(data[0].hasOwnProperty('receiveAttention')){
                        string += '    <td>'+data[i].receiveTime+'</td>';
                        string += '    <td style="text-align:right;"><button type="button" class="star '+(data[i].receiveAttention?'active':'')+'" data-edition="receive"></button></td>';
                    }else{
                        var attentionStatus = $.session.get('attentionStatus') === '2';
                        string += '    <td>'+(attentionStatus?data[i].deleteTime:data[i].sendTime)+'</td>';
                        string += '    <td style="text-align:right;"><button type="button" class="'+(attentionStatus?'layui-disabled':'')+' star '+(data[i].attention?'active':'')+'" '+(attentionStatus?'disabled':'')+'></button></td>';

                    };
                    string += '</tr>';
                }
            }else{
                string += '<tr>';
                string += '    <td colspan="7" style="text-align: center;">';
                string += '       没有结果！';
                string += '    </td>';
                string += '</tr>';
            }
            this.templateTag.append(string);
            form.render();
        }.bind(that));
    }
};
//获取url中的参数
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
};
// 删除附件函数
function delAttachment(options){
    $.ajax({
        type:'post',
        url:'/web/createmail/delete-upload.htm',
        data:options.data,
        dataType:'json',
        success:function(res){
            options.then(res)
        },
        error:function(){
            options.fail()
        }
    })
};
// 封装联系人接口
function getContactData(options){
    options.params.openType = false;
    $.ajax({
        type:'post',
        url:'/web/hr/hr-framework-tree.htm',
        dataType:'json',
        data:options.params,
        beforeSend:function(xhr){
            options.before(xhr);
        },
        success:function(res){
            options.render(res)
        },
        error:function(msg){
            options.fail(msg);
        }
    });

};
jQuery.extend({
    loadContactsData:function(){
        // 新增人员请求函数
        function contactsData(options){
            $.ajax({
                type:'post',
                url:options.url,
                data:options.params,
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
                    if(res.code == '200'){
                        options.render(res.data)
                    }else{
                        layer.msg('网络出错',{time: 2000,icon: 2});
                    }
                },
                error:function(){
                    layer.close(layer_msg);
                    layer.msg('网络出错',{time: 2000,icon: 2});
                }
            });
        };

        function contactsHTML(data){
            $('.contacts-box-l .contacts-table-outer').html('');
            var tableHTML = '';
            tableHTML += '<table class="contacts-table">';
            tableHTML += '	<tbody>';
            if(data.length>0){
                for(var i=0;i<data.length;i++){
                    tableHTML += '		<tr>';
                    tableHTML += '			<td style="width:50px;text-align:center;">';
                    tableHTML += '				<input name="contacts-choice" data-type="'+data[i].type+'" data-receiveUserId="'+data[i].userId+'" data-receiveLastName="'+data[i].lastName+'" data-departmentId="'+data[i].departmentId+'" type="checkbox" class="contacts-table-input">';
                    tableHTML += '				<img src="/resources/web/images/head-icon.png" alt="">';
                    tableHTML += '			</td>';
                    tableHTML += '			<td style="color:#22262a;">';
                    tableHTML += '				'+data[i].lastName+'';
                    tableHTML += '			</td>';
                    tableHTML += '			<td  style="color:#999;">';
                    tableHTML += '				';
                    tableHTML += '			</td>';
                    tableHTML += '		</tr>';
                };
            }else{
                tableHTML += '<tr>';
                tableHTML += '	<td style="text-align: center;">没有找到联系人</td>';
                tableHTML += '</tr>';
            };
            tableHTML += '	</tbody>';
            tableHTML += '</table>';
            $('.contacts-box-l .contacts-table-outer').html(tableHTML);
            $('.contacts-table-outer table tr:first').trigger('click');
            repetition('left');
            alreadyDialog();
            allLeftArrow();
            leftArrow();
            rightArrow();

        };
        // 右箭头点击
        $('body').off('click').on('click','.dialog-contacts .contacts-box-c .single-r',function(){
            var electArr;
            var tree = $('.contacts-box-l .contact-tree .tree-branch.active');
            if(tree.length != 0){
                if(tree.data('userid') === undefined){
                    // electArr = tree.siblings().children().find('[data-userid]');
                    if(tree.hasClass('layui-disabled'))return layer.msg('已添加',{time: 1000});
                    addRightTr(tree,'.contacts-box-r .contacts-table-outer table tbody','right');
                    tree.addClass('layui-disabled')
                    tree.siblings().remove();
                }else{
                    electArr = tree;
                }
            }else{
                electArr = $('.contacts-box-l .contacts-table-outer table tr').find('input[name="contacts-choice"]:checked');
            };
            addRightTr(electArr,'.contacts-box-r .contacts-table-outer table tbody','right');
            leftArrow();
            tree.length!=0?treeUniq('left'):repetition('left');
            rightArrow();
            allrightArrow();
            trItemSize();
            allLeftArrow();
            $('.dialog-contacts .contacts-box-c .single-r').removeClass('active').addClass('layui-disabled');
        });

        // 左箭头点击
        // 克隆
        // $('.dialog-contacts').on('click',function(){alert(1)})
        $('.dialog-contacts').off('click','.contacts-box-c .single-l').on('click','.contacts-box-c .single-l',function(){
            var isTree = $('.contacts-table-outer .contact-tree').length;
            var electArr = $('.contacts-box-r .contacts-table-outer table tr').find('input[name="contacts-choice"]:checked') || [];
            var subcompanyArr = $('.contacts-box-r .contacts-table-outer table tr.active').find('[data-type="subcompany"]') || [];
            var departmentArr = $('.contacts-box-r .contacts-table-outer table tr.active').find('[data-type="department"]') || [];
            if(isTree>0 || subcompanyArr.length>0 || departmentArr.length>0){
                for(var i=0;i<subcompanyArr.length;i++){
                    electArr.push(subcompanyArr[i])
                }
                for(var j=0;j<departmentArr.length;j++){
                    electArr.push(departmentArr[j])
                }
                addLeftTree(electArr);
                treeUniq('right');
            }else{
                addRightTr(electArr,'.contacts-box-l .contacts-table-outer table tbody','left');
                repetition('right');
            }
            leftArrow();
            rightArrow();
            allLeftArrow();
            allrightArrow();
            trItemSize();
            isTree>0 && $('.dialog-contacts .contacts-box-c .single-r').removeClass('layui-disabled').addClass('active');
        });
        // 选择全部的左箭头
        $('.dialog-contacts').off('click','.contacts-box-c .entire-l').on('click','.contacts-box-c .entire-l',function(){
            var allCheckedArr = $('.contacts-box-l .contacts-table-outer table tr').find('input[name="contacts-choice"]');
            addRightTr(allCheckedArr,'.contacts-box-r .contacts-table-outer table tbody','right');
            repetition('left');
            leftArrow();
            rightArrow();
            allLeftArrow();
            allrightArrow();
            trItemSize();
        });
        // 选择全部的右箭头
        $('.dialog-contacts').off('click','.contacts-box-c .entire-r').on('click','.contacts-box-c .entire-r',function(){
            var isTree = $('.contacts-table-outer .contact-tree').length;
            var allCheckedArr = $('.contacts-box-r .contacts-table-outer table tr').find('input[name="contacts-choice"]');
            if(isTree>0){
                addLeftTree(allCheckedArr);
                treeUniq('right');
                // $.organization();
                // $('.contacts-box-r .contacts-table-outer table tbody').html('');
            }else{
                addRightTr(allCheckedArr,'.contacts-box-l .contacts-table-outer table tbody','left');
                repetition('right');

            };
            leftArrow();
            rightArrow();
            allLeftArrow();
            allrightArrow();
            trItemSize();
            isTree>0 && $('.dialog-contacts .contacts-box-c .single-r').removeClass('layui-disabled').addClass('active');
        });

        // 右边按钮搜索
        $('#have-search').on('keyup',function(e){
            var val = $(this).val();
            var rightTr = $('.contacts-box-r .contacts-table-outer table tr').find('input[name="contacts-choice"]');
            var reg = new RegExp(val, "ig");
            for(var i=0;i<rightTr.length;i++){
                if(reg.test($(rightTr[i]).data('receivelastname'))){
                    rightTr[i].parentNode.parentNode.style.display = '';
                }else{
                    rightTr[i].parentNode.parentNode.style.display = 'none';
                }
            };
        });
        $('.contacts-box-l').off("dblclick",".contacts-table-outer table tr").on("dblclick",".contacts-table-outer table tr",function(){
            var singleChoice = $(this).find('input[name="contacts-choice"]');
            addRightTr(singleChoice,'.contacts-box-r .contacts-table-outer table tbody','right');
            repetition('left');
            leftArrow();
            rightArrow();
            allLeftArrow();
            allrightArrow();
            trItemSize();
        });
        $('.contacts-box-l').off('click','.contacts-table-outer table tr').on('click','.contacts-table-outer table tr',function(){
            clickTr(this)
            leftArrow();
            rightArrow();
        });
        $(document).off('click','.contacts-box-r .contacts-table-outer table tr').on('click','.contacts-box-r .contacts-table-outer table tr',function(){
            clickTr(this);
            rightArrow();
        });
        // 右边table tr双击
        $(document).off('dblclick','.contacts-box-r .contacts-table-outer table tr').on('dblclick','.contacts-box-r .contacts-table-outer table tr',function(){
            var isTree = $('.contacts-table-outer .contact-tree').length;
            var singleChoice = $(this).find('input[name="contacts-choice"]');
            if(isTree>0){
                addLeftTree(singleChoice);
                treeUniq('right');
            }else{
                addRightTr(singleChoice,'.contacts-box-l .contacts-table-outer table tbody','left');
                repetition('right');
            }
            leftArrow();
            rightArrow();
            allLeftArrow();
            allrightArrow();
            trItemSize();
        });
        var firstNum = null;
        $(document).off('click','.dialog-contacts .contacts-nav li').on('click','.dialog-contacts .contacts-nav li',function(){
            var that = $(this);
            repetition('left');
            // 判断如果是当前的tab，点击无效
            if(firstNum == $(this).index()){
                return false;
            }else{
                $(this).addClass('active').siblings().removeClass('active');
                if($(this).data('group') === '组织结构'){
                    $.organization();
                    $('.dialog-contacts .contacts-box-c .entire-l').css('display','none');
                }else if($(this).data('group') === '最近'){
                    contactsData({
                        url:'/web/createmail/find-lately-hrm.htm',
                        params:{
                            userId:$.session.get('userId')
                        },
                        render:function(data){
                            contactsHTML(data);
                        }
                    });
                    $('.dialog-contacts .contacts-box-c .entire-l').css('display','inline-block');
                }else if($(this).data('group') === '同部门'){
                    contactsData({
                        url:'/web/createmail/find-hrm-same-department.htm',
                        params:{
                            userId:$.session.get('userId')
                        },
                        render:function(data){
                            contactsHTML(data);
                        }
                    });
                    $('.dialog-contacts .contacts-box-c .entire-l').css('display','inline-block');
                };
            };
            firstNum = $(this).index();
            $('#contacts_search').val('');
            leftArrow();
            if($(this).data('group')=== '组织结构'){
                $('.dialog-contacts .contacts-box-c .single-r').removeClass('layui-disabled').addClass('active');
            }
        });
        $('.dialog-contacts .contacts-nav li').first().trigger('click');
        // 搜索按钮
        $('#contacts_search_btn').on('click',function(e){
            var likeName = $(this).siblings().val();
            var layer_msg;
            contactsData({
                url:'/web/createmail/find-like-hrm.htm',
                params:{
                    likeName:likeName,
                    pageRows:200
                },
                render:function(data){
                    contactsHTML(data);
                }
            });
            $('.dialog-contacts .contacts-nav li').removeClass('active');
            firstNum = null;
            $('.dialog-contacts .contacts-box-c .entire-l').css('display','inline-block');
            leftArrow();
        });
        // 搜索按钮
        $('#contacts_search').on('keydown',function(e){
            if(e.keyCode === 13){
                var likeName = $(this).val();
                var layer_msg;
                contactsData({
                    url:'/web/createmail/find-like-hrm.htm',
                    params:{
                        likeName:likeName,
                        pageRows:200
                    },
                    render:function(data){
                        contactsHTML(data);
                    }
                });
                $('.dialog-contacts .contacts-nav li').removeClass('active');
                firstNum = null;
                $('.dialog-contacts .contacts-box-c .entire-l').css('display','inline-block');
                leftArrow();
            }
        });
        // 部门按钮
        $(document).off('click','.dialog-contacts .tree-branch').on('click','.dialog-contacts .tree-branch:not(.org-branch-personnel)',function(){
            $(this).parents('.contact-tree').find('.org-branch-three').removeClass('active');
            $('.dialog-contacts .tree-branch').removeClass('active');
            if(!$(this).hasClass('layui-disabled')){
                $('.dialog-contacts .contacts-box-c .single-r').removeClass('layui-disabled').addClass('active');
            }else{
                $('.dialog-contacts .contacts-box-c .single-r').removeClass('active').addClass('layui-disabled');
            }
            $(this).addClass('active');
            // leftArrow();
        });
        // 组织架构的人双击
        $(document).off('dblclick','.dialog-contacts .org-branch-personnel').on('dblclick','.dialog-contacts .org-branch-personnel',function(){
            var singleChoice = $(this);
            addRightTr(singleChoice,'.contacts-box-r .contacts-table-outer table tbody','right');
            treeUniq('left');
            leftArrow();
            allrightArrow();
            trItemSize();
        });
    },
    organization : function (){
        var ulHTML = '';
        ulHTML += '<ul class="contact-tree">';
        ulHTML += '	<li>';
        ulHTML += '		<div class="tree-branch org-branch-one" data-type="usertotal" data-lastname="组织架构" data-usertotal="1">';
        ulHTML += '			<img class="tree-icon icon-up" src="/resources/web/images/contact-icon01.png" alt="">';
        ulHTML += '			<img class="tree-icon icon-down" src="/resources/web/images/contact-icon02.png" alt="">';
        ulHTML += '			组织架构';
        ulHTML += '		</div>';
        ulHTML += '	</li>';
        ulHTML += '</ul>';
        $('.contacts-box-l .contacts-table-outer').html(ulHTML);
        // 公司分部
        $('.dialog-contacts .org-branch-one').one('click',function(){
            if($('.dialog-contacts .contacts-box-r .contacts-table-outer').find('[data-type="usertotal"]').length){
                $('.dialog-contacts .contacts-box-c .single-r').removeClass('active').addClass('layui-disabled');
                $(this).addClass('layui-disabled');
                return false;
            }
            $.getContact(this,{openType:true},'article')
            // $.getContact({id:this},'article');
        });
        $('.dialog-contacts .org-branch-one').trigger('click').addClass('cur');

    },
    getContact : function (_this,options,type){
        var $self = $(_this);
        var getContactDataMsg;
        getContactData({
            params:options,
            before:function(){
                if(type === 'new'){
                    $('.rightArea').append('<div class="loading"><span class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</span></div>');

                }else{
                    getContactDataMsg = layui.layer;
                    layer.msg('数据加载中', {
                        icon: 16,
                        area:'40px',
                        time:0
                    });
                }
            },
            render:function(res){
                if(type === 'new'){
                    $('.rightArea .loading').remove();
                }else{
                    layer.close(getContactDataMsg.index);
                }
                if(res.code === '200'){
                    var data = res.data;
                    var count = 0;
                    var ul = '';
                    ul += '<ul class="tree-box">';
                    for(var i=0;i<data.length;i++){
                        if(data[i].supsubcomid){
                            ul += '	<li>';
                            ul += '		<div class="tree-branch org-branch-two" data-count="'+data[i].count+'" data-lastName="'+data[i].subcompanyname+'" data-type="'+data[i].type+'" data-supsubcomid="'+data[i].supsubcomid+'"><img class="tree-icon icon-up" src="/resources/web/images/more-icon01.png" alt=""><img class="tree-icon icon-down" src="/resources/web/images/more-icon02.png" alt="">'+data[i].subcompanyname+'</div>';
                            ul += '	</li>';
                            count += data[i].count;
                        }else if(data[i].departmentid){
                            ul += '	<li>';
                            ul += '		<div class="tree-branch org-branch-three" data-count="'+data[i].count+'" data-lastName="'+data[i].departmentname+'" data-type="'+data[i].type+'" data-departmentid="'+data[i].departmentid+'"><img class="tree-icon icon-up" src="/resources/web/images/more-icon01.png" alt=""><img class="tree-icon icon-down" src="/resources/web/images/more-icon02.png" alt="">'+data[i].departmentname+'</div>';
                            ul += '	</li>';
                        }else if(data[i].userId){
                            ul += '	<li>';
                            ul += '		<div class="tree-branch org-branch-personnel" data-type="'+data[i].type+'" data-userId="'+data[i].userId+'" data-lastName="'+data[i].lastName+'" data-departmentid="'+data[i].departmentId+'">'+data[i].lastName+'</div>';
                            ul += '	</li>';
                        }
                    }
                    $($self).after(ul);
                    if($($self).hasClass('org-branch-one')){
                        $($self).attr('data-count',count)
                    }
                    treeUniq('left',$($self));//去重
                    alreadyDialog();

                }else{
                    layer.msg('加载出错',{time: 2000,icon: 2});
                }

            },
            fail:function(){
                if(type === 'new'){
                    $('.rightArea .loading').remove();
                }else{
                    layer.close(getContactDataMsg.index);
                    layer.msg('加载出错',{time: 2000,icon: 2});
                }

            }
        })
    },
    myTime: {
        /**
         * 时间戳转换日期
         * @param <int> unixTime    待时间戳(秒)
         * @param <bool> isFull    返回完整时间(Y-m-d 或者 Y-m-d H:i:s)
         * @param <int>  timeZone   时区
         */
        UnixToDate: function(unixTime, isFull, timeZone) {
            if (typeof (timeZone) == 'number'){
                unixTime = parseInt(unixTime) + parseInt(timeZone) * 60 * 60;
            }
            var time = new Date(unixTime);
            var ymdhis = "";
            ymdhis += time.getUTCFullYear() + "-";
            ymdhis += (time.getUTCMonth()+1) + "-";
            ymdhis += time.getUTCDate() + ' ';
            if (isFull === true){
                ymdhis += " " + time.getUTCHours() + ":";
                ymdhis += time.getUTCMinutes() + ":";
                ymdhis += time.getUTCSeconds();
            }
            return ymdhis;
        }
    }
});
// 新增联系人//////////////////////////////////////
// 新增人员请求数据字符串拼接
function addRightTr(data,tag,direction){
    if(!data)return;
    var rightTrHTML = '';
    for(var j=0;j<data.length;j++){
        if(direction==='left' && $(data[j]).data('type') !== 'userMssage'){
            $('.dialog-contacts .contacts-box-r .contacts-table tr').find('[name="contacts-choice"]').parents('tr').remove();
            continue;
        }
        rightTrHTML += '		<tr>';
        rightTrHTML += '			<td style="width:50px;text-align:center;">';
        if($(data[j]).data('type') === 'userMssage'){
            rightTrHTML += '			<input name="contacts-choice" data-type="'+$(data[j]).data('type')+'"  data-receiveUserId="'+($(data[j]).data('receiveuserid')||$(data[j]).data('userid'))+'" data-receiveLastName="'+($(data[j]).data('receivelastname')||$(data[j]).data('lastname'))+'" data-departmentId="'+($(data[j]).data('departmentid'))+'"type="checkbox" class="contacts-table-input">';
            rightTrHTML += '			<img src="/resources/web/images/head-icon.png" alt="">';
        }else if($(data[j]).data('type') === 'department'){
            rightTrHTML += '			<input name="contacts-choice" data-count="'+$(data[j]).data('count')+'" data-type="'+$(data[j]).data('type')+'"  data-receiveLastName="'+($(data[j]).data('receivelastname')||$(data[j]).data('lastname'))+'" data-departmentId="'+($(data[j]).data('departmentid'))+'"type="checkbox" class="contacts-table-input">';
            rightTrHTML += '			<span class="layui-icon layui-icon-file"></span>';
        }else if($(data[j]).data('type') === 'subcompany'){
            rightTrHTML += '			<input name="contacts-choice" data-count="'+$(data[j]).data('count')+'" data-type="'+$(data[j]).data('type')+'"  data-receiveLastName="'+($(data[j]).data('receivelastname')||$(data[j]).data('lastname'))+'" data-supsubcomid="'+($(data[j]).data('supsubcomid'))+'"type="checkbox" class="contacts-table-input">';
            rightTrHTML += '			<span class="layui-icon layui-icon-home"></span>';
        }else{
            rightTrHTML += '			<input name="contacts-choice" data-count="'+$(data[j]).data('count')+'" data-type="'+$(data[j]).data('type')+'"  data-receiveLastName="'+($(data[j]).data('receivelastname')||$(data[j]).data('lastname'))+'" data-usertotal="'+($(data[j]).data('usertotal'))+'"type="checkbox" class="contacts-table-input">';
            rightTrHTML += '			<span class="layui-icon layui-icon-home"></span>';
        }
        rightTrHTML += '			</td>';
        rightTrHTML += '			<td style="color:#22262a;">';
        rightTrHTML += '				'+($(data[j]).data('receivelastname') ||$(data[j]).data('lastname'))+'';
        rightTrHTML += '                '+($(data[j]).data('type') === 'userMssage'? '':'('+$(data[j]).data('count')+')')+'';
        rightTrHTML += '			</td>';
        rightTrHTML += '			<td  style="color:#999;">';
        rightTrHTML += '				';
        rightTrHTML += '			</td>';
        rightTrHTML += '		</tr>';
    };
    $(tag).append(rightTrHTML);
};
// 组织架构右边添加回左边
function addLeftTree(data){
    for(var i=0;i<data.length;i++){
        var ulTag = $('.dialog-contacts .org-branch-three[data-departmentid="'+$(data[i]).data('departmentid')+'"]').siblings();
        if($(data[i]).data('type') === 'usertotal'){
            $('.dialog-contacts .contacts-table-outer .contact-tree').find('[data-type="usertotal"]').removeClass('layui-disabled cur');
            $(data[i]).parents('tr').remove();
            $('.dialog-contacts .contacts-box-c .single-r').removeClass('layui-disabled').addClass('active');
            continue;
        }
        if($(data[i]).data('type') !== 'userMssage'){
            $('.dialog-contacts .contacts-table-outer .contact-tree').find($(data[i]).data('type')==='department'?'[data-departmentid="'+$(data[i]).data('departmentid')+'"][data-type="department"]':'[data-supsubcomid="'+$(data[i]).data('supsubcomid')+'"][data-type="subcompany"]').removeClass('layui-disabled cur');
            $(data[i]).parents('tr').remove();
            continue;
        }
        if( ulTag.length !== 0){
            var liHTML = '';
            liHTML += '<li>';
            liHTML += '	<div class="tree-branch org-branch-personnel" data-type="'+$(data[i]).data('type')+'" data-userid="'+$(data[i]).data('receiveuserid')+'" data-lastname="'+$(data[i]).data('receivelastname')+'" data-departmentid="'+$(data[i]).data('departmentid')+'">'+$(data[i]).data('receivelastname')+'</div>';
            liHTML += '</li>';
            ulTag.append(liHTML);
        }else{
            $('.contacts-box-r .contacts-table-outer table tr input[data-receiveuserid="'+$(data[i]).data('receiveuserid')+'"]').parent().parent().remove();
        }

    }
};
// 左箭头高亮
function leftArrow(){
    var checkedTr = $('.dialog-contacts .contacts-box-l .contacts-table tr input[name="contacts-choice"]:checked').length || $('.contacts-box-l .contact-tree .tree-box .tree-branch.active').length;
    if(checkedTr>0){
        $('.dialog-contacts .contacts-box-c .single-r').removeClass('layui-disabled').addClass('active');
    }else{
        $('.dialog-contacts .contacts-box-c .single-r').removeClass('active').addClass('layui-disabled');
    }
};
// 右箭头高亮
function rightArrow(){
    var checkedTr = $('.dialog-contacts .contacts-box-r .contacts-table tr input[name="contacts-choice"]:checked').length;
    if(checkedTr>0){
        $('.dialog-contacts .contacts-box-c .single-l').removeClass('layui-disabled').addClass('active');
    }else{
        $('.dialog-contacts .contacts-box-c .single-l').removeClass('active').addClass('layui-disabled');
    }
};
// 选择全部的左箭头高亮
function allLeftArrow(){
    if($('.contacts-box-l .contacts-table-outer table tr input[name="contacts-choice"]').length > 0){
        $('.dialog-contacts .contacts-box-c .entire-l').removeClass('layui-disabled').addClass('active');
    }else{
        $('.dialog-contacts .contacts-box-c .entire-l').removeClass('active').addClass('layui-disabled');
    };
};
// 选择全部的右箭头高亮
function allrightArrow(){
    if($('.contacts-box-r .contacts-table-outer table tr input[name="contacts-choice"]').length > 0){
        $('.dialog-contacts .contacts-box-c .entire-r').removeClass('layui-disabled').addClass('active');
    }else{
        $('.dialog-contacts .contacts-box-c .entire-r').removeClass('active').addClass('layui-disabled');
    };
}
// 选中状态
function clickTr(_this){
    var that = $(_this);
    if(that.hasClass('active')){
        that.removeClass('active');
        that.find('input[name="contacts-choice"]').prop('checked',false);
    }else{
        that.addClass('active');
        that.find('input[name="contacts-choice"]').prop('checked',true);
    };
};
// 人数
function trItemSize(){
    var rightTr = $('.contacts-box-r .contacts-table-outer table tr').find('[name="contacts-choice"]');
    var count = 0;
    for(var i=0;i<rightTr.length;i++){
        if($(rightTr[i]).data('type') === 'userMssage'){
            count += 1;
        }else{
            count += parseInt($(rightTr[i]).data('count'));
        }
    }
    $('.layui-layer-btn .layui-layer-btn0').text('确认('+count+'人)');
};
// 添加联系人去重
function repetition(Arrow){
    var leftTr = $('.contacts-box-l .contacts-table-outer table tr').find('input[name="contacts-choice"]');
    var rightTr = $('.contacts-box-r .contacts-table-outer table tr').find('input[name="contacts-choice"]');
    for(var i=0;i<leftTr.length;i++){
        for(var k=0;k<rightTr.length;k++){
            if($(leftTr[i]).data('receiveuserid') === $(rightTr[k]).data('receiveuserid') ){
                if(Arrow === 'left'){
                    $(leftTr[i].parentNode.parentNode).remove();
                }else{
                    $(rightTr[k].parentNode.parentNode).remove();
                }

            }
        }
    };
};
// 点击组织架构的人高亮
function addLight(_this){
    var that = $(_this);
    if(that.hasClass('active')){
        that.removeClass('active');
    }else{
        that.addClass('active');
    };
    that.parents('.contact-tree').find('.org-branch-three').removeClass('active');
    leftArrow();
};
// 组织架构去重
function treeUniq(Arrow,dom){
    // var treeCur = $('.contacts-box-l .contact-tree .tree-box .org-branch-personnel');
    var rightTr = $('.contacts-box-r .contacts-table-outer table tr').find('input[name="contacts-choice"]');
    // var treeBox = dom.siblings('.tree-box');
    var treeCur = dom?dom.siblings().find('[data-type]'):$('.contacts-box-l .contact-tree').find('[data-type]');
    // for(){
    //
    // }
    for(var i=0;i<treeCur.length;i++){
        for(var k=0;k<rightTr.length;k++){
            if($(rightTr[k]).data('type') === 'userMssage'){
                if($(treeCur[i]).data('userid') === $(rightTr[k]).data('receiveuserid')){
                    if(Arrow === 'left'){
                        $(treeCur[i]).remove();
                    }else{
                        $(rightTr[k].parentNode.parentNode).remove();
                    }
                }
            }else if($(rightTr[k]).data('type') === 'subcompany'){
                if($(treeCur[i]).data('supsubcomid') === $(rightTr[k]).data('supsubcomid')){
                    $(treeCur[i]).addClass('layui-disabled')
                }
            }else if($(rightTr[k]).data('type') === 'department'){
                if($(treeCur[i]).data('departmentid') === $(rightTr[k]).data('departmentid')){
                    $(treeCur[i]).addClass('layui-disabled')
                }
            }

        }
    }
};
// 页面已有的联系人去到弹窗页面去重
function alreadyDialog(){
    var isTree = $('.contacts-table-outer .contact-tree').length;
    var alreadyChoice = $('.object-tab .layui-table-body tbody input[name="choice"]');
    var trChoice = $('.contacts-box-l .contacts-table tbody input[name="contacts-choice"]');
    var liChoice = $('.contacts-box-l .contact-tree .tree-box .org-branch-personnel')
    var dialogChoice = isTree>0?liChoice:trChoice;
    for(var i=0;i<alreadyChoice.length;i++){
        for(var k=0;k<dialogChoice.length;k++){
            if(Number(alreadyChoice[i].value) === ($(dialogChoice[k]).data('receiveuserid')||$(dialogChoice[k]).data('userid'))){
                isTree>0?$(dialogChoice[k]).remove():$(dialogChoice[k].parentNode.parentNode).remove();
            }
        }
    }
}
// 新增联系人//////////////////////////////////////
(function (window) {
    function cookie() { }
    cookie.prototype.add = add;
    cookie.prototype.get = function (name) {
        var nameEQ = name + "=";
        var arr = document.cookie.split(";");
        for (var i = 0; i < arr.length; i++) {
            var str = arr[i];
            while (str.charAt(0) == " ") {
                str = str.substring(1, str.length);
            }
            if (str.indexOf(nameEQ) == 0)
                return decodeURIComponent(str.substring(nameEQ.length, str.length));
        }
        return null;
    }
    cookie.prototype.del = del;
    cookie.prototype.delAll = function (url) {
        var strCookie = document.cookie;
        // 将多cookie切割为多个名/值对
        var arrCookie = strCookie.split("; ");
        // 遍历cookie数组，处理每个cookie对
        for (var i = 0; i < arrCookie.length; i++) {
            var arr = arrCookie[i].split("=");
            if (arr.length > 0)
                del(arr[0], (url || location.hostname));
        }
    }

    function add(name, value, time, domain, path) {
        var maxAgeStr = "; max-age=" + time;
        var domainStr = "; domain=" + (domain || location.hostname);
        var pathStr = "; path=" + (path || '/');
        var str = name + "=" + encodeURIComponent(value) + maxAgeStr + domainStr + pathStr;
        document.cookie = str;
    }

    function del(name, url) {
        add(name, "", 0, url);
    }
    window.cookie = new cookie();
})(window);



