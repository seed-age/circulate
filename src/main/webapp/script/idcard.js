	String.prototype.trim=function(){return this.replace(/(^\s*)|(\s*$)/g,"");}

	var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子  
	var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];// 身份证验证位值.10代表X  
	function IdCardValidate(idCard) { 
	    idCard = idCard.toString().trim();  
	    if (idCard.length == 15) {  
	        return isValidityBrithBy15IdCard(idCard);  
	    } else if (idCard.length == 18) {  
	        var a_idCard = idCard.split("");// 得到身份证数组  
	        if(isValidityBrithBy18IdCard(idCard)&&isTrueValidateCodeBy18IdCard(a_idCard)){ 
	            return true;  
	        }else {   
	            return false;  
	        }  
	    } else {  
	        return false;  
	    }  
	}  

	function isValidityBrithBy18IdCard(idCard18){  
	    var year =  idCard18.substring(6,10);  
	    var month = idCard18.substring(10,12);  
	    var day = idCard18.substring(12,14);  
	    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));  
	    // 这里用getFullYear()获取年份，避免千年虫问题  
	    if(temp_date.getFullYear()!=parseFloat(year)  
	          ||temp_date.getMonth()!=parseFloat(month)-1  
	          ||temp_date.getDate()!=parseFloat(day)){  
	            return false;  
	    }else{  
	        return true;  
	    }  
	}  
	 function isValidityBrithBy15IdCard(idCard15){  
	      var year =  idCard15.substring(6,8);  
	      var month = idCard15.substring(8,10);  
	      var day = idCard15.substring(10,12);  
	      var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));  
	      // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法  
	      if(temp_date.getYear()!=parseFloat(year)  
	              ||temp_date.getMonth()!=parseFloat(month)-1  
	              ||temp_date.getDate()!=parseFloat(day)){  
	                return false;  
	        }else{  
	            return true;  
	        }  
	  }  
	 
	function isTrueValidateCodeBy18IdCard(a_idCard) {  
	    var sum = 0; // 声明加权求和变量  
	    if (a_idCard[17].toLowerCase() == 'x') {  
	        a_idCard[17] = 10;// 将最后位为x的验证码替换为10方便后续操作  
	    }  
	    for ( var i = 0; i < 17; i++) {  
	        sum += Wi[i] * a_idCard[i];// 加权求和  
	    }  
	    valCodePosition = sum % 11;// 得到验证码所位置  
	    if (a_idCard[17] == ValideCode[valCodePosition]) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}  