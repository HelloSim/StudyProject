package com.sim.traveltool.music.core;

import com.sim.traveltool.music.hooks.CollectHook;
import com.sim.traveltool.music.hooks.DownloadHook;
import com.sim.traveltool.music.hooks.PlaylistHook;
import com.sim.traveltool.music.hooks.SongPlayHook;
import com.sim.traveltool.music.vpn.block.BlockHttp;
import com.sim.traveltool.music.vpn.block.BlockHttps;
import com.sim.traveltool.music.vpn.hookhttp.HookHttp;

public class Server
{
    private static Server instance = new Server();

    public static Server getInstance()
    {
        return instance;
    }

    private void setHooks()
    {
        HookHttp.getInstance().addHook(new PlaylistHook());
        HookHttp.getInstance().addHook(new SongPlayHook());
        HookHttp.getInstance().addHook(new CollectHook());
        HookHttp.getInstance().addHook(new DownloadHook());
    }

    private void setHttpsBlock()
    {
        /*not used yet*/
        BlockHttps.getInstance().addHost("music.163.com");
        BlockHttps.getInstance().addHost("interface3.music.163.com");
        BlockHttps.getInstance().addHost("interface.music.163.com");
        BlockHttps.getInstance().addHost("apm.music.163.com");
        BlockHttps.getInstance().addHost("apm3.music.163.com");
        BlockHttps.getInstance().addHost("clientlog3.music.163.com");
        BlockHttps.getInstance().addHost("clientlog.music.163.com");
    }

    private void setHttpBlock()
    {
        BlockHttp.getInstance().addHost("apm.music.163.com");
        BlockHttp.getInstance().addHost("apm3.music.163.com");
        BlockHttp.getInstance().addHost("clientlog3.music.163.com");
        BlockHttp.getInstance().addHost("clientlog.music.163.com");
    }

    public void start()
    {
        setHooks();
        setHttpsBlock();
        setHttpBlock();
    }
}
