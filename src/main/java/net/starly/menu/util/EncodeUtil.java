package net.starly.menu.util;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class EncodeUtil {
    private EncodeUtil() {}


    public static String encode(Object object) {
        byte[] serializedBytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); BukkitObjectOutputStream boos = new BukkitObjectOutputStream(bos)) {
            boos.writeObject(object);
            serializedBytes = bos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return Base64.getEncoder().encodeToString(serializedBytes);
    }

    public static <T> T decode(String serializedString, Class<T> clazz) {
        byte[] serializedBytes = Base64.getDecoder().decode(serializedString);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(serializedBytes); BukkitObjectInputStream bois = new BukkitObjectInputStream(bis)) {
            return clazz.cast(bois.readObject());
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
