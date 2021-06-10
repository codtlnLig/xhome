
package net.swa.file.web.action;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.RGBImageFilter;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.swa.system.web.action.AbstractBaseAction;
import net.swa.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image")
public class ImageAction extends AbstractBaseAction
{
    private static final long serialVersionUID = 5333303240193114340L;

    private Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping(value = "/validateCode")
    public void validateCode(HttpServletRequest request , HttpServletResponse response) throws Exception
    {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //验证码长度
        final int CODE_LENGTH = 4;
        Random rand = new Random();
        //可选字体
        String[] FontFamily = {"Times New Roman", "宋体", "黑体", "Arial Unicode MS", "Lucida Sans" };
        //选转标志，验证码旋转
        boolean ROTATE_FLAG = true;
        //高度，设置旋转后，高度设为30，否则设置成25，效果好一些
        int height = 25;
        String code = "";
        String sRand = "";

        int fontsize;
        int fontstyle = 1;
        double oldrot = 0;

        // 在内存中创建图象
        int width = CODE_LENGTH * 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setColor(getRandColor(100, 255));
        g2.fillRect(0, 0, width, height);

        // 取随机产生的认证码(4位数字)
        for (int i = 0; i < CODE_LENGTH; i++)
        {
            sRand = StringUtil.getRandomString(1);
            code += sRand;
            fontsize = Math.abs(rand.nextInt(24));
            // 18-24 fontsize
            if (fontsize < 5)
                fontsize +=22;
            if (fontsize <8)
                fontsize += 19;
            if (fontsize < 15)
                fontsize += 12;
            fontstyle = rand.nextInt(6);
            g2.setFont(new Font(FontFamily[rand.nextInt(5)], fontstyle, fontsize));//new Font("Verdana", fontstyle, fontsize));
            double rot = -0.25 + Math.abs(Math.toRadians(rand.nextInt(25)));
            //如果设置选装标志，则旋转文字
            if (ROTATE_FLAG)
            {
                g2.rotate(-oldrot, 10, 15);
                oldrot = rot;
                g2.rotate(rot, 3 * (i%2) + 8, 15);
            }
            float stroke = Math.abs(rand.nextFloat() % 30);
            g2.setStroke(new BasicStroke(stroke));
            g2.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            g2.setColor(getRandColor(1, 100));
            g2.drawString(sRand, 20 * i, 20);
        }

        // 将认证码存入session
        request.getSession().setAttribute("validateCode", code);
        // 图象生效
        g2.dispose();
        try
        {
            ImageIO.write(image, "JPEG", response.getOutputStream());//将内存中的图片通过流动形式输出到客户端
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    public Color getRandColor(int fc , int bc)
    { // 给定范围获得随机颜色
        Random random = new Random();
        if (fc > 255)
        {
            fc = 255;
        }
        if (bc > 255)
        {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public static BufferedImage rotateImg(BufferedImage image , int degree , Color bgcolor) throws IOException
    {
        int iw = image.getWidth();// 原始图象的宽度
        int ih = image.getHeight();// 原始图象的高度
        int w = 0;
        int h = 0;
        int x = 0;
        int y = 0;
        degree = degree % 360;
        if (degree < 0)
            degree = 360 + degree;// 将角度转换到0-360度之间
        double ang = Math.toRadians(degree);// 将角度转为弧度

        /**
         * 确定旋转后的图象的高度和宽度
         */

        if (degree == 180 || degree == 0 || degree == 360)
        {
            w = iw;
            h = ih;
        }
        else if (degree == 90 || degree == 270)
        {
            w = ih;
            h = iw;
        }
        else
        {
            int d = iw + ih;
            w = (int) (d * Math.abs(Math.cos(ang)));
            h = (int) (d * Math.abs(Math.sin(ang)));
        }

        x = (w / 2) - (iw / 2);// 确定原点坐标
        y = (h / 2) - (ih / 2);
        BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        Graphics2D gs = (Graphics2D) rotatedImage.getGraphics();
        if (bgcolor == null)
        {
            rotatedImage = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        }
        else
        {
            gs.setColor(bgcolor);
            gs.fillRect(0, 0, w, h);// 以给定颜色绘制旋转后图片的背景
        }

        AffineTransform at = new AffineTransform();
        at.rotate(ang, w / 2, h / 2);// 旋转图象
        at.translate(x, y);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        op.filter(image, rotatedImage);
        image = rotatedImage;
        return rotatedImage;
    }

    public BufferedImage reSize(BufferedImage srcBufImage , int width , int height)
    {
        BufferedImage bufTarget = null;
        double sx = (double) width / srcBufImage.getWidth();
        double sy = (double) height / srcBufImage.getHeight();
        int type = srcBufImage.getType();
        if (type == BufferedImage.TYPE_CUSTOM)
        {
            ColorModel cm = srcBufImage.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            bufTarget = new BufferedImage(cm, raster, alphaPremultiplied, null);
        }
        else
            bufTarget = new BufferedImage(width, height, type);
        Graphics2D g = bufTarget.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(srcBufImage, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return bufTarget;
    }
}

class MyFilter extends RGBImageFilter
{
    // 抽象类RGBImageFilter是ImageFilter的子类，
    // 继承它实现图象ARGB的处理
    int alpha = 0;

    public MyFilter(int alpha)
    {// 构造器，用来接收需要过滤图象的尺寸，以及透明度
        this.canFilterIndexColorModel = true;
        // TransparentImageFilter类继承自RGBImageFilter，它的构造函数要求传入原始图象的宽度和高度。
        // 该类实现了filterRGB抽象函数
        // ，缺省的方式下，该函数将x，y所标识的象素的ARGB值传入，程序员按照一定的程序逻辑处理后返回该象素新的ARGB值
        this.alpha = alpha;
    }

    public int filterRGB(int x , int y , int rgb)
    {
        DirectColorModel dcm = (DirectColorModel) ColorModel.getRGBdefault();
        // DirectColorModel类用来将ARGB值独立分解出来
        int red = dcm.getRed(rgb);
        int green = dcm.getGreen(rgb);
        int blue = dcm.getBlue(rgb);
        // int alp = dcm.getAlpha(rgb);
        if (red == 255 && blue == 255 && green == 255)
        {// 如果像素为白色，则让它透明
            alpha = 0;
        }
        else
        {
            alpha = 255;
        }
        return alpha << 24 | red << 16 | green << 8 | blue;// 进行标准ARGB输出以实现图象过滤
    }

}