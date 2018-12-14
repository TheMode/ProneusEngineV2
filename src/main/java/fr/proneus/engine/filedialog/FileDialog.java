package fr.proneus.engine.filedialog;

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.nfd.NFDPathSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.util.nfd.NativeFileDialog.*;

public class FileDialog {

    private FileDialogResult result;
    private String path;

    private List<String> pathList = new ArrayList<>();

    public static FileDialog openSingle(String filter, String defaultPath) {
        FileDialog fileDialog = new FileDialog();
        PointerBuffer outPath = memAllocPointer(1);
        try {
            int result = NFD_OpenDialog(filter, defaultPath, outPath);
            fillObject(fileDialog, result, outPath);
        } finally {
            memFree(outPath);
        }
        return fileDialog;
    }

    public static FileDialog openMulti(String filter, String defaultPath) {
        FileDialog fileDialog = new FileDialog();
        try (NFDPathSet pathSet = NFDPathSet.calloc()) {
            int result = NFD_OpenDialogMultiple(filter, defaultPath, pathSet);
            fileDialog.result = convertResult(result);
            if (result == NFD_OKAY) {
                long count = NFD_PathSet_GetCount(pathSet);
                for (long i = 0; i < count; i++) {
                    String path = NFD_PathSet_GetPath(pathSet, i);
                    fileDialog.pathList.add(path);
                }
                NFD_PathSet_Free(pathSet);
            }
        }
        return fileDialog;
    }

    public static FileDialog openFolder(String defaultPath) {
        FileDialog fileDialog = new FileDialog();
        PointerBuffer outPath = memAllocPointer(1);

        try {
            int result = NFD_PickFolder(defaultPath, outPath);
            fillObject(fileDialog, result, outPath);
        } finally {
            memFree(outPath);
        }
        return fileDialog;
    }

    public static FileDialog openSave(String filter, String defaultPath) {
        FileDialog fileDialog = new FileDialog();
        PointerBuffer savePath = memAllocPointer(1);

        try {
            int result = NFD_SaveDialog(filter, defaultPath, savePath);
            fillObject(fileDialog, result, savePath);
        } finally {
            memFree(savePath);
        }
        return fileDialog;
    }

    private static void fillObject(FileDialog fileDialog, int result, PointerBuffer path) {
        fileDialog.result = convertResult(result);
        if (result == NFD_OKAY) {
            fileDialog.path = path.getStringUTF8(0);
        }
        nNFD_Free(path.get(0));
    }

    private static FileDialogResult convertResult(int result){
        return FileDialogResult.values()[result];
    }

    public FileDialogResult getResult() {
        return result;
    }

    public String getPath() {
        return path;
    }

    public List<String> getPathList() {
        return Collections.unmodifiableList(pathList);
    }

    public enum FileDialogResult {
        ERROR, SUCCESS, CANCEL;
    }
}
