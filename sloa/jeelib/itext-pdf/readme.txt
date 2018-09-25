PDF操作工具集
1.复制jar到WEB-INF/lib目录。
2.建立包com.sunnsoft.util.pdf，复制code里面的工具代码到包里，然后参考PdfSample来操作PDF。

PDF签章的key需要CA签名。
签章CA认证：
	签名程序用到的key 生成的命令：（CN=网站域名，OU和O需要填写比较正式的格式，keypass和storepass需要更改）
	 .\keytool -genkey -alias isunn-pdf -keypass isunncn.pdf -keyalg RSA -keysize 1024 -validity 7200 -storetype PKCS12 -keystore D:/tmp/sample.p12 -storepass isunncn.pdf -dname "CN=www.isunn.cn,OU=ISunn,O=数商云,L=广州,ST=广东,C=中国"
	导出证书用于提交CA认证
	 .\keytool -certreq -alias isunn-pdf  -storetype PKCS12 -keystore D:/tmp/sample.p12 -storepass isunncn.pdf -file D:/tmp/sample.csr
	CA认证后的证书再导入到keystore,导入之前，先备份keystore，避免错误。
	 .\keytool -import -keystore cacerts -storepass isunncn.pdf -keypass isunncn.pdf -alias isunn-pdf -file D:/tmpcert/CA.cer 
	