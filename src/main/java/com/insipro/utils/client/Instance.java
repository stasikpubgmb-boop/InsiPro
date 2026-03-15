package com.insipro.utils.client;

import lombok.experimental.UtilityClass;
import com.insipro.utils.client.managers.api.draggable.AbstractDraggable;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleProvider;
import com.insipro.features.module.ModuleRepository;
import com.insipro.Essence;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@UtilityClass
public class Instance {
    private final ConcurrentMap<Class<? extends Module>, Module> instanceModules = new ConcurrentHashMap<>();
    private final ConcurrentMap<Class<? extends AbstractDraggable>, AbstractDraggable> instanceDraggables = new ConcurrentHashMap<>();

    public <T extends Module> T get(Class<T> clazz) {
        return clazz.cast(instanceModules.computeIfAbsent(clazz, instance -> {
            Essence essence = Essence.getInstance();
            if (essence == null) {
                return null;
            }
            
            
            ModuleProvider moduleProvider = essence.getModuleProvider();
            if (moduleProvider != null) {
                return moduleProvider.get(instance);
            }
            
            
            ModuleRepository moduleRepository = essence.getModuleRepository();
            if (moduleRepository != null) {
                return moduleRepository.modules().stream()
                    .filter(m -> instance.isAssignableFrom(m.getClass()))
                    .map(instance::cast)
                    .findFirst()
                    .orElse(null);
            }
            
            return null;
        }));
    }

    public <T extends Module> T get(String module) {
        Essence essence = Essence.getInstance();
        if (essence == null) {
            return null;
        }
        
        
        ModuleProvider moduleProvider = essence.getModuleProvider();
        if (moduleProvider != null) {
            return moduleProvider.get(module);
        }
        
        
        ModuleRepository moduleRepository = essence.getModuleRepository();
        if (moduleRepository != null) {
            return (T) moduleRepository.modules().stream()
                .filter(m -> m.getName().equalsIgnoreCase(module))
                .findFirst()
                .orElse(null);
        }
        
        return null;
    }

    public <T extends AbstractDraggable> T getDraggable(Class<T> clazz) {
        return clazz.cast(instanceDraggables.computeIfAbsent(clazz, instance -> Essence.getInstance().getDraggableRepository().get(instance)));
    }

    public <T extends AbstractDraggable> T getDraggable(String draggable) {
        return Essence.getInstance().getDraggableRepository().get(draggable);
    }
}
