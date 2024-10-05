import org.jocl.*;

import static org.jocl.CL.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HostManager {
    private static HostManager instance;
    private static cl_context context;
    private static cl_command_queue command_queue;
    private static cl_program program;
    private static cl_kernel kernel;
    private static cl_device_id device_id;

    public static HostManager getInstance() {
        if (instance == null) {
            instance = new HostManager();
        }
        return instance;
    }

    public cl_context getContext() {
        return context;
    }

    public cl_command_queue getCommandQueue() {
        return command_queue;
    }

    public cl_kernel getKernel() {
        return kernel;
    }

    public cl_device_id getDeviceId() {
        return device_id;
    }

    public static void addDevice(OpenCLUtils.Device device) {
        device_id = device.getDeviceId();
    }

    public static void createContext(OpenCLUtils.Device device) {
        CL.setExceptionsEnabled(true);

        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, device.getParentPlatformId());

        context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device.getDeviceId()},
                null, null, null);


    }
    public static void createCommandQueue(OpenCLUtils.Device device) {
        cl_queue_properties properties = new cl_queue_properties();
        command_queue = clCreateCommandQueueWithProperties(context, device.getDeviceId(), properties, null);
    }

    public static void createProgram(String src) {
        String source = readFile("src/kernels/mrg31k3p.cl") + readFile(src);
        program = clCreateProgramWithSource(context, 1, new String[]{source}, null, null);
        clBuildProgram(program, 0, null, "-I -cl-mad-enable", null, null);
    }

    public static void createKernel(String kernel_name) {
        kernel = clCreateKernel(program, kernel_name, null);
    }

    private static String readFile(String fileName)
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
