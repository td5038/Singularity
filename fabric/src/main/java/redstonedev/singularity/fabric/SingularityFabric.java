package redstonedev.singularity.fabric;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import fr.catcore.server.translations.fabric.ServerTranslationsFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import redstonedev.singularity.Singularity;

public class SingularityFabric implements IdentifiableResourceReloadListener, ModInitializer {
    private static final ServerTranslationsFabric inner = new ServerTranslationsFabric();

    @Override
    public void onInitialize() {
        Singularity.init();
        inner.onInitialize();
    }

    @Override
    public String getName() {
        return inner.getName();
    }

    @Override
    public ResourceLocation getFabricId() {
        return inner.getFabricId();
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier synchronizer, ResourceManager manager,
            ProfilerFiller prepareProfiler, ProfilerFiller applyProfiler, Executor prepareExecutor,
            Executor applyExecutor) {
        return inner.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
    }
}