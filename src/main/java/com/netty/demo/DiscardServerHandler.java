package com.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.AsciiString;
import io.netty.util.ReferenceCountUtil;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author 冯志宇 2018/4/10
 * 处理服务端channel
 */
@Component
@ChannelHandler.Sharable
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    HashMap<String,Channel> channleMap=new HashMap();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        channleMap.put("SHDL20170108",channel);

        ByteBuf in=(ByteBuf)msg;
        //全部的报文
        byte[] bytes=new byte[in.readableBytes()];
        in.readBytes(bytes);
        System.out.println(ByteBufUtil.hexDump(bytes));

        in.readerIndex(6);
        int cmd=in.order(ByteOrder.LITTLE_ENDIAN).readShort();
        System.out.println("命令"+cmd);

        //根据命令解析数据域
        switch (cmd){
            case 102:
                    System.out.println("心跳");
                    System.out.println(new Timestamp(System.currentTimeMillis()));
                    byte[] b101 = stringToByte("aaf50f0010c165000800000000016e");
                    response(ctx,b101);
                break;
            case 104:
                    System.out.println("状态");
                    byte[] b103 = stringToByte("aaf5360010d5670008000000012d0000000000000000000000000000000000000000000000000000000000000000000000000000009d");
                    response(ctx,b103);
                break;
            case 106: //编码，字符串
                    System.out.println("签到");
                    Decode106.response(ctx);
                break;
            case 202:
                    System.out.println("充电桩上报充电记录信息");
                    byte[] b201 = stringToByte("aaf52e001016c90008000000002d00000000000000000000000000000000000000000000000000000000000000fe");
                    response(ctx,b201);
                break;
        }
        in.resetReaderIndex();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void response(ChannelHandlerContext ctx,byte[] resp){
        ByteBuf buf = Unpooled.copiedBuffer(resp);
        ctx.writeAndFlush(buf);
    }

    public byte[] stringToByte(String s){
        byte[] temp=new byte[s.length()/2];
        int ps=0;
        for (int i=0;i<s.length();i+=2){
            int num = Integer.parseInt(s.substring(i, i + 2), 16);
            temp[ps]=(byte)num;
            ps++;
        }
        return temp;
    }
}
