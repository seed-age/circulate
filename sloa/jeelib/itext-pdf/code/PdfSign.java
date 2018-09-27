package com.sunnsoft.util.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 * @author 林宇民 Andy (llade)
 *
 */
@Service
public class PdfSign {
	
	
	@Value("${pdf.sign.pkcs12.file.path}")
	private String pkcs12FilePath ;//pkcs12 密钥文件
	
	@Value("${pdf.sign.keypass}")
	private String keyAliasPass ; //密钥密码
	@Value("${pdf.sign.storepass}")
	private String keyStorePass ; //密钥文件密码
	@Value("${pdf.sign.keyalias}")
	private String keyAlias ; //文件中密钥别名
	
	
	/**
	 * @param pkcs12FilePath the pkcs12FilePath to set
	 */
	public void setPkcs12FilePath(String keyStoreFilePath) {
		this.pkcs12FilePath = keyStoreFilePath;
	}

	/**
	 * @param keyAliasPass the keyAliasPass to set
	 */
	public void setKeyAliasPass(String keyAliasPass) {
		this.keyAliasPass = keyAliasPass;
	}

	/**
	 * @param keyStorePass the keyStorePass to set
	 */
	public void setKeyStorePass(String keyStorePass) {
		this.keyStorePass = keyStorePass;
	}

	/**
	 * @param keyAlias the keyAlias to set
	 */
	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	/**
	 * @param stamperFilePath the stamperFilePath to set
	 */
	public void setStamperFilePath(String stamperFileName) {
		this.stamperFilePath = stamperFileName;
	}

	private String stamperFilePath ;//图章png文件的路径
	
	private URL stamperFileUrl;
	
	private Certificate[] chain ;//证书链

	private PrivateKey pk ;//签名私钥
	
	/**
	 * 
	 * @param src 需要签章的pdf输入流
	 * @param dest 签完章的pdf输出流
	 * @param reason 签名的原因，显示在pdf签名属性中
	 * @param location 签名的地点，显示在pdf签名属性中
	 * @param signLastPage 是否盖在最后一页,默认false,盖章在第一页
	 * @param x 图章坐标X轴 左下角坐标x轴坐标为0
	 * @param y 图章左下角坐标Y轴 左下角坐标y轴坐标为0
	 * @param scale 图章缩放比例
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws DocumentException
	 */
	
	public void sign(
			InputStream src  
            , OutputStream dest 
            , String reason 
            , String location 
            , boolean signLastPage 
            , float x 
            , float y 
            , float scale
			) 
            throws GeneralSecurityException, IOException, DocumentException {
        // Creating the reader and the stamper，开始pdfreader
        PdfReader reader = new PdfReader(src);
        int pages = reader.getNumberOfPages();
        //目标文件输出流
        //创建签章工具PdfStamper ，最后一个boolean参数 
        //false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
        //true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
        PdfStamper stamper = PdfStamper.createSignature(reader, dest, '\0', null, true);
        
        //读取图章图片，这个image是itext包的image
        
        Image image = Image.getInstance(stamperFileUrl); 
        //获取图片高度和宽度,签名的时候使用原图大小的章，因此需要计算图章右上角坐标
        float width = image.getWidth();
        float height = image.getHeight();
        
        // 获取数字签章属性对象，设定数字签章的属性
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason(reason);
        appearance.setLocation(location);
        //设置签名的位置，页码，签名域名称，多次追加签名的时候，签名预名称不能一样
        //签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
        //四个参数的分别是，图章左下角x，图章左下角y，图章右上角x，图章右上角y
        Rectangle rec = new Rectangle(x, y, x + width * scale, y + height + scale);
        
        appearance.setVisibleSignature(rec, signLastPage ? pages:1, "sig1");
       
        
        appearance.setSignatureGraphic(image); 
        appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
        //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
        appearance.setRenderingMode(RenderingMode.GRAPHIC);

        // 这里的itext提供了2个用于签名的接口，可以自己实现，后边着重说这个实现
        // 摘要算法
        ExternalDigest digest = new BouncyCastleDigest();
        // 签名算法
        ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA1, null);
        // 调用itext签名方法完成pdf签章
        MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, CryptoStandard.CMS);
        stamper.close();
	}
	
	@PostConstruct
	public void init() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException  {
		 Assert.notNull(keyAliasPass, "keyAliasPass 不能为空");
		 Assert.notNull(keyStorePass,"keyStorePass 不能为空");
		 Assert.notNull(keyAlias,"keyAlias 不能为空");
		 Assert.notNull(pkcs12FilePath, "pkcs12FilePath 不能为空");
		 Assert.notNull(stamperFilePath,"stamperFilePath 不能为空");
		 KeyStore ks = KeyStore.getInstance("PKCS12");
         ks.load(new FileInputStream(pkcs12FilePath), keyStorePass.toCharArray());
         pk = (PrivateKey) ks.getKey(keyAlias, keyAliasPass.toCharArray());
         chain = ks.getCertificateChain(keyAlias);
         stamperFileUrl = new File(stamperFilePath).toURI().toURL();
         Assert.notNull(stamperFileUrl, "找不到stamperFileName指定的图片文件：" + stamperFilePath);
	}
	
	
}
