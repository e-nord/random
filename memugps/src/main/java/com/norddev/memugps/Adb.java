package com.norddev.memugps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Adb {

    private static final String DEFAULT_ADB_EXECUTABLE = "adb";
    public static boolean ENABLE_ECHO_COMMAND = true;

    private final String mAdbExecutable;
    private String mSerial;
    private String mAdbCommand;
    private String[] mAdbArgs;

    public Adb() {
        this(DEFAULT_ADB_EXECUTABLE);
    }

    public Adb(String adbExecutable) {
        mAdbExecutable = adbExecutable;
    }

    public static String exec(String adbExecutable, String adbCommand, String[] adbArgs, String serial) throws IOException, InterruptedException {
        List<String> args = new LinkedList<>();
        args.add(adbExecutable);
        if (serial != null) {
            args.add("-s");
            args.add(serial);
            args.add("wait-for-device");
        }
        if(adbCommand != null){
            args.add(adbCommand);
        }
        if(adbArgs != null){
            Collections.addAll(args, adbArgs);
        }
        if(ENABLE_ECHO_COMMAND){
            for(int i = 0; i < args.size(); i++){
                if(i > 0){
                    System.out.print(" ");
                }
                System.out.print(args.get(i));
            }
            System.out.println();
        }
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process p = null;
        try {
            p = processBuilder.start();
            p.waitFor();
            InputStream sdoutStream = p.getInputStream();
            InputStream stderrStream = p.getErrorStream();
            String stdout = readStream(sdoutStream);
            String stderr = readStream(stderrStream);
            if(!stdout.isEmpty()) {
                System.out.println(stdout);
            }
            if(!stderr.isEmpty()){
                System.err.println(stderr);
            }
            return stdout;
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    private static String readStream(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            if(!line.isEmpty()) {
                builder.append(line);
                builder.append("\n");
            }
        }
        return builder.toString().trim();
    }

    public Adb setSerial(String serial) {
        if(serial != null) {
            mSerial = serial;
        }
        return this;
    }

    public Adb connect(String host){
        mAdbCommand = "connect";
        mAdbArgs = new String[]{ host };
        return this;
    }

    public Adb shell(String... cmd){
        mAdbCommand = "shell";
        mAdbArgs = cmd;
        return this;
    }

    public Adb reboot(String... args){
        mAdbCommand = "reboot";
        mAdbArgs = args;
        return this;
    }

    public Adb install(String apk) {
        mAdbCommand = "install";
        mAdbArgs = new String[]{"-r", apk};
        return this;
    }

    public Adb uninstall(String packageName) {
        mAdbCommand = "uninstall";
        mAdbArgs = new String[]{packageName};
        return this;
    }

    public Adb pull(String remote, String local) {
        mAdbCommand = "pull";
        mAdbArgs = new String[]{remote, local};
        return this;
    }

    public Adb push(String local, String remote) {
        mAdbCommand = "push";
        mAdbArgs = new String[]{local, remote};
        return this;
    }

    public List<String> devices() throws IOException, InterruptedException {
        List<String> serials = new LinkedList<>();
        String output = exec(mAdbExecutable, "devices", null, null);
        Scanner scanner = new Scanner(output);
        scanner.nextLine(); //skip header
        while(scanner.hasNextLine()){
            String serial = scanner.next();
            if (!serial.equals("*")) { //skip startup messages
                serials.add(serial);
            }
            scanner.nextLine();
        }
        return serials;
    }

    public String exec() throws IOException, InterruptedException {
        return exec(mAdbExecutable, mAdbCommand, mAdbArgs, mSerial);
    }
}
