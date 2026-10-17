package org.reforged.generals;

import org.libsdl.app.SDLActivity;

/**
 * SDL's Android activity, loading the shared SDL3 runtime and the game's native entry point.
 * The native library exports SDL_main because PosixMain.cpp includes SDL_main.h.
 */
public final class GeneralsReforgedActivity extends SDLActivity {
    @Override
    protected String[] getLibraries() {
        return new String[] {
            "SDL3",
            "GeneralsReforged"
        };
    }
}
