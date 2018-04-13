package com.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.nio.channels.SocketChannel;

/**
 * @author 冯志宇 2018/4/12
 */
@Controller
@RequestMapping(value = "/")
public class DiscardController {
    @Autowired
    DiscardServerHandler handler;

    @RequestMapping(value = "/start",method = RequestMethod.GET)
    public String start(){
        Channel channel = handler.channleMap.get("SHDL20170108");
        byte[] b103 = stringToByte("aaf54c001011070008000000000000000100000000000000000000000020180412173359ff012d0000000000000000000000000000000000000000000000000000000000000000010000002f");
        ByteBuf buf = Unpooled.copiedBuffer(b103);
        channel.writeAndFlush(buf);
        return "成功";
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
