/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.wj.dubbo.netty;

import cn.edu.wj.rpc.dubbo.netty.Server;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeChannel;
import cn.edu.wj.rpc.dubbo.remoting.exchange.ExchangeServer;
import cn.edu.wj.rpc.dubbo.remoting.exchange.Exchangers;
import cn.edu.wj.rpc.dubbo.remoting.exchange.support.Replier;

import com.alibaba.dubbo.common.URL;


/**
 * NettyClientToServerTest
 *
 */
public class NettyClientToServerTest extends ClientToServerTest {

    protected ExchangeServer newServer(int port, Replier<?> receiver) throws Exception {
        return Exchangers.bind(URL.valueOf("exchange://localhost:" + port + "?server=netty"), receiver);
    }

    protected ExchangeChannel newClient(int port) throws Exception {
        return Exchangers.connect(URL.valueOf("exchange://localhost:" + port + "?client=netty&timeout=5000"));
    }

}