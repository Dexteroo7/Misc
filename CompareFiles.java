import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.System;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Timer;

/**
 * Created by dexter on 4/12/14.
 */
public final class CompareFiles {

    private static long readTotal1 = 0, readTotal2 = 0;

    public static void main (String [] args) throws IOException {

        final long start = System.nanoTime();

        final File file1 = new File(args[0]);
        final File file2 = new File(args[1]);

        //check if the files exist and are not blank
        if(!file1.exists() || !file2.exists() ||
            file1.length() == 0 || file2.length() == 0) {
            System.out.println("ILLEGAL FILES");
            return;
        }

        //if the length of the files is not same they are obviously not the same files
        if(file1.length() != file2.length()) {
            System.out.println("DIFFERENT SIZE");
            return;
        }

        final FileChannel channel1 = new FileInputStream(file1).getChannel();
        final FileChannel channel2 = new FileInputStream(file2).getChannel();

        //DirectByteBuffers for faster IO
        final ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(1 * 128 * 1024);
        final ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(1 * 128 * 1024);

        System.out.println("Starting Compare");

        while(true) {

            int read1, read2 =0;
            read1 = channel1.read(byteBuffer1);
            if(read1 == -1) break;

            while (read2 < read1 && read2 >= 0) {
                read2 += (channel2.read(byteBuffer2));
            }
            byteBuffer1.flip();byteBuffer2.flip();
            if(byteBuffer1.compareTo(byteBuffer2) != 0) {
                System.out.println("NOT SAME");
                return;
            }

            byteBuffer1.clear();
            byteBuffer2.clear();
//            readTotal1+= ((read1/1024)/1024);
//            readTotal2+= ((read2/1024)/1024);
//            System.out.println(readTotal1 + " " + readTotal2);
        }
        System.out.println("SAME :)");
        final long end = System.nanoTime();
        System.out.println((end-start)/1000 + " ms");
        System.out.println((end-start)/1000000000 + " seconds");

        return;
    }
}
