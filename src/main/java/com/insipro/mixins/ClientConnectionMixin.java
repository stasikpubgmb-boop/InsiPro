package com.insipro.mixins;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.handler.PacketSizeLogger;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.insipro.utils.client.managers.event.EventManager;
import com.insipro.common.proxy.Proxy;
import com.insipro.common.proxy.ProxyServer;
import com.insipro.events.packet.PacketEvent;

import java.net.InetSocketAddress;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Unique
    private static final ThreadLocal<Boolean> essence$sendingChestBypassPosition = ThreadLocal.withInitial(() -> false);

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void handlePacketPre(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        PacketEvent packetEvent = new PacketEvent(packet, PacketEvent.Type.RECEIVE);
        EventManager.callEvent(packetEvent);
        if (packetEvent.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"),cancellable = true)
    private void sendPre(Packet<?> packet, CallbackInfo info) {
        PacketEvent packetEvent = new PacketEvent(packet, PacketEvent.Type.SEND);
        EventManager.callEvent(packetEvent);
        if (packetEvent.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method = "addHandlers", at = @At("RETURN"))
    private static void addHandlersHook(ChannelPipeline pipeline, NetworkSide side, boolean local, PacketSizeLogger packetSizeLogger, CallbackInfo ci) {
        Proxy proxy = ProxyServer.proxy;
        if (proxy != null && ProxyServer.proxyEnabled && side == NetworkSide.CLIENTBOUND && !local) {
            pipeline.addFirst(new Socks5ProxyHandler(new InetSocketAddress(proxy.getIp(), proxy.getPort()), proxy.username, proxy.password));
            
            ProxyServer.lastUsedProxy = proxy;
        }
    }
}
