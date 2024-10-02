import static org.jocl.CL.*;
import java.io.*;
import org.jocl.*;
import java.awt.Color;
import java.util.ArrayList;

public class HostProgram {
    public cl_kernel kernel;
    public int width = 0;
    public int height = 0;
    public cl_mem pixelMem;
    public cl_command_queue commandQueue;
    private cl_context context;
    private cl_mem hittableBuffer;
    private cl_mem colorMapMem;
    private int[] colorMap;

    public HostProgram(int width, int height) {
        this.width = width;
        this.height = height;
        initCL();
    }

    private void initCL(){}

    private String readFile(String fileName)
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName)));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while (true)
            {
                line = br.readLine();
                if (line == null)
                {
                    break;
                }
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
