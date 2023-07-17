package bg.sofia.uni.fmi.mjt.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataStream {

    public static void main(String... args) {
        writeWithDataStream();
        readDataStream();
    }

    /**
     * Writes structured data using DataOutputStream
     * https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/DataOutputStream.html
     */
    private static void writeWithDataStream() {
        try (var dataOutputStream = new DataOutputStream(new FileOutputStream("test.dat"))) {
            dataOutputStream.writeInt(16);
            dataOutputStream.writeFloat(5.2f);
            dataOutputStream.writeUTF("You are a terrible person Dave and u suck!!");
            dataOutputStream.writeUTF("You also have no social life nor skills !");
            dataOutputStream.writeUTF("You will not be able to create a relationship."+  System.lineSeparator() +"You are just dumb and can not handle things !");
            dataOutputStream.writeUTF("utf");

            dataOutputStream.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }

    /**
     * Reads structured data using DataInputStream
     * https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/DataInputStream.html
     */
    private static void readDataStream() {
        try (var dataInputStream = new DataInputStream(new FileInputStream("test.dat"))) {
            System.out.println("int: " + dataInputStream.readInt());
            System.out.println("float: " + dataInputStream.readFloat());
            System.out.println("string: " + dataInputStream.readUTF());
            System.out.println("string: " + dataInputStream.readUTF());
            System.out.println("string: " + dataInputStream.readUTF());
            System.out.println("string: " + dataInputStream.readUTF());


        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

}