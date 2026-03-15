package com.insipro;

import com.insipro.commands.manager.CommandRepository;
import com.insipro.utils.client.managers.file.exception.FileProcessingException;
import com.insipro.utils.client.logs.Logger;
import com.insipro.utils.display.scissor.ScissorAssist;
import net.fabricmc.api.ModInitializer;
import com.insipro.common.repository.box.BoxESPRepository;
import com.insipro.common.repository.rct.RCTRepository;
import com.insipro.common.repository.way.WayRepository;
import com.insipro.common.discord.DiscordManager;
import com.insipro.utils.client.managers.api.draggable.DraggableRepository;
import com.insipro.utils.client.managers.file.*;
import com.insipro.common.repository.macro.MacroRepository;
import com.insipro.utils.client.managers.event.EventManager;
import com.insipro.features.module.ModuleProvider;
import com.insipro.features.module.ModuleRepository;
import com.insipro.features.module.ModuleSwitcher;
import com.insipro.utils.client.sound.SoundManager;
import com.insipro.display.screens.clickgui.MenuScreen;
import com.insipro.utils.connection.cloud.CloudConfigWebSocketClient;
import com.insipro.main.client.ClientInfo;
import com.insipro.main.client.ClientInfoProvider;
import com.insipro.main.listener.ListenerRepository;
import com.insipro.commands.CommandDispatcher;
import com.insipro.utils.features.aura.striking.StrikerConstructor;
import com.google.common.eventbus.EventBus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.ScheduledExecutorService;

import com.insipro.utils.client.managers.file.impl.account.AccountRepository;
import com.insipro.utils.client.managers.file.impl.AccountFile;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Essence implements ModInitializer {
    @Getter
    static Essence instance;
    EventManager eventManager = new EventManager();
    EventBus eventBus = new EventBus();
    ModuleRepository moduleRepository;
    ModuleSwitcher moduleSwitcher;
    CommandRepository commandRepository;
    CommandDispatcher commandDispatcher;
    BoxESPRepository boxESPRepository = new BoxESPRepository(eventManager);
    MacroRepository macroRepository = new MacroRepository(eventManager);
    WayRepository wayRepository = new WayRepository(eventManager);
    RCTRepository RCTRepository = new RCTRepository(eventManager);
    com.insipro.features.impl.misc.HolyWorldAutoJoin holyWorldAutoJoin;
    ModuleProvider moduleProvider;
    DraggableRepository draggableRepository;
    DiscordManager discordManager;
    FileRepository fileRepository;
    FileController fileController;
    ScissorAssist scissorManager = new ScissorAssist();
    ClientInfoProvider clientInfoProvider;
    ListenerRepository listenerRepository;
    StrikerConstructor attackPerpetrator = new StrikerConstructor();
    CloudConfigWebSocketClient cloudConfigClient;
    
    AccountRepository accountRepository;
    @Getter
    String nativeUsername = "t.me/qstarlab | t.me/mincedclient";
    @Getter
    String nativeUserIdentifier = "1337";
    @Getter
    String nativeUserRole = "admin";
    boolean initialized;
    boolean showIrcMessages = false;
    ScheduledExecutorService reconnectScheduler;
    boolean reconnecting = false;

    @Override
    public void onInitialize() {
        instance = this;

        
        initClientInfoProvider();
        initModules();
        initDraggable();
        initFileManager();
        initCommands();
        initListeners();
        initDiscordRPC();
        initHolyWorldAutoJoin();

        SoundManager.init();

        MenuScreen menuScreen = new MenuScreen();
        menuScreen.initialize();
        initialized = true;
    }
    

    private void initWebSocketClient() {
        try {
            
            if (cloudConfigClient != null) {
                cloudConfigClient.connect();
            }
        } catch (Exception e) {
            Logger.error("Failed to initialize WebSocket client: " + e.getMessage());
        }
    }

    private void initDraggable() {
        draggableRepository = new DraggableRepository();
        draggableRepository.setup();
    }

    private void initModules() {
        moduleRepository = new ModuleRepository();
        moduleRepository.setup();
        moduleProvider = new ModuleProvider(moduleRepository.modules());
        moduleSwitcher = new ModuleSwitcher(moduleRepository.modules(), eventManager);
    }

    private void initCommands() {
        commandRepository = new CommandRepository();
        commandDispatcher = new CommandDispatcher(eventManager);
    }

    private void initDiscordRPC() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("linux")) {
            return;
        }
        discordManager = new DiscordManager();
        discordManager.init();
    }

    private void initHolyWorldAutoJoin() {
        holyWorldAutoJoin = new com.insipro.features.impl.misc.HolyWorldAutoJoin();
        eventManager.register(holyWorldAutoJoin);
    }

    private void initClientInfoProvider() {
        try {
            File gameDir = FabricLoader.getInstance().getGameDir().toFile();
            File clientDirectory = new File(gameDir, "Essence");
            File filesDirectory = new File(clientDirectory, "files");
            File moduleFilesDirectory = new File(filesDirectory,"config");
            clientInfoProvider = new ClientInfo("Essence", nativeUsername, nativeUserRole, clientDirectory, filesDirectory, moduleFilesDirectory);
        } catch (Exception e) {
            Logger.error("Failed to initialize client info provider: " + e.getMessage());
        }
    }

    private void initFileManager() {
        if (clientInfoProvider == null) {
            Logger.error("ClientInfoProvider is null, cannot initialize file manager");
            return;
        }
        DirectoryCreator directoryCreator = new DirectoryCreator();
        directoryCreator.createDirectories(clientInfoProvider.clientDir(), clientInfoProvider.filesDir(), clientInfoProvider.configsDir());
        fileRepository = new FileRepository();
        fileRepository.setup(this);
        accountRepository = new AccountRepository();
        fileRepository.getClientFiles().add(new AccountFile(accountRepository));
        fileController = new FileController(fileRepository.getClientFiles(), clientInfoProvider.filesDir(), clientInfoProvider.configsDir());
        try {
            fileController.loadFiles();
            
            com.insipro.utils.config.ConfigManager.loadDefaultConfig();
        } catch (FileProcessingException e) {
            Logger.error("Failed to load files: " + e.getMessage());
        }
    }

    private void initListeners() {
        listenerRepository = new ListenerRepository();
        listenerRepository.setup();
    }
    }
