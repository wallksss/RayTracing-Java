import org.jocl.*;

import static org.jocl.CL.*;

import java.util.ArrayList;
import java.util.List;

public class OpenCLUtils {

    static {
        CL.setExceptionsEnabled(true);
    }

    public static class Platform {
        private cl_platform_id platformId;
        private String platformName;

        public Platform(cl_platform_id platformId) {
            this.platformId = platformId;
            this.platformName = getPlatformName(platformId);
        }

        public String getPlatformName() {
            return platformName;
        }

        public cl_platform_id getPlatformId() {
            return platformId;
        }

        private static String getPlatformName(cl_platform_id platformId) {
            long[] size = new long[1];
            clGetPlatformInfo(platformId, CL_PLATFORM_NAME, 0, null, size);
            byte[] buffer = new byte[(int) size[0]];
            clGetPlatformInfo(platformId, CL_PLATFORM_NAME, buffer.length, Pointer.to(buffer), null);
            return new String(buffer, 0, buffer.length - 1);
        }

        public static List<Platform> getPlatforms() {
            int[] numPlatforms = new int[1];
            clGetPlatformIDs(0, null, numPlatforms);
            cl_platform_id[] platforms = new cl_platform_id[numPlatforms[0]];
            clGetPlatformIDs(platforms.length, platforms, null);

            List<Platform> platformList = new ArrayList<>();
            for (cl_platform_id platform : platforms) {
                platformList.add(new Platform(platform));
            }
            return platformList;
        }
    }

    public static class Device {
        private cl_device_id deviceId;
        private cl_platform_id parentPlatformId;
        private String deviceName;
        private String deviceType;
        private String deviceOpenCLVersion;
        private int deviceMaxComputeUnits;
        private int deviceMaxGroupWorkSize;

        public Device(cl_device_id deviceId, cl_platform_id parentPlatformId) {
            this.deviceId = deviceId;
            this.parentPlatformId = parentPlatformId;
            this.deviceName = getDeviceName(deviceId);
            this.deviceMaxComputeUnits = getDeviceMaxComputeUnits(deviceId);
            this.deviceMaxGroupWorkSize = getDeviceMaxWorkGroupSize(deviceId);
            this.deviceType = getDeviceType(deviceId);
            this.deviceOpenCLVersion = getDeviceOpenCLVersion(deviceId);
        }

        public cl_device_id getDeviceId() {
            return deviceId;
        }

        public cl_platform_id getParentPlatformId() {
            return parentPlatformId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public String getDeviceOpenCLVersion() {
            return deviceOpenCLVersion;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public int getDeviceMaxComputeUnits() {
            return deviceMaxComputeUnits;
        }

        public int getDeviceMaxGroupWorkSize() {
            return deviceMaxGroupWorkSize;
        }

        private static String getDeviceName(cl_device_id deviceId) {
            long[] size = new long[1];
            clGetDeviceInfo(deviceId, CL_DEVICE_NAME, 0, null, size);
            byte[] buffer = new byte[(int) size[0]];
            clGetDeviceInfo(deviceId, CL_DEVICE_NAME, buffer.length, Pointer.to(buffer), null);
            return new String(buffer, 0, buffer.length - 1);
        }

        private static String getDeviceOpenCLVersion(cl_device_id deviceId) {
            long[] size = new long[1];
            clGetDeviceInfo(deviceId, CL_DEVICE_VERSION, 0, null, size);
            byte[] buffer = new byte[(int) size[0]];
            clGetDeviceInfo(deviceId, CL_DEVICE_VERSION, buffer.length, Pointer.to(buffer), null);
            return new String(buffer, 0, buffer.length - 1);
        }

        private static String getDeviceType(cl_device_id deviceId) {
            long[] deviceType = new long[1];
            clGetDeviceInfo(deviceId, CL.CL_DEVICE_TYPE, Sizeof.cl_long, Pointer.to(deviceType), null);

            if ((deviceType[0] & CL.CL_DEVICE_TYPE_CPU) != 0) {
                return "CPU";
            } else if ((deviceType[0] & CL.CL_DEVICE_TYPE_GPU) != 0) {
                return "GPU";
            } else if ((deviceType[0] & CL.CL_DEVICE_TYPE_ACCELERATOR) != 0) {
                return "Accelerator";
            } else if ((deviceType[0] & CL.CL_DEVICE_TYPE_DEFAULT) != 0) {
                return "Default";
            } else {
                return "Unknown";
            }
        }

        private static int getDeviceMaxComputeUnits(cl_device_id deviceId) {
            long[] size = new long[1];
            clGetDeviceInfo(deviceId, CL.CL_DEVICE_MAX_COMPUTE_UNITS, 0, null, size);
            int[] buffer = new int[(int) size[0] / Sizeof.cl_int];
            clGetDeviceInfo(deviceId, CL.CL_DEVICE_MAX_COMPUTE_UNITS, buffer.length * Sizeof.cl_int, Pointer.to(buffer), null);
            return buffer[0];
        }

        private static int getDeviceMaxWorkGroupSize(cl_device_id deviceId) {
            long[] size = new long[1];
            clGetDeviceInfo(deviceId, CL.CL_DEVICE_MAX_WORK_GROUP_SIZE, 0, null, size);
            int[] buffer = new int[(int) size[0] / Sizeof.cl_int];
            clGetDeviceInfo(deviceId, CL.CL_DEVICE_MAX_WORK_GROUP_SIZE, buffer.length * Sizeof.cl_int, Pointer.to(buffer), null);
            return buffer[0];
        }

        public static List<Device> getDevices(cl_platform_id platformId) {
            int[] numDevices = new int[1];
            clGetDeviceIDs(platformId, CL_DEVICE_TYPE_ALL, 0,null, numDevices);
            cl_device_id[] devices = new cl_device_id[numDevices[0]];
            clGetDeviceIDs(platformId, CL_DEVICE_TYPE_ALL, devices.length, devices, null);

            List<Device> deviceList = new ArrayList<>();
            for (cl_device_id device : devices) {
                deviceList.add(new Device(device, platformId));
            }
            return deviceList;
        }
    }
}
