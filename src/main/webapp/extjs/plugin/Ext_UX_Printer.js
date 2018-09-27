/**
 * @description : Extjs 实现局部打印
 * @author : Ext官网上一个叫nerdydude81 的孙子
 * @override : Ext.Element,Ext.Component
 * @see : http://www.sencha.comforumshowthread.php44885-Print-Element I solved
 * @description : 不用window.open();创建新web页来实现打印
 * @see 56行：<br>
 *      strTemplate主要的思路就是创建内部的dom节点,和iframe元素<br>
 *      然后用流的方式写出，并用window.print()打印这个iframe
 *      @example : 
 *      grid.printEl({
                                        printCSS :'extjs/resource/ext-all.css',
                                        printTitle : '费用支出表'
                                });
 */

// 上来就重载所有组件的EL，并未所有Element添加打印配置属性
Ext.override(Ext.Element, {
	/**
	 * @cfg {string}css: 打印页面的内联css样式; eg: body{color:'';font:''}
	 */
	css : ''
	/**
	 * @cfg {string}printCSS: 外联样式表的路径<link ref='stylesheet'
	 *      href='this.printCss' />
	 */
	,
	printCSS : null
	/**
	 * @cfg {string} addHeader: 在生成的html元素最上方添加的内容
	 */
	,
	addHeader : ''
	/**
	 * @cfg {string} addFooter: 在生成的html元素最下方添加的内容
	 */
	,
	addFooter : ''
	/**
	 * @cfg(Boolean) printStryle : 复制这个Ext.Element的样式的属性到 打印页(iframe)
	 */
	,
	printStyle : true
	/**
	 * @property {string} printTitle : 打印页面上面的标题
	 */
	,
	printTitle : document.title
	/**
	 * 打印这个元素
	 * @param config
	 *            {object} (optional)
	 */
	,
	print : function(config) {
		Ext.apply(this, config);

		var el = Ext.get(this.id).dom;
		var c = document.getElementById('printcontainer');
		var iFrame = document.getElementById('printframe');

		var strTemplate = '<HTML><HEAD>{0}<TITLE ID="_title">{1}</TITLE><style type="text/css">'
				+ this.css
				+ '</style></HEAD><BODY onload="{2}"><DIV>'
				+ this.addHeader
				+ '</DIV><DIV width="100%" {3}>{4}</DIV><DIV>'
				+ this.addFooter + '</DIV></BODY></HTML>';
		var strLinkTpl = '<link rel="stylesheet" type="text/css" href="{0}"/>'
		var strAttr = '';
		var strFormat;
		var strHTML;

		// 去除我们没有复制的垃圾配置到输出的iframe
		if (iFrame != null)
			c.removeChild(iFrame);
		if (c != null)
			el.removeChild(c);

		// 从这个elemtnt复制属性
		for (var i = 0; i < el.attributes.length; i++) {
			if (Ext.isEmpty(el.attributes[i].value)
					|| el.attributes[i].value.toLowerCase() != 'null') {
				strFormat = Ext.isEmpty(el.attributes[i].value)
						? '{0}="true" '
						: '{0}="{1}" ';
				if (this.printStyle ? this.printStyle : el.attributes[i].name
						.toLowerCase() != 'style')
					strAttr += Ext.util.Format.format(strFormat, el.attributes[i].name,
							el.attributes[i].value);
			}
		}

		var strLink = '';
		if (this.printCSS) {
			if (!Ext.isArray(this.printCSS))
				this.printCSS = [this.printCSS];

			for (var i = 0; i < this.printCSS.length; i++) {
				strLink += Ext.util.Format.format(strLinkTpl, this.printCSS[i]);
			}
		}
		// 这里构建输出的HTML 浮动框架的文本
		strHTML = Ext.util.Format.format(strTemplate, strLink, this.printTitle, Ext.isIE
						? 'document.execCommand(\'print\');'
						: 'window.print();', strAttr, el.innerHTML);
		// 这孙子没法在火狐下面打印隐藏的iframe
		// 所以他封装一个隐藏的div
		c = document.createElement('div');
		c.setAttribute('style', 'width:0px;height:0px;'
						+ (Ext.isSafari
								? 'display:none;'
								: 'visibility:hidden;'));
		c.setAttribute('id', 'printcontainer');
		el.appendChild(c);

		// 兼容ie，ie用另一种属性样式
		if (Ext.isIE)
			c.style.display = 'none';

		iFrame = document.createElement('iframe');
		iFrame.setAttribute('id', 'printframe');
		iFrame.setAttribute('name', 'printframe');
		c.appendChild(iFrame);
                
		// 像iframe里面写入新的String类型的dom
		iFrame.contentWindow.document.open();
		iFrame.contentWindow.document.write(strHTML);
		iFrame.contentWindow.document.close();
		// if (this.isGrid) {
		// var iframeBody = Ext.get(iFrame.contentWindow.document.body);
		// var cc = Ext.get(iframeBody.first().dom.parentNode);
		// cc.child('div.x-panel-body').setStyle('height', '');
		// cc.child('div.x-grid3').setStyle('height', '');
		// cc.child('div.x-grid3-scroller').setStyle('height', '');
		// }
		// if (Ext.isIE)
		// iFrame.contentWindow.document.execCommand('print');
		// else
		// iFrame.contentWindow.print();
	}
});
/**
 * 这里重载Ext.Component,Ext所有组件都继承这个，所以打印时只需要传入Component而不用传入底层dom
 */
Ext.override(Ext.Component, {
			printEl : function(config) {
				this.el
						.print(Ext.isEmpty(config)
								? this.initialConfig
								: config);
			},
			printBody : function(config) {
				this.body.print(Ext.isEmpty(config)
						? this.initialConfig
						: config);
			}
		})