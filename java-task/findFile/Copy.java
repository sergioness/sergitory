import java.io.*;

public class Copy {
    static public void main(String args[]){
        if(args.length == 2){
            copy(args[0], args[1]);
            System.out.println("Done!");
        } else System.out.println("java Copy <src-file> <dest-file>");
    }
    static public void copy(String srcFile, String destFile){

        File src = new File(srcFile);
        File dest = new File(destFile);

        byte[] buffer = new byte[(int)src.length()];
            try{
                InputStream is = new FileInputStream(src);
                while(is.read(buffer)!=-1){

                }
            OutputStream os = new FileOutputStream(dest);
            os.write(buffer);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
