package com.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AsciiString;

/**
 * @author 冯志宇 2018/4/12
 * 解析106签到报文的数据域
 */
public class Decode106 {

    /**
     * 解析106报文的数据域
     * */
    public static String decode(ByteBuf in){
        //解码桩的编号
        in.readerIndex(12);
        byte[] b1=new byte[32];
        in.readBytes(b1,0,32);
        StringBuffer buffer=new StringBuffer();
        for (byte b:b1){
            buffer.append(AsciiString.b2c(b));
        }
        System.out.println("编号是"+buffer.toString());
        return buffer.toString();
    }

    /**
     * 响应签到报文
     */
    public static void response(ChannelHandlerContext ctx){
        byte[] resp105={(byte)0xAA,(byte)0xf5,0x0d,0x00,0x10,(byte)0xd0,0x69,0x00,0x08,0x00,0x00,0x00,0x71};
        ByteBuf resp = Unpooled.copiedBuffer(resp105);
        ctx.writeAndFlush(resp);
    }

}
