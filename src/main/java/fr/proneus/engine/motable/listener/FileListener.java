package fr.proneus.engine.motable.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import fr.proneus.engine.motable.MenuState;
import fr.proneus.engine.motable.Script;
import fr.themode.motable.network.FileType;
import fr.themode.motable.network.ScriptEvent;
import fr.themode.motable.network.packet.connection.FileCheckConnectPacket;
import fr.themode.motable.network.packet.connection.FileInfoConnectPacket;
import fr.themode.motable.network.packet.connection.ServerConnectionConfirmationPacket;
import fr.themode.motable.network.packet.file.FileEndPacket;
import fr.themode.motable.network.packet.file.FileInfoPacket;
import fr.themode.motable.network.packet.file.ScriptEndPacket;
import fr.themode.motable.network.packet.file.ScriptInfoPacket;
import fr.themode.utils.file.FileUtils;
import org.apache.commons.codec.binary.Base32;

import java.io.*;
import java.util.Collection;
import java.util.regex.Pattern;

public class FileListener extends Listener {

    public ByteArrayOutputStream outputStream;
    public String fileId;
    public FileType fileType;
    public ScriptEvent event;
    public String customEvent;
    public int priority;
    public long fileSize;
    public long total;
    private MenuState menuState;
    // TODO replace base32 by encryption (with server key)
    private String serverUniqueKey;
    private boolean isFile;

    public FileListener(MenuState menuState) {
        this.menuState = menuState;
        this.outputStream = new ByteArrayOutputStream();
    }

    @Override
    public void received(Connection connection, Object object) {


        if (object instanceof ServerConnectionConfirmationPacket) {
            // Change to game state and remove file listener
            menuState.ready();
        }

        if (object instanceof FileCheckConnectPacket) {
            Collection<File> files = FileUtils.getFiles("/resources/"); //FileUtils.listFilesForFolder(new File("resources/").getAbsoluteFile());
            FileInfoConnectPacket infoPacket = new FileInfoConnectPacket();
            infoPacket.hasFile = !files.isEmpty();

            if (infoPacket.hasFile) {
                String[] filesId = new String[files.size()];
                String[] checksums = new String[files.size()];
                int counter = 0;
                for (File file : files) {
                    // TODO get file name
                    String decodedName = base32decode(file.getName());
                    if (!getFileExtension(decodedName).equals(".asset"))
                        continue;
                    try {
                        // Get file type
                        decodedName = decodedName.split(Pattern.quote("."))[0];
                        String[] splitedName = decodedName.split(Pattern.quote("-"));
                        String fileId = splitedName[0];
                        FileType fileType = FileType.valueOf(splitedName[splitedName.length - 1]);
                        // Add texture
                        if (fileType.equals(FileType.TEXTURE)) {
                            menuState.addTexture(fileId, file.getPath());
                        }
                        // TODO other files

                        // Get data and append arrays
                        FileInputStream fis = new FileInputStream(file);
                        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
                        fis.close();
                        // Add fileId and md5 result to packet
                        filesId[counter] = fileId;
                        checksums[counter] = md5;

                        counter++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                infoPacket.filesId = filesId;
                infoPacket.checksums = checksums;
            }

            connection.sendTCP(infoPacket);
        } else if (object instanceof FileInfoPacket) {
            FileInfoPacket infoPacket = (FileInfoPacket) object;
            fileId = infoPacket.fileId;
            fileType = infoPacket.fileType;
            fileSize = infoPacket.fileSize;
            isFile = true;
        } else if (object instanceof ScriptInfoPacket) {
            ScriptInfoPacket infoPacket = (ScriptInfoPacket) object;
            event = infoPacket.event;
            customEvent = infoPacket.customEvent;
            priority = infoPacket.priority;
            fileSize = infoPacket.fileSize;
            isFile = false;
        }


        if (object instanceof byte[] && isFile) {
            byte[] value = (byte[]) object;
            appendBytes(value);

            if (total == fileSize) {
                FileOutputStream out = null;
                try {
                    // Create file
                    String fileName = fileId + "-" + fileType.toString() + ".asset";
                    String encodedName = base32encode(fileName);
                    File file = new File("resources/" + encodedName);
                    file.getParentFile().mkdirs();
                    out = new FileOutputStream(file);
                    out.write(outputStream.toByteArray());
                    out.close();
                    System.out.println("[FileListener] INSTALLED FILE: " + fileId);
                    // Add texture
                    if (fileType.equals(FileType.TEXTURE)) {
                        menuState.addTexture(fileId, file.getPath());
                    }
                    // TODO other files
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clear();
                // Request another file if needed
                FileEndPacket endPacket = new FileEndPacket();
                connection.sendTCP(endPacket);
            }
        } else if (object instanceof byte[] && !isFile) {
            byte[] value = (byte[]) object;
            appendBytes(value);

            if (total == fileSize) {
                String scriptString = new String(outputStream.toByteArray());

                StringBuilder builder = new StringBuilder();
                builder.append("load(\"nashorn:mozilla_compat.js\");\n");
                builder.append("importPackage(\"fr.proneus.engine.graphic\");\n");
                builder.append("var MathUtils = MathHelper.static;\n");
                builder.append(scriptString);

                scriptString = builder.toString();

                Script script = null;

                if (event != null) {
                    script = new Script(scriptString, event, priority);
                } else if (customEvent != null) {
                    script = new Script(scriptString, customEvent, priority);
                }

                if (script != null)
                    menuState.addScrip(script);

                clear();
                // Ask for other or for server's confirmation
                ScriptEndPacket endPacket = new ScriptEndPacket();
                connection.sendTCP(endPacket);
            }
        }
    }

    private void appendBytes(byte[] bytes) {
        this.total += bytes.length;
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String base32encode(String name) {
        Base32 base32 = new Base32();
        return base32.encodeAsString(name.getBytes());
    }

    private String base32decode(String name) {
        Base32 base32 = new Base32();
        return new String(base32.decode(name.getBytes()));
    }

    private String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    private void clear() {
        fileId = null;
        fileType = null;

        event = null;
        customEvent = null;
        priority = 0;

        fileSize = 0;
        total = 0;
        outputStream = new ByteArrayOutputStream();
        isFile = false;
    }
}
