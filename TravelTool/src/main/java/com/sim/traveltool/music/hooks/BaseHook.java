package com.sim.traveltool.music.hooks;


import com.sim.traveltool.music.vpn.hookhttp.Hook;
import com.sim.traveltool.music.vpn.hookhttp.Request;

public abstract class BaseHook extends Hook
{
    @Override
    public void hookRequest(Request request)
    {
        request.getHeaderFields().remove("X-NAPM-RETRY");
    }
}