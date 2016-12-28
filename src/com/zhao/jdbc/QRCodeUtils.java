package com.zhao.jdbc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

/**
 * 二维码工具类
 * 使用QRCode.jar生成
 *
 * @author zhaoyan
 * @since 2016.12.28 9:17
 */
public class QRCodeUtils {
    public static void main(String[] args) throws Exception {
        QRCodeUtils qrCodeUtils = new QRCodeUtils();
        qrCodeUtils.makeErWeiMa("D:\\QRCode\\", "河北移20160001号");
    }

    /**
     * 在二维码下方插入文字
     *
     * @param bi  二维码图片流
     * @param str 需要插入的文字
     * @return 新的图片流
     */
    public BufferedImage addChars(BufferedImage bi, String str) {
        // 新建一个画布，高度要比之前高20，用来放文字
        BufferedImage biNew = new BufferedImage(bi.getWidth(), bi.getWidth() + 20, BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D对象
        Graphics2D gs = biNew.createGraphics();
        // 设置背景为白色
        gs.setBackground(Color.WHITE);

        gs.clearRect(0, 0, biNew.getWidth(), biNew.getHeight());

        // 将原始的文件数据复制到新的画布上
        biNew.setData(bi.getData());

        // 字体为11时，看起来很费力，几乎看不清楚，但是这种情况比较少，所以这里写个判断。保证大多数都能看清，那些超级长的合同编号，要保证能完全显示
        Font font = null;
        if (str.length() < 20) {
            font = new Font(null, Font.BOLD, 12);
        } else {
            font = new Font(null, Font.BOLD, 11);
        }

        gs.setFont(font);

        // 获取字体上下文，用来收集字体所占的高度和长度，用来适配在指定区域居中
        FontRenderContext context = gs.getFontRenderContext();
        Rectangle2D stringBounds = font.getStringBounds(str, context);
        double fontWith = stringBounds.getWidth();
        double fontHeight = stringBounds.getHeight();

        // 设置画笔颜色为黑色
        gs.setPaint(Color.BLACK);

        // 写入文字
        gs.drawString(str, (biNew.getWidth() - (int) fontWith) / 2, biNew.getHeight() - (int) fontHeight / 2);

        return biNew;
    }

    /**
     * 生成二维码(QRCode)图片的公共方法
     *
     * @param content 存储内容
     * @param imgType 图片类型
     * @param size    二维码尺寸
     * @return
     */
    private BufferedImage qRCodeCommon(String content, String imgType, int size) {
        BufferedImage bufImg = null;
        try {
            Qrcode qrcodeHandler = new Qrcode();
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeErrorCorrect('M');
            qrcodeHandler.setQrcodeEncodeMode('B');
            // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
            qrcodeHandler.setQrcodeVersion(size);
            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes("utf-8");
            // 图片尺寸
            int imgSize = 67 + 12 * (size - 1);
            bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            // 设置背景颜色
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, imgSize, imgSize);

            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容> 二维码
            if (contentBytes.length > 0 && contentBytes.length < 800) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
            }
            gs.dispose();
            bufImg.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImg;
    }

    public void makeErWeiMa(String erWeiMaPath, String content) throws Exception {
        BufferedImage bufImg = qRCodeCommon(content, "jpg", 10);
        bufImg = addChars(bufImg, content);
        // 以合同流水号为文件名生成二维码图片，防止多个人同时访问时使用了同一个二维码图片
        String erWeiMaName = content;
        File erWeiMaFile = new File(erWeiMaPath + erWeiMaName);
        try {
            erWeiMaFile.createNewFile();
            ImageIO.write(bufImg, "jpg", erWeiMaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
