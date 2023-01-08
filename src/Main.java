import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        MultiServer ms = new MultiServer();
        ms.start(8998);
    }
}
